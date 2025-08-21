package org.yapp.apis

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.yapp.apis.config.MockTestConfiguration

@SpringBootTest
@Import(MockTestConfiguration::class)
abstract class BaseIntegrationTest
