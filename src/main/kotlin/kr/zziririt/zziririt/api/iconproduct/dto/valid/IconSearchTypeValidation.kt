package kr.zziririt.zziririt.api.iconproduct.dto.valid

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import kr.zziririt.zziririt.domain.iconproduct.model.SaleStatus
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [IconSearchTypeValidator::class])
annotation class ValidSaleStatusType(
    val message: String = "올바르지 않은 SaleStatus 값입니다.",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Any>> = []
)

class IconSearchTypeValidator : ConstraintValidator<ValidSaleStatusType, String> {
    override fun initialize(constraintAnnotation: ValidSaleStatusType) {}

    override fun isValid(value: String?, context: ConstraintValidatorContext): Boolean {
        return value == null || SaleStatus.entries.any { it.name == value.uppercase() }
    }
}