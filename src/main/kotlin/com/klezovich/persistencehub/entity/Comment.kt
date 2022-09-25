package com.klezovich.persistencehub.entity

import org.hibernate.Hibernate
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "comment")
open class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    open var id: Long? = null

    open var text: String? = null

    @ManyToOne
    @JoinColumn(name = "post_id")
    open var post: Post? = null

    @OneToOne(mappedBy = "comment", cascade = [CascadeType.ALL], orphanRemoval = true)
    open var commentDetails: CommentDetails? = null

    fun addCommentDetails(cd: CommentDetails) {
        commentDetails = cd
        cd.comment = this
    }

    fun removeCommentDetails(cd: CommentDetails) {
        commentDetails = null
        cd.comment = null
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Comment

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()
}