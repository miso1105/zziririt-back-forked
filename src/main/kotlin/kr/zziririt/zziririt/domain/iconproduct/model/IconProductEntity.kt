package kr.zziririt.zziririt.domain.iconproduct.model

import jakarta.persistence.*
import kr.zziririt.zziririt.domain.icon.model.IconEntity
import kr.zziririt.zziririt.global.entity.BaseEntity
import kr.zziririt.zziririt.global.exception.ErrorCode
import kr.zziririt.zziririt.global.exception.RestApiException
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction

@Entity
@Table(name = "icon_shop")
@SQLDelete(sql = "UPDATE icon_shop SET is_deleted = true WHERE id = ?")
@SQLRestriction(value = "is_deleted = false")
class IconProductEntity(

    @ManyToOne
    @JoinColumn(name = "icon_id")
    val icon: IconEntity,

    @Column(name = "price")
    val price: Long,

    @Column(name = "icon_quantity")
    var iconQuantity: Int,

    @Enumerated(EnumType.STRING)
    @Column(name = "sale_status")
    var saleStatus: SaleStatus

) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    init {
        if (this.iconQuantity <= 0) {
            throw RestApiException(ErrorCode.ITEM_POLICY_VIOLATION)
        }
    }

    fun changeStatus(status: SaleStatus) {
        this.saleStatus = status
    }

    fun reduceIconQuantityAndChangeSaleStatus() {
        this.iconQuantity--

        if (this.iconQuantity == 0) {
            this.saleStatus = SaleStatus.SOLDOUT
        }
    }
}