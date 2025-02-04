package com.green.greengramver.feed;

import com.green.greengramver.entity.Feed;
import com.green.greengramver.entity.FeedPic;
import com.green.greengramver.entity.FeedPicIds;
import com.green.greengramver.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FeedRepository extends JpaRepository<Feed, Long> {
    //쿼리 메소드로 delete, update는 비추천
    int deleteByFeedIdAndWriterUser(Long feedId, User writerUser);
    //JPQL (Java Persistence Query Language)
    @Modifying //이 애노테이션이 있어야 delete or update JPQL, 리턴타입은 void or int
    @Query("delete from Feed f where f.feedId=:feedId AND f.writerUser.userId=:writerUserId")
    int deleteFeed(Long feedId, Long writerUserId);
    /*
    Feed (대문자로 시작) - 클래스명 작성해야 함

    feedId=1, writerUserId=2 가정하에 아래 SQL문이 만들어진다.

    실제 생성된 코드)
    DELETE FROM feed f
    WHERE f.feed_id = 1
    AND f.user_id = 2

    예상한 코드)
    DELETE FROM feed f
    INNER JOIN user
    ON f.user_id = user.user_id
    WHERE f.feed_id = 1
    and f.user_id = 2

     */
}
