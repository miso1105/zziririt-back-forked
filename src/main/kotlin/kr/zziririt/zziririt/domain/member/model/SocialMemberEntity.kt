package kr.zziririt.zziririt.domain.member.model

import jakarta.persistence.*
import kr.zziririt.zziririt.global.entity.BaseTimeEntity

@Entity
@Table(name = "social_member")
class SocialMemberEntity(

    @Column(name = "email", nullable = false, unique = true)
    val email: String,

    @Column(name = "nickname", nullable = false)
    var nickname: String,

    @Enumerated(EnumType.STRING)
    val provider: OAuth2Provider,

    @Column(name = "provider_id", nullable = false)
    val providerId: String,

    @Column(name = "member_role", nullable = false)
    val memberRole: MemberRole = MemberRole.VIEWER,

    ) : BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}