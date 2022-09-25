package com.klezovich.persistencehub.entity

import org.hibernate.Hibernate
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.Table

@Entity
@Table(name = "tag")
open class Tag {

    constructor()
    constructor(name: String) {
        this.name = name
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    open var id: Long? = null

    open var name: String? = null

    @ManyToMany(cascade = [CascadeType.REFRESH])
    @JoinTable(
        name = "tag_posts",
        joinColumns = [JoinColumn(name = "tag_id")],
        inverseJoinColumns = [JoinColumn(name = "posts_id")]
    )
    open var posts: MutableSet<Post> = mutableSetOf()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Tag

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()
}