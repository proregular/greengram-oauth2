package com.green.greengramver.user;

import com.green.greengramver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

// <연결할 엔터티, PK 타입>
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUid(String uid);
}
