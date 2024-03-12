package kr.zziririt.zziririt.domain.member.model

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "login_history")
class LoginHistoryEntity(

    @ManyToOne
    @JoinColumn(name = "member_id")
    val memberId: SocialMemberEntity,

    @Column(name = "ip")
    val ip: String,

    @Column(name = "user_environment")
    val userEnvironment: String,

    ) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    @CreatedDate
    @Column(name = "created_at")
    lateinit var createdAt: LocalDateTime

}