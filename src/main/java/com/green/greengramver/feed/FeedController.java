package com.green.greengramver.feed;

import com.green.greengramver.common.model.ResultResponse;
import com.green.greengramver.feed.model.FeedGetReq;
import com.green.greengramver.feed.model.FeedGetRes;
import com.green.greengramver.feed.model.FeedPostReq;
import com.green.greengramver.feed.model.FeedPostRes;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/feed")
public class FeedController {
    private final FeedService service;

    @PostMapping
    @Operation(summary = "Feed 등록", description = "피드를 등록하는 API")
    public ResultResponse<FeedPostRes> postFeed(@RequestPart List<MultipartFile> pics
                                              , @RequestPart FeedPostReq p) {
        log.info("p----------------------: {}", p);
        FeedPostRes res = service.postFeed(pics, p);

        return ResultResponse.<FeedPostRes>builder()
                .resultMessage("피드 등록 완료")
                .resultData(res)
                .build();
    }

    @GetMapping
    @Operation(summary = "Feed 조회", description = "피드를 조회하는 API")
    public ResultResponse<List<FeedGetRes>> getFeedList(@ParameterObject @ModelAttribute FeedGetReq p) {
        log.info("p----------------------: {}", p);

        List<FeedGetRes> list = service.getFeedList(p);

        return ResultResponse.<List<FeedGetRes>>builder()
                .resultMessage(String.format("%d rows", list.size()))
                .resultData(list)
                .build();
    }
}
