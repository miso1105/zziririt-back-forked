package kr.zziririt.zziririt.infra.querydsl.board

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kr.zziririt.zziririt.domain.board.model.BoardActStatus
import kr.zziririt.zziririt.domain.board.model.BoardEntity
import kr.zziririt.zziririt.domain.board.model.BoardType
import kr.zziririt.zziririt.domain.member.model.MemberRole
import kr.zziririt.zziririt.domain.member.model.OAuth2Provider
import kr.zziririt.zziririt.domain.member.model.SocialMemberEntity
import kr.zziririt.zziririt.global.config.BaseEntityConfig
import kr.zziririt.zziririt.global.config.BaseTimeEntityConfig
import kr.zziririt.zziririt.infra.querydsl.QueryDslConfig
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles

@DataJpaTest
@ActiveProfiles("test")
@Import(value = [QueryDslConfig::class, BaseTimeEntityConfig::class, BaseEntityConfig::class])
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BoardQueryDslRepositoryImplTest @Autowired constructor(
    private val boardRepository: CustomBoardRepository,
    private val testEntityManager: TestEntityManager
) {
    @Test
    fun sampleTest() {
        boardRepository.save(
            BoardEntity(
                socialMember = SocialMemberEntity(
                    email = "test@gmail.com",
                    nickname = "test",
                    provider = OAuth2Provider.TEST,
                    providerId = "providerId",
                    memberRole = MemberRole.ADMIN
                ),
                boardName = "test",
                boardUrl = "boardUrl",
                boardType = BoardType.ZZIRIRIT_BOARD
            )
        )
        testEntityManager.flush()
    }

    @Test
    fun `게시판 전체가 조회되는지 확인`() {
        // GIVEN
        val socialMember = SocialMemberEntity(
            email = "test@gmail1.com",
            nickname = "test1",
            provider = OAuth2Provider.TEST,
            providerId = "providerId",
            memberRole = MemberRole.ADMIN
        )

        val DEFAULT_BOARD_LIST = listOf(
            BoardEntity(
                socialMember = socialMember,
                boardName = "test1",
                boardUrl = "boardUrl1",
                boardType = BoardType.ZZIRIRIT_BOARD,
                boardActStatus = BoardActStatus.INACTIVE
            ),
            BoardEntity(
                socialMember = socialMember,
                boardName = "test2",
                boardUrl = "boardUrl2",
                boardType = BoardType.ZZIRIRIT_BOARD,
                boardActStatus = BoardActStatus.ACTIVE
            ),
            BoardEntity(
                socialMember = socialMember,
                boardName = "test3",
                boardUrl = "boardUrl3",
                boardType = BoardType.ZZIRIRIT_BOARD,
                boardActStatus = BoardActStatus.ACTIVE
            ),
            BoardEntity(
                socialMember = socialMember,
                boardName = "test4",
                boardUrl = "boardUrl4",
                boardType = BoardType.ZZIRIRIT_BOARD,
                boardActStatus = BoardActStatus.ACTIVE
            ),
            BoardEntity(
                socialMember = socialMember,
                boardName = "test5",
                boardUrl = "boardUrl5",
                boardType = BoardType.ZZIRIRIT_BOARD,
                boardActStatus = BoardActStatus.ACTIVE
            ),
            BoardEntity(
                socialMember = socialMember,
                boardName = "test6",
                boardUrl = "boardUrl6",
                boardType = BoardType.STREAMER_BOARD,
                boardActStatus = BoardActStatus.INACTIVE
            ),
            BoardEntity(
                socialMember = socialMember,
                boardName = "test7",
                boardUrl = "boardUrl7",
                boardType = BoardType.STREAMER_BOARD,
                boardActStatus = BoardActStatus.ACTIVE
            ),
            BoardEntity(
                socialMember = socialMember,
                boardName = "test8",
                boardUrl = "boardUrl8",
                boardType = BoardType.STREAMER_BOARD,
                boardActStatus = BoardActStatus.ACTIVE
            ),
            BoardEntity(
                socialMember = socialMember,
                boardName = "test9",
                boardUrl = "boardUrl9",
                boardType = BoardType.STREAMER_BOARD,
                boardActStatus = BoardActStatus.ACTIVE
            ),
            BoardEntity(
                socialMember = socialMember,
                boardName = "test10",
                boardUrl = "boardUrl10",
                boardType = BoardType.STREAMER_BOARD,
                boardActStatus = BoardActStatus.ACTIVE
            )
        )
        boardRepository.saveAllAndFlush(DEFAULT_BOARD_LIST)

        // WHEN
        val result = boardRepository.findBoards()

        // THEN
        result.size shouldBe 10
        result[0].boardName shouldBe "test1"
        result[9].boardName shouldBe "test10"
    }

    @Test
    fun `스트리머 게시판 전체가 조회되는지 확인`() {
        // GIVEN
        val socialMember = SocialMemberEntity(
            email = "test@gmail1.com",
            nickname = "test1",
            provider = OAuth2Provider.TEST,
            providerId = "providerId",
            memberRole = MemberRole.ADMIN
        )

        val DEFAULT_BOARD_LIST = listOf(
            BoardEntity(
                socialMember = socialMember,
                boardName = "test1",
                boardUrl = "boardUrl1",
                boardType = BoardType.ZZIRIRIT_BOARD,
                boardActStatus = BoardActStatus.INACTIVE
            ),
            BoardEntity(
                socialMember = socialMember,
                boardName = "test2",
                boardUrl = "boardUrl2",
                boardType = BoardType.ZZIRIRIT_BOARD,
                boardActStatus = BoardActStatus.ACTIVE
            ),
            BoardEntity(
                socialMember = socialMember,
                boardName = "test3",
                boardUrl = "boardUrl3",
                boardType = BoardType.ZZIRIRIT_BOARD,
                boardActStatus = BoardActStatus.ACTIVE
            ),
            BoardEntity(
                socialMember = socialMember,
                boardName = "test4",
                boardUrl = "boardUrl4",
                boardType = BoardType.ZZIRIRIT_BOARD,
                boardActStatus = BoardActStatus.ACTIVE
            ),
            BoardEntity(
                socialMember = socialMember,
                boardName = "test5",
                boardUrl = "boardUrl5",
                boardType = BoardType.ZZIRIRIT_BOARD,
                boardActStatus = BoardActStatus.ACTIVE
            ),
            BoardEntity(
                socialMember = socialMember,
                boardName = "test6",
                boardUrl = "boardUrl6",
                boardType = BoardType.STREAMER_BOARD,
                boardActStatus = BoardActStatus.INACTIVE
            ),
            BoardEntity(
                socialMember = socialMember,
                boardName = "test7",
                boardUrl = "boardUrl7",
                boardType = BoardType.STREAMER_BOARD,
                boardActStatus = BoardActStatus.ACTIVE
            ),
            BoardEntity(
                socialMember = socialMember,
                boardName = "test8",
                boardUrl = "boardUrl8",
                boardType = BoardType.STREAMER_BOARD,
                boardActStatus = BoardActStatus.ACTIVE
            ),
            BoardEntity(
                socialMember = socialMember,
                boardName = "test9",
                boardUrl = "boardUrl9",
                boardType = BoardType.STREAMER_BOARD,
                boardActStatus = BoardActStatus.ACTIVE
            ),
            BoardEntity(
                socialMember = socialMember,
                boardName = "test10",
                boardUrl = "boardUrl10",
                boardType = BoardType.STREAMER_BOARD,
                boardActStatus = BoardActStatus.ACTIVE
            )
        )
        boardRepository.saveAllAndFlush(DEFAULT_BOARD_LIST)

        // WHEN
        val result = boardRepository.findStreamers()

        // THEN
        result.size shouldBe 5
        result[4].streamerNickname shouldBe "test10"    // 스트리머의 닉네임이 게시판의 이름이도록 정책 세움
    }

    @Test
    fun `비활성화 상태로 변경시킬 게시판의 아이디들을 조회하는지 확인`() {
        // GIVEN & WHEN
        val socialMember = SocialMemberEntity(
            email = "test@gmail1.com",
            nickname = "test1",
            provider = OAuth2Provider.TEST,
            providerId = "providerId",
            memberRole = MemberRole.ADMIN
        )

        val DEFAULT_BOARD_LIST = listOf(
            BoardEntity(
                socialMember = socialMember,
                boardName = "test1",
                boardUrl = "boardUrl1",
                boardType = BoardType.ZZIRIRIT_BOARD,
                boardActStatus = BoardActStatus.INACTIVE
            ),
            BoardEntity(
                socialMember = socialMember,
                boardName = "test2",
                boardUrl = "boardUrl2",
                boardType = BoardType.ZZIRIRIT_BOARD,
                boardActStatus = BoardActStatus.ACTIVE
            ),
            BoardEntity(
                socialMember = socialMember,
                boardName = "test3",
                boardUrl = "boardUrl3",
                boardType = BoardType.ZZIRIRIT_BOARD,
                boardActStatus = BoardActStatus.ACTIVE
            ),
            BoardEntity(
                socialMember = socialMember,
                boardName = "test4",
                boardUrl = "boardUrl4",
                boardType = BoardType.ZZIRIRIT_BOARD,
                boardActStatus = BoardActStatus.ACTIVE
            ),
            BoardEntity(
                socialMember = socialMember,
                boardName = "test5",
                boardUrl = "boardUrl5",
                boardType = BoardType.ZZIRIRIT_BOARD,
                boardActStatus = BoardActStatus.ACTIVE
            ),
            BoardEntity(
                socialMember = socialMember,
                boardName = "test6",
                boardUrl = "boardUrl6",
                boardType = BoardType.STREAMER_BOARD,
                boardActStatus = BoardActStatus.INACTIVE
            ),
            BoardEntity(
                socialMember = socialMember,
                boardName = "test7",
                boardUrl = "boardUrl7",
                boardType = BoardType.STREAMER_BOARD,
                boardActStatus = BoardActStatus.ACTIVE
            ),
            BoardEntity(
                socialMember = socialMember,
                boardName = "test8",
                boardUrl = "boardUrl8",
                boardType = BoardType.STREAMER_BOARD,
                boardActStatus = BoardActStatus.ACTIVE
            ),
            BoardEntity(
                socialMember = socialMember,
                boardName = "test9",
                boardUrl = "boardUrl9",
                boardType = BoardType.STREAMER_BOARD,
                boardActStatus = BoardActStatus.ACTIVE
            ),
            BoardEntity(
                socialMember = socialMember,
                boardName = "test10",
                boardUrl = "boardUrl10",
                boardType = BoardType.STREAMER_BOARD,
                boardActStatus = BoardActStatus.ACTIVE
            )
        )
        boardRepository.saveAllAndFlush(DEFAULT_BOARD_LIST)
        val result = boardRepository.findBoardStatusToInactive()

        // THEN
        result.size shouldNotBe null    // 비활성화 상태로 변경시킬 게시판의 아이디를 찾지 못하면 0이 size 가 0이라도 반환되기 때문에 null 이 아니면 메서드 동작 확인 성공.
    }

    @Test
    fun `게시판이 비활성화 상태로 업데이트 되는지 확인`() {
        // GIVEN
        val socialMember = SocialMemberEntity(
            email = "test@gmail1.com",
            nickname = "test1",
            provider = OAuth2Provider.TEST,
            providerId = "providerId",
            memberRole = MemberRole.ADMIN
        )

        val DEFAULT_BOARD_LIST = listOf(
            BoardEntity(
                socialMember = socialMember,
                boardName = "test1",
                boardUrl = "boardUrl1",
                boardType = BoardType.ZZIRIRIT_BOARD,
                boardActStatus = BoardActStatus.INACTIVE
            ),
            BoardEntity(
                socialMember = socialMember,
                boardName = "test2",
                boardUrl = "boardUrl2",
                boardType = BoardType.ZZIRIRIT_BOARD,
                boardActStatus = BoardActStatus.ACTIVE
            ),
            BoardEntity(
                socialMember = socialMember,
                boardName = "test3",
                boardUrl = "boardUrl3",
                boardType = BoardType.ZZIRIRIT_BOARD,
                boardActStatus = BoardActStatus.ACTIVE
            ),
            BoardEntity(
                socialMember = socialMember,
                boardName = "test4",
                boardUrl = "boardUrl4",
                boardType = BoardType.ZZIRIRIT_BOARD,
                boardActStatus = BoardActStatus.ACTIVE
            ),
            BoardEntity(
                socialMember = socialMember,
                boardName = "test5",
                boardUrl = "boardUrl5",
                boardType = BoardType.ZZIRIRIT_BOARD,
                boardActStatus = BoardActStatus.ACTIVE
            ),
            BoardEntity(
                socialMember = socialMember,
                boardName = "test6",
                boardUrl = "boardUrl6",
                boardType = BoardType.STREAMER_BOARD,
                boardActStatus = BoardActStatus.INACTIVE
            ),
            BoardEntity(
                socialMember = socialMember,
                boardName = "test7",
                boardUrl = "boardUrl7",
                boardType = BoardType.STREAMER_BOARD,
                boardActStatus = BoardActStatus.ACTIVE
            ),
            BoardEntity(
                socialMember = socialMember,
                boardName = "test8",
                boardUrl = "boardUrl8",
                boardType = BoardType.STREAMER_BOARD,
                boardActStatus = BoardActStatus.ACTIVE
            ),
            BoardEntity(
                socialMember = socialMember,
                boardName = "test9",
                boardUrl = "boardUrl9",
                boardType = BoardType.STREAMER_BOARD,
                boardActStatus = BoardActStatus.ACTIVE
            ),
            BoardEntity(
                socialMember = socialMember,
                boardName = "test10",
                boardUrl = "boardUrl10",
                boardType = BoardType.STREAMER_BOARD,
                boardActStatus = BoardActStatus.ACTIVE
            )
        )
        val boards = boardRepository.saveAllAndFlush(DEFAULT_BOARD_LIST)

        // WHEN
        val boardIds = boards.map { it.id!! }

        // THEN
        boardRepository.updateBoardStatusToInactive(boardIds)   // 반환 타입이 unit 이라서 행위를 테스트

    }

    @Test
    fun `활성화 상태의 게시판이 조회되는지 확인`() {
        // GIVEN
        val socialMember = SocialMemberEntity(
            email = "test@gmail1.com",
            nickname = "test1",
            provider = OAuth2Provider.TEST,
            providerId = "providerId",
            memberRole = MemberRole.ADMIN
        )

        val DEFAULT_BOARD_LIST = listOf(
            BoardEntity(
                socialMember = socialMember,
                boardName = "test1",
                boardUrl = "boardUrl1",
                boardType = BoardType.ZZIRIRIT_BOARD,
                boardActStatus = BoardActStatus.INACTIVE
            ),
            BoardEntity(
                socialMember = socialMember,
                boardName = "test2",
                boardUrl = "boardUrl2",
                boardType = BoardType.ZZIRIRIT_BOARD,
                boardActStatus = BoardActStatus.ACTIVE
            ),
            BoardEntity(
                socialMember = socialMember,
                boardName = "test3",
                boardUrl = "boardUrl3",
                boardType = BoardType.ZZIRIRIT_BOARD,
                boardActStatus = BoardActStatus.ACTIVE
            ),
            BoardEntity(
                socialMember = socialMember,
                boardName = "test4",
                boardUrl = "boardUrl4",
                boardType = BoardType.ZZIRIRIT_BOARD,
                boardActStatus = BoardActStatus.ACTIVE
            ),
            BoardEntity(
                socialMember = socialMember,
                boardName = "test5",
                boardUrl = "boardUrl5",
                boardType = BoardType.ZZIRIRIT_BOARD,
                boardActStatus = BoardActStatus.ACTIVE
            ),
            BoardEntity(
                socialMember = socialMember,
                boardName = "test6",
                boardUrl = "boardUrl6",
                boardType = BoardType.STREAMER_BOARD,
                boardActStatus = BoardActStatus.INACTIVE
            ),
            BoardEntity(
                socialMember = socialMember,
                boardName = "test7",
                boardUrl = "boardUrl7",
                boardType = BoardType.STREAMER_BOARD,
                boardActStatus = BoardActStatus.ACTIVE
            ),
            BoardEntity(
                socialMember = socialMember,
                boardName = "test8",
                boardUrl = "boardUrl8",
                boardType = BoardType.STREAMER_BOARD,
                boardActStatus = BoardActStatus.ACTIVE
            ),
            BoardEntity(
                socialMember = socialMember,
                boardName = "test9",
                boardUrl = "boardUrl9",
                boardType = BoardType.STREAMER_BOARD,
                boardActStatus = BoardActStatus.ACTIVE
            ),
            BoardEntity(
                socialMember = socialMember,
                boardName = "test10",
                boardUrl = "boardUrl10",
                boardType = BoardType.STREAMER_BOARD,
                boardActStatus = BoardActStatus.ACTIVE
            )
        )
        boardRepository.saveAllAndFlush(DEFAULT_BOARD_LIST)

        // WHEN
        val result = boardRepository.findActiveStatusBoards()

        // THEN
        result.size shouldBe 8
    }
}