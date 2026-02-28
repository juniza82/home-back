package com.home.domain.crawler.dto

/**
 * 다양한 소스(디시, 아카 등)에서 수집한 게시글 공통 정보를 담는 DTO
 */
data class CrawlerPostDto(
    val title: String,      // 제목
    val author: String,     // 작성자
    val date: String,       // 작성일
    val url: String,        // 게시글 상세 URL
    val source: String      // 출처 (예: DC, Arca)
)
