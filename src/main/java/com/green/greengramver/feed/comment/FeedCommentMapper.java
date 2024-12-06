package com.green.greengramver.feed.comment;

import com.green.greengramver.feed.comment.model.FeedCommentDelReq;
import com.green.greengramver.feed.comment.model.FeedCommentDto;
import com.green.greengramver.feed.comment.model.FeedCommentGetReq;
import com.green.greengramver.feed.comment.model.FeedCommentPostReq;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FeedCommentMapper {
    int insFeedComment(FeedCommentPostReq p);
    List<FeedCommentDto> selFeedCommentList(FeedCommentGetReq p);
    int delFeedComment(FeedCommentDelReq p);
}
