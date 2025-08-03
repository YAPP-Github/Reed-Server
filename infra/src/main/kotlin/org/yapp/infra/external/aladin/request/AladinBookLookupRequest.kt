package org.yapp.infra.external.aladin.request

data class AladinBookLookupRequest private constructor(
    val itemId: String,
    val itemIdType: String,
    val optResult: List<String>?,
    val cover: String?
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
        cover?.let { params["Cover"] = it }
        return params
    }

    companion object {
        fun of(
            itemId: String,
            itemIdType: String,
            optResult: List<String>? = null,
            cover: String? = null
        ): AladinBookLookupRequest {
            return AladinBookLookupRequest(itemId, itemIdType, optResult, cover)
        }
    }
}
