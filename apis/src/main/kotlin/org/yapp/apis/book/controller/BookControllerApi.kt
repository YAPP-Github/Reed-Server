package org.yapp.apis.book.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.yapp.apis.book.dto.request.BookDetailRequest
import org.yapp.apis.book.dto.request.BookSearchRequest
import org.yapp.apis.book.dto.request.UserBookRegisterRequest
import org.yapp.apis.book.dto.response.BookDetailResponse
import org.yapp.apis.book.dto.response.BookSearchResponse
import org.yapp.apis.book.dto.response.UserBookPageResponse
import org.yapp.apis.book.dto.response.UserBookResponse
import org.yapp.domain.userbook.BookStatus
import org.yapp.domain.userbook.UserBookSortType
import org.yapp.globalutils.exception.ErrorResponse
import java.util.*


@Tag(name = "Books", description = "도서 정보를 조회하는 API")
@RequestMapping("/api/v1/books")
interface BookControllerApi {

    @Operation(
        summary = "도서 검색", description = "알라딘 API를 통해 키워드로 도서를 검색합니다.  \n" +
                " 유저의 도서 상태(읽음, 읽는 중 등)가 함께 표시됩니다.  "
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
                description = "잘못된 요청 파라미터",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @GetMapping("/search")
    fun searchBooks(
        @AuthenticationPrincipal userId: UUID,
        @Valid @Parameter(description = "도서 검색 요청 객체") request: BookSearchRequest
    ): ResponseEntity<BookSearchResponse>

    @Operation(summary = "도서 상세 조회", description = "특정 도서의 상세 정보를 조회합니다.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "성공적으로 도서 상세 정보를 조회했습니다.",
                content = [Content(schema = Schema(implementation = BookDetailResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "잘못된 요청 파라미터",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "해당하는 itemId를 가진 도서를 찾을 수 없습니다.",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @GetMapping("/detail")
    fun getBookDetail(
        @AuthenticationPrincipal userId: UUID,
        @Valid @Parameter(description = "도서 상세 조회 요청 객체") request: BookDetailRequest
    ): ResponseEntity<BookDetailResponse>

    @Operation(summary = "서재에 책 등록 또는 상태 업데이트 (Upsert)", description = "사용자의 서재에 책을 등록하거나, 이미 등록된 책의 상태를 업데이트합니다.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "책이 서재에 성공적으로 등록되었습니다.",
                content = [Content(schema = Schema(implementation = UserBookResponse::class))]
            ),
            ApiResponse(
                responseCode = "200",
                description = "책 상태가 성공적으로 업데이트되었습니다.",
                content = [Content(schema = Schema(implementation = UserBookResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "잘못된 요청 파라미터",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "존재하지 않는 책 (ISBN 오류)",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @PutMapping("/upsert")
    fun upsertBookToMyLibrary(
        @AuthenticationPrincipal userId: UUID,
        @Valid @RequestBody request: UserBookRegisterRequest
    ): ResponseEntity<UserBookResponse>

    @Operation(summary = "사용자 서재 조회", description = "현재 사용자의 서재에 등록된 모든 책을 조회합니다.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "서재 조회 성공",
                content = [Content(schema = Schema(implementation = UserBookPageResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid request or credentials",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "사용자를 찾을 수 없습니다.",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @GetMapping("/my-library")
    fun getUserLibraryBooks(
        @AuthenticationPrincipal userId: UUID,
        @RequestParam(required = false) status: BookStatus?,
        @RequestParam(required = false) sort: UserBookSortType?,
        @PageableDefault(size = 10, sort = ["createdAt"], direction = Sort.Direction.DESC)
        pageable: Pageable
    ): ResponseEntity<UserBookPageResponse>
}
