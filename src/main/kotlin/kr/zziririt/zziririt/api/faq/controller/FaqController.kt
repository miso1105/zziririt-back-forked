package kr.zziririt.zziririt.api.faq.controller

import kr.zziririt.zziririt.api.faq.dto.CreateFaqRequest
import kr.zziririt.zziririt.api.faq.dto.FaqResponse
import kr.zziririt.zziririt.api.faq.dto.UpdateFaqRequest
import kr.zziririt.zziririt.api.faq.service.FaqService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("/api/v1/cs/faqs")
@RestController
class FaqController(
    private val faqService: FaqService
) {

    @PostMapping
    fun createFaq(
        @RequestBody createFaqRequest: CreateFaqRequest
    ): ResponseEntity<FaqResponse> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(faqService.createFaq(createFaqRequest))
    }

    @PutMapping("/{faqId}")
    fun updateFaq(
        @PathVariable faqId: Long,
        @RequestBody updateFaqRequest: UpdateFaqRequest
    ): ResponseEntity<FaqResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(faqService.updateFaq(faqId, updateFaqRequest))
    }

    @DeleteMapping("/{faqId}")
    fun deleteFaq(@PathVariable faqId: Long): ResponseEntity<Unit> {
        faqService.deleteFaq(faqId)
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build()
    }

    @GetMapping
    fun getFaqs(): ResponseEntity<List<FaqResponse>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(faqService.getFaqs())
    }
}