package com.maoding.mytask.service;

import com.maoding.core.base.service.BaseService;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.mytask.dto.HandleMyTaskDTO;
import com.maoding.mytask.dto.MyTaskActiveRequestDTO;
import com.maoding.mytask.dto.MyTaskListDTO;
import com.maoding.mytask.entity.MyTaskEntity;
import com.maoding.project.dto.DeliverEditDTO;

import java.util.List;
import java.util.Map;


/**
 * 深圳市设计同道技术有限公司
 * 类    名：NoticeService
 * 类描述：我的任务Service
 * 作    者：MaoSF
 * 日    期：2016年11月30日-下午3:10:45
 */
 public interface MyTaskService extends BaseService<MyTaskEntity> {

    /**
     * 方法描述：根据参数查询我的任务（companyId,companyUserId)
     * 作者：MaoSF
     * 日期：2016/12/1
     */
    List<MyTaskEntity> getMyTaskByParamter(Map<String, Object> param) throws Exception;

    /**
     * 方法描述：根据参数查询我的任务（companyId,companyUserId)
     * 作者：MaoSF
     * 日期：2016/12/1
     */
    /** 在移动端已经使用List<MyTaskEntity> getMyTaskByParam()代替 **/
    @Deprecated
     Map<String, Object> getMyTaskByParamMap(Map<String, Object> param) throws Exception;

    /**
     * 方法描述：根据参数查询我的任务（companyId,companyUserId)（此方法，不查询财务数据）
     * 作者：MaoSF
     * 日期：2016/12/1
     * @param:
     * @return:
     */
    List<MyTaskEntity> getMyTaskByParam(Map<String, Object> param) throws Exception;

    /**
     * 方法描述：根据参数查询我的任务（companyId,companyUserId，projectId)
     * 作者：MaoSF
     * 日期：2016/12/1
     */
     List<MyTaskEntity> getMyTaskByProjectId(String projectId,String companyId ,String companyUserId) throws Exception;

    /**
     * 方法描述：保存我的任务(技术审查费付款确认，合作技术费付款确认（taskType=4 or 6）)
     * taskType=10，合同回款，财务到款，taskType =20，其他费用，财务付款，taskType= 21，其他费用，财务到款，taskType = 29：财务对发票信息进行确认和补充
     * 作者：MaoSF
     * 日期：2016/12/21
     */
     AjaxMessage saveMyTask(String targetId,int taskType,String companyId,String createBy,String currentCompanyId) throws Exception;

    /**
     * 方法描述：保存我的任务（报销任务）
     * 作者：MaoSF
     * 日期：2016/12/21
     */
     AjaxMessage saveMyTask(String targetId,int taskType,String companyId,String companyUserId,String createBy,String currentCompanyId,boolean isAgreeAndTrans) throws Exception;

    /**
     * 方法描述：保存我的任务（发送给指定的人）,是否推送消息
     * 作者：MaoSF
     * 日期：2016/12/21
     */
     AjaxMessage saveMyTask(String targetId,int taskType,String companyId,String companyUserId,boolean isSendMessage,String createBy,String currentCompanyId) throws Exception;

    /**
     * 方法描述：保存我的任务（更换系统中某特定职务的人之后，把所有的任务移交给新人）
     * 作者：MaoSF
     * 日期：2016/12/21
     */
     AjaxMessage saveMyTask(MyTaskEntity entity,boolean isSendMessage) throws Exception;

    /**
     * 方法描述：处理合同回款到款
     * 作者：MaoSF
     * 日期：2017/4/26
     */
    AjaxMessage handleMyTask(HandleMyTaskDTO dto) throws Exception;

    /**
     * 方法描述：根据targetId修改状态
     * 作者：MaoSF
     * 日期：2016/12/1
     */
     int updateStatesByTargetId(Map<String,Object> paraMap);

    /**
     * 方法描述：忽略我的任务
     * 作者：MaoSF
     * 日期：2016/12/21
     */
    AjaxMessage ignoreMyTask(String targetId, int taskType, String companyUserId) throws Exception;

   /**
    * 方法描述：忽略我的任务
    * 作者：MaoSF
    * 日期：2016/12/21
    */
   AjaxMessage ignoreMyTask(String projectId, Integer taskType, String companyId ,String companyUserId) throws Exception;
    /**
     * 方法描述：忽略我的任务（删除了该节点后，任务全部忽略）
     * 作者：MaoSF
     * 日期：2016/12/21
     */
    AjaxMessage ignoreMyTask(String targetId) throws Exception;

    /**
     * 方法描述：忽略我的任务（用于忽略分解任务的个人任务）
     * 作者：Zhangchengliang
     * 日期：2017/7/24
     */
    AjaxMessage ignoreMyTaskForResponsible(String projectId, String companyId, String handlerId) throws Exception;

    /**
     * 方法描述：结束我的任务
     * 作者：ZCL
     * 日期：2017/5/5
     */
    void finishMyTask(MyTaskEntity myTask);

    /**
     * 作用：激活已完成的任务
     * 作者：ZCL
     * 日期：2017-5-20
     * @param dto 激活申请
     * @return 0：正常激活，-1：无法激活
     * @throws Exception
     */
    String activeMyTask(MyTaskActiveRequestDTO dto) throws Exception;

   /**
    * 作用：激活已完成的任务
    * 作者：ZCL
    * 日期：2017-5-20
    * @param dto 激活申请
    * @return 0：正常激活，-1：无法激活
    * @throws Exception
    */
   String activeMyTask2(MyTaskActiveRequestDTO dto) throws Exception;

    /**
     * 方法描述：查询任务列表
     * 作者：MaoSF
     * 日期：2017/05/04
     */
    List<MyTaskListDTO> getMyTaskList(Map<String, Object> param) throws Exception;

    /**
     * 方法描述：查询任务列表
     * 作者：MaoSF
     * 日期：2017/05/04
     */
    List<MyTaskListDTO> getMyTaskList2(Map<String, Object> param) throws Exception;

    /**
     * 方法描述：查询任务列表
     * 作者：MaoSF
     * 日期：2017/05/04
     */
    Map<String,Object> getMyTaskList4(Map<String, Object> param) throws Exception;

    /**
     * 方法描述：根据参数查询我的任务（companyId,companyUserId，projectId)
     * 作者：MaoSF
     * 日期：2016/12/1
     * @param:
     * @return:
     */
    AjaxMessage getMyTaskByProjectId(Map<String, Object> param) throws Exception;


    /**
     * 生产列表，单独确认按钮获取maoding_web_my_task中id
     *
     * */
    String getCompleteTaskId(Map<String,Object> param);

    /**
     * @author  张成亮
     * @date    2018/7/16
     * @description     创建交付相关的个人任务
     * @param   request 交付申请信息
     **/
    void saveDeliverTask(DeliverEditDTO request);

    /**
     * @author  张成亮
     * @date    2018/7/19
     * @description     创建或修改交付任务
     * @param   request 交付任务申请
     * @return  创建或修改后的交付任务
     **/
    void changeDeliver(DeliverEditDTO request);

}
