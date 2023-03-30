package com.jiamian.translation.dao.repository;

import com.jiamian.translation.model.ModelTags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author DingGuangHui
 * @date 2023/3/28
 */
@Repository
public interface ModelTagsRepository extends JpaRepository<ModelTags,Long> {
	List<ModelTags> findByModelId(Long modelId);
}
