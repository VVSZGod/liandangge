package com.jiamian.translation.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiamian.translation.dao.model.WorkOrder;
import com.jiamian.translation.dao.repository.WorkOrderRepository;
import com.jiamian.translation.request.WorkOrderReq;

import cn.hutool.core.bean.BeanUtil;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WorkOrderServiceImpl {

	@Autowired
	private WorkOrderRepository workOrderRepository;

	@Transactional(rollbackFor = Exception.class)
	public void createWorkOrder(WorkOrderReq workOrderReq, Long userId) {
		WorkOrder workOrder = new WorkOrder();
		BeanUtil.copyProperties(workOrderReq, workOrder);
		workOrder.setUserId(userId);
		workOrder.setCreateTime(LocalDateTime.now());
		workOrderRepository.save(workOrder);
	}
}
