package kr.zziririt.zziririt.api.post.dto.valid

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import kr.zziririt.zziririt.api.post.dto.SearchType
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [SearchTypeValidator::class])
annotation class ValidSearchType(
    val message: String = "올바르지 않은 search type입니다.",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Any>> = []
)

class SearchTypeValidator : ConstraintValidator<ValidSearchType, String> {
    override fun initialize(constraintAnnotation: ValidSearchType) {}

    override fun isValid(value: String?, context: ConstraintValidatorContext): Boolean {
        return value == null || SearchType.entries.any { it.name == value.uppercase() }
    }
}