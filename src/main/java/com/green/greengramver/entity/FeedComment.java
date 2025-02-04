package com.green.greengramver.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class FeedComment extends UpdatedAt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feedCommentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id")
    private Feed feed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 150, nullable = false)
    private String comment;
}
