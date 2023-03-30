package com.jiamian.translation.dao.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.jiamian.translation.model.Model;

/**
 * @author DingGuangHui
 * @date 2023/2/16
 */
@Repository
public interface ModelRepository
		extends JpaRepository<Model, Long>, JpaSpecificationExecutor<Model> {

	Optional<Model> findByModelId(Long modelId);

	@Query(nativeQuery = true, value = "select count(*) from model where status=1 and ali_url!=''")
	int selectModelUploadCount();

	@Query(nativeQuery = true, value = "select count(*) from model where status=1")
	int selectModelCount();
}
