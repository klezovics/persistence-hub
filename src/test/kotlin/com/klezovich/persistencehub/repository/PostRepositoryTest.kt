package com.klezovich.persistencehub.repository

import com.klezovich.persistencehub.entity.Comment
import com.klezovich.persistencehub.entity.CommentDetails
import com.klezovich.persistencehub.entity.Post
import com.klezovich.persistencehub.entity.Tag
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import javax.persistence.EntityManager
import javax.transaction.Transactional

@DataJpaTest
class PostRepositoryTest {

    @Autowired
    lateinit var postRepository: PostRepository

    @Autowired
    lateinit var tagRepository: TagRepository

    @Autowired
    lateinit var em: EntityManager

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

        em.flush()

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

    @Test
    fun testEditInTransaction() {
        val id = Post().apply { text = "Initial text" }.also { postRepository.save(it) }.id!!
        doInTransaction(id)

        postRepository.findById(id).get().let {
            assertEquals("AUTO FLUSH TEXT", it.text)
        }
    }


    @Transactional(value = Transactional.TxType.REQUIRES_NEW)
    fun doInTransaction(postId: Long) {
        postRepository.findById(postId).get().apply {
            text = "AUTO FLUSH TEXT"
        }.also {
            // postRepository.save(it)
        }
    }


    @Test
    fun `test can save posts and tags`() {
        val t1 = Tag("T1")
        val t2 = Tag("T2")

        tagRepository.saveAll(listOf(t1,t2))

        val p1 = Post("P1")
        val p2 = Post("P2")

        p1.addTag(t1)
        p2.addTag(t1)
        p2.addTag(t2)

        val ids = postRepository.saveAll(listOf(p1,p2)).map { it.id }

        em.flush()

        ids.first().also {
            postRepository.findById(it!!).get().run {
                assertEquals("P1", text)
                assertEquals(1, tags.size)
                assertEquals(setOf("T1"), tags.map { it.name }.toSet() )
            }
        }

        ids[1].also {
            postRepository.findById(it!!).get().run {
                assertEquals("P2", text)
                assertEquals(2, tags.size)
                assertEquals(setOf("T1","T2"), tags.map { it.name }.toSet())
            }
        }
    }
}