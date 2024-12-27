package com.green.greengramver.feed.comment;

import com.green.greengramver.common.model.ResultResponse;
import com.green.greengramver.feed.comment.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("feed/comment")
public class FeedCommentController {
    private final FeedCommentService service;

    @PostMapping
    public ResultResponse<Long> postFeedComment(@Valid @RequestBody FeedCommentPostReq p) {
        long result = service.postFeedComment(p);

        return ResultResponse.<Long>builder()
                .resultMessage("댓글 등록 성공")
                .resultData(result)
                .build();
    }

    @GetMapping
    public ResultResponse<FeedCommentGetRes> getFeedComment(@Valid @ParameterObject @ModelAttribute FeedCommentGetReq p) {
        FeedCommentGetRes res = service.getFeedComment(p);

        return ResultResponse.<FeedCommentGetRes>builder()
                .resultMessage(String.format("%d rows", res.getCommentList().size()))
                .resultData(res)
                .build();
    }

    @GetMapping("/ver2")
    @Operation(summary = "피드 댓글 리스트", description = "댓글 더보기 처리")
    public ResultResponse<FeedCommentGetRes> getFeedComment2(@Parameter(description = "피드 PK", example = "4") @RequestParam("feed_id") long feedId
                                                           , @Parameter(description = "페이지", example = "20") @RequestParam int page) {
        FeedCommentGetReq p = new FeedCommentGetReq(0, 0, 0);
        p.setFeedId(feedId);


        FeedCommentGetRes res = service.getFeedComment(p);

        return ResultResponse.<FeedCommentGetRes>builder()
                .resultMessage(String.format("%d rows", res.getCommentList().size()))
                .resultData(res)
                .build();
    }

    // 삭제시 받아야 할 데이터 feedComment + 로그인한 사용자의 PK
    // FE -data 전달 방식 : Query-String
    @DeleteMapping
    public ResultResponse<Integer> delFeedComment(@ParameterObject @ModelAttribute FeedCommentDelReq p) {
        int result = service.delFeedComment(p);

        String msg = "삭제에 성공했습니다.";

        if(result == 0) {
            msg = "삭제에 실패했습니다.";
        }

        return ResultResponse.<Integer>builder()
                .resultMessage(msg)
                .resultData(result)
                .build();
    }
}
