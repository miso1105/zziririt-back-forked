package kr.zziririt.zziririt.api.faq.controller

import kr.zziririt.zziririt.api.faq.dto.CreateFaqRequest
import kr.zziririt.zziririt.api.faq.dto.FaqResponse
import kr.zziririt.zziririt.api.faq.service.FaqService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/v1/cs/faqs")
@RestController
class FaqController(
 private val faqService: FaqService
) {

    @PostMapping
    fun createFaq(@RequestBody request: CreateFaqRequest): ResponseEntity<FaqResponse> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(faqService.createFaq(request))
    }

}
//TODO: 롤 체크- 시큐리티