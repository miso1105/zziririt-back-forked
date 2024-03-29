package kr.zziririt.zziririt.infra.querydsl.board

interface BoardQueryDslRepository {

    fun findBoards(): List<BoardRowDto>

    fun findStreamers(): List<StreamerBoardRowDto>

    fun findBoardStatusToInactive(): List<Long>

    fun updateBoardStatusToInactive(inactiveBoardIdList: List<Long>)

    fun findActiveStatusBoards(): List<BoardRowDto>
}