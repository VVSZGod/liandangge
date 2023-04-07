package com.jiamian.translation.service;

import com.jiamian.translation.dao.model.ModelCreator;
import com.jiamian.translation.dao.repository.ModelCreatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ModelCreatorServiceImpl {

	@Autowired
	private ModelCreatorRepository modelCreatorRepository;

	public List<Long> searchModelByUserName(String userName) {
		return modelCreatorRepository.selectModelByUserName(userName);
	}

	public ModelCreator selectOneModelByModelId(Long modelId) {
		Optional<ModelCreator> modelCreator = modelCreatorRepository
				.selectOneModelByModelId(modelId);
		return modelCreator.get();
	}
}
