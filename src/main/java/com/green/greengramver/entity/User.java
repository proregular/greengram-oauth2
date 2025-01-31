package com.green.greengramver.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity // 테이블을 만들고 DML때 사용
@Getter
@Setter
public class User extends UpdatedAt{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private Long userId;

    @Column(nullable = false, length = 30, unique = true)
    private String uid;

    @Column(nullable = false, length = 100)
    private String upw;

    @Column(length = 100)
    private String nickName;

    @Column(length = 50)
    private String pic;

}
