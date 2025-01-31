package com.green.greengramver.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Feed extends UpdatedAt{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feedId;

    @ManyToOne
    @JoinColumn(name = "writer_user_id", nullable = false)
    private User writerUser;

    @Column(length = 1_000)
    private String contents;

    @Column(length = 30)
    private String location;
}
