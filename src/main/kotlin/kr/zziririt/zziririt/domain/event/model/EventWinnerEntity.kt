//package kr.zziririt.zziririt.domain.event.model
//
//import jakarta.persistence.*
//import kr.zziririt.zziririt.domain.member.model.SocialMemberEntity
//import org.hibernate.annotations.SQLDelete
//import org.hibernate.annotations.SQLRestriction
//
//@Entity
//@Table(name = "event_winner")
//@SQLDelete(sql = "UPDATE event_winner SET is_deleted = true WHERE id = ?")
//@SQLRestriction(value = "is_deleted = false")
//class EventWinnerEntity(
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    var id: Long? = null,
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "event_id", referencedColumnName = "event_id")
//    val eventEntity: EventEntity,
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "member_id", referencedColumnName = "member_id")
//    val member: SocialMemberEntity
//) {
//
//}