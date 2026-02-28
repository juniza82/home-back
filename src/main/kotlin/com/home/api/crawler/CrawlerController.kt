package com.home.api.crawler

import com.home.common.dto.ApiResponse
import com.home.domain.crawler.dto.CrawlerPostDto
import com.home.domain.crawler.service.ArcaCrawlerService
import com.home.domain.crawler.service.DcCrawlerService
import com.home.domain.notification.service.NotificationManager
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/crawler")
@Tag(name = "크롤링 Api", description = "디시인사이드 및 아카라이브 크롤링 api입니다.")
class CrawlerController(
    private val dcCrawlerService: DcCrawlerService,
    private val arcaCrawlerService: ArcaCrawlerService,
    private val notificationManager: NotificationManager
) {

    @GetMapping("/dc/today/{galleryId}")
    @Operation(summary = "디시 오늘 게시글 조회")
    fun getDcTodayPosts(@PathVariable galleryId: String): ApiResponse<List<CrawlerPostDto>> {
        val posts = dcCrawlerService.getTodayPosts(galleryId)
        return ApiResponse.success(posts)
    }

    @GetMapping("/arca/today/{channelId}")
    @Operation(summary = "아카 오늘 게시글 조회")
    fun getArcaTodayPosts(@PathVariable channelId: String): ApiResponse<List<CrawlerPostDto>> {
        val posts = arcaCrawlerService.getTodayPosts(channelId)
        return ApiResponse.success(posts)
    }

    @PostMapping("/all/today/send-all")
    @Operation(summary = "디시/아카 오늘 게시글 통합 슬랙/디스코드 발송")
    fun sendAllToMessengers(
        @RequestParam dcGalleryId: String,
        @RequestParam arcaChannelId: String
    ): ApiResponse<Map<String, Boolean>> {
        val dcPosts = dcCrawlerService.getTodayPosts(dcGalleryId)
        val arcaPosts = arcaCrawlerService.getTodayPosts(arcaChannelId)
        
        val message = StringBuilder("📅 *오늘의 커뮤니티 인기글 요약*\n\n")
        
        message.append("✅ *디씨인싸 ($dcGalleryId)*\n")
        if (dcPosts.isEmpty()) {
            message.append("- 오늘 올라온 글 없음\n")
        } else {
            dcPosts.forEachIndexed { i, p -> message.append("${i+1}. <${p.url}|${p.title}> (${p.author})\n") }
        }
        
        message.append("\n✅ *샤인머스켓 ($arcaChannelId)*\n")
        if (arcaPosts.isEmpty()) {
            message.append("- 오늘 올라온 글 없음\n")
        } else {
            arcaPosts.forEachIndexed { i, p -> message.append("${i+1}. <${p.url}|${p.title}> (${p.author})\n") }
        }

        // 모든 메신저(Slack, Discord)에 발송
        val results = notificationManager.sendToAll(message.toString())
        return ApiResponse.success(results)
    }
}
