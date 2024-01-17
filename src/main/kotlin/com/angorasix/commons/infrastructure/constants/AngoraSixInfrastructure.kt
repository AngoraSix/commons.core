package com.angorasix.commons.infrastructure.constants

/**
 * <p>
 * </p>
 *
 * @author rozagerardo
 */
class AngoraSixInfrastructure private constructor() {
    companion object {
        const val REQUEST_ATTRIBUTE_CONTRIBUTOR_KEY = "ANGORASIX_REQUEST_CONTRIBUTOR"

        // This header should be used only to resolve HATEOAS actions, it shouldn't be used for security decisions
        const val REQUEST_IS_ADMIN_HINT_HEADER = "A6-IsAdminHint"

        const val REQUEST_ATTRIBUTE_AFFECTED_CONTRIBUTORS_KEY = "ANGORASIX_AFFECTED_CONTRIBUTORS"

        // This header is used to indicate a request will trigger an event
        // We'll want to retrieve the affected contributors' ids as a header in that case
        const val TRIGGERS_EVENT_HEADER = "A6-Triggers-Event"

        // This header is used to transmit the Contributor Ids that are affected by an event within backend services
        const val EVENT_AFFECTED_CONTRIBUTOR_IDS_HEADER = "A6-Event-Affected-Contributors"

        // This header is used by the Google Cloud Run infrastructure in service-to-service authentication
        const val GOOGLE_CLOUD_RUN_INFRA_AUTH_HEADER = "X-Serverless-Authorization"
    }
}
