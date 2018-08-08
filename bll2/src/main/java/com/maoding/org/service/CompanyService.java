package com.maoding.org.service;

import com.maoding.core.base.service.BaseService;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.org.dto.*;
import com.maoding.org.entity.CompanyEntity;
import com.maoding.org.entity.CompanyInviteEntity;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：CompanyService
 * 类描述：团队（公司）Service
 * 作    者：MaoSF
 * 日    期：2016年7月7日-下午3:22:43
 */
public interface CompanyService extends BaseService<CompanyEntity> {

    /**
     * 保存公司(创建组织)
     * @param company
     * @throws Exception
     */
    String saveCompany(CompanyEntity company,  String userName,String orgManagerId,String currUserId)throws Exception;

    /**
     * 方法描述：新增或修改公司
     * 作        者：MaoSF
     * 日        期：2016年7月11日-上午9:47:25
     */
     AjaxMessage saveOrUpdateCompany(CompanyDTO dto) throws Exception;

    /**
     * 方法描述：增加分支机构
     * 作        者：MaoSF
     * 日        期：2016年7月9日-上午11:15:52
     *
     */
     AjaxMessage createSubCompany(SubCompanyDTO dto) throws Exception;

    /**
     * 方法描述：修改分支机构
     * 作        者：MaoSF
     * 日        期：2016年7月9日-上午11:15:52
     *
     * @param dto
     * @return
     * @throws Exception
     */
     AjaxMessage updateSubCompany(SubCompanyDTO dto) throws Exception;

    /**
     * 方法描述：删除分支机构
     * 作        者：MaoSF
     * 日        期：2016年7月9日-上午11:15:52
     *
     * @param orgId(分支机构id)
     * @param orgPid(当前公司id)
     * @return
     * @throws Exception
     */
     AjaxMessage deleteSubCompany(String orgPid, String orgId) throws Exception;

    /**
     * 方法描述：增加合伙人
     * 作        者：MaoSF
     * 日        期：2016年7月9日-上午11:15:52
     */
     AjaxMessage createBusinessPartner(BusinessPartnerDTO dto) throws Exception;

    /**
     * 方法描述：删除事业合伙人
     * 作        者：MaoSF
     * 日        期：2016年7月9日-上午11:15:52
     *
     * @param orgId(分支机构id)
     * @param orgPid(当前公司id)
     * @return
     * @throws Exception
     */
     AjaxMessage deleteBusinessPartner(String orgPid, String orgId) throws Exception;

    /**
     * 方法描述：修改合伙人
     * 作        者：MaoSF
     * 日        期：2016年7月9日-上午11:15:52
     *
     * @param dto
     * @return
     * @throws Exception
     */
     AjaxMessage updateBusinessPartner(BusinessPartnerDTO dto) throws Exception;

    /**
     * 方法描述：根据id查询（转化成DTO）
     * 作        者：MaoSF
     * 日        期：2016年7月8日-下午3:27:28
     *
     * @param id
     * @return
     * @throws Exception
     */
     CompanyDTO getCompanyById(String id) throws Exception;

    /**
     * 方法描述：获取所有企业（组织切换列表）return(id,companyName,companyType,companyShortName,status,filePath)
     * 作        者：MaoSF
     * 日        期：2016年7月8日-下午2:03:16
     *
     * @param userId
     * @return(id,companyName,companyType,companyShortName,status,filePath)
     */
     List<CompanyDTO> getCompanyByUserId(String userId);



    /**
     * 方法描述：获取组织架构树信息
     * 作        者：MaoSF
     * 日        期：2016年7月9日-下午6:42:43
     *
     * @param map
     * @return
     */
     OrgTreeDTO getOrgTree(Map<String, Object> map) throws Exception;

    /**
     * 方法描述：获取单层组织架构树信息
     * 作        者：Zhangchengliang
     * 日        期：2017/4/14
     *
     */
     OrgTreeDTO getOrgTreeSimple(Map<String, Object> map) throws Exception;

    /**
     * 方法描述：获取组织架构树信息（消息通告选择发送组织  使用）
     * 作        者：MaoSF
     * 日        期：2016年7月9日-下午6:42:43
     *
     * @param map
     * @return
     */
     OrgTreeDTO getOrgTreeForNotice(Map<String, Object> map) throws Exception;

    /**
     * 获取某节点公司的根节点id
     *
     * @param id
     * @return
     * @throws Exception
     */
     String getRootCompanyId(String id);

    /***************过滤以挂靠的组织**************/
    /**
     * 方法描述：查询（未挂靠组织的组织），大B搜索小b，传pid=当前公司，小b搜索大B传companyId=当前公司
     * 作        者：MaoSF
     * 日        期：2016年7月11日-下午10:49:56
     *
     * @param map
     * @return
     */
     List<CompanyDTO> getCompanyFilterbyParam(Map<String, Object> map) throws Exception;

    /**
     * 方法描述：查询（未挂靠组织的组织）条数，大B搜索小b，传pid=当前公司，小b搜索大B传companyId=当前公司
     * 作        者：MaoSF
     * 日        期：2016年7月11日-下午10:49:56
     *
     * @param map
     * @return
     */
     int getCompanyFilterbyParamCount(Map<String, Object> map) throws Exception;

    /**
     * 方法描述：查询（未挂靠组织的组织）分页，大B搜索小b，传pid=当前公司，小b搜索大B传companyId=当前公司
     * 作        者：MaoSF
     * 日        期：2016年7月11日-下午10:49:56
     *
     * @param map
     * @return
     */
     Map getCompanyFilterbyParamPage(Map<String, Object> map) throws Exception;

    /**
     * 方法描述：解散团队
     * 作        者：MaoSF
     * 日        期：2016年7月15日-下午2:01:41
     *
     * @param companyId
     * @return
     */
     AjaxMessage disbandCompany(String companyId) throws Exception;


    /**
     * 方法描述：根据当前id,查找所有的父节点
     * 作        者：MaoSF
     * 日        期：2016年7月15日-下午2:01:41
     *
     * @param id
     * @return
     */
     List<CompanyEntity> getAllParentCompanyList(String id) throws Exception;

    /**
     * 方法描述：根据当前id,查找所有的子节点
     * 作        者：MaoSF
     * 日        期：2016年7月15日-下午2:01:41
     *
     * @param id
     * @return
     */
     List<CompanyEntity> getAllChildrenCompany(String id) throws Exception;

    /**
     * 获取当前组织所在的整个组织架构（所有组织)
     */
    List<CompanyEntity> getAllOrg(String id) throws Exception;

    /**
     * 方法描述：获取常用的合作伙伴
     * 作者：MaoSF
     * 日期：2016/8/26
     *
     * @param:
     * @return:
     */
     List<CompanyDTO> getUsedCooperationPartners(String companyId) throws Exception;


    /**
     * 方法描述：获取所有的公司
     * 作        者：MaoSF
     * 日        期：2016年7月11日-下午10:49:56
     *
     * @return
     */
     List<CompanyEntity> getAllCompany() throws Exception;

    /**
     * 方法描述：获取所有的公司
     * 作        者：MaoSF
     * 日        期：2016年7月11日-下午10:49:56
     *
     * @return
     */
     List<CompanyEntity> getAllCompanyIm() throws Exception;


    /**
     * 方法描述：添加权限，系统管理员角色（初始话云端数据）
     * 作者：MaoSF
     * 日期：2016/11/7
     *
     * @param:
     * @return:
     */
     AjaxMessage initCompanyRole() throws Exception;

    /**
     * 方法描述：系统管理员权限（初始话云端数据）2016-11-11
     * 作者：MaoSF
     * 日期：2016/11/11
     *
     * @param:
     * @return:
     */
     AjaxMessage initRolePermission() throws Exception;

    //==============================================新接口2.0===============================================================

    /**
     * 方法描述：根据当前id 查找所有子公司和部门
     * 作        者：TangY
     * 日        期：2016年7月15日-下午2:01:41
     *
     * @param id
     * @return
     */
     List<CompanyDTO> getCompanysAndDepartsByCompanyId(String id) throws Exception;



    /***************过滤以挂靠的组织**************/
    /**
     * 方法描述：查询（未挂靠组织的组织），大B搜索小b，传pid=当前公司，小b搜索大B传companyId=当前公司
     * 作        者：MaoSF
     * 日        期：2016年7月11日-下午10:49:56
     *
     * @param map
     * @return
     */
     Map getCompanyFilterPage(Map<String, Object> map)
            throws Exception;


    /**
     * 方法描述：获取模糊查询的结果
     * 作        者：MaoSF
     * 日        期：2016年7月11日-下午10:49:56
     *
     * @param companyName
     * @return
     */
     List<CompanyEntity> getCompanyListBySearch(String companyName);



    /**
     * 方法描述：获取我的项目查询条件
     * 作者：TangY
     * 日期：2016/7/29
     * @param:int
     * @return:
     */
     List<CompanyEntity> getCompanyForMyProject(String companyUserId);


    /**
     * 方法描述：查询公司的子公司
     * 作        者：MaoSF
     * 日        期：2016年7月15日-下午2:01:41
     * @param id
     * @return
     */
     List<CompanyEntity> getChildrenCompany(String id);

    /**
     * 方法描述：查询公司的子公司、事业合伙人及自己
     * 作        者：MaoSF
     * 日        期：2016年7月15日-下午2:01:41
     * @return(id,companyName)
     */
    List<CompanyDataDTO> listCompanyAndChildren(String id) throws Exception;

    List<CompanyRelationDTO> getExpAmountCompanyAndChildren(String id) throws Exception;

    OrgTreeDTO getRelationTypeIsThree(String id) throws Exception;

    /**
     * 方法描述：返回选择团队列表（分支机构，合作伙伴，所有公司），任务转发给其他公司，团队选择
     * 作者：MaoSF
     * 日期：2017/1/5
     * @param:
     * @return:
     */
    List<CompanyDataDTO> getCompanyForSelect(String id,String projectId)throws Exception;

    //---------------------事业合伙人接口--------------------------------

    /**
     * 方法描述：事业合伙人加入的界面（数据查询，查询我所在的组织）
     * 作者：chenzhujie
     * 日期：2017/1/16
     * @param:map(cellphone)
     * @return:
     */
    Map<String,Object> getCompanyPrincipal(Map<String, Object> map)throws Exception;



    /**
     * 方法描述：点击邀请信息请求数据
     * 作        者：chenzhujie
     * 日        期：2017/2/27
     * @return
     */
    AjaxMessage getCompanyByInviteUrl(String id)throws Exception;

    /**
     * 方法描述：邀请事业合伙人
     * 作者：MaoSF
     * 日期：2017/4/1
     * @param:
     * @return:
     */
    AjaxMessage inviteParent(InviteParentDTO dto) throws Exception;

    /**
     * 方法描述：邀请事业合伙人身份验证
     * 作者：MaoSF
     * 日期：2017/4/1
     * @param:map(id,cellphone),id为url地址中携带的id
     * @return:
     */
    AjaxMessage verifyIdentityForParent(Map<String, Object> map)throws Exception;

    /**
     * 方法描述：根据id查询事业合伙人邀请信息
     * 作者：MaoSF
     * 日期：2017/4/1
     * @param:
     * @return:
     */
    CompanyInviteEntity getCompanyInviteById(String id) throws Exception;


    /**
     * 方法描述：设置事业合伙人的别名
     * 作者：MaoSF
     * 日期：2017/4/18
     * @param:
     * @return:
     */
    AjaxMessage setBusinessPartnerNickName(BusinessPartnerDTO dto) throws Exception;

    /**
     * 方法描述：获取公司别名
     * 作者：MaoSF
     * 日期：2017/4/18
     * @param:
     * @return:
     */
    //String getNickName(String companyId,String currentCompanyId);

    /**
     * 方法：设置免费期
     * 作者：zhangchengliang
     * 日期：2017/7/11
     */
    void setExpiryDate(String orgId, Date expiryDate, String operatorUserId);
    void setExpiryDate(String orgId, Date expiryDate);

    /**
     * 方法：延长免费期
     * 作者：zhangchengliang
     * 日期：2017/7/11
     */
    void extendExpiryDate(String orgId, Integer days, String operatorUserId);
    void extendExpiryDate(String orgId, Integer days);

    /**
     * 方法：提交审核
     * 作者：zhangchengliang
     * 日期：2017/7/11
     */
    void applyAuthentication(OrgAuthenticationDTO authentication);

    /**
     * 方法：处理审核
     * 作者：zhangchengliang
     * 日期：2017/7/11
     */
    OrgAuthenticationDTO authorizeAuthentication(OrgAuthorizeResultDTO authorizeResult);

    /**
     * 方法：列出申请审核记录
     * 作者：zhangchengliang
     * 日期：2017/7/11
     */
    List<OrgAuthenticationDTO> listAuthentication(OrgAuthenticationQueryDTO query);

    /**
     * 方法：按页列出申请审核记录
     * 作者：zhangchengliang
     * 日期：2017/7/11
     */
    OrgAuthenticationPageDTO getAuthenticationPage(OrgAuthenticationQueryDTO query);

    /**
     * 方法：获取注册信息
     * 作者：zhangchengliang
     * 日期：2017/7/11
     */
    OrgAuthenticationDTO getAuthenticationById(String orgId);


    String getOrgTypeId(String companyId);

    /**
     * 获取财务处理的组织id
     */
    String getFinancialHandleCompanyId(String companyId);

    String getExpensesDetailLedgerCompany(String companyId,String flag) throws Exception;

    List<String> getExpensesDetailLedgerCompanyList(String companyId,String flag) throws Exception;

    /**
     * 描述     查询组织信息
     * 日期     2018/8/8
     * @author  张成亮
     * @return  组织信息
     * @param   query 组织查询条件
     **/
    List<CompanyDTO> listCompany(CompanyQueryDTO query);
}
