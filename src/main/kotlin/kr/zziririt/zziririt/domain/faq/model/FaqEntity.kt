package kr.zziririt.zziririt.domain.faq.model

import jakarta.persistence.*
import kr.zziririt.zziririt.api.faq.dto.FaqResponse
import kr.zziririt.zziririt.global.entity.BaseEntity
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction

@Entity
@Table(name = "faq")
@SQLDelete(sql = "UPDATE faq SET is_deleted = true WHERE id = ?")
@SQLRestriction(value = "is_deleted = false")
class FaqEntity (
    @Column(name = "question")
    var question: String,

    @Column(name = "answer")
    var answer : String,
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}

fun FaqEntity.toResponse(): FaqResponse{
    return FaqResponse(
        id = id!!,
        question = question,
        answer = answer,
    )
}