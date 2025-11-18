package com.angorasix.commons.presentation.dto

import com.angorasix.commons.domain.inputs.FieldSpec
import com.angorasix.commons.domain.inputs.InlineFieldOptions
import com.angorasix.commons.domain.inputs.InlineFieldSpec
import com.angorasix.commons.domain.inputs.OptionSpec

/**
 * <p>
 *     Similar to the HAL-FORM output, but to be transmitted inside the response body,
 *     at least until Spring provides support for defining options dynamically:
 *     https://docs.spring.io/spring-hateoas/docs/current/reference/html/#mediatypes.hal-forms.options.
 * </p>
 *
 * @author rozagerardo
 */
data class InlineFieldSpecDto(
    val name: String, // key for the UI to show the label appropriately
    val type: FieldSpec,
    val options: InlineFieldOptionsDto?,
    val promptData: Map<String, Any>? = null, // if it's a complex select field, this will be used to show the data
)

data class InlineFieldOptionsDto(
    val selectedValues: List<String>,
    val inline: List<OptionSpecDto>,
)

data class OptionSpecDto(
    val prompt: String, // key for the UI to show the label appropriately
    val value: String,
    val promptData: Map<String, Any>? = null, // if it's a complex select field, this will be used to show the data
)

fun InlineFieldSpec.convertToDto(): InlineFieldSpecDto = InlineFieldSpecDto(name, type, options?.convertToDto(), promptData)

fun InlineFieldOptions.convertToDto(): InlineFieldOptionsDto = InlineFieldOptionsDto(selectedValues, inline.map { it.convertToDto() })

fun OptionSpec.convertToDto(): OptionSpecDto = OptionSpecDto(prompt, value, promptData)
