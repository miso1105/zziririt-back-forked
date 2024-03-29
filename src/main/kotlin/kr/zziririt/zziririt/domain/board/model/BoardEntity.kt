package kr.zziririt.zziririt.domain.board.model

import jakarta.persistence.*
import kr.zziririt.zziririt.domain.member.model.SocialMemberEntity
import kr.zziririt.zziririt.global.entity.BaseEntity
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction

@Entity
@Table(name = "board")
@SQLDelete(sql = "UPDATE board SET is_deleted = true WHERE id = ?")
@SQLRestriction(value = "is_deleted = false")
class BoardEntity(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "social_member_id", nullable = false)
    var socialMember: SocialMemberEntity,

    @Column(name = "board_name", nullable = false)
    var boardName: String,

    @Column(name = "board_url", nullable = false, unique = true)
    var boardUrl: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "board_type")
    var boardType: BoardType,

    @Enumerated(EnumType.STRING)
    @Column(name = "board_act_status")
    var boardActStatus: BoardActStatus = BoardActStatus.ACTIVE,

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    val boardCategory: MutableList<BoardCategoryEntity> = mutableListOf()
) : BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    fun update(boardName: String, socialMember: SocialMemberEntity) {
        this.boardName = boardName
        this.socialMember = socialMember
    }
}