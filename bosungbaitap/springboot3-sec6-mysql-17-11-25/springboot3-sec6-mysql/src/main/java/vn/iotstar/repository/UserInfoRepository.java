package vn.iotstar.repository;

import java.util.Optional;



import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;



import vn.iotstar.springboot3.entity.UserInfo;



@Repository

public interface UserInfoRepository extends JpaRepository<UserInfo, Integer> {



Optional<UserInfo> findByName(String username);

}
