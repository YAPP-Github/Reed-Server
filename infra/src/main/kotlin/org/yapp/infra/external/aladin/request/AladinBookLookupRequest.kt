package org.yapp.infra.external.aladin.request

import org.yapp.globalutils.book.BookCoverSize

data class AladinBookLookupRequest private constructor(
    val itemId: String,
    val itemIdType: String = "ISBN13",
    val cover: String?
) {
    fun toMap(): Map<String, Any> {
        val params = mutableMapOf<String, Any>()
        params["ItemId"] = itemId
        params["ItemIdType"] = itemIdType
        cover?.let { params["Cover"] = it }
        return params
    }

    companion object {
        fun from(
            itemId: String,
        ): AladinBookLookupRequest {
            return AladinBookLookupRequest(itemId, "ISBN13", BookCoverSize.BIG.apiValue)
        }
    }
}
