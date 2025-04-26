package com.angorasix.commons.domain.inputs

/**
 * <p>
 *     Similar to the HAL-FORM output, but to be transmitted inside the response body,
 *     at least until Spring provides support for defining options dynamically:
 *     https://docs.spring.io/spring-hateoas/docs/current/reference/html/#mediatypes.hal-forms.options.
 * </p>
 *
 * @author rozagerardo
 */
data class InlineFieldSpec(
    val name: String, // key for the UI to show the label appropriately
    val type: FieldSpec,
    val options: InlineFieldOptions? = null,
) {
    constructor(name: String, type: FieldSpec, options: List<OptionSpec>) : this(
        name,
        type,
        InlineFieldOptions(emptyList(), options),
    )
}

data class InlineFieldOptions(
    val selectedValues: List<String>,
    val inline: List<OptionSpec>,
)

data class OptionSpec(
    val value: String,
    val prompt: String, // key for the UI to show the label appropriately
    val promptData: Map<String, Any>? = null, // if it's a complex select field, this will be used to show the data
)

enum class FieldSpec(
    val value: String,
) {
    TEXT("text"),
    SELECT("select"),
    DATE("date"),
    TIME("time"),
    DATETIME("datetime"),
    SELECT_COMPLEX("select_complex"),
}
