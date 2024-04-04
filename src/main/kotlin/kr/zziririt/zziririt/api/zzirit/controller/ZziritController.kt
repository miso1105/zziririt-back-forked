package kr.zziririt.zziririt.api.zzirit.controller

import kr.zziririt.zziririt.api.zzirit.service.ZziritService
import kr.zziririt.zziririt.global.responseEntity
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/zzirits")
class ZziritController (
    private val zziritService: ZziritService
){

    @GetMapping("/rankings")
    fun getZziritRanking() = responseEntity(HttpStatus.OK) {
        zziritService.findZziritRanking()
    }

}