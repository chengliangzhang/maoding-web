package com.maoding.attach.dto;

import com.maoding.core.util.StringUtil;

/**
 * 文件路径，如果是fastdfs,fastdfsUrl为fastdfs服务器的地址
 */
public class FilePathDTO {

    private String headImg;

    public String getHeadImg() {
        if(!StringUtil.isNullOrEmpty(this.headImg)){
            headImg = FastdfsUrlServer.fastdfsUrl+this.headImg;
        }
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }
}
