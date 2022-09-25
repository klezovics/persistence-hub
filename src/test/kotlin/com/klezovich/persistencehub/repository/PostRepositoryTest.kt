package com.klezovich.persistencehub.repository

import com.klezovich.persistencehub.entity.Comment
import com.klezovich.persistencehub.entity.CommentDetails
import com.klezovich.persistencehub.entity.Post
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class PostRepositoryTest {

    @Autowired
    lateinit var postRepository: PostRepository

    @Test
    fun `test can save and load entity graph`() {
        val id = Post().apply {
            text = "Ban Hibernate"
            addComment(Comment().apply {
                text = "Oh no"
                addCommentDetails(CommentDetails().apply {
                    details = "Not so much!"
                })
            })
        }.also {
            postRepository.save(it)
        }.id!!

        postRepository.findById(id).get().run {
            assertEquals("Ban Hibernate", text)
            assertEquals(1, comments.size)
            comments.first().run {
                assertEquals("Oh no", text)
                assertNotNull(commentDetails)
                commentDetails!!.run {
                    assertEquals(details, "Not so much!")
                }
            }
        }
    }

}