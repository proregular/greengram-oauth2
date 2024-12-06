package com.green.greengramver.feed.like;

import com.green.greengramver.common.model.ResultResponse;
import com.green.greengramver.feed.like.model.FeedLikeReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("feed/like")
public class FeedLikeController {
    private final FeedLikeService service;

    @GetMapping
    public ResultResponse<Integer> feedLikeToggle(@ParameterObject @ModelAttribute FeedLikeReq p) {
        log.info("feedLikeToggle > p : {}", p);
        int result = service.feedLikeToggle(p);

        return ResultResponse.<Integer>builder()
                .resultMessage(result == 0 ? "좋아요 취소" : "좋아요 등록")
                .resultData(result)
                .build();
    }
}
