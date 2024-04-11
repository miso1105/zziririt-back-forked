//package kr.zziririt.zziririt.api.event.dto
//
//import kr.zziririt.zziririt.domain.event.model.EventEntity
//import java.time.LocalDateTime
//
//data class EventRegisterRequest(
//    val eventName: String,
//    val startAt: LocalDateTime,
//    val winnerCount: Long
//) {
//    fun toEntity() = EventEntity(
//        eventName = eventName,
//        startAt = startAt,
//        winnerCount = winnerCount
//    )
//}
