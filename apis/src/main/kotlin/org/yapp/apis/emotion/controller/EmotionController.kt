package org.yapp.apis.emotion.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.yapp.apis.emotion.dto.response.EmotionListResponse
import org.yapp.apis.emotion.service.EmotionService

@RestController
@RequestMapping("/api/v2/emotions")
class EmotionController(
    private val emotionService: EmotionService
) : EmotionControllerApi {

    @GetMapping
    override fun getEmotions(): ResponseEntity<EmotionListResponse> {
        val response = emotionService.getEmotionList()
        return ResponseEntity.ok(response)
    }
}
