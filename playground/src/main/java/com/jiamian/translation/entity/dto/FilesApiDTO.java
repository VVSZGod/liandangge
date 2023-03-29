package com.jiamian.translation.entity.dto;

import com.google.common.collect.Maps;
import lombok.Data;

import java.util.Map;

/**
 * @author DingGuangHui
 * @date 2023/3/28
 */
@Data
public class FilesApiDTO {

	private String name = "";
	private int id;
	private double sizeKB;
	private String type = "";
	private String format;
	private String pickleScanResult;
	private String pickleScanMessage;
	private String virusScanResult;
	private String scannedAt;
	private String downloadUrl;
	private boolean primary=true;

	private Map hashes = Maps.newHashMap();

}
