package com.jiamian.translation.dao.repository;

import com.jiamian.translation.model.ModelCreator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author DingGuangHui
 * @date 2023/3/28
 */
@Repository
public interface ModelCreatorRepository extends JpaRepository<ModelCreator, Long> {
	ModelCreator findByModelId(Long modelId);
}
