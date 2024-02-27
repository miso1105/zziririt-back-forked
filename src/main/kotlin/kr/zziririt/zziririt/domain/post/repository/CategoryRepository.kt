package kr.zziririt.zziririt.domain.post.repository

import kr.zziririt.zziririt.domain.post.model.CategoryEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CategoryRepository : CrudRepository<CategoryEntity, Long> {
}