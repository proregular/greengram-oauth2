package com.green.greengramver.feed;

import com.green.greengramver.feed.model.FeedDeleteReq;
import com.green.greengramver.feed.model.FeedGetReq;
import com.green.greengramver.feed.model.FeedGetRes;
import com.green.greengramver.feed.model.FeedPostReq;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FeedMapper {
    int insFeed(FeedPostReq p);
    List<FeedGetRes> selFeedList(FeedGetReq p);

    int delFeedLikeAndFeedCommentAndFeedPic(FeedDeleteReq p);
    int delFeed(FeedDeleteReq p);
}
