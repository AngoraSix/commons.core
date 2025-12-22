package com.angorasix.commons.infrastructure.intercommunication.survey

class SurveyRegistered(
    val surveyResponseId: String,
    val surveyKey: String,
    val response: Map<String, Any>,
    val contributorId: String? = null,
)
