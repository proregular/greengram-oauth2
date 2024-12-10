package com.green.greengramver.feed;

import com.green.greengramver.feed.model.FeedPicDTO;
import com.green.greengramver.feed.model.FeedPicSel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FeedPicMapper {
    int insFeedPic(FeedPicDTO p);
    List<String> selFeedPic(long feedId);
    List<FeedPicSel> selFeedPicListByFeedIds(List<Long> feedIds);
}
