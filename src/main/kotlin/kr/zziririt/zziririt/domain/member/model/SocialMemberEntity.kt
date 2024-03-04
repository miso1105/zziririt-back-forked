package kr.zziririt.zziririt.domain.member.model

import jakarta.persistence.*
import kr.zziririt.zziririt.global.entity.BaseTimeEntity

@Entity
@Table(name = "social_member")
class SocialMemberEntity(

    @Column(name = "email", nullable = false, unique = true)
    val email: String,

    @Column(name = "name", nullable = false)
    var name: String,

    @Column(name = "nickname", nullable = false)
    var nickname: String,

    @Column(name = "profile_image_url", nullable = false)
    var profileImageUrl: String,

    @Enumerated(EnumType.STRING)
    val provider: OAuth2Provider,

    @Column(name = "provider_id", nullable = false)
    val providerId: String,

    @Column(name = "birthday", nullable = false)
    val birthday : String,

    @Column(name = "age", nullable = false)
    val age : String,

    @Column(name = "birthyear", nullable = false)
    val birthyear : String,

    @Column(name = "member_role", nullable = false)
    val memberRole: MemberRole = MemberRole.VIEWER,

    @Column(name = "gender", nullable = false)
    val gender : String,

    @Column(name = "mobile", nullable = false)
    val mobile : String,

    ) : BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}