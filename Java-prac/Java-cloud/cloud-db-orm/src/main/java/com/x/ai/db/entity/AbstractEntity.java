package com.x.ai.db.entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@MappedSuperclass
public class AbstractEntity {
    private Long id;
    private Long createdAt;

    private Long updateAt;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(nullable = false, updatable = false)
    public Long getCreatedAt() {
        return createdAt;
    }

    @Column(nullable = false, updatable = false)
    public Long getUpdateAt() {
        return updateAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdateAt(Long updateAt) {
        this.updateAt = updateAt;
    }

    @Transient
    public ZonedDateTime getCreatedDateTime() {
        return Instant.ofEpochMilli(this.createdAt).atZone(ZoneId.systemDefault());
    }

    @Transient
    public ZonedDateTime getUpdatedDateTime() {
        return Instant.ofEpochMilli(this.updateAt).atZone(ZoneId.systemDefault());
    }

    @PrePersist
    public void preInsert() {
        setCreatedAt(System.currentTimeMillis());
        setUpdateAt(System.currentTimeMillis());
    }

//    不能有两个  @PrePersist, 原来还有一个 @PreUpdate
    @PostUpdate
    public void preUpdate() {
        setUpdateAt(System.currentTimeMillis());
    }
}