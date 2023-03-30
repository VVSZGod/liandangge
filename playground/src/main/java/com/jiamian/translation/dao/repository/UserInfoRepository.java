package com.jiamian.translation.dao.repository;

import com.jiamian.translation.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author DingGuangHui
 * @date 2023/2/13
 */
@Repository
public interface UserInfoRepository
		extends JpaRepository<Users, Long>, JpaSpecificationExecutor<Users> {

	Optional<Users> findByUserId(Long aLong);

	Optional<Users> findByPhoneNumber(String phoneNumber);

	@Override
	Users save(Users entity);

}
