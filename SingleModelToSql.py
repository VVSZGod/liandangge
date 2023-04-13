import requests
import json
import urllib
import ssl
import qiniu
from qiniu import BucketManager
import hashlib
import datetime
# 个别模型信息下载

# 参数修改
## 需要查询的model_id
model_id = 3772






## 定义七牛
# 七牛图片url
qiniu_url_template = 'https://msdn.miaoshouai.com/msdn/image/{}'
access_key = ''
secret_key = ''
bucket_name = 'msdn'
qiniu_auth = qiniu.auth.Auth(access_key, secret_key)

#############################################################################################################################
## 定义urlib
ssl._create_default_https_context = ssl._create_unverified_context
opener = urllib.request.build_opener()
opener.addheaders = [('Referer','www.civitai.com')]
urllib.request.install_opener(opener)

# 定义sql 模板
model_insert_sql = "INSERT INTO `model`(`model_id`, `name`, `type`, `model_version_id`, `model_url`, `nsfw`, `status`, `description`,`downloadCount`,`favoriteCount`,`commentCount`,`ratingCount`,`rating`,`version`,`create_date`) VALUES ( {}, '{}', '{}', {}, '{}', '{}', '1', '{}',{},{},{},{},'{}','{}','{}');"
meta_insert_sql = "INSERT INTO `meta`(`model_id`, `model_version_id`, `imageid`, `qiniu_url`, `model_name`, `seed`, `sampler`, `cfgScale`, `steps`, `prompt`, `negativePrompt`) VALUES ( {}, '{}', {}, '{}', '{}', '{}', '{}', '{}','{}', '{}', '{}');"
creator_insert_sql = "INSERT INTO `model_creator`(  `username`, `model_id`, `image`) VALUES ('{}', {}, '{}');"
tags_insert_sql = "INSERT INTO `model_tags`( `model_id`, `tagText`, `trainedWords`, `baseModel`) VALUES ( {}, '{}', '{}', '{}');"

def formatDate(time_str):

    time_obj = datetime.datetime.fromisoformat(time_str.replace("Z", "+00:00"))
    return time_obj.strftime("%Y-%m-%d %H:%M:%S")


# md5 加密图片
def md5(string):
    # Create an MD5 object
    md5 = hashlib.md5()

    # Update the object with the string
    md5.update(string.encode())

    # Get the encrypted result
    result = md5.hexdigest()

    # Return the result
    return result

# 下载civitai 图片
def downloadCivitaiImg(url,file_name):
    # civitai 分页 url
    
    filePath = '{}'.format(file_name)
    urllib.request.urlretrieve(url, filePath)
    print('下载图片'+url)
    uploadQiniu(file_name,filePath)


def uploadQiniu(fileName,localPath):
    fileName = 'msdn/image/' + fileName
    print('上传七牛:{}'.format(qiniu_url.format(fileName)))
    upload_token = qiniu_auth.upload_token('msdn')
    qiniu.put_file(upload_token,fileName , localPath)

def writeLineAndFlush(f,data):
    f.write(data)
    f.write('\n')
    f.flush()

def getImgAndUploadQiniu(url,fileName):
    data = requests.get(url).content
    upload_token = qiniu_auth.upload_token('msdn')
    qiniu.put_data(upload_token,fileName,data)
    print('上传七牛连接',qiniu_url_template.format(file_name))
    return qiniu_url_template.format(file_name)


if __name__ == "__main__":
    model_insert_f = open('inser_{}.sql'.format(str(model_id)),'w',encoding='utf-8')

    model = json.loads(requests.get('https://civitai.com/api/v1/models/{}'.format(model_id)).text)
    m_stats = model['stats']
    model_id = model['id']
    model_name = model['name']
    model_type = model['type']
    model_nsfw = 1 if bool(model['nsfw']) else 0
    model_description =  model['description'].replace("'","\\'") 
    
    downloadCount = m_stats['downloadCount']
    favoriteCount = m_stats['favoriteCount']
    commentCount= m_stats['commentCount']
    ratingCount= m_stats['ratingCount']
    rating = m_stats['rating']
    for modelVersion in  model['modelVersions']:
        mv = modelVersion
        model_version_id = mv['id']
        version = mv['name']
        create_date = formatDate(mv['createdAt'])
        #downloadUrl = 'https://civitai.com/api/download/models/{}'.format(model_version_id)
        downloadUrl = ''
        model_insert = model_insert_sql.format(model_id,model_name,model_type,model_version_id,downloadUrl,model_nsfw,model_description,downloadCount,favoriteCount,commentCount,ratingCount,rating,version,create_date)
        writeLineAndFlush(model_insert_f,model_insert)

        images = mv['images']
        
        for i in images:
            if i['meta'] is not None:
                
                met = i['meta'] 
                width = i['width']
                height = i['height']
                seed = met['seed']
                sampler = met['sampler']
                cfgScale = met['cfgScale']
                steps = met['steps']
                promp = met['prompt']
                neg_p = '' if met.get('negativePrompt') is None else met.get('negativePrompt')
                modelVersionId = mv['id']
                file_name = '{}.png'.format(md5(i['url']))
                qiniu_img = getImgAndUploadQiniu(i['url'],file_name)
                mt_insert = meta_insert_sql.format(model_id,model_version_id,0,qiniu_img,model_name,seed,sampler,cfgScale,steps,promp,neg_p)
                writeLineAndFlush(model_insert_f,mt_insert)

    creatorUserName = model['creator']['username']
    creatorImage = model['creator']['image']
    if(creatorImage != None):
        fileName = '{}.png'.format(md5(creatorImage))
        qiniu_img = getImgAndUploadQiniu(creatorImage,fileName)
        creator_sql = creator_insert_sql.format(creatorUserName,model_id,qiniu_img)
        writeLineAndFlush(model_insert_f,creator_sql)
    else:
        creator_sql = creator_insert_sql.format(creatorUserName,model_id,'https://thirdwx.qlogo.cn/mmopen/vi_32/POgEwh4mIHO4nibH0KlMECNjjGxQUq24ZEaGT4poC6icRiccVGKSyXwibcPq4BWmiaIGuG1icwxaQX6grC9VemZoJ8rg/132')
        writeLineAndFlush(model_insert_f,creator_sql)

    tag_str = ''
    for t in model['tags']:
        tag_str = tag_str + t.get('name') +','
    
    tags = tag_str[:-1].replace('[','').replace(']','').replace("'","")
    trainWords = str(mv['trainedWords']).replace('[','').replace(']','').replace("'","")
    tags_sql = tags_insert_sql.format(model_id,tags,trainWords,mv['baseModel'])
    writeLineAndFlush(model_insert_f,tags_sql)
    model_insert_f.close()

