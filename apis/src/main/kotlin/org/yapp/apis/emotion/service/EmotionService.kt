package org.yapp.apis.emotion.service

import org.yapp.apis.emotion.dto.response.EmotionListResponse
import org.yapp.domain.detailtag.DetailTagDomainService
import org.yapp.globalutils.annotation.ApplicationService

@ApplicationService
class EmotionService(
    private val detailTagDomainService: DetailTagDomainService
) {
    fun getEmotionList(): EmotionListResponse {
        val detailTags = detailTagDomainService.findAll()
        return EmotionListResponse.from(detailTags)
    }
}

