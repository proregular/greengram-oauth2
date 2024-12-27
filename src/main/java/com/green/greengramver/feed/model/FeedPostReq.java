package com.green.greengramver.feed.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Schema(description = "피드 등록 정보")
@ToString
public class FeedPostReq {
    @JsonIgnore
    private long feedId;

    @Schema(description = "글쓴이 아이디", example = "12", requiredMode = Schema.RequiredMode.REQUIRED)
    private long writerUserId;
    @Size(max=1000, message = "내용은 1000자 이하만 가능합니다.")
    @Schema(description = "피드 내용", example = "블라블라")
    private String contents;
    @Size(max=30, message = "위치는 30자 이하만 가능합니다.")
    @Schema(description = "피드 위치", example = "대구")
    private String location;

    public void setSignedUserId(long writerUserId) {
        this.writerUserId = writerUserId;
    }
}
