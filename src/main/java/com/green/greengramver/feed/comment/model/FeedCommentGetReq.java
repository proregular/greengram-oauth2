package com.green.greengramver.feed.comment.model;

import com.green.greengramver.common.Constants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.beans.ConstructorProperties;

@Getter
@Setter
@ToString
public class FeedCommentGetReq {

    @Positive
    @Schema(title="피드 PK", example = "4", requiredMode = Schema.RequiredMode.REQUIRED)
    private long feedId;

    @PositiveOrZero
    @Schema(title = "시작인덱스", description = "0 이상의 값만 입력", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer startIdx;

    @Min(value = 21, message = "사이즈는 20이상이어야 합니다.")
    @Schema(title = "페이지 당 아이템 수", description = "default: 20", example = "20")
    private int size;

    @ConstructorProperties({"feed_id", "start_idx", "size"})
    public FeedCommentGetReq(long feedId, int startIdx, Integer size) {
        this.feedId = feedId;
        this.startIdx = startIdx;
        this.size = (size == null ? Constants.getDefault_page_size() : size) + 1;
    }
}
