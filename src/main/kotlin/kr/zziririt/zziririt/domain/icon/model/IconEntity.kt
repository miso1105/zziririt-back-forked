package kr.zziririt.zziririt.domain.icon.model

import jakarta.persistence.*
import kr.zziririt.zziririt.global.entity.BaseEntity
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction

@Entity
@Table(name = "icon")
@SQLDelete(sql = "UPDATE icon SET is_deleted = true WHERE id = ?")
@SQLRestriction(value = "is_deleted = false")
class IconEntity(

    @Column(name = "icon_name")
    val iconName: String,

    @Column(name = "icon_url")
    val iconUrl: String,

    @Column(name = "icon_request_url")
    val iconApplicationUrl: String,

    @Column(name = "icon_maker")
    val iconMaker: String,

    ) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
}