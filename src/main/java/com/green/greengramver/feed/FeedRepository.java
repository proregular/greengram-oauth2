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
     */
}
