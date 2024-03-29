package kr.zziririt.zziririt.domain.board.model

import jakarta.persistence.*

@Entity
@Table(name = "category")
class CategoryEntity (
    @Column(name = "board_id")
    val boardId: Long,

    @Column(name = "category_name")
    var categoryName: String
){
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    fun updateCategoryName(newCategoryName: String) {
        this.categoryName = newCategoryName
    }

}