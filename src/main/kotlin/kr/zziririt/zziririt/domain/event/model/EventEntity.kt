//package kr.zziririt.zziririt.domain.event.model
//
//import jakarta.persistence.*
//import kr.zziririt.zziririt.api.event.dto.EventStatus
//import kr.zziririt.zziririt.global.entity.BaseTimeEntity
//import org.hibernate.annotations.SQLDelete
//import org.hibernate.annotations.SQLRestriction
//import org.hibernate.annotations.SQLUpdates
//import java.time.LocalDateTime
//
//@Entity
//@Table(name = "event")
//@SQLDelete(sql = "UPDATE event SET is_deleted = true WHERE id = ?")
//@SQLUpdates
//@SQLRestriction(value = "is_deleted = false")
//class EventEntity(
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    var id: Long? = null,
//
//    @Column(name = "eventName")
//    val eventName: String,
//
//    @Column(name = "startAt")
//    val startAt: LocalDateTime,
//
//    @Column(name = "winnerCount")
//    val winnerCount: Long,
//
//    @Enumerated(EnumType.STRING)
//    val status: EventStatus = EventStatus.IN_PROGRESS
//): BaseTimeEntity() {
//
//
//}