package com.green.greengramver.feed.like;

import com.green.greengramver.feed.like.model.FeedLikeReq;
import com.green.greengramver.feed.like.model.FeedLikeVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FeedLikeTestMapper {
    @Select("SELECT * FROM feed_like WHERE feed_id = #{feedId} AND user_id = #{userId}")
    FeedLikeVo selFeedLikeByFeedIdAndUserId(FeedLikeReq p);

    // 리스트 일때는 null이 아니라 사이즈 0짜리 ArrayList가 넘어온다(null 안넘어옴 주의 리턴 타입을 객체로 했을 때는 null이 넘어간다.)
    @Select("SELECT * FROM feed_like")
    List<FeedLikeVo> selFeedLikeAll();
}
