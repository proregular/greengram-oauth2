package com.green.greengramver.feed.comment.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(description = "피드의 댓글 정보")
public class FeedCommentGetRes {
   private boolean moreComment;
   private List<FeedCommentDto> commentList;
}
