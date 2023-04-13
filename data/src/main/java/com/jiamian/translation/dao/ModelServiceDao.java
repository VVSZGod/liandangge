package com.jiamian.translation.dao;

import java.util.List;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;

import cn.hutool.core.collection.CollectionUtil;
import com.jiamian.translation.dao.repository.ModelCreatorRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.jiamian.translation.enums.SortTypeEnum;

@Component
@Slf4j
public class ModelServiceDao {

	@PersistenceContext
	EntityManager entityManager;

	public JSONObject getModelListByTag(Integer pageNo, Integer pageSize,
			Integer sortType, String tag) {
		JSONObject jsonObject = new JSONObject();
		int firstResult;
		pageNo = pageSize * pageNo;
		StringBuilder sql = new StringBuilder();
		sql.append(" select m.model_id,");
		if (SortTypeEnum.DOWN_COUNT.value().equals(sortType)) {
			sql.append(" (m.downloadCount+m.ldg_download_count)");
		} else if (SortTypeEnum.TIME.value().equals(sortType)) {
			sql.append(" m.create_date");
		} else {
			sql.append(" m.rating");
		}
		sql.append(
				" from  model m left join model_tags t on m.model_id=t.model_id"
						+ " where m.status=1 and LOWER(t.tagText) like '%")
				.append(tag).append("%' ");
		String sqlCount = " select count(*) from ( select count(*) from  model m left join model_tags t on m.model_id=t.model_id"
                + " where m.status=1 and LOWER(t.tagText) like '%" + tag
                + "%' group by m.model_id) a";
		// 不论上传多少模型，确保一个模型版本model_url不等于空展示列表
		sql.append(" and (m.model_url!='' or m.model_url is not null)");
		if (SortTypeEnum.DOWN_COUNT.value().equals(sortType)) {
			sql.append(
					" group by m.model_id,(m.downloadCount+m.ldg_download_count)");
			sql.append(
					" order by (m.downloadCount+m.ldg_download_count) desc ,m.model_id desc");
		} else if (SortTypeEnum.TIME.value().equals(sortType)) {
			sql.append(" group by m.model_id,m.create_date");
			sql.append(" order by m.create_date desc ,m.model_id desc");
		} else {
			sql.append(" group by m.model_id,m.rating");
			sql.append(" order by m.rating desc,m.model_id desc ");
		}
		sql.append(" limit ").append(pageNo).append(",").append(pageSize);
		Query query = entityManager.createNativeQuery(sql.toString());
		List<Object[]> resultList = query.getResultList();
		Query queryCount = entityManager.createNativeQuery(sqlCount);
		firstResult = Integer.parseInt(queryCount.getSingleResult().toString());
		jsonObject.put("count", firstResult);
		jsonObject.put("list", resultList);
		return jsonObject;
	}

}
