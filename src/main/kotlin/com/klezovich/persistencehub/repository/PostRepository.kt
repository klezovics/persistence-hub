package com.klezovich.persistencehub.repository

import com.klezovich.persistencehub.entity.Post
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PostRepository : JpaRepository<Post, Long> {


    fun findAllByOrderByTextDesc(): List<Post>

}