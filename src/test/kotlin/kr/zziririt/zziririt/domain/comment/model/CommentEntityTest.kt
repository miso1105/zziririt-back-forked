package kr.zziririt.zziririt.domain.comment.model

import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldBe
import kr.zziririt.zziririt.domain.board.model.BoardEntity
import kr.zziririt.zziririt.domain.board.model.BoardType
import kr.zziririt.zziririt.domain.member.model.MemberRole
import kr.zziririt.zziririt.domain.member.model.MemberStatus
import kr.zziririt.zziririt.domain.member.model.OAuth2Provider
import kr.zziririt.zziririt.domain.member.model.SocialMemberEntity
import kr.zziririt.zziririt.domain.post.model.PostEntity

class CommentEntityTest: FeatureSpec({
    val socialMember = SocialMemberEntity(email = "parkbro95@naver.com", nickname = "두주", provider = OAuth2Provider.NAVER, providerId = "dpUMgQQIWSw5QL3ExCfTOEPoimd239", memberRole = MemberRole.VIEWER, memberStatus = MemberStatus.NORMAL)
    val board = BoardEntity(parent = null, socialMember = socialMember, boardName = "보드 이름", boardUrl = "board Url", boardType = BoardType.ZZIRIRIT_BOARD)
    val post = PostEntity(board = board, socialMember = socialMember, title = "게시글 제목", content = "게시글 내용", privateStatus = false)
    val commentFixture = CommentEntity(post = post, socialMember = socialMember, content = "댓글 내용", privateStatus = false, zziritCount = 0L)

    feature("Comment Entity update 메서드를 실행한다.") {
        scenario("update 메서드를 사용하여 content 를 수정 시 content 가 수정되어야 한다.") {
            commentFixture.update(content = "수정된 댓글의 내용")
        }
    }

    feature("Comment Entity incrementZzirit 메서드를 실행한다.") {
        scenario("incrementZzirit 메서드를 사용하면 Comment Entity 의 zziritCount 가 1 증가해야한다.") {
            val beforeZzritCount = commentFixture.zziritCount

            commentFixture.incrementZzirit()

            commentFixture.zziritCount shouldBe beforeZzritCount + 1
        }
    }

    feature("Comment Entity togglePrivateStatus 메서드를 실행한다.") {
        scenario("togglePrivateStatus 메서드를 사용하면 Comment Entity 의 privateStatus 가 변경되어야 한다.") {
            val beforePrivateStatus = commentFixture.privateStatus

            commentFixture.togglePrivateStatus()

            commentFixture.privateStatus shouldBe !beforePrivateStatus
        }
    }
})


