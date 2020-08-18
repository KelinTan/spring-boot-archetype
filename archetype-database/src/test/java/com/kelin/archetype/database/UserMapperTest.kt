package com.kelin.archetype.database

import com.kelin.archetype.core.testing.KtBaseSpringTest
import com.kelin.archetype.core.testing.database.MockDatabase
import com.kelin.archetype.database.entity.primary.User
import com.kelin.archetype.database.mapper.primary.UserMapper
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest

@MockDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class UserMapperTest : KtBaseSpringTest() {
    @Autowired
    lateinit var userMapper: UserMapper

    @Test
    fun `find by name`() {
        userMapper.findByName("test1") verify {
            item(0) verify {
                id eq 1
                userName eq "test1"
            }
        }
    }

    @Test
    fun `find one by id `() {
        userMapper.findOne(1) verify {
            id eq 1
            userName eq "test1"
        }
    }

    @Test
    fun `find by entity`() {
        userMapper.findByEntity(User().apply {
            userName = "test2"
        }) verify {
            item(0) verify {
                id eq 2
                userName eq "test2"
            }
        }
    }

    @Test
    fun `insert selective`() {
        val user = User().apply {
            userName = "insert"
        }
        userMapper.insertSelective(user)

        userMapper.findByName("insert") verify {
            size greater 0
        }
    }

    @Test
    fun `update selective`() {
        val user = User().apply {
            id = 1
            userName = "update"
        }
        userMapper.updateSelective(user)

        userMapper.findByName("update") verify {
            size greater 0
        }
    }

    @Test
    fun `find by entity with page`() {
        userMapper.findByEntityWithPage(User(), PageRequest.of(1, 2)) verify {
            size eq 2
        }
    }

    @Test
    fun `count by entity`() {
        userMapper.countByEntity(User().apply {
            userName = "test1"
        }) eq 1
    }

    @Test
    fun `delete by id`() {
        userMapper.findOne(3) not null
        userMapper.delete(3)
        userMapper.findOne(3) eq null
    }

    @Test
    fun `update all`() {
        userMapper.update(User().apply {
            id = 3
            userName = "update"
        })

        userMapper.findOne(3) verify {
            userName eq "update"
        }
    }

    @Test
    fun `find page`() {
        userMapper.findPage(User(), PageRequest.of(1, 2)) verify {
            totalPages eq 2
            totalElements eq 4
            content verify {
                item(0) verify {
                    id eq 3
                }
                item(1) verify {
                    id eq 4
                }
            }
        }
    }
}