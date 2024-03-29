package kr.zziririt.zziririt.api.board.service

import kr.zziririt.zziririt.api.board.dto.request.CategoryResponse
import kr.zziririt.zziririt.api.board.dto.request.UpdateCategoryNameRequest
import kr.zziririt.zziririt.domain.board.repository.CategoryRepository
import kr.zziririt.zziririt.global.exception.ErrorCode
import kr.zziririt.zziririt.global.exception.ModelNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CategoryService (
    private val categoryRepository: CategoryRepository
){
    @Transactional
    fun getCategoryById(categoryId: Long) : CategoryResponse {
        val categoryCheck = categoryRepository.findByIdOrNull(categoryId)
            ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)

        return CategoryResponse.from(categoryCheck)
    }

    fun getAllCategories(): List<CategoryResponse> {
        val categoryCheck = categoryRepository.findAll()

        return categoryCheck.map { CategoryResponse.from(it) }
    }

    @Transactional
    fun updateCategoryById(categoryId: Long, request: UpdateCategoryNameRequest) {
        val category = categoryRepository.findByIdOrNull(categoryId)
            ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)

        category.updateCategoryName(request.newUpdateCategoryName)

        categoryRepository.save(category)
    }
}