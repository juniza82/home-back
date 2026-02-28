package com.home.domain.crawler.service

import com.home.domain.crawler.dto.CrawlerPostDto
import org.jsoup.Jsoup
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * 디시인사이드 갤러리 크롤링 서비스
 */
@Service
class DcCrawlerService {
    private val log: Logger = LoggerFactory.getLogger(this.javaClass)
    private val userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"

    /**
     * 특정 갤러리에서 오늘 날짜의 게시글 목록을 가져옴
     * @param galleryId 갤러리 ID (예: clothing)
     * @param isMinor 마이너 갤러리 여부
     */
    fun getTodayPosts(galleryId: String, isMinor: Boolean = true): List<CrawlerPostDto> {
        val today = LocalDate.now().format(DateTimeFormatter.ofPattern("MM.dd"))
        val baseUrl = if (isMinor) "https://gall.dcinside.com/mgallery/board/lists/" else "https://gall.dcinside.com/board/lists/"
        val url = "$baseUrl?id=$galleryId&mode=best"

        log.info("Crawling DC Inside gallery for today: $url")

        return try {
            val doc = Jsoup.connect(url)
                .userAgent(userAgent)
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8")
                .header("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
                .get()

            val rows = doc.select("table.gall_list tbody tr.us-post")
            
            rows.mapNotNull { row ->
                val dateTd = row.select("td.gall_date")
                val dateText = dateTd.text() // 오늘 글은 "14:25", 과거 글은 "02.27" 형식
                val fullDate = dateTd.attr("title") // 상세 날짜 "2026-02-28 14:25:30"
                
                // 오늘 글인지 확인: 상세 날짜(title)에 오늘 날짜(MM.dd)가 포함되어 있거나, 
                // 화면상 텍스트(dateText)가 ":"를 포함(시간 형식)하는 경우
                if (fullDate.contains(today) || dateText.contains(":")) {
                    val titleElement = row.select("td.gall_tit a").first()
                    val title = titleElement?.text() ?: ""
                    val postUrl = "https://gall.dcinside.com" + titleElement?.attr("href")
                    val author = row.select("td.gall_writer").text()

                    CrawlerPostDto(
                        title = title,
                        author = author,
                        date = if (dateText.contains(":")) "오늘 $dateText" else dateText,
                        url = postUrl,
                        source = "DC"
                    )
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            log.error("Failed to crawl DC Inside: ${e.message}")
            emptyList()
        }
    }
}
