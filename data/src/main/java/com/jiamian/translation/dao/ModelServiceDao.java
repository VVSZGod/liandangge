package com.jiamian.translation.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.jiamian.translation.enums.SortTypeEnum;

@Component
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
				" select m.id,m.model_id,m.name,m.type,m.create_date,m.model_version_id,m.downloadCount,m.rating,m.ldg_download_count,m.ali_url from  model m left join model_tags t on m.model_id=t.model_id"
						+ " where m.status=1 and LOWER(t.tagText) like '%")
				.append(tag).append("%' ");
		sqlCount.append(
				" select count(*) from  model m left join model_tags t on m.model_id=t.model_id"
						+ " where m.status=1 and LOWER(t.tagText) like '%")
				.append(tag).append("%' ");
		if (SortTypeEnum.DOWN_COUNT.value().equals(sortType)) {
			sql.append(
					" order by (downloadCount+ldg_download_count) desc ,model_id desc");
		} else if (SortTypeEnum.TIME.value().equals(sortType)) {
			sql.append(" order by create_date,model_id desc");
		} else {
			sql.append(" order by rating,model_id desc ");
		}
		sql.append(" limit ").append(pageNo).append(",").append(pageSize);
		Query query = entityManager.createNativeQuery(sql.toString());
		List<Object[]> resultList = query.getResultList();
		Query queryCount = entityManager.createNativeQuery(sqlCount.toString());
		firstResult = Integer.parseInt(queryCount.getSingleResult().toString());
		jsonObject.put("count", firstResult);
		jsonObject.put("list", resultList);
		return jsonObject;
	}
}
