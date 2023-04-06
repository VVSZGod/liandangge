package com.jiamian.translation.service;

import com.jiamian.translation.dao.repository.ModelCreatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModelCreatorServiceImpl {

	@Autowired
	private ModelCreatorRepository modelCreatorRepository;

	public List<Long> searchModelByUserName(String userName) {
		return null;
	}
}
