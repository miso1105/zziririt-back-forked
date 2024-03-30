package kr.zziririt.zziririt.domain.point.model

import jakarta.persistence.*
import kr.zziririt.zziririt.domain.member.model.SocialMemberEntity
import kr.zziririt.zziririt.global.entity.BaseEntity
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction

@Entity
@Table(name = "point")
@SQLDelete(sql = "UPDATE point SET is_deleted = true WHERE id = ?")
@SQLRestriction(value = "is_deleted = false")
class PointHistoryEntity(
    @Column(name = "change_amount", nullable = false)
    var changeAmount: Long,

    @Column(name = "reason", nullable = false)
    val reason: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "social_member_id", nullable = false)
    val member: SocialMemberEntity,
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}