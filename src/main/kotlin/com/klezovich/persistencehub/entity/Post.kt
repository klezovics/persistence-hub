package com.klezovich.persistencehub.entity

import org.hibernate.Hibernate
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToMany
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "post")
open class Post {

    constructor()
    constructor(text: String) {
        this.text = text
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    open var id: Long? = null
    open var text: String? = null


    @OneToMany(mappedBy = "post", cascade = [CascadeType.ALL], orphanRemoval = true)
    open var comments: MutableSet<Comment> = mutableSetOf()

    @ManyToMany(
        mappedBy = "posts",
        cascade = [CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH]
    )
    open var tags: MutableSet<Tag> = mutableSetOf()

    fun addTag(tag: Tag) {
        tags.add(tag)
        tag.posts.add(this)
    }

    fun removeTag(tag: Tag) {
        tag.posts.remove(this)
        tags.remove(tag)
    }


    fun addComment(c: Comment) {
        comments.add(c)
        c.post = this
    }

    fun removeComment(c: Comment) {
        c.post = null
        comments.remove(c)
    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Post

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()
}