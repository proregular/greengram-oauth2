package com.green.greengramver.feed;

import com.green.greengramver.common.MyFileUtils;
import com.green.greengramver.common.exception.CustomException;
import com.green.greengramver.common.exception.FeedErrorCode;
import com.green.greengramver.config.security.AuthenticationFacade;
import com.green.greengramver.entity.Feed;
import com.green.greengramver.entity.User;
import com.green.greengramver.feed.comment.FeedCommentMapper;
import com.green.greengramver.feed.comment.model.FeedCommentDto;
import com.green.greengramver.feed.comment.model.FeedCommentGetReq;
import com.green.greengramver.feed.comment.model.FeedCommentGetRes;
import com.green.greengramver.feed.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedService {
    private final FeedMapper feedMapper;
    private final FeedPicMapper picsMapper;
    private final FeedCommentMapper feedCommentMapper;
    private final MyFileUtils myFileUtils;
    private final FeedPicMapper feedPicMapper;
    private final AuthenticationFacade authenticationFacade;
    private final FeedRepository feedRepository;

    @Transactional
    public FeedPostRes postFeed(List<MultipartFile> pics, FeedPostReq p) {
        User signedUser = new User();
        signedUser.setUserId(authenticationFacade.getSignedUserId());

        Feed feed = new Feed();
        feed.setWriterUser(signedUser);
        feed.setContents(p.getContents());
        feed.setLocation(p.getLocation());

//        int result = feedMapper.insFeed(p);
//        if(result == 0) {
//            throw new CustomException(FeedErrorCode.FAIL_TO_REG);
//        }
        feedRepository.save(feed);
        // 파일 등록-------------------------------
        long feedId = feed.getFeedId();

        String middlePath = String.format("feed/%d", feedId); // 파일저장경로
        // 폴더 생성
        myFileUtils.makeFolders(middlePath);

        // 랜덤 파일명 저장용 >> feed_pics 테이블에 저장할 때 사용
        List<String> picList = new ArrayList<>(pics.size());

        for(MultipartFile pic : pics) {
            String saveFileName = myFileUtils.makeRandomFileName(pic);
            String filePath = String.format("%s/%s", middlePath, saveFileName);

            picList.add(saveFileName);

            try {
                myFileUtils.transferTo(pic, filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FeedPicDTO feedPicDTO = new FeedPicDTO();

        feedPicDTO.setFeedId(feedId);
        feedPicDTO.setPics(picList);

        int resultPics = picsMapper.insFeedPic(feedPicDTO);

        return FeedPostRes.builder()
                .feedId(feedId)
                .pics(picList)
                .build();
    }

    public List<FeedGetRes> getFeedList(FeedGetReq p) {
        p.setSignedUserId(authenticationFacade.getSignedUserId());

        List<FeedGetRes> list = feedMapper.selFeedList(p);

        for(FeedGetRes item : list) {
            item.setPics(picsMapper.selFeedPic(item.getFeedId()));

            //피드당 댓글 4개
            FeedCommentGetReq commentGetReq = new FeedCommentGetReq(item.getFeedId(), 0, 3);

            List<FeedCommentDto> commentList = feedCommentMapper.selFeedCommentList(commentGetReq);

            FeedCommentGetRes commentGetRes = new FeedCommentGetRes();
            commentGetRes.setCommentList(commentList);
            commentGetRes.setMoreComment(commentList.size() == commentGetReq.getSize());

            if(commentGetRes.isMoreComment()) {
                commentList.remove(commentList.size() - 1);
            }
            item.setComment(commentGetRes);
        }

        return list;
    }

    public List<FeedGetRes> getFeedList2(FeedGetReq p) {

        return null;
    }

    // select 3번, 피드 5,000개 있음, 페이지당 20개씩 가져온다.
    public List<FeedGetRes> getFeedList3(FeedGetReq p) {
        p.setSignedUserId(authenticationFacade.getSignedUserId());
        //피드 리스트
        List<FeedGetRes> list = feedMapper.selFeedList(p);
        if(list.size() == 0) {
            return list;
        }

        // 스트림을 이용한 추출
        List<Long> feddIds2 = list.stream().map(FeedGetRes::getFeedId).toList();

        // feed_id를 골라내야한다.
        List<Long> feedIds = new ArrayList<Long>(list.size());

        for(FeedGetRes item : list) {
            feedIds.add(item.getFeedId());
        }

        // 피드와 관련된 사진 리스트
        List<FeedPicSel> feedPicsList = feedPicMapper.selFeedPicListByFeedIds(feedIds);

        log.info("feedPicList: {}", feedPicsList);

        Map<Long, List<String>> picHashMap = new HashMap<>();

        for(FeedPicSel item : feedPicsList) {
            long feedId = item.getFeedId();

            if(!picHashMap.containsKey(feedId)) {
                picHashMap.put(feedId, new ArrayList<String>(2));
            }

            List<String> pics = picHashMap.get(feedId);

            pics.add(item.getPic());
        }

        // 피드 관련된 사진 리스트(SG 코드)
//        for(FeedGetRes res : list) {
//            res.setPics(picHashMap.get(res.getFeedId()));
//        }

        // 피드 관련된 코멘트 리스트
//        List<FeedCommentDto> feedComments = feedCommentMapper.selFeedCommentListByFeedIdsLimit4(feedIds);
//
//        Map<Long, List<FeedCommentDto>> commentHashMap = new HashMap<>();
//
//        for(FeedCommentDto item : feedComments) {
//            long feedId = item.getFeedId();
//            if(!commentHashMap.containsKey(feedId)) {
//                commentHashMap.put(feedId, new ArrayList<FeedCommentDto>());
//            }
//            List<FeedCommentDto> comments = commentHashMap.get(feedId);
//            comments.add(item);
//        }
//
//        Map<Long, FeedCommentGetRes> commentResHashMap = new HashMap<>();
//
//        for(Long feedId : feedIds) {
//            FeedCommentGetRes res = new FeedCommentGetRes();
//
//            res.setCommentList(commentHashMap.get(feedId));
//
//            if(res.getCommentList() != null && res.getCommentList().size() == 4) {
//                res.setMoreComment(true);
//            }
//
//            if(!commentResHashMap.containsKey(feedId)) {
//                commentResHashMap.put(feedId, res);
//            }
//        }
//
//        for(FeedGetRes item : list) {
//            item.setComment(commentResHashMap.get(item.getFeedId()));
//        }

        //피드와 관련된 댓글 리스트(강사님 ver)
        List<FeedCommentDto> feedCommentList = feedCommentMapper.selFeedCommentListByFeedIds(feedIds);
        Map<Long, FeedCommentGetRes> commentHashMap = new HashMap<>();

        for(FeedCommentDto item : feedCommentList) {
            long feedId = item.getFeedId();

            if(!commentHashMap.containsKey(feedId)) {
                FeedCommentGetRes feedCommentGetRes = new FeedCommentGetRes();

                feedCommentGetRes.setCommentList(new ArrayList<>(4));
                commentHashMap.put(feedId, feedCommentGetRes);
            }

            FeedCommentGetRes feedCommentGetRes = commentHashMap.get(feedId);
            feedCommentGetRes.getCommentList().add(item);
        }

        for(FeedGetRes res : list) {
            res.setPics(picHashMap.get(res.getFeedId()));

            FeedCommentGetRes feedCommentGetRes = commentHashMap.get(res.getFeedId());

            if(feedCommentGetRes == null) {
                feedCommentGetRes = new FeedCommentGetRes();

                feedCommentGetRes.setCommentList(new ArrayList<>());
                res.setComment(feedCommentGetRes);
            } else if (feedCommentGetRes.getCommentList().size() == 4) {
                feedCommentGetRes.setMoreComment(true);
                feedCommentGetRes.getCommentList().remove(feedCommentGetRes.getCommentList().size() - 1);
            }

            res.setComment(feedCommentGetRes);
        }

        log.info("list: {}", list);

        return list;
    }

    @Transactional
    public int deleteFeed(FeedDeleteReq p) {
        p.setSignedUserId(authenticationFacade.getSignedUserId());
        //피드 댓글, 좋아요 삭제
        int affectedRowsEtc = feedMapper.delFeedLikeAndFeedCommentAndFeedPic(p);
        log.info("affectedRows: {}", affectedRowsEtc);

        //피드 사진 삭제
        String deletePath = String.format("%s/feed/%d", myFileUtils.getUploadPath(), p.getFeedId());
        myFileUtils.deleteFolder(deletePath, true);

        //피드 삭제
        int affectedRows = feedMapper.delFeed(p);

        return 1;
    }
}
