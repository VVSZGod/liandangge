package com.jiamian.translation.dao.repository;

import com.jiamian.translation.model.Meta;
import com.jiamian.translation.model.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author DingGuangHui
 * @date 2023/2/16
 */
@Repository
public interface ModelRepository
		extends JpaRepository<Model, Long>, JpaSpecificationExecutor<Model> {

	Optional<Model> findByModelIdAndStatus(Long modelId, Integer status);

	@Query(nativeQuery = true, value = "select count(*) from model where status=1 and ali_url!=''")
	int selectModelUploadCount();

	@Query(nativeQuery = true, value = "select count(*) from model where status=1")
	int selectModelCount();
}
