package com.home.common.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/healthcheck")
class DevOpsController {

    @GetMapping("")
    fun lifeCycleTest(): HttpStatus {
        return HttpStatus.OK
    }
}