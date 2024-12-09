package com.green.greengramver.feed.model;

import com.green.greengramver.common.model.Paging;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.BindParam;

@Slf4j
@Getter
@ToString
@Schema(title="피드 정보")
public class FeedGetReq extends Paging {
    @Schema(description = "로그인 유저 Pk", name="signed_user_id")
    private long signedUserId;

    @Schema(title="프로필 유저 PK", name = "profile_user_id", example = "18", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long profileUserId;

    //@ConstructorProperties({"page", "size", "signed_user_id"})
    public FeedGetReq(Integer page, Integer size
            , @BindParam("signed_user_id") long signedUserId
            , @BindParam("profile_user_id") @Nullable Long profileUserId) {
        super(page, size);
        this.signedUserId = signedUserId;
        this.profileUserId = profileUserId;
        log.info("page: {}, size: {}, userId: {}", page, size, signedUserId);
    }
}
