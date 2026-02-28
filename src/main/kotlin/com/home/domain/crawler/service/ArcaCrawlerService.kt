package com.home.domain.crawler.service

import com.home.domain.crawler.dto.CrawlerPostDto
import org.jsoup.Jsoup
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

/**
 * 아카라이브 채널 크롤링 서비스
 */
@Service
class ArcaCrawlerService {
    private val log: Logger = LoggerFactory.getLogger(this.javaClass)
    private val userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"

    /**
     * 특정 채널에서 오늘 날짜의 게시글 목록을 가져옴
     * @param channelId 채널 ID (예: ashtrayy)
     */
    fun getTodayPosts(channelId: String): List<CrawlerPostDto> {
        // 한국 시간(KST) 기준으로 오늘 날짜 설정 (서버가 UTC일 수 있으므로 +9시간 고려)
        val nowKst = LocalDateTime.now().plusHours(9)
        val todayStr = nowKst.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        val url = "https://arca.live/b/$channelId"

        log.info("Crawling Arca Live channel for today: $url (KST: $todayStr)")

        return try {
            val doc = Jsoup.connect(url)
                .userAgent(userAgent)
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8")
                .header("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
                .header("Referer", "https://arca.live/")
                .timeout(20000)
                .get()

            val rows = doc.select("div.article-list .vrow:not(.notice)")
            
            rows.mapNotNull { row ->
                val timeElement = row.select("time")
                val isoDate = timeElement.attr("datetime") // "2026-02-28T14:25:00+09:00"
                
                if (isoDate.isNotBlank()) {
                    // Arca 내부 시간을 파싱하여 KST로 보정 (+9시간)
                    val originalTime = OffsetDateTime.parse(isoDate).toLocalDateTime()
                    val kstTime = originalTime.plusHours(9)
                    val kstDateStr = kstTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                    val kstTimeStr = kstTime.format(DateTimeFormatter.ofPattern("HH:mm"))

                    // 보정된 시간이 오늘 날짜와 일치하면 포함
                    if (kstDateStr == todayStr) {
                        val titleElement = row.select("a.title").last()
                        val title = titleElement?.text()?.trim() ?: ""
                        val postUrl = "https://arca.live" + titleElement?.attr("href")
                        val author = row.select("span.user-info").text().trim()

                        CrawlerPostDto(
                            title = title,
                            author = author,
                            date = "KST $kstTimeStr",
                            url = postUrl,
                            source = "Arca"
                        )
                    } else null
                } else null
            }
        } catch (e: Exception) {
            log.error("Failed to crawl Arca Live: \${e.message}")
            emptyList()
        }
    }
}
