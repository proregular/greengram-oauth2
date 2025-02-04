package com.green.greengramver.feed.comment;

import com.green.greengramver.entity.FeedComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedCommentRepository extends JpaRepository<FeedComment, Long> {
    @Modifying
    @Query("delete from FeedComment f where f.feedCommentId=:feedCommentId AND f.user.userId=:userId")
    int deleteFeedComment(Long feedCommentId, Long userId);
}
