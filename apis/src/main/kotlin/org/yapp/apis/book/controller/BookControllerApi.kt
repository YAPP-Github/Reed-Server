package org.yapp.apis.book.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.yapp.apis.book.dto.request.BookDetailRequest
import org.yapp.apis.book.dto.request.BookSearchRequest
import org.yapp.apis.book.dto.response.BookDetailResponse
import org.yapp.apis.book.dto.response.BookSearchResponse

/**
 * API interface for book controller.
 */
@Tag(name = "Books", description = "도서 정보를 조회하는 API")
@RequestMapping("/api/v1/books")
interface BookControllerApi {

    @Operation(
        summary = "도서 검색",
        description = "키워드를 사용하여 알라딘 도서 정보를 검색합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "성공적인 검색",
                content = [Content(schema = Schema(implementation = BookSearchResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "잘못된 요청 파라미터"
            )
        ]
    )
    @GetMapping("/search")
    fun searchBooks(
        @Valid
        @Parameter(
            description = "도서 검색 요청 객체. 다음 쿼리 파라미터를 포함합니다:<br>" +
                    "- `query` (필수): 검색어 <br>" +
                    "- `queryType` (선택): 검색어 타입 (예: Title, Author). 기본값은 All <br>" +
                    "- `maxResults` (선택): 한 페이지당 결과 개수 (1-50). 기본값 10 <br>" +
                    "- `start` (선택): 결과 시작 페이지. 기본값 1 <br>" +
                    "- `sort` (선택): 정렬 방식 (예: PublishTime, SalesPoint). 기본값 Accuracy <br>" +
                    "- `categoryId` (선택): 카테고리 ID",
            examples = [
                ExampleObject(name = "기본 검색", value = "http://localhost:8080/api/v1/books/search?query=코틀린"),
                ExampleObject(
                    name = "상세 검색",
                    value = "http://localhost:8080/api/v1/books/search?query=클린코드&queryType=Title&maxResults=10&sort=PublishTime"
                ),
                ExampleObject(
                    name = "카테고리 검색",
                    value = "http://localhost:8080/api/v1/books/search?query=Spring&categoryId=170&start=2&maxResults=5"
                )
            ]
        )
        request: BookSearchRequest
    ): ResponseEntity<BookSearchResponse>


    @Operation(
        summary = "도서 상세 조회",
        description = "특정 도서의 상세 정보를 조회합니다. `itemId`는 쿼리 파라미터로 전달됩니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "성공적으로 도서 상세 정보를 조회했습니다.",
                content = [Content(schema = Schema(implementation = BookDetailResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "잘못된 요청 파라미터 (예: 유효하지 않은 itemId 또는 itemIdType)"
            ),
            ApiResponse(
                responseCode = "404",
                description = "해당하는 itemId를 가진 도서를 찾을 수 없습니다."
            )
        ]
    )
    @GetMapping("/detail")
    fun getBookDetail(
        @Valid
        @Parameter(
            description = "도서 상세 조회 요청 객체. 다음 쿼리 파라미터를 포함합니다:<br>" +
                    "- `itemId` (필수): 조회할 도서의 고유 ID (ISBN, ISBN13, 알라딘 ItemId 등)<br>" +
                    "- `itemIdType` (선택): `itemId`의 타입 (ISBN, ISBN13, ItemId). 기본값은 ISBN입니다.<br>" +
                    "- `optResult` (선택): 조회할 부가 정보 목록 (쉼표로 구분). 예시: `BookInfo,Toc,PreviewImg`",
            examples = [
                ExampleObject(
                    name = "ISBN으로 상세 조회",
                    value = "http://localhost:8080/api/v1/books/detail?itemId=9791162241684&itemIdType=ISBN13"
                ),
                ExampleObject(
                    name = "ISBN 및 부가 정보 포함",
                    value = "http://localhost:8080/api/v1/books/detail?itemId=8994492040&itemIdType=ISBN&optResult=BookInfo,Toc"
                )
            ]
        )
        request: BookDetailRequest
    ): ResponseEntity<BookDetailResponse>
}
