package org.yapp.infra.external.aladin.request

data class AladinBookLookupRequest private constructor(
    val itemId: String,
    val itemIdType: String = "ISBN13",
    val output: String?,
    val cover: String?
) {
    fun toMap(): Map<String, Any> {
        val params = mutableMapOf<String, Any>()
        params["ItemId"] = itemId
        params["ItemIdType"] = itemIdType
        output?.let { params["Output"] = it }
        cover?.let { params["Cover"] = it }
        return params
    }

    companion object {
        fun from(
            itemId: String,
        ): AladinBookLookupRequest {
            return AladinBookLookupRequest(itemId, "ISBN13", "JS", "Big")
        }
    }
}
