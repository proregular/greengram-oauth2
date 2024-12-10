package com.green.greengramver.feed.model;

import com.green.greengramver.feed.comment.model.FeedCommentGetRes;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class FeedGetRes {
    private long feedId;
    private String contents;
    private String location;
    private String createdAt;
    private long writerUserId;
    private String writerNm;
    private String writerPic;
    private int isLike;

    private List<String> pics = new ArrayList<>();
    private FeedCommentGetRes comment;
}
