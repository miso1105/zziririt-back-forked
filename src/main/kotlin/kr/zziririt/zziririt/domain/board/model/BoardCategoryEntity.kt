package kr.zziririt.zziririt.domain.board.model

import jakarta.persistence.*

@Entity
@Table(name = "board_category")
class BoardCategoryEntity (

    @ManyToOne
    @JoinColumn(name = "board_id")
    val board: BoardEntity,

    @ManyToOne
    @JoinColumn(name = "category_id")
    val category: CategoryEntity
){
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
}