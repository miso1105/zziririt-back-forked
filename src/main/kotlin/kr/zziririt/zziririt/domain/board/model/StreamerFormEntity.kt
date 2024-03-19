package kr.zziririt.zziririt.domain.board.model

import jakarta.persistence.*
import kr.zziririt.zziririt.domain.member.model.SocialMemberEntity
import kr.zziririt.zziririt.global.entity.BaseEntity
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction

@Entity
@Table(name = "streamer_form")
@SQLDelete(sql = "UPDATE board SET is_deleted = true WHERE id = ?")
@SQLRestriction(value = "is_deleted = false")
class StreamerFormEntity(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    val socialMemberEntity: SocialMemberEntity,

    @Column(name = "apply_url")
    var applyUrl: String,

    @Column(name = "apply_board_name")
    var applyBoardName: String,

    @Column(name = "streamer_picture")
    var certificationStreamerPicture: String? = null
):BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    fun update(applyUrl: String, applyBoardName: String) {
        this.applyUrl = "https://zziririt.kr/${applyUrl}"
        this.applyBoardName = applyBoardName
    }

    fun uploadImage(streamerPictureUrl: String) {
        this.certificationStreamerPicture = streamerPictureUrl
    }
}