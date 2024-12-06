package com.green.greengramver.feed.like;

import com.green.greengramver.feed.like.model.FeedLikeReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedLikeService {
    private final FeedLikeMapper mapper;

    public int feedLikeToggle(FeedLikeReq p) {
        int result = mapper.delFeedLike(p);

        if(result == 0) {
            return mapper.insFeedLike(p); // 좋아요 등록이 되었을 때 return 1
        }

        return 0; // 좋아요가 취소 되었을 때 return 0
    }
}
