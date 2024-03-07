package kr.zziririt.zziririt.domain.report.model

import jakarta.persistence.*
import kr.zziririt.zziririt.domain.member.model.SocialMemberEntity
import kr.zziririt.zziririt.global.entity.BaseEntity
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction
import java.time.LocalDateTime


@Entity
@Table(name = "banned_history")
@SQLDelete(sql = "UPDATE post SET is_deleted = true WHERE id = ?")
@SQLRestriction(value = "is_deleted = false")
class BannedHistoryEntity(

    @Column(name = "banned_reason", nullable = false)
    val bannedReason: String,

    @Column(name = "banned_start_date", nullable = false)
    var bannedStartDate: LocalDateTime,

    @Column(name = "banned_period", nullable = false)
    val bannedPeriod: Long,

    @Column(name = "banned_end_date", nullable = false)
    var bannedEndDate: LocalDateTime,

    @ManyToOne
    @JoinColumn(name = "banned_member_id")
    val bannedMemberId: SocialMemberEntity,

    @ElementCollection
    @CollectionTable(name = "banned_reported_ids", joinColumns = [JoinColumn(name = "banned_history_id")])
    @Column(name = "reported_id")
    val reportedIdList: MutableList<Long> = mutableListOf()

) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
}