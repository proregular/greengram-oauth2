package com.green.greengramver.feed;

import com.green.greengramver.feed.like.model.FeedPicVo;
import com.green.greengramver.feed.model.FeedPicDTO;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.MyBatisSystemException;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class FeedPicMapperTest {
    @Autowired
    FeedPicMapper feedPicMapper;

    @Autowired
    FeedPicTestMapper feedPicTestMapper;

    @Test
    void insFeedPicNoFeedIdThrowForeignKeyException() {
        FeedPicDTO givenParam = new FeedPicDTO();
        givenParam.setFeedId(10L);
        givenParam.setPics(new ArrayList<>(1));
        givenParam.getPics().add("a.jpg");

        assertThrows(DataIntegrityViolationException.class, () -> {
            feedPicMapper.insFeedPic(givenParam);
        });
    }

    @Test
    void insFeedPicNoPicNullPicsThrowNotNullException() {
        FeedPicDTO givenParam = new FeedPicDTO();
        givenParam.setFeedId(1L);

        assertThrows(MyBatisSystemException.class, () -> {
            feedPicMapper.insFeedPic(givenParam);
        });
    }

    @Test
    void insFeedPic__PicStringLengthMoreThan50__ThrowException() {
        FeedPicDTO givenParam = new FeedPicDTO();
        givenParam.setFeedId(1L);
        givenParam.setPics(new ArrayList<>(1));
        givenParam.getPics().add("a123456789_123456789_123456789_123456789.jpg");
        assertThrows(BadSqlGrammarException.class, () -> {
            feedPicMapper.insFeedPic(givenParam);
        });
    }

    @Test
    void insFeedPic() {
        String[] pics = {"a.jpg", "b.jpg", "c.jpg" };
        FeedPicDTO givenParam = new FeedPicDTO();
        givenParam.setFeedId(5L);
        givenParam.setPics(new ArrayList<>(pics.length));

        for(String pic : pics) {
            givenParam.getPics().add(pic);
        }

        List<FeedPicVo> feedPicListBefore = feedPicTestMapper.selFeedPicList(givenParam.getFeedId());
        int actualAffectedRows = feedPicMapper.insFeedPic(givenParam);
        List<FeedPicVo> feedPicListAfter = feedPicTestMapper.selFeedPicList(givenParam.getFeedId());

        assertAll(
                () -> {

                }
                , () -> assertEquals(givenParam.getPics().size(), actualAffectedRows)
                , () -> assertEquals(0, feedPicListBefore.size())
                , () -> assertEquals(givenParam.getPics().size(), feedPicListAfter.size())
                , () ->  assertTrue(feedPicListAfter.containsAll(Arrays.asList(pics)))
        );
        assertEquals(givenParam.getPics().size(), actualAffectedRows);
    }

}