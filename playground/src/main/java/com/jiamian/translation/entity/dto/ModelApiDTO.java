package com.jiamian.translation.entity.dto;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

/**
 * @author DingGuangHui
 * @date 2023/3/28
 */
@Data
public class ModelApiDTO {
	private long id;
	private String name = "";
	private String description= "";
	private String type= "";
	private boolean poi = true;
	private boolean nsfw = false;
	private boolean allowCommercialUse = false;
	private boolean allowNoCredit = false;
	private boolean allowDerivatives = true;
	private boolean allowDifferentLicense = false;
	private StatusApiDTO stats = new StatusApiDTO();
	private CreatorApiDTO creator = new CreatorApiDTO();
	private List<String> tags = Lists.newArrayList();
	private List<ModelVersionsApiDTO> modelVersions = Lists.newArrayList();

}
