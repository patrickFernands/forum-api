package com.example.forum.forum_api.entities;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class Vote {

    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = false)
    private User voter;

    @Column(name = "is_upvote", nullable = false)
    private Boolean isUpvote;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Vote() {
    }

    public Vote(User voter, Boolean isUpvote) {
        this.voter = voter;
        this.isUpvote = isUpvote;
    }

    public User getVoter() {
        return voter;
    }

    public Boolean getIsUpvote() {
        return isUpvote;
    }

    public void upvote() {
        isUpvote = true;
    }

    public void downvote() {
        isUpvote = false;
    }

    public Long getId() {
        return id;
    }

}
