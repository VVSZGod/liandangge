package com.jiamian.translation.dao.repository;

import com.jiamian.translation.model.ModelCreator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author DingGuangHui
 * @date 2023/3/29
 */
@Repository
public interface ModelCreatorRepository
		extends JpaRepository<ModelCreator, Long> {
	List<ModelCreator> findByModelId(Long modelId);
}
