package com.green.greengramver.feed.model;

import com.green.greengramver.common.model.Paging;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.BindParam;

@Slf4j
@Getter
@Schema(title="피드 정보")
public class FeedGetReq extends Paging {
    @Schema(description = "로그인 유저 Pk", name="signed_user_id")
    private long signedUserId;

    //@ConstructorProperties({"page", "size", "signed_user_id"})
    public FeedGetReq(Integer page, Integer size, @BindParam("signed_user_id") long signedUserId) {
        super(page, size);
        this.signedUserId = signedUserId;
        log.info("page: {}, size: {}, userId: {}", page, size, signedUserId);
    }
}
