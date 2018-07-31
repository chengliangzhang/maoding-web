package com.maoding.activiti.dto;


import com.maoding.core.base.dto.CoreDTO;
import com.maoding.core.base.dto.CoreEditDTO;

import java.util.List;

/**
 * 深圳市设计同道技术有限公司
 * @author : 张成亮
 * @date   : 2018/7/27
 * @package: DeplymentEditDTO
 * @description : 流程修改信息，用于增加流程、删除或关闭流程
 */
public class DeploymentEditDTO extends CoreEditDTO {
    /** 名称 */
    private String name;
    /** 允许启动流程的角色(group)列表 */
    private List<CoreDTO> candidateGroupList;
    /** 允许启动流程的用户(user)列表 */
    private List<CoreDTO> candidateUserList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CoreDTO> getCandidateGroupList() {
        return candidateGroupList;
    }

    public void setCandidateGroupList(List<CoreDTO> candidateGroupList) {
        this.candidateGroupList = candidateGroupList;
    }

    public List<CoreDTO> getCandidateUserList() {
        return candidateUserList;
    }

    public void setCandidateUserList(List<CoreDTO> candidateUserList) {
        this.candidateUserList = candidateUserList;
    }
}
