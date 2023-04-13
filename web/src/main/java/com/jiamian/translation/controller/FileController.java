package com.jiamian.translation.controller;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.jiamian.translation.entity.JsonResult;
import com.jiamian.translation.exception.BOException;
import com.jiamian.translation.service.QiNiuService;

import cn.hutool.core.util.ArrayUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/file")
@Api(tags = "文件相关")
@CrossOrigin(origins = "*", maxAge = 3600)
public class FileController {

	@Autowired
	private QiNiuService qiNiuService;

	// 允许上传的扩展名
	private static final String[] extensionPermit = { "jpeg", "jpg", "bmp",
			"gif", "png" };

	// @GetMapping("/upload/token")
	// @ApiOperation(value = "返回值jsonObj: key: token 和 host")
	// public JsonResult getUploadToken() {
	// String token = qiNiuService.uploadToken(null);
	// JSONObject rs = new JSONObject();
	// rs.put("token", token);
	// rs.put("host", "https://" + qiNiuService.getDomain());
	// return JsonResult.succResult(rs);
	// }

	@PostMapping("/upload/img")
	@ApiOperation(value = "上传图片")
	public JsonResult<String> uploadPic(
			@RequestParam(value = "img") MultipartFile image)
			throws IOException {
		String extension = getExtension(image.getOriginalFilename());
		if (!ArrayUtil.containsIgnoreCase(extensionPermit, extension)) {
			throw new BOException("文件不存在");
		}
		String fileKey = "miaoshou/p/"
				+ UUID.randomUUID().toString().replace("-", "").toLowerCase()
				+ "." + extension;
		return JsonResult.succResult(qiNiuService.upload(image.getInputStream(),
				fileKey, extension));
	}

	@PostMapping("/upload/file")
	@ApiOperation(value = "上传文件, 自动识别后缀")
	public JsonResult<String> uploadHtml(
			@RequestParam(value = "file") MultipartFile file)
			throws IOException {
		String extension = getExtension(file.getOriginalFilename());
		String fileKey = "miaoshou/p/"
				+ UUID.randomUUID().toString().replace("-", "").toLowerCase()
				+ "." + extension;
		return JsonResult.succResult(
				qiNiuService.upload(file.getInputStream(), fileKey, extension));
	}

	public static String getExtension(String filename) {
		if (filename == null) {
			return null;
		} else {
			int index = indexOfExtension(filename);
			return index == -1 ? "" : filename.substring(index + 1);
		}
	}

	public static int indexOfExtension(String filename) {
		if (filename == null) {
			return -1;
		} else {
			int extensionPos = filename.lastIndexOf(46);
			int lastSeparator = indexOfLastSeparator(filename);
			return lastSeparator > extensionPos ? -1 : extensionPos;
		}
	}

	public static int indexOfLastSeparator(String filename) {
		if (filename == null) {
			return -1;
		} else {
			int lastUnixPos = filename.lastIndexOf(47);
			int lastWindowsPos = filename.lastIndexOf(92);
			return Math.max(lastUnixPos, lastWindowsPos);
		}
	}
}
