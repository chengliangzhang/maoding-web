package com.maoding.hxIm.service;

import com.maoding.hxIm.dto.ImAccountDTO;
import com.maoding.hxIm.dto.ImGroupDTO;

import java.util.List;

/**
 * Created by sandy on 2017/8/7.
 */
public interface ImService {

    /**
     * 方法描述：创建群组
     */
     void createImGroup(String groupId, String admin, String companyName,Integer groupType) throws Exception;

    /**
     * 方法描述：修改群组
     */
     void updateImGroup(String groupId, String companyName,Integer groupType) throws Exception;

    /**
     * 方法描述：删除群组
     */
     void deleteImGroup(String groupId,int groupType) throws Exception;

    /**
     * 群组添加成员
     */
     void addMembers(String groupId,String userId) throws Exception;

    /**
     * 群组删除成员
     */
    void deleteMembers(String groupId,String userId) throws Exception;

    /**
     * 创建im账号
     */
    void createImAccount(String userId, String userName, String password) throws Exception;

    /**
     * 修改im密码
     */
    void updateImAccount(String userId, String password) throws Exception;

    /**
     * 創建用戶，插入imAccount
     **/
    void insertImAccount(ImAccountDTO dto) throws Exception;

    /**
     * 批量創建用戶，插入imAccount
     **/
    void insertImAccountBatch(List<ImAccountDTO> list) throws Exception;

    /**
     * 創建群組，插入imGroup
     **/
    void insertImGroup(ImGroupDTO dto) throws Exception;
}
