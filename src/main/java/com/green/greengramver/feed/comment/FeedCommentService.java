package com.green.greengramver.feed.comment;

import com.green.greengramver.config.security.AuthenticationFacade;
import com.green.greengramver.feed.FeedService;
import com.green.greengramver.feed.comment.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedCommentService {
    private final FeedCommentMapper mapper;
    private final AuthenticationFacade authenticationFacade;

    public Long postFeedComment(FeedCommentPostReq p) {
        p.setFeedCommentId(authenticationFacade.getSignedUserId());
        
        int result = mapper.insFeedComment(p);

        return p.getFeedCommentId();
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

    public int delFeedComment(FeedCommentDelReq p) {
        p.setSignedUserId(authenticationFacade.getSignedUserId());
        return mapper.delFeedComment(p);
    }
}
