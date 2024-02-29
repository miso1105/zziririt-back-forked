package kr.zziririt.zziririt.api.faq.service

import jakarta.transaction.Transactional
import kr.zziririt.zziririt.api.faq.dto.CreateFaqRequest
import kr.zziririt.zziririt.api.faq.dto.FaqResponse
import kr.zziririt.zziririt.domain.faq.model.FaqEntity
import kr.zziririt.zziririt.domain.faq.model.toResponse
import kr.zziririt.zziririt.domain.faq.repository.FaqRepository
import org.springframework.stereotype.Service


@Service
class FaqService(
 private val faqRepository: FaqRepository
) {

    @Transactional
    fun createFaq(request: CreateFaqRequest): FaqResponse {
        return faqRepository.save(
            FaqEntity(
                 question = request.question,
                 answer = request.answer
            )
        )
        .toResponse()
    }

}