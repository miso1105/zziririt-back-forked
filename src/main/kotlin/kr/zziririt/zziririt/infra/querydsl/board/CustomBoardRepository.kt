package kr.zziririt.zziririt.infra.querydsl.board

import kr.zziririt.zziririt.infra.jpa.board.BoardJpaRepository

interface CustomBoardRepository: BoardJpaRepository, BoardQueryDslRepository {
}