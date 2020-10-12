package com.kelin.archetype.jwt

import com.kelin.archetype.test.KtBaseSpringTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

/**
 * @author Kelin Tan
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class JwtManagerTest : KtBaseSpringTest() {
    @Autowired
    lateinit var jwtManager: JwtManager

    @Test
    fun `test jwt generate and verify`() {
        val generateToken = jwtManager.generateToken("test") not null

        jwtManager.verify(generateToken) verify {
            subject eq "test"
        }
    }
}