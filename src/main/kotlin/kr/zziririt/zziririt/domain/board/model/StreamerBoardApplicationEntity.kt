package kr.zziririt.zziririt.domain.board.model

import jakarta.persistence.*
import kr.zziririt.zziririt.domain.member.model.SocialMemberEntity
import kr.zziririt.zziririt.global.entity.BaseEntity
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction

@Entity
@Table(name = "streamer_board_application")
@SQLDelete(sql = "UPDATE streamer_board_application SET is_deleted = true WHERE id = ?")
@SQLRestriction(value = "is_deleted = false")
class StreamerBoardApplicationEntity(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    val socialMemberEntity: SocialMemberEntity,

    @Column(name = "apply_url")
    var applyUrl: String,

    @Column(name = "apply_board_name")
    var applyBoardName: String,

    @Column(name = "streamer_picture")
    var certificationStreamerPicture: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "streamer_board_application_status")
    var streamerBoardApplicationStatus: StreamerBoardApplicationStatus = StreamerBoardApplicationStatus.WAITING

) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    fun update(applyUrl: String, applyBoardName: String) {
        this.applyUrl = applyUrl
        this.applyBoardName = applyBoardName
    }

    fun uploadImage(streamerPictureUrl: String) {
        this.certificationStreamerPicture = streamerPictureUrl
    }

    fun updateApplicationStatus(request: StreamerBoardApplicationStatus) {
        this.streamerBoardApplicationStatus = request
    }
}