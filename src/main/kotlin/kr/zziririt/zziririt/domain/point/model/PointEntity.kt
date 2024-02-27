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
class PointEntity(
    @Column(name = "change", nullable = false)
    val change: Long,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "social_member_id", nullable = false)
    val socialMember: SocialMemberEntity,
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}