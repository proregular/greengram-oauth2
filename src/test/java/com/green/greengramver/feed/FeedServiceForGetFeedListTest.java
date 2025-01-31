package com.green.greengramver.feed;

import com.green.greengram.feed.comment.model.FeedCommentDto;
import com.green.greengram.feed.model.FeedGetRes;
import com.green.greengram.feed.model.FeedWithPicCommentDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FeedServiceForGetFeedListTest extends FeedServiceParentTest {
    //feedId = 3, 댓글 3개
    static List<FeedCommentDto> feedId3Comments = new ArrayList<>(3);
    //feedId = 4, 댓글 4개
    static List<FeedCommentDto> feedId4Comments = new ArrayList<>(4);

    static long FEED_ID_3 = 3L;
    static long FEED_ID_4 = 4L;

    @BeforeAll
    static void setFeedCommentDto() {
        for(int i=1; i<=3; i++) {
            FeedCommentDto dto = new FeedCommentDto();
            feedId3Comments.add(dto);
            dto.setFeedId(FEED_ID_3);
            dto.setFeedCommentId(i);
            dto.setComment(String.format("댓글%d", i));
            dto.setWriterUserId(i);
            dto.setWriterNm(String.format("홍길동%d", i));
            dto.setWriterPic(String.format("프로파일%d.jpg", i));
        }

        for(int i=4; i<=7; i++) {
            FeedCommentDto dto = new FeedCommentDto();
            feedId4Comments.add(dto);
            dto.setFeedId(FEED_ID_4);
            dto.setFeedCommentId(i);
            dto.setComment(String.format("댓글%d", i));
            dto.setWriterUserId(i);
            dto.setWriterNm(String.format("홍길동%d", i));
            dto.setWriterPic(String.format("프로파일%d.jpg", i));
        }
    }

    FeedWithPicCommentDto expectedFeedCommentDto;
    @BeforeEach
    void setFeedWithPicCommentDto() {
        expectedFeedCommentDto = FeedWithPicCommentDto.builder()
                .feedId(3L)
                .contents("컨텐츠")
                .location("위치")
                .createdAt("2025-01-03 11:03:03")
                .writerUserId(10L)
                .writerNm("홍길동")
                .writerPic("프로파일사진.jpg")
                .isLike(1)
                .pics(Arrays.asList("a.jpg", "b.jpg", "c.jpg"))
                .commentList(new ArrayList<>(3))
                .build();
    }

    @Test
    @DisplayName("FeedGetRes commentList가 size 3, 사이즈 유지")
    void objFeedGetRes_1() {
        List<FeedCommentDto> list = expectedFeedCommentDto.getCommentList();
        list.addAll(feedId3Comments);

        FeedGetRes actualResult = new FeedGetRes(expectedFeedCommentDto);
        assertAll(
              () -> assertEquals(expectedFeedCommentDto.getFeedId(), actualResult.getFeedId())
            , () -> assertEquals(expectedFeedCommentDto.getContents(), actualResult.getContents())
            , () -> assertEquals(expectedFeedCommentDto.getLocation(), actualResult.getLocation())
            , () -> assertEquals(expectedFeedCommentDto.getCreatedAt(), actualResult.getCreatedAt())
            , () -> assertEquals(expectedFeedCommentDto.getWriterUserId(), actualResult.getWriterUserId())
            , () -> assertEquals(expectedFeedCommentDto.getWriterNm(), actualResult.getWriterNm())
            , () -> assertEquals(expectedFeedCommentDto.getWriterPic(), actualResult.getWriterPic())
            , () -> assertEquals(expectedFeedCommentDto.getIsLike(), actualResult.getIsLike())
            , () -> assertEquals(expectedFeedCommentDto.getPics(), actualResult.getPics())
            , () -> assertFalse(actualResult.getComment().isMoreComment())
            , () -> assertEquals(expectedFeedCommentDto.getCommentList(), actualResult.getComment().getCommentList())
        );
    }


    @Test
    @DisplayName("FeedGetRes commentList가 size 4였을 때 size 3으 로처리")
    void objFeedGetRes_2() {
        List<FeedCommentDto> list = expectedFeedCommentDto.getCommentList();
        list.addAll(feedId4Comments);

        FeedGetRes actualResult = new FeedGetRes(expectedFeedCommentDto);
        assertAll(
              () -> assertEquals(expectedFeedCommentDto.getFeedId(), actualResult.getFeedId())
            , () -> assertEquals(expectedFeedCommentDto.getContents(), actualResult.getContents())
            , () -> assertEquals(expectedFeedCommentDto.getLocation(), actualResult.getLocation())
            , () -> assertEquals(expectedFeedCommentDto.getCreatedAt(), actualResult.getCreatedAt())
            , () -> assertEquals(expectedFeedCommentDto.getWriterUserId(), actualResult.getWriterUserId())
            , () -> assertEquals(expectedFeedCommentDto.getWriterNm(), actualResult.getWriterNm())
            , () -> assertEquals(expectedFeedCommentDto.getWriterPic(), actualResult.getWriterPic())
            , () -> assertEquals(expectedFeedCommentDto.getIsLike(), actualResult.getIsLike())
            , () -> assertEquals(expectedFeedCommentDto.getPics(), actualResult.getPics())
            , () -> assertTrue(actualResult.getComment().isMoreComment())
            , () -> assertEquals(3, actualResult.getComment().getCommentList().size())
            , () -> {
                List<FeedCommentDto> commentList = actualResult.getComment().getCommentList();
                for(int i=0; i<commentList.size(); i++) {
                    assertEquals(feedId4Comments.get(i), commentList.get(i));
                }
            }
        );
    }


}
