package org.yapp.apis.book.service.impl

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.yapp.apis.book.dto.request.BookDetailRequest
import org.yapp.apis.book.dto.request.BookSearchRequest
import org.yapp.apis.book.dto.response.BookDetailResponse
import org.yapp.apis.book.dto.response.BookSearchResponse
import org.yapp.apis.book.constant.BookQueryServiceQualifier
import org.yapp.apis.book.service.BookQueryService
import org.yapp.infra.external.aladin.helper.AladinApiHelper
import org.yapp.infra.external.aladin.response.AladinBookDetailResponse
import org.yapp.infra.external.aladin.response.AladinSearchResponse

@Service
@Qualifier(BookQueryServiceQualifier.ALADIN)
class AladinBookQueryService(
    private val aladinApiHelper: AladinApiHelper,
) : BookQueryService {
    private val log = KotlinLogging.logger {}

    override fun searchBooks(request: BookSearchRequest): BookSearchResponse {
        log.info("Service - Converting BookSearchRequest to AladinBookSearchRequest and calling Aladin API for book search.")
        val aladinSearchRequest = request.toAladinRequest()
        val response: AladinSearchResponse = aladinApiHelper.searchBooks(aladinSearchRequest)
        return BookSearchResponse.Companion.from(response)
    }


    override fun getBookDetail(request: BookDetailRequest): BookDetailResponse {
        log.info("Service - Converting BookDetailRequest to AladinBookLookupRequest and calling Aladin API for book detail lookup.")
        val aladinLookupRequest = request.toAladinRequest()
        val aladinResponse: AladinBookDetailResponse = aladinApiHelper.lookupBook(aladinLookupRequest)
        return BookDetailResponse.Companion.from(aladinResponse)
    }
}
