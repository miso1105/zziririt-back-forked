package kr.zziririt.zziririt.domain.member.model

import jakarta.persistence.*
import kr.zziririt.zziririt.domain.icon.model.IconEntity
import kr.zziririt.zziririt.global.entity.BaseEntity
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction

@Entity
@Table(name = "member_icon")
@SQLDelete(sql = "UPDATE member_icon SET is_deleted = true WHERE id = ?")
@SQLRestriction(value = "is_deleted = false")
class MemberIconEntity (

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member")
    val member : SocialMemberEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "icon")
    val icon : IconEntity,

) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
}