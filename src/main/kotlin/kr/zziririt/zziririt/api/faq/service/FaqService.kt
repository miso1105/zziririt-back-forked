package kr.zziririt.zziririt.api.faq.service

import jakarta.transaction.Transactional
import kr.zziririt.zziririt.api.faq.dto.CreateFaqRequest
import kr.zziririt.zziririt.api.faq.dto.FaqResponse
import kr.zziririt.zziririt.domain.faq.repository.FaqRepository
import org.springframework.stereotype.Service

@Service
class FaqService(
    private val faqRepository: FaqRepository
) {
    @Transactional
    fun createFaq(createFaqRequest: CreateFaqRequest): FaqResponse {
        val newFaqEntity = createFaqRequest.toFaqEntity()
        val savedFaqEntity = faqRepository.save(newFaqEntity)
        return FaqResponse.from(faqEntity = savedFaqEntity)
    }
}