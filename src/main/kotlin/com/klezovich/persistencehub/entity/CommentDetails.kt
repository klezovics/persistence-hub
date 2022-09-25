package com.klezovich.persistencehub.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "comment_details")
open class CommentDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    open var id: Long? = null

    open var details:String?= null

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "comment_id")
    open var comment: Comment? = null
}