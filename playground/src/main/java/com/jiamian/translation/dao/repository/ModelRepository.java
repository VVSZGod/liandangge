package com.jiamian.translation.dao.repository;

import com.jiamian.translation.model.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author DingGuangHui
 * @date 2023/2/16
 */
@Repository
public interface ModelRepository extends
		JpaRepository<Model, Long>, JpaSpecificationExecutor<Model> {

	Optional<Model> findByModelId(Long modelId);
}
