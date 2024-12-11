package com.green.greengramver.feed.comment.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;

// Value Object
@Getter
@Builder
public class FeedCommentDto {
    @JsonIgnore
    private long feedId;
    private long feedCommentId;
    private String comment;
    private long writerUserId;
    private String writerNm;
    private String writerPic;
}
