package kr.zziririt.zziririt.api.faq.service

import jakarta.transaction.Transactional
import kr.zziririt.zziririt.api.faq.dto.CreateFaqRequest
import kr.zziririt.zziririt.api.faq.dto.FaqResponse
import kr.zziririt.zziririt.api.faq.dto.UpdateFaqRequest
import kr.zziririt.zziririt.domain.faq.model.FaqEntity
import kr.zziririt.zziririt.domain.faq.repository.FaqRepository
import kr.zziririt.zziririt.global.exception.ErrorCode
import kr.zziririt.zziririt.global.exception.ModelNotFoundException
import org.springframework.data.repository.findByIdOrNull
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

    @Transactional
    fun updateFaq(faqId: Long, updateFaqRequest: UpdateFaqRequest): FaqResponse {
        val faqEntity = faqRepository.findByIdOrNull(faqId)
            ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND, "FAQ not found")
        faqEntity.question = updateFaqRequest.question
        faqEntity.answer = updateFaqRequest.answer
        val updatedFaqEntity = faqRepository.save(faqEntity)
        return FaqResponse.from(updatedFaqEntity)
    }

    @Transactional
    fun deleteFaq(faqId: Long) {
        val faqToDelete = faqRepository.findByIdOrNull(faqId)
            ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND, "FAQ not found")
        faqRepository.delete(faqToDelete)
    }

    fun getFaqs(): List<FaqResponse> {
        val faqEntities: List<FaqEntity> = faqRepository.findAll()
        if (faqEntities.isEmpty()) {
            throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND, "FAQ not found")
        }
        return faqEntities.map { FaqResponse.from(it) }
    }
}