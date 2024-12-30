package com.green.greengramver.feed.like.model;


import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
/*
    immutable하게 객체를 만들고 싶다. 그러면 setter를 빼야함.
    private한 맴버필드에 값 넣는 방법 2가지 (생성자, setter)
    stter를 빼기로 했기 때문에 남은 선택지는 생성자만 남았다.
    생성자를 이용해서 객체 생성을 해야 하는데 맴버필드값을 세팅하는
    경우의 수가 많을 수 있다.
 */
@Getter
@Builder
@EqualsAndHashCode
public class FeedLikeVo {
    private long feedId;
    private long userId;
    private String createdAt;
}
