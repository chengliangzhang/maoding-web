package com.maoding.attach.dto;

import com.maoding.core.util.StringUtil;

/**
 * 文件路径，如果是fastdfs,fastdfsUrl为fastdfs服务器的地址
 */
public class FilePathDTO {

    private String headImg;

    private String fileFullPath;
    public String getHeadImg() {
        if(!StringUtil.isNullOrEmpty(this.headImg)){
            headImg = FastdfsUrlServer.fastdfsUrl+this.headImg;
        }
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getFileFullPath() {
        if(!StringUtil.isNullOrEmpty(this.fileFullPath)){
            if(!this.fileFullPath.contains(FastdfsUrlServer.fastdfsUrl)){
                fileFullPath = FastdfsUrlServer.fastdfsUrl+this.fileFullPath;
            }
        }
        return fileFullPath;
    }
    public void setFileFullPath(String fileFullPath) {
        this.fileFullPath = fileFullPath;
    }
}
