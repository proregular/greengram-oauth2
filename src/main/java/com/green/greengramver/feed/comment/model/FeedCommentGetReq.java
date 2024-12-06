package com.green.greengramver.feed.comment.model;

import com.green.greengramver.common.Constants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.beans.ConstructorProperties;

@Getter
@Setter
@ToString
public class FeedCommentGetReq {
    @Schema(title="피드 PK", example = "4", requiredMode = Schema.RequiredMode.REQUIRED)
    private long feedId;

    @Schema(title = "시작인덱스", description = "0 이상의 값만 입력", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer startIdx;

    @Schema(title = "페이지 당 아이템 수", description = "default: 20", example = "20")
    private int size;

    @ConstructorProperties({"feed_id", "start_idx", "size"})
    public FeedCommentGetReq(long feedId, int startIdx, Integer size) {
        this.feedId = feedId;
        this.startIdx = startIdx;
        this.size = (size == null ? Constants.getDefault_page_size() : size) + 1;
    }
}
