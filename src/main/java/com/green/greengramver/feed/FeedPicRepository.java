package com.green.greengramver.feed;

import com.green.greengramver.entity.FeedPic;
import com.green.greengramver.entity.FeedPicIds;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FeedPicRepository extends JpaRepository<FeedPic, FeedPicIds> {
}
