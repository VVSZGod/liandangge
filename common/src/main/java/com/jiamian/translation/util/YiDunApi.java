package com.jiamian.translation.util;

import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.jiamian.translation.entity.MachineCheckResult;
import com.jiamian.translation.entity.MachineCheckResultEnum;

import cn.hutool.core.util.ObjectUtil;

/**
 * Author :  Rocky
 * Date : 2/29/16 15:03
 * Description :
 * Test :
 */

public class YiDunApi {
    private final static Logger log = LoggerFactory.getLogger(YiDunApi.class);
    //secret key
    private final static String SECRETKEY = "";

    //secret id
    private final static String SECRETID = "";

    //business id
    private final static String BUSINESSID = "";
    private final static String IMAGE_BUSINESSID = "";
    private final static String VIDEO_BUSINESSID = "";

    private final static String NICK_NAME_BUSINESSID = "";
    private final static String HEAD_IMG_URL_BUSINESSID = "";


    //易盾文本检查接口地址
    private final static String TEXT_CHECK_V3 = "https://as.dun.163yun.com/v3/text/check";
    // 图像检查接口
    private final static String IMAGE_CHECK = "https://as.dun.163yun.com/v3/image/check";


    // 视频提交和检查接口
    private final static String VIDEO_SUBMIT = "https://api.aq.163.com/v3/video/submit";
    private final static String VIDEO_CHECK_RESULT = "http://as.dun.163.com/v1/video/query/task";


    public static MachineCheckResult checkText(String text) {
        return checkText(text, "");
    }

    /**
     * http://support.dun.163.com/documents/2018041901?docId=150425947576913920&locale=zh-cn
     *
     * @param text 要鉴定的文本
     * @param uid  代表用户的唯一表示，没有可不传，或直接传空
     * @return 返回鉴定的结果
     */
    public static MachineCheckResult checkText(String text, String uid) {
        Map<String, String> params = new HashMap<String, String>();
        // 1.设置公共参数
        params.put("secretId", SECRETID);
        params.put("businessId", BUSINESSID);
        params.put("version", "v3.1");
        params.put("timestamp", String.valueOf(System.currentTimeMillis()));
        params.put("nonce", String.valueOf(new Random().nextInt(100000000)));

        // 2. 设置私有参数，以下以文本数据检查接口私有参数为例
        params.put("dataType", "1");
        params.put("dataId", UUID.randomUUID().toString());
        params.put("content", text);
        params.put("account", StringUtils.isNotEmpty(uid) ? uid : "");
        params.put("publishTime", String.valueOf(System.currentTimeMillis()));

        JSONObject response;
        String logInfo = null;
        String result=null;
        try {
            String signature = SignatureUtils.getSignature(SECRETKEY, params);
            params.put("signature", signature);
            response = OkHttpUtil.postRequest(TEXT_CHECK_V3, params);

            int code = response.getInteger("code");
            logInfo = response.getString("msg");
            log.info("易盾返回 " + response.toJSONString());

            // 调用异常
            if (code != 200) {
                log.info("易盾返回非200响应" + logInfo);
                return new MachineCheckResult(MachineCheckResultEnum.FAIL, "易盾返回非200响应" + response);
            }

            JSONObject resultObject = response.getJSONObject("result");

            try {
                int action = resultObject.getInteger("action");

                if (action == 0) {
                    log.info("易盾返回通过内容: " + logInfo);
                    return new MachineCheckResult(MachineCheckResultEnum.PASS, logInfo);
                } else if (action == 2) {
                    log.info("易盾审核不通过：" + logInfo);
                    JSONArray labels = resultObject.getJSONArray("labels");
                    return new MachineCheckResult(MachineCheckResultEnum.NO_PASS,  labels.toJSONString());
                } else {
                    //返回的内容表示嫌疑，对返回的结果做一遍处理
                    JSONArray labels = resultObject.getJSONArray("labels");
                    for (int f = 0; f < labels.size(); f++) {
                        int level = labels.getJSONObject(f).getInteger("level");
                        if (level != 0) {
                            log.info("易盾审核不通过：" + logInfo);
                            return new MachineCheckResult(MachineCheckResultEnum.NO_PASS, labels.toJSONString());
                        }
                    }
                    return new MachineCheckResult(MachineCheckResultEnum.PASS, labels.toJSONString());
//                    log.info("易盾返回疑似内容: " + logInfo);
//                    return new MachineCheckResult(MachineCheckResultEnum.PASS, "疑似:" + logInfo );
                }
            } catch (Exception e) {
                return new MachineCheckResult(MachineCheckResultEnum.FAIL, e.getMessage());
            }
        } catch (Exception e) {
            String errorMsg = "易盾接口异常" + e.getStackTrace()[0].getClassName() + ":" + e.getStackTrace()[0].getLineNumber();
            log.error(errorMsg);
            return new MachineCheckResult(MachineCheckResultEnum.FAIL, errorMsg);
        }
    }

    public static MachineCheckResult checkNickName(String text, String uid) {
        Map<String, String> params = new HashMap<String, String>();
        // 1.设置公共参数
        params.put("secretId", SECRETID);
        params.put("businessId", NICK_NAME_BUSINESSID);
        params.put("version", "v3.1");
        params.put("timestamp", String.valueOf(System.currentTimeMillis()));
        params.put("nonce", String.valueOf(new Random().nextInt(100000000)));

        // 2. 设置私有参数，以下以文本数据检查接口私有参数为例
        params.put("dataType", "1");
        params.put("dataId", UUID.randomUUID().toString());
        params.put("content", text);
        params.put("account", StringUtils.isNotEmpty(uid) ? uid : "");
        params.put("publishTime", String.valueOf(System.currentTimeMillis()));

        JSONObject response;
        String logInfo = null;
        String result=null;
        try {
            String signature = SignatureUtils.getSignature(SECRETKEY, params);
            params.put("signature", signature);
            response = OkHttpUtil.postRequest(TEXT_CHECK_V3, params);

            int code = response.getInteger("code");
            logInfo = response.getString("msg");
            log.info("易盾返回 " + response.toJSONString());

            // 调用异常
            if (code != 200) {
                log.info("易盾返回非200响应" + logInfo);
                return new MachineCheckResult(MachineCheckResultEnum.FAIL, "易盾返回非200响应" + response);
            }

            JSONObject resultObject = response.getJSONObject("result");

            try {
                int action = resultObject.getInteger("action");

                if (action == 0) {
                    log.info("易盾返回通过内容: " + logInfo);
                    return new MachineCheckResult(MachineCheckResultEnum.PASS, logInfo);
                } else if (action == 2) {
                    log.info("易盾审核不通过：" + logInfo);
                    JSONArray labels = resultObject.getJSONArray("labels");
                    return new MachineCheckResult(MachineCheckResultEnum.NO_PASS,  labels.toJSONString());
                } else {
                    //返回的内容表示嫌疑，对返回的结果做一遍处理
                    JSONArray labels = resultObject.getJSONArray("labels");
                    for (int f = 0; f < labels.size(); f++) {
                        int level = labels.getJSONObject(f).getInteger("level");
                        if (level != 0) {
                            log.info("易盾审核不通过：" + logInfo);
                            return new MachineCheckResult(MachineCheckResultEnum.NO_PASS, labels.toJSONString());
                        }
                    }
                    return new MachineCheckResult(MachineCheckResultEnum.PASS, labels.toJSONString());
//                    log.info("易盾返回疑似内容: " + logInfo);
//                    return new MachineCheckResult(MachineCheckResultEnum.PASS, "疑似:" + logInfo );
                }
            } catch (Exception e) {
                return new MachineCheckResult(MachineCheckResultEnum.FAIL, e.getMessage());
            }
        } catch (Exception e) {
            String errorMsg = "易盾接口异常" + e.getStackTrace()[0].getClassName() + ":" + e.getStackTrace()[0].getLineNumber();
            log.error(errorMsg);
            return new MachineCheckResult(MachineCheckResultEnum.FAIL, errorMsg);
        }
    }

    public static MachineCheckResult checkImage(String url) {
        return checkImage(url, "hongwan_pic", "");
    }

    public static MachineCheckResult checkHeadImg(String url, String picName, String uid) {
        try {
            Map<String, String> params = new HashMap<>();
            JSONArray resultArray = new JSONArray();
            // 1.设置公共参数
            params.put("secretId", SECRETID);
            params.put("businessId", HEAD_IMG_URL_BUSINESSID);
            params.put("version", "v3.1");
            params.put("timestamp", String.valueOf(System.currentTimeMillis()));
            params.put("nonce", String.valueOf(new Random().nextInt(100000)));

            // 2.设置私有参数
            JsonArray jsonArray = new JsonArray();
            // 传图片url进行检测，name结构产品自行设计，用于唯一定位该图片数据
            JsonObject image1 = new JsonObject();
            image1.addProperty("name", picName);
            image1.addProperty("type", 1);
            image1.addProperty("data", url);
            jsonArray.add(image1);

            params.put("images", jsonArray.toString());
            params.put("account", StringUtils.isNotEmpty(uid) ? uid : "");

            // 3.生成签名信息
            String signature = null;
            signature = SignatureUtils.getSignature(SECRETKEY, params);
            params.put("signature", signature);

            // 4.发送HTTP请求，这里使用的是HttpClient工具包，产品可自行选择自己熟悉的工具包发送请求
            JSONObject response = OkHttpUtil.postRequest(IMAGE_CHECK, params);

            if (ObjectUtil.isNull(response)) {
                return new MachineCheckResult(MachineCheckResultEnum.FAIL, "易盾返回为空");
            }

            // 5.解析接口返回值
            Integer code = response.getInteger("code");
            String msg = response.getString("msg");

            if (code == 200) {
                resultArray = response.getJSONArray("result");
            } else {
                return new MachineCheckResult(MachineCheckResultEnum.FAIL, "易盾请求异常，异常码：" + code + "，异常信息：" + msg);
            }

            try {
                for (int i = 0; i < resultArray.size(); i++) {
                    JSONObject jsonObject = resultArray.getJSONObject(i);
                    JSONArray labels = jsonObject.getJSONArray("labels");
                    for (int f = 0; f < labels.size(); f++) {
                        JSONObject labelsJSONObject = labels.getJSONObject(i);
                        int level = labelsJSONObject.getInteger("level");
                        if (level != 0) {
                            return new MachineCheckResult(MachineCheckResultEnum.NO_PASS, resultArray.toJSONString());
                        }
                    }
                }
                return new MachineCheckResult(MachineCheckResultEnum.PASS, resultArray.toJSONString());
            } catch (Exception e) {
                return new MachineCheckResult(MachineCheckResultEnum.FAIL, e.getMessage());
            }
        } catch (Exception e) {
            String errorMsg = "易盾审核异常:" + e.getStackTrace()[0].getClassName() + ":" + e.getStackTrace()[0].getLineNumber();
            return new MachineCheckResult(MachineCheckResultEnum.FAIL, errorMsg);
        }
    }

    public static void main(String[] args) {
        System.out.println(checkImage("https://pub-8b49af329fae499aa563997f5d4068a4.r2.de…ations/fe3cf3e3-7664-40b2-9354-7db1e4c6ef85-0" +
                ".png"));
    }

    /**
     * 鉴定图片
     * http://support.dun.163.com/documents/2018041902?docId=150429557194936320
     * 参考checkImage
     *
     * @param url 图片的地址
     * @return 返回鉴定的结果，自定义的力度
     */
    public static MachineCheckResult checkImage(String url, String picName, String uid) {
        try {
            Map<String, String> params = new HashMap<>();
            JSONArray resultArray = new JSONArray();
            // 1.设置公共参数
            params.put("secretId", SECRETID);
            params.put("businessId", IMAGE_BUSINESSID);
            params.put("version", "v3.1");
            params.put("timestamp", String.valueOf(System.currentTimeMillis()));
            params.put("nonce", String.valueOf(new Random().nextInt(100000)));

            // 2.设置私有参数
            JsonArray jsonArray = new JsonArray();
            // 传图片url进行检测，name结构产品自行设计，用于唯一定位该图片数据
            JsonObject image1 = new JsonObject();
            image1.addProperty("name", picName);
            image1.addProperty("type", 1);
            image1.addProperty("data", url);
            jsonArray.add(image1);

            params.put("images", jsonArray.toString());
            params.put("account", StringUtils.isNotEmpty(uid) ? uid : "");

            // 3.生成签名信息
            String signature = null;
            signature = SignatureUtils.getSignature(SECRETKEY, params);
            params.put("signature", signature);

            // 4.发送HTTP请求，这里使用的是HttpClient工具包，产品可自行选择自己熟悉的工具包发送请求
            JSONObject response = OkHttpUtil.postRequest(IMAGE_CHECK, params);

            if (ObjectUtil.isNull(response)) {
                return new MachineCheckResult(MachineCheckResultEnum.FAIL, "易盾返回为空");
            }

            // 5.解析接口返回值
            Integer code = response.getInteger("code");
            String msg = response.getString("msg");

            if (code == 200) {
                resultArray = response.getJSONArray("result");
            } else {
                return new MachineCheckResult(MachineCheckResultEnum.FAIL, "易盾请求异常，异常码：" + code + "，异常信息：" + msg);
            }

            try {
                for (int i = 0; i < resultArray.size(); i++) {
                    JSONObject jsonObject = resultArray.getJSONObject(i);
                    JSONArray labels = jsonObject.getJSONArray("labels");
                    for (int f = 0; f < labels.size(); f++) {
                        JSONObject labelsJSONObject = labels.getJSONObject(i);
                        int level = labelsJSONObject.getInteger("level");
                        if (level != 0) {
                            return new MachineCheckResult(MachineCheckResultEnum.NO_PASS, resultArray.toJSONString());
                        }
                    }
                }
                return new MachineCheckResult(MachineCheckResultEnum.PASS, resultArray.toJSONString());
            } catch (Exception e) {
                return new MachineCheckResult(MachineCheckResultEnum.FAIL, e.getMessage());
            }
        } catch (Exception e) {
            String errorMsg = "易盾审核异常:" + e.getStackTrace()[0].getClassName() + ":" + e.getStackTrace()[0].getLineNumber();
            return new MachineCheckResult(MachineCheckResultEnum.FAIL, errorMsg);
        }
    }

    /**
     * 提交视屏url
     *
     * @param url
     * @param callback
     */
    public static String submitVideo(String url, String callback) {
        String taskId = "";

        log.info("正在执行易盾鉴黄服务: [url={},callback={}]", url, callback);
        Map<String, String> params = new HashMap<String, String>();
        // 1.设置公共参数
        params.put("secretId", SECRETID);
        params.put("businessId", VIDEO_BUSINESSID);
        params.put("version", "v3");
        params.put("timestamp", String.valueOf(System.currentTimeMillis()));
        params.put("nonce", String.valueOf(new Random().nextInt(100000)));

        // 2.设置私有参数
        params.put("url", url);
        params.put("dataId", UUID.randomUUID().toString());
        if (StringUtils.isNotEmpty(callback)) {
            params.put("callbackUrl", callback);
        }
        params.put("scFrequency", "5");

        // 3.生成签名信息
        params.put("signature", SignatureUtils.getSignature(SECRETKEY, params));
        JSONObject response = new JSONObject();
        // 4.发送HTTP请求，这里使用的是HttpClient工具包，产品可自行选择自己熟悉的工具包发送请求
        response = OkHttpUtil.postRequest(VIDEO_SUBMIT, params);

        // 5.解析接口返回值
        int code = response.getInteger("code");
        String msg = response.getString("msg");
        if (code == 200) {
            JSONObject result = response.getJSONObject("result");
            int status = result.getInteger("status");
            if (status == 0) {
                taskId = result.getString("taskId");
                log.info("推送成功! [taskId={}]", taskId);
            } else {
                log.info("推送失败! [taskId={}]", taskId);
            }
        } else {
            log.error(String.format("易盾返回异常ERROR: code=%s, msg=%s", code, msg));
        }
        return taskId;
    }


    /**
     * 获取视频审核结果
     *
     * @param taskId 视屏url
     * @return
     */
    public static MachineCheckResult getVideoCheckResults(String taskId) {

        Map<String, String> params = new HashMap<>();
        // 1.设置公共参数
        params.put("secretId", SECRETID);
        params.put("businessId", VIDEO_BUSINESSID);
        params.put("version", "v3");
        params.put("timestamp", String.valueOf(System.currentTimeMillis()));
        params.put("nonce", String.valueOf(new Random().nextInt()));

        // 2. 生成私有参数
        Set<String> taskIds = new HashSet<>();
        taskIds.add(taskId);
        params.put("taskIds", new Gson().toJson(taskIds));

        // 3.生成签名信息
        params.put("signature", SignatureUtils.getSignature(SECRETKEY, params));

        // 4.发送HTTP请求
        JSONObject response = OkHttpUtil.postRequest(VIDEO_CHECK_RESULT, params);

        // 5.解析接口返回值
        if (response.getInteger("code") == 200) {
            JSONArray resultArray = response.getJSONArray("result");
            if (resultArray.size() == 0) {
                log.info("{}:暂无回调数据", taskId);
                return new MachineCheckResult(MachineCheckResultEnum.PENDING, "暂无回调数据");
            }
            int status = -1;
            for (int i = 0; i < resultArray.size(); i++) {
                JSONObject jObject = resultArray.getJSONObject(i);
                status = jObject.getInteger("status");
                //-1:提交检测失败，0:正常，10：检测中，20：不是7天内数据，30：taskId不存在，110：请求重复，120：参数错误，130：解析错误，140：数据类型错误
                if (status == 10) {
                    return new MachineCheckResult(MachineCheckResultEnum.PENDING, "暂无回调数据");
                }

                int videoLevel = jObject.getInteger("level");
                if (videoLevel == 0) {
                    return new MachineCheckResult(MachineCheckResultEnum.PASS, resultArray.toJSONString());
                }
            }
            return new MachineCheckResult(MachineCheckResultEnum.NO_PASS, "审核不通过:" + taskId + " status:" + status);
        } else {
            return new MachineCheckResult(MachineCheckResultEnum.NO_PASS, "Yidun获取审核结果异常,taskId:" + taskId);
        }
    }


    /**
     * 通过HttpServletRequest做签名验证
     *
     * @param jsonObject
     * @return boolean
     */
    public static boolean verifySignature(JSONObject jsonObject) {
        String secretId = jsonObject.getString("secretId");
        String businessId = jsonObject.getString("businessId");
        String signature = jsonObject.getString("signature");
        if (StringUtils.isEmpty(secretId) || StringUtils.isEmpty(signature)) {
            // 签名参数为空，直接返回失败
            return false;
        }
        Map<String, String> params = Maps.newHashMap();
        for (String paramName : jsonObject.keySet()) {
            if (!"signature".equals(paramName)) {
                params.put(paramName, jsonObject.getString(paramName));
            }
        }
        // SECRETKEY:产品私有密钥 SECRETID:产品密钥ID BUSINESSID:业务ID,开通服务时，易盾会提供相关密钥信息
        String serverSignature = SignatureUtils.getSignature(SECRETKEY, params);
        return signature.equals(serverSignature) && SECRETID.equals(secretId) && BUSINESSID.equals(businessId);
    }

}
