//package com.jiamian.translation.util
//
//import com.alibaba.fastjson.JSONArray
//import com.alibaba.fastjson.JSONObject
//import com.google.common.collect.Lists
//import khttp.post
//import khttp.structures.files.FileLike
//import org.apache.commons.codec.digest.DigestUtils
//import java.io.File
//import java.io.FileInputStream
//import java.io.IOException
//
///**
// * @author  DingGuangHui
// * @date 2023/3/31
// */
//object BaiduYunPanUtil {
//
//
//    val PRECREATE_URL = "https://pan.baidu.com/rest/2.0/xpan/file?method=precreate&access_token=%s"
//
//    val CREATE_URL = "https://pan.baidu.com/rest/2.0/xpan/file?method=create&access_token=%s"
//
//    val UPLOAD_URL = "https://d.pcs.baidu.com/rest/2.0/pcs/superfile2?method=upload&access_token=%s&type=tmpfile&path=%s&uploadid=%s&partseq=%s"
//
//    @JvmStatic
//    fun main(args: Array<String>) {
//        uploadFile("D:\\software\\chat\\wechatTemp\\WeChat Files\\Answer-0506\\FileStorage\\File\\2023-03\\meta1" +
//                ".csv", "/apps/qqw/test2.csv", "123.e70a807e0669101309fa035021f31b78.Y5E-CRa0e2a9wtUyjoYV9GUYYWh8HfAb9zQ40JO.wYVN9A")
//    }
//
//    fun uploadFile(localFilePath: String, yunFilePath: String, accessToken: String) {
//        val md5List = readFileByByte(localFilePath, 1024 * 1024 * 4)
//        preCreateFiles(accessToken, yunFilePath, md5List)?.let {
//            println("=== uploadid:$it ===")
//            doUploadFile(accessToken, yunFilePath, it, md5List)
//            createFile(accessToken, yunFilePath, 1, 0, it, md5List)
//        }
//    }
//
//    /**
//     * 文件字节分组
//     */
//    private fun readFileByByte(filePath: String, byteNumberLimit: Int): List<Pair<String, ByteArray>> {
//        val file = File(filePath)
//        val md5List = Lists.newArrayList<Pair<String, ByteArray>>()
//        try {
//            FileInputStream(file).use { inputStream ->
//                val buffer = ByteArray(byteNumberLimit)
//                var bytesRead: Int
//                do {
//                    bytesRead = inputStream.read(buffer)
//                    if (bytesRead > 0) {
//                        val md5 = DigestUtils.md5Hex(buffer.sliceArray(0 until bytesRead))
//                        md5List.add(Pair(md5, buffer.sliceArray(0 until bytesRead)))
//                    }
//                } while (bytesRead != -1)
//            }
//        } catch (ex: IOException) {
//            ex.printStackTrace()
//        }
//        return md5List
//    }
//
//    private fun preCreateFiles(accessToken: String, uploadFilePath: String, md5List: List<Pair<String, ByteArray>>):
//            String? {
//        val preCreatParam = JSONObject()
//        val totalSize = md5List.sumBy { it.second.size }
//        preCreatParam.put("block_list", JSONArray(md5List.map {
//            it.first
//        }))
//        preCreatParam.put("autoinit", 1)
//        preCreatParam.put("path", uploadFilePath)
//        preCreatParam.put("isdir", 0)
//        preCreatParam.put("size", totalSize)
//
//        return post(PRECREATE_URL.format(accessToken), data = preCreatParam).jsonObject.getString("uploadid")
//    }
//
//
//    private fun doUploadFile(accessToken:
//                             String, path: String, uploadId: String, md5List: List<Pair<String, ByteArray>>) {
//        println("== start do upload ==")
//        for (index in md5List.indices) {
//            println(post(UPLOAD_URL.format(accessToken, path, uploadId, index),
//                    files = Lists.newArrayList(FileLike("file", md5List[index].second))).jsonObject)
//        }
//    }
//
//    private fun createFile(accessToken: String, yunFilePath: String, rtype: Int, isDir: Int, uploadId: String, md5List:
//    List<Pair<String,
//            ByteArray>>) {
//        val preCreatParam = JSONObject()
//        val totalSize = md5List.sumBy { it.second.size }
//        preCreatParam.put("block_list", JSONArray(md5List.map {
//            it.first
//        }))
//        preCreatParam.put("path", yunFilePath)
//        preCreatParam.put("size", totalSize)
//        preCreatParam.put("rtype", rtype)
//        preCreatParam.put("isdir", isDir)
//        preCreatParam.put("uploadid", uploadId)
//        val jsonObject = post(CREATE_URL.format(accessToken), data = preCreatParam).jsonObject
//        println(jsonObject.toString())
//    }
//}