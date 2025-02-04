package com.green.greengramver.feed.comment;

import com.green.greengramver.common.exception.CustomException;
import com.green.greengramver.common.exception.FeedErrorCode;
import com.green.greengramver.config.security.AuthenticationFacade;
import com.green.greengramver.entity.Feed;
import com.green.greengramver.entity.FeedComment;
import com.green.greengramver.entity.User;
import com.green.greengramver.feed.FeedService;
import com.green.greengramver.feed.comment.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedCommentService {
    private final FeedCommentMapper mapper;
    private final AuthenticationFacade authenticationFacade;
    private final FeedCommentRepository feedCommentRepository;

    public Long postFeedComment(FeedCommentPostReq p) {
//        p.setFeedCommentId(authenticationFacade.getSignedUserId());
//        int result = mapper.insFeedComment(p);
//        return p.getFeedCommentId();
        Feed feed = new Feed();

        feed.setFeedId(p.getFeedId());

        User user = new User();
        user.setUserId(authenticationFacade.getSignedUserId());

        FeedComment feedComment = new FeedComment();
        feedComment.setFeed(feed);
        feedComment.setUser(user);
        feedComment.setComment(p.getComment());

        feedCommentRepository.save(feedComment);

        return feedComment.getFeedCommentId();
    }

    public FeedCommentGetRes getFeedComment(FeedCommentGetReq p) {
        FeedCommentGetRes res = new FeedCommentGetRes();

        /*if(p.getStartIdx() < 1) {
            res.setCommentList(new ArrayList<>());
            return res;
        }*/

        List<FeedCommentDto> commentList = mapper.selFeedCommentList(p);

        res.setCommentList(commentList);
        res.setMoreComment(commentList.size() == p.getSize());

        if(res.isMoreComment()) {
            commentList.remove(commentList.size() - 1);
        }

        res.setCommentList(commentList);

        return res;
    }

    @Transactional
    public void delFeedComment(FeedCommentDelReq p) {
        // 내가 한 버젼
//        p.setSignedUserId(authenticationFacade.getSignedUserId());
//        return mapper.delFeedComment(p);

//        User signedUser = new User();
//        signedUser.setUserId(authenticationFacade.getSignedUserId());
//
//        int affectRows = feedCommentRepository.deleteFeedComment(p.getFeedCommentId(), authenticationFacade.getSignedUserId());
//        log.info("affectRows: " + affectRows);
//        if(affectRows == 0) {
//            throw new CustomException(FeedErrorCode.FAIL_TO_DEL);
//        }
//
//        return affectRows;
        // 강사님 버젼
        FeedComment feedComment = feedCommentRepository.findById(p.getFeedCommentId()).orElse(null);
                                // 그래프 탐색: feedComment 테이블 내용을 가져왔는데 User 테이블 정보를 탐색
        if(feedComment == null || feedComment.getUser().getUserId().equals(authenticationFacade.getSignedUserId())) {
            throw new CustomException(FeedErrorCode.FAIL_TO_DEL_COMMENT);
        }
        feedCommentRepository.delete(feedComment);
    }
}
