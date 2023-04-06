package com.jiamian.translation.dao.repository;

import java.util.List;

import com.jiamian.translation.dao.model.ModelType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.jiamian.translation.dao.model.ModelCreator;

/**
 * @author DingGuangHui
 * @date 2023/3/29
 */
@Repository
public interface ModelCreatorRepository
		extends JpaRepository<ModelCreator, Long> {
	List<ModelCreator> findByModelId(Long modelId);

	@Query(nativeQuery = true, value = "select model_id from model_creator where username like %:userName%")
	List<Long> selectModelByUserName(String userName);
}
