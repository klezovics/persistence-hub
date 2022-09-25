package com.klezovich.persistencehub.repository;

import com.klezovich.persistencehub.entity.Tag
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TagRepository : JpaRepository<Tag, Long>