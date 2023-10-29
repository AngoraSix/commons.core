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

        // This header is used by the Google Cloud Run infrastructure in service-to-service authentication
        const val GOOGLE_CLOUD_RUN_INFRA_AUTH_HEADER = "X-Serverless-Authorization"
    }
}
