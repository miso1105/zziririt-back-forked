package kr.zziririt.zziririt.domain.post.model

import jakarta.persistence.*
import kr.zziririt.zziririt.global.entity.BaseEntity
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction

@Entity
@Table(name = "category")
@SQLDelete(sql = "UPDATE category SET is_deleted = true WHERE id = ?")
@SQLRestriction(value = "is_deleted = false")
class CategoryEntity(
    @Column(name = "category_name", nullable = false)
    var categoryName: String,
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}