package com.jiamian.translation.dao;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.jiamian.translation.common.enums.SortTypeEnum;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

public class ModelServiceDao {

	@PersistenceContext
	EntityManager entityManager;

	public JSONObject getModelListByTag(Integer pageNo, Integer pageSize,
			Integer sortType, String tag) {
		JSONObject jsonObject = new JSONObject();
		int firstResult = 0;
		pageNo = pageSize * pageNo;
		StringBuilder sql = new StringBuilder();
		StringBuilder sqlCount = new StringBuilder();
		sql.append(
				" select m.id,m.model_id,m.name,m.type,m.create_date,m.description,m.downloadCount,m.rating from  model m left join model_tags t on m.model_id=t.model_id"
						+ "where LOWER(t.tagText) like '%")
				.append(tag).append("%' ");
		sqlCount.append(
				" select count(*) from  model m left join model_tags t on m.model_id=t.model_id"
						+ "where LOWER(t.tagText) like '%")
				.append(tag).append("%' ");
		if (SortTypeEnum.DOWN_COUNT.value().equals(sortType)) {
			sql.append(" order by downloadCount,model_id desc");
		} else if (SortTypeEnum.TIME.value().equals(sortType)) {
			sql.append(" order by create_date,downloadCount desc");
		} else {
			sql.append(" order by rating,downloadCount desc ");
		}
		sql.append("limit ").append(pageNo).append(",").append("pageSize");
		Query query = entityManager.createNativeQuery(sql.toString());
		List<Object[]> resultList = query.getResultList();
		Query queryCount = entityManager.createNativeQuery(sqlCount.toString());
		firstResult = queryCount.getFirstResult();
		jsonObject.put("count", firstResult);
		jsonObject.put("list", resultList);
		return jsonObject;
	}
}
