package com.jiamian.translation.dao.repository;

import com.jiamian.translation.model.ModelType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author DingGuangHui
 * @date 2023/2/16
 */
@Repository
public interface ModelTypeRepository extends JpaRepository<ModelType, Long>,
		JpaSpecificationExecutor<ModelType> {

	@Query(nativeQuery = true, value = "select * from model_type where status=1 order by sort_key desc ")
	List<ModelType> selectModeTypeList();
}
