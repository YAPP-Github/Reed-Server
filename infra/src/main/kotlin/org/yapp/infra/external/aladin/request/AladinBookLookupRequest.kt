package org.yapp.infra.external.aladin.request

data class AladinBookLookupRequest private constructor(
    val itemId: String,
    val itemIdType: String,
    val optResult: List<String>?
) {
    fun toMap(): Map<String, Any> {
        val params = mutableMapOf<String, Any>()
        params["ItemId"] = itemId
        params["ItemIdType"] = itemIdType
        optResult?.let {
            if (it.isNotEmpty()) {
                params["OptResult"] = it
            }
        }
        return params
    }

    companion object {
        fun create(
            itemId: String,
            itemIdType: String,
            optResult: List<String>? = null
        ): AladinBookLookupRequest {
            return AladinBookLookupRequest(itemId, itemIdType, optResult)
        }
    }
}
