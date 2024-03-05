package kr.zziririt.zziririt.domain.member.model

import jakarta.persistence.*
import kr.zziririt.zziririt.global.entity.BaseTimeEntity
import java.time.LocalDateTime

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

    @Enumerated(EnumType.STRING)
    @Column(name = "member_role", nullable = false)
    var memberRole: MemberRole = MemberRole.VIEWER,

    @Enumerated(EnumType.STRING)
    @Column(name = "member_status", nullable = false)
    var memberStatus: MemberStatus = MemberStatus.NORMAL,

    @Column(name = "banned_start_date", nullable = true)
    var bannedStartDate: LocalDateTime?,

    @Column(name = "banned_end_date", nullable = true)
    var bannedEndDate: LocalDateTime?,


    ) : BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    fun banForPeriod(startTime: LocalDateTime, endTime: LocalDateTime) {
        memberStatus = MemberStatus.BANNED
        bannedStartDate = startTime
        bannedEndDate = endTime
    }

    fun isBanned(): Boolean {
        if (memberStatus != MemberStatus.BANNED) {
            return false
        }

        val now = LocalDateTime.now()
        return now.isAfter(bannedStartDate) && now.isBefore(bannedEndDate)
    }

}