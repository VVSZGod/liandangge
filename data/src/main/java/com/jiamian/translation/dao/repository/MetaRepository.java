package com.jiamian.translation.dao.repository;

import com.jiamian.translation.dao.model.Meta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author DingGuangHui
 * @date 2023/2/16
 */
@Repository
public interface MetaRepository extends JpaRepository<Meta, Long> {

	@Query(nativeQuery = true, value = "select * from meta where model_id=:modelId and qiniu_url!=''")
	List<Meta> findByModelId(Long modelId);

	@Query(nativeQuery = true, value = "select * from meta where model_id=:modelId and qiniu_url!='' limit 1")
	Optional<Meta> selectModelByModelIdOne(
			@Param(value = "modelId") Long modelId);
}
