package kr.zziririt.zziririt.global.entity

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.jpa.domain.support.AuditingEntityListener

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity : BaseTimeEntity() {

    @Column(name = "is_deleted", nullable = false)
    var isDeleted: Boolean = false
        protected set

    @CreatedBy
    @Column(name = "created_by")
    var createdBy: Long? = null
        protected set

    @LastModifiedBy
    @Column(name = "modified_by")
    var modifiedBy: Long? = null
        protected set
}