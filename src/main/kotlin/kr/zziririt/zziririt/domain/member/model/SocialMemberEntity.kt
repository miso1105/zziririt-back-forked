package kr.zziririt.zziririt.domain.member.model

import jakarta.persistence.*
import kr.zziririt.zziririt.global.entity.BaseEntity
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction

@Entity
@Table(name = "social_member")
@SQLDelete(sql = "UPDATE social_member SET is_deleted = true WHERE id = ?")
@SQLRestriction(value = "is_deleted = false")
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

    @Column(name = "member_role", nullable = false)
    val memberRole: MemberRole = MemberRole.VIEWER,

    ) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}