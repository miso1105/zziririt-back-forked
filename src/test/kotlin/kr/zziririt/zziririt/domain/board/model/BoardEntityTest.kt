package kr.zziririt.zziririt.domain.board.model

import io.kotest.core.spec.style.FeatureSpec
import kr.zziririt.zziririt.domain.member.model.MemberRole
import kr.zziririt.zziririt.domain.member.model.MemberStatus
import kr.zziririt.zziririt.domain.member.model.OAuth2Provider
import kr.zziririt.zziririt.domain.member.model.SocialMemberEntity


class BoardEntityTest: FeatureSpec ({
    val socialMember = SocialMemberEntity(email = "parkbro95@naver.com", nickname = "두주", provider = OAuth2Provider.NAVER, providerId = "dpUMgQQIWSw5QL3ExCfTOEPoimd239", memberRole = MemberRole.VIEWER, memberStatus = MemberStatus.NORMAL)
    val boardFixture = BoardEntity(parent = null, socialMember = socialMember, boardName = "test boardName", boardUrl = "board Url", boardType = BoardType.ZZIRIRIT_BOARD)

    feature("Board Entity update 메서드를 실행한다.") {
        scenario("update 메서드를 사용하여 board 수정 시 boardName 이 수정되어야 한다.") {
            boardFixture.update(boardName = "modified test boardName")
        }
    }
})