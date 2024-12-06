package com.green.greengramver.feed.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class FeedPicDTO {
    private long feedId;
    private List<String> pics;
}
