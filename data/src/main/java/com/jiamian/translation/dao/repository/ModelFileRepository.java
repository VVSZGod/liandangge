package com.jiamian.translation.dao.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jiamian.translation.dao.model.ModelFile;

/**
 * @author DingGuangHui
 * @date 2023/4/23
 */
@Repository
public interface ModelFileRepository extends JpaRepository<ModelFile, Long> {

	Optional<ModelFile> findByModelIdAndModelVersionId(Long modelId,
			Long modelVersionId);
}
