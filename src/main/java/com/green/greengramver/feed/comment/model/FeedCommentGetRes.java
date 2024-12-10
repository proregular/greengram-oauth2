package com.green.greengramver.feed.comment.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Schema(description = "피드의 댓글 정보")
@ToString
public class FeedCommentGetRes {
   private boolean moreComment;
   private List<FeedCommentDto> commentList;
}
