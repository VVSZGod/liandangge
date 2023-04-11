package com.jiamian.translation.dao.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.jiamian.translation.dao.model.Model;

/**
 * @author DingGuangHui
 * @date 2023/2/16
 */
@Repository
public interface ModelRepository
		extends JpaRepository<Model, Long>, JpaSpecificationExecutor<Model> {

	Optional<Model> findByModelIdAndStatusAndModelVersionId(Long modelId,
			Integer status, Long modelVersionId);

	List<Model> findByModelIdAndStatus(Long modelId,Integer status);

	@Query(nativeQuery = true, value = "select count(*) from model where status=1 and ali_url!=''")
	int selectModelUploadCount();

	@Query(nativeQuery = true, value = "select count(*) from model where status=1")
	int selectModelCount();

	@Query(nativeQuery = true, value = "select * from model where status=:status and model_id=:modelId limit 1")
	Optional<Model> selectModelByModelIdAndStatus(Long modelId, Integer status);

	@Query(nativeQuery = true, value = "select * from model where   model_id=:modelId and model_url!='' limit 1")
	Optional<Model> selectModelByModelId(Long modelId);
}
