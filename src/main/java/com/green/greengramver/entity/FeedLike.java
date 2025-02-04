package com.green.greengramver.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class FeedLike  extends CreatedAt {
    @EmbeddedId
    private FeedLikeIds feedLikeIds;

    @ManyToOne
    @MapsId("feedId")
    @JoinColumn(name = "feed_id")
    private Feed feed;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;
}
