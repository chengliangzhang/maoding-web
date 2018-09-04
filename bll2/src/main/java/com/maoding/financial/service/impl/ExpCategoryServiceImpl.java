package com.maoding.financial.service.impl;

import com.maoding.core.base.dao.GenericDao;
import com.maoding.core.base.dto.BaseDTO;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.core.constant.CompanyBillType;
import com.maoding.core.util.BeanUtils;
import com.maoding.core.util.StringUtil;
import com.maoding.financial.dao.ExpCategoryDao;
import com.maoding.financial.dao.ExpCategoryRelationDao;
import com.maoding.financial.dto.*;
import com.maoding.financial.entity.ExpCategoryEntity;
import com.maoding.financial.entity.ExpCategoryRelationEntity;
import com.maoding.financial.service.ExpCategoryService;
import com.maoding.financial.service.ExpMainService;
import com.maoding.org.dao.CompanyUserDao;
import com.maoding.org.dto.CompanyDTO;
import com.maoding.org.dto.DepartDTO;
import com.maoding.org.service.CompanyService;
import com.maoding.org.service.CompanyUserService;
import com.maoding.org.service.DepartService;
import com.maoding.process.service.ProcessService;
import com.maoding.project.dto.ProjectDTO;
import com.maoding.project.service.ProjectService;
import com.maoding.system.dao.DataDictionaryDao;
import com.maoding.system.dto.DataDictionaryDataDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 深圳市设计同道技术有限公司
 * 类    名 : ExpCategoryServiceImpl
 * 描    述 : 报销类别ServiceImpl
 * 作    者 : MaoSF
 * 日    期 : 2016/10/09-15:52
 */

@Service("expCategoryService")
public class ExpCategoryServiceImpl extends GenericDao<ExpCategoryEntity> implements ExpCategoryService {

    @Autowired
    private ExpCategoryDao expCategoryDao;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CompanyUserDao companyUserDao;

    @Autowired
    private DepartService departService;

    @Autowired
    private CompanyUserService companyUserService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private DataDictionaryDao dataDictionaryDao;

    @Autowired
    private ExpCategoryRelationDao expCategoryRelationDao;

    @Autowired
    private ExpMainService expMainService;

    @Autowired
    private ProcessService processService;

    private String otherIncome = "gdfy_qtywsr";
    private String directCost = "bx_ywfy";
    private String directCost2 = "zjxmcb";
    private String mainIncome = "zyywsr";

    private String mainIncomeId = "a322548663a811e8a033f8db88fcba36";
    /**
     * 方法描述：报销基础数据
     * 作   者：LY
     * 日   期：2016/7/27 17:59
     */
    @Override
    public AjaxMessage getExpBaseData(String companyId, String userId, String type) throws Exception {
        AuditQueryDTO query = new AuditQueryDTO();
        query.setCurrentCompanyId(companyId);
        query.setAccountId(userId);
        query.setAuditType(type);
        return AjaxMessage.succeed(getExpBaseData(query));
    }

    @Override
    public Map<String, Object> getExpBaseData(AuditQueryDTO query) throws Exception {
        String companyId = query.getCurrentCompanyId();
        String userId = query.getAccountId();

        Map<String, Object> map = new HashMap<>();
        map.put("expTypeList", getExpCategoryTypeList(companyId,userId));
        map.put("projectList", expMainService.getProjectListWS(companyId,userId));

        //合并获取关联审批接口
        QueryAuditDTO passAuditQuery = BeanUtils.createFrom(query,QueryAuditDTO.class);
        map.put("auditList",expMainService.getPassAuditData(passAuditQuery));

        //合并获取审批人接口
        AuditEditDTO auditRequest = BeanUtils.createFrom(query,AuditEditDTO.class);
        Map<String,Object> auditMap = processService.getCurrentProcess(auditRequest);
        map.putAll(auditMap);

        Map<String, Object> mapParams = new HashMap<>();
        mapParams.put("companyId", companyId);
        mapParams.put("userId", userId);
        map.put("departList", departService.getDepartByUserIdContentCompany(mapParams));

        CompanyDTO company = companyService.getCompanyById(companyId);

        List<DepartDTO> list = departService.getDepartByCompanyId(mapParams);
        DepartDTO dto = new DepartDTO();
        dto.setId(companyId);
        if (null != company && null != company.getCompanyName()) {
            dto.setDepartName(company.getCompanyName());
        }
        list.add(0, dto);
        map.put("departListByCompanyId", list);
        map.put("companyUserList", companyUserService.getCompanyUserByCompanyId(companyId));
        return map;
    }

    /**
     * 方法描述：报销类别基础数据
     * 作   者：LY
     * 日   期：2016/7/27 17:59
     */
    @Override
    public AjaxMessage getCategoryBaseData(String companyId, String userId) throws Exception {
        Integer categoryType = 1;
        companyId = companyService.getRootCompanyId(companyId);
        Map<String, Object> map = new HashMap<String, Object>();
        List<ExpCategoryEntity> list = this.getExpType(companyId,"",categoryType);
        //如果该公司的报销类别尚未初始化数据。则初始化
        if (list == null || list.size() == 0) {
            //初始化本公司的报销类别基础数据
            this.initDefaultData(companyId,userId,1);
        }
        //重新获取数据
        map.put("expTypeList", getExpTypeList(companyId));
        return new AjaxMessage().setCode("0").setData(map);
    }

    /**
     * 初始化默认数据
     */
    private void initDefaultData(String companyId,String userId,Integer categoryType) throws Exception {
        List<DataDictionaryDataDTO> dataList = new ArrayList<>();
        String code = null;
        if(categoryType==2){
            code = "gdfy";
        }else if(categoryType==1){
            code = "bx";
        }else if(categoryType==3){
            code = "fyft";
        }
        dataList = this.dataDictionaryDao.getExpTypeList(code);
        for (DataDictionaryDataDTO d : dataList) {
            int payType = CompanyBillType.DIRECTION_PAYER;
            if(otherIncome.equals(d.getCode())){
                payType = CompanyBillType.DIRECTION_PAYEE;
            }

            String pid = d.getId();
            if(!directCost.equals(d.getCode())){
                ExpCategoryEntity expCategoryEntity = new ExpCategoryEntity();
                expCategoryEntity.setId(StringUtil.buildUUID());
                expCategoryEntity.setCompanyId(companyId);
                expCategoryEntity.setName(d.getName());
                expCategoryEntity.setCode(d.getCode());
                expCategoryEntity.setExpTypeMemo(d.getMemo());
                expCategoryEntity.setSeq(d.getSeq());
                expCategoryEntity.setCreateBy(userId);
                expCategoryEntity.setCategoryType(categoryType);
                expCategoryEntity.setStatus("0");
                expCategoryEntity.setShowStatus(1);
                expCategoryEntity.setPayType(payType);
                if(categoryType==3){
                    expCategoryEntity.setShowStatus(0); // 如果是类型为分摊费用，默认不选中
                }else {
                    ExpCategoryRelationEntity relation = new ExpCategoryRelationEntity();
                    relation.initEntity();
                    relation.setCompanyId(companyId);
                    relation.setCategoryTypeId(expCategoryEntity.getId());
                    expCategoryRelationDao.insert(relation); //这份可以不存，为了保存统一，还是存
                }
                expCategoryDao.insert(expCategoryEntity);
                pid = expCategoryEntity.getId();
            }
            for (DataDictionaryDataDTO dChild : d.getChildList()) {
                ExpCategoryEntity child = new ExpCategoryEntity();
                child.setId(StringUtil.buildUUID());
                child.setPid(pid);
                child.setCompanyId(companyId);
                child.setName(dChild.getName());
                child.setCode(dChild.getCode());
                child.setExpTypeMemo(dChild.getMemo());
                child.setSeq(dChild.getSeq());
                child.setCreateBy(userId);
                child.setCategoryType(categoryType);
                child.setStatus("0");
                child.setShowStatus(1);
                child.setPayType(payType);
                if(categoryType==3){
                    child.setShowStatus(0); // 如果是类型为分摊费用，默认不选中
                }else {
                    //保存一份到relation中
                    ExpCategoryRelationEntity relation = new ExpCategoryRelationEntity();
                    relation.initEntity();
                    relation.setCompanyId(companyId);
                    relation.setCategoryTypeId(child.getId());
                    expCategoryRelationDao.insert(relation);
                }
                expCategoryDao.insert(child);
            }
        }
    }

    public void setDefaultExp(String companyId) throws Exception {
        ExpCountDTO count = this.expCategoryDao.getExpCategoryByCompanyId(companyId);
        if(count==null || count.getExp()==0){
            //如果该公司的报销类别尚未初始化数据。则初始化
            initDefaultData(companyId,null,1);
        }
        if(count==null || count.getFix()==0){
            //如果该公司的固定支出尚未初始化数据。则初始化
            initDefaultData(companyId,null,2);
        }
        if(count==null || count.getShare()==0){
            //如果该公司的报销类别尚未初始化数据。则初始化
            initDefaultData(companyId,null,3);
        }
    }

    public void insertCategoryFromRootCompany(QueryExpCategoryDTO query){
        int count = this.expCategoryRelationDao.getSelectedCategory(query.getCompanyId());
        if(count==0) {
            //复杂总公司的记录到当前组织中
            this.expCategoryRelationDao.insertCategoryFromRootCompany(query);
        }
    }
    /**
     * 方法描述：固定费用类型
     * 用于我要报销界面，报销类型选择
     * 作        者：MaoSF
     * 日        期：2015年12月7日-上午11:21:49
     */
    @Override
    public List<ExpCategoryDataDTO> getExpTypeList(QueryExpCategoryDTO query) throws Exception{
        String companyId = query.getCompanyId();
        String rootCompanyId = companyService.getRootCompanyId(companyId);
        query.setRootCompanyId(rootCompanyId);
        this.setDefaultExp(rootCompanyId);
        this.insertCategoryFromRootCompany(query);
        List<ExpCategoryDataDTO> result = this.expCategoryDao.getExpCategoryList(query);
//        if(!CollectionUtils.isEmpty(result)){
//            if(!"主营业务收入".equals(result.get(0).getName()) && (query.getCategoryType()==null || query.getCategoryType()==2)){
//                ExpCategoryEntity mainIncome = expCategoryDao.selectById(mainIncomeId);
//                mainIncome.setShowStatus(0);
//                result.add(0,(ExpCategoryDataDTO)BaseDTO.copyFields(mainIncome,ExpCategoryDataDTO.class));
//            }
//        }
        //重新处理一下disabled
        result.stream().forEach(exp->{
            if(!CollectionUtils.isEmpty(exp.getChildList())){
                if(exp.getChildList().size()==1 && StringUtil.isNullOrEmpty(exp.getChildList().get(0).getId())){
                    exp.setChildList(new ArrayList<>());
                }else {
                    exp.getChildList().stream().forEach(child->{
                        if(child!=null && child.getCategoryType()!=null){
                            if(child.getCategoryType()==0){
                                child.setDisabled(true);
                            }
                            if(child.getCategoryType()==3 && companyId.equals(rootCompanyId)){
                                child.setDisabled(true);
                            }
                        }
                    });
                }
            }
        });

        return result;
    }

    @Override
    public List<ExpCategoryDataDTO> getExpShareTypeList(QueryExpCategoryDTO query) throws Exception {
        query.setCategoryType(3);//费用分摊类型
        String companyId = query.getCompanyId();
        query.setRootCompanyId(companyId);//因为设置该处的就是最顶级
        ExpCountDTO count = this.expCategoryDao.getExpCategoryByCompanyId(companyId);
        if(count==null || count.getShare()==0){
            //如果该公司的报销类别尚未初始化数据。则初始化
            initDefaultData(companyId,query.getAccountId(),3);
        }
        return this.expCategoryDao.getExpCategoryList(query);
    }

    @Override
    public List<ExpCategoryDataDTO> getExpTypeListForProfitReport(QueryExpCategoryDTO query) throws Exception {

        String subCompanyId = query.getCompanyId();
        String rootCompanyId = null;
        List<String> companyIdList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(query.getCompanyIdList())){
            rootCompanyId = companyService.getRootCompanyId(query.getCompanyIdList().get(0));
            this.setDefaultExp(rootCompanyId);
            //下面的只对总公司进行设置。如果子公司没有设置，那么统一使用总公司的
            query.setRootCompanyId(rootCompanyId);
            query.setCompanyId(rootCompanyId);
            this.insertCategoryFromRootCompany(query);
            companyIdList.add(rootCompanyId);
            companyIdList.addAll(query.getCompanyIdList());
        }else if(!StringUtil.isNullOrEmpty(subCompanyId)){
            query.setCompanyIdList(Arrays.asList(subCompanyId));
            this.setDefaultExp(rootCompanyId);
            query.setRootCompanyId(rootCompanyId);
            query.setCompanyId(subCompanyId);
            this.insertCategoryFromRootCompany(query);
            companyIdList.add(subCompanyId);
        }

        //重新封裝一下参数，虽然传递进来的query 与要 传递到dao中的类型一样。为了避免部分参数不需要的情况，重新封装一下
        QueryExpCategoryDTO param = new QueryExpCategoryDTO();
        param.setRootCompanyId(rootCompanyId);
        param.setCompanyIdList(companyIdList);
        if(!CollectionUtils.isEmpty(query.getParentTypeList())){
            param.setParentTypeList(query.getParentTypeList());
        }
        List<ExpCategoryDataDTO> list = this.expCategoryDao.getExpTypeListForProfitReport(param);
        return list;
    }

    @Override
    public List<ExpCategoryDataDTO> getExpCategoryListByType(String companyId, Integer categoryType) {
        QueryExpCategoryDTO query = new QueryExpCategoryDTO();
        query.setCompanyId(companyId);
        query.setRootCompanyId(this.companyService.getRootCompanyId(companyId));
        query.setCategoryType(categoryType);
        return this.expCategoryDao.getExpCategoryListByType(query);
    }

    @Override
    public List<ExpCategoryDataDTO> getExpCategoryListByType(List<String> companyIdList, Integer categoryType) {
        if(CollectionUtils.isEmpty(companyIdList)){
            return new ArrayList<>();
        }
        QueryExpCategoryDTO query = new QueryExpCategoryDTO();
        query.setCompanyIdList(companyIdList);
        query.setRootCompanyId(this.companyService.getRootCompanyId(companyIdList.get(0)));
        query.setCategoryType(categoryType);
        return this.expCategoryDao.getExpCategoryListByType(query);
    }

    @Override
    public int saveExpTypeShowStatus(SaveExpCategoryShowStatusDTO dto) throws Exception {
        String companyId = dto.getCompanyId();
        String rootCompanyId = this.companyService.getRootCompanyId(companyId);
        if(dto.getCategoryType()==null || dto.getCategoryType()==0){ //为了在sql的判断处理简单些
            dto.setCategoryType(null);
        }
        if(dto.getPayType()==null || dto.getPayType()==0){ //为了在sql的判断处理简单些
            dto.setPayType(null);
        }

        if(companyId.equals(rootCompanyId)){ //保存原有的逻辑
            //把原来的设置为不展示
            dto.setIds(null);
            dto.setShowStatus(0);
            dto.setCompanyId(rootCompanyId);
            this.expCategoryDao.saveExpCategoryShowStatus(dto);
            //把新的设置为展示
            if(dto.getIdList().size()>0){
                dto.setShowStatus(1);
                dto.setIds(org.apache.commons.lang3.StringUtils.join(dto.getIdList(),","));
                this.expCategoryDao.saveExpCategoryShowStatus(dto);
            }
        }
        // 删除原来组织的relation信息
        dto.setRelationCompanyId(rootCompanyId);
        this.expCategoryRelationDao.deleteCategoryByCompany(dto);
        if(!CollectionUtils.isEmpty(dto.getCategoryTypeList())){
            //插入原来的数据
            return this.expCategoryRelationDao.insertBatch(dto);
        }
        return 0;
    }

    @Override
    public int saveExpShareTypeShowStatus(SaveExpCategoryShowStatusDTO dto) throws Exception {
        if(!dto.getCompanyId().equals(dto.getCurrentCompanyId())){
            dto.setRelationCompanyId(dto.getCurrentCompanyId());
        }
        //把原来的设置为不展示
        dto.setIds(null);
        dto.setShowStatus(0);
        dto.setCategoryType(3);
        this.expCategoryDao.saveExpCategoryShowStatus(dto);
        //把新的设置为展示
        if(dto.getIdList().size()>0){
            dto.setShowStatus(1);
            dto.setIds(org.apache.commons.lang3.StringUtils.join(dto.getIdList(),","));
            return this.expCategoryDao.saveExpCategoryShowStatus(dto);
        }
        return 0;
    }



    /**
     * 方法描述：查询报销类型
     * 用于我要报销界面，报销类型选择
     * 作        者：MaoSF
     * 日        期：2015年12月7日-上午11:21:49
     */
    @Override
    @Deprecated
    /** 更改为getExpTypeList(String rootCompanyId,String companyId) **/
    public List<ExpTypeDTO> getExpTypeList(String companyId) {
        return getExpTypeList(companyId,1);
    }


    /**
     * 费用类型查询统一接口
     */
    @Deprecated
    /** 更改为getExpTypeList(String rootCompanyId,String companyId) **/
    private List<ExpTypeDTO> getExpTypeList(String companyId, Integer categoryType){
        QueryExpCategoryDTO query = new QueryExpCategoryDTO();
        query.setCompanyId(companyId);
        query.setCategoryType(categoryType);
        List<ExpTypeDTO> expTypes = new ArrayList<>();
        try{
            this.setDefaultExp(companyId);
            List<ExpCategoryDataDTO> list = this.getExpCategoryListByType(companyId,categoryType);
            for(ExpCategoryDataDTO e:list){
                ExpTypeDTO dto = new ExpTypeDTO();
                dto.setParent((ExpCategoryEntity) BaseDTO.copyFields(e,ExpCategoryEntity.class));
                dto.setChild(BaseDTO.copyFields(e.getChildList(),ExpCategoryEntity.class));
                expTypes.add(dto);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return expTypes;
    }

    /**
     * 方法描述：查询报销类型
     * 用于我要报销界面，报销类型选择
     * 作        者：MaoSF
     * 日        期：2015年12月7日-上午11:21:49
     * @return
     */
    @Override
    public List<ExpTypeDTO> getExpTypeList(String rootCompanyId,String companyId) throws Exception{
        if(rootCompanyId==null){
            rootCompanyId = companyService.getRootCompanyId(companyId);
        }
        setDefaultExp(rootCompanyId);
        List<ExpTypeDTO> expTypes = new ArrayList<ExpTypeDTO>();
        QueryExpCategoryDTO query = new QueryExpCategoryDTO();
        query.setRootCompanyId(rootCompanyId);
        query.setCompanyId(companyId);
        query.setCategoryType(1);
        insertCategoryFromRootCompany(query);
        List<ExpCategoryDataDTO> list = this.expCategoryDao.getExpCategoryListByType(query);
        if (!CollectionUtils.isEmpty(list)) {
            list.stream().forEach(exp->{
                ExpTypeDTO typeBean = new ExpTypeDTO();
                ExpCategoryEntity parent = new ExpCategoryEntity();
                parent.setId(exp.getId());
                parent.setExpTypeMemo(exp.getExpTypeMemo());
                parent.setName(exp.getName());
                parent.setCode(exp.getCode());
                parent.setPid(exp.getPid());
                typeBean.setParent(parent);
                exp.getChildList().stream().forEach(c->{
                    ExpCategoryEntity child = new ExpCategoryEntity();
                    child.setId(c.getId());
                    child.setExpTypeMemo(c.getExpTypeMemo());
                    child.setName(c.getName());
                    child.setCode(c.getCode());
                    child.setPid(c.getPid());
                    typeBean.getChild().add((child));
                });
                expTypes.add(typeBean);
            });
        }
        return expTypes;
    }

    private List<ExpCategoryEntity> getExpType(String companyId, String pid, Integer categoryType){
        Map<String,Object> param = new HashMap<>();
        param.put("pid", pid);
        param.put("companyId", companyId);
        param.put("categoryType", categoryType);
        param.put("status", "0");
        return expCategoryDao.getDataByParemeter(param);
    }

    @Override
    public AjaxMessage saveOrUpdateCategoryBaseData(ExpTypeOutDTO dto, String companyId) throws Exception {
        Integer categoryType = 1;
        List<ExpTypeDTO> listAll = dto.getExpTypeDTOList();
        ExpCategoryEntity entity = null;
        listAll.get(0).getParent().setCompanyId(companyId);//理论上不需要重新初始化，此句不可删除，因为存在companyId为null的
        if (StringUtil.isNullOrEmpty(listAll.get(0).getParent().getCompanyId())) {
            for (int i = 0; i < listAll.size(); i++) {
                String pid = StringUtil.buildUUID();
                entity = listAll.get(i).getParent();
                entity.setUpdateBy(dto.getAccountId());
                entity.setId(pid);
                entity.setCompanyId(companyId);
                entity.setCategoryType(categoryType);
                entity.setStatus("0");
                expCategoryDao.insert(entity);
                List<ExpCategoryEntity> list = listAll.get(i).getChild();
                for (ExpCategoryEntity expCategoryDTO : list) {
                    BaseDTO.copyFields(expCategoryDTO, entity);
                    entity.setPid(pid);
                    entity.setId(StringUtil.buildUUID());
                    entity.setCompanyId(companyId);
                    if (StringUtil.isNullOrEmpty(expCategoryDTO.getStatus())) {
                        entity.setStatus("0");
                    }
                    entity.setCategoryType(categoryType);
                    expCategoryDao.insert(entity);
                }
            }

            return new AjaxMessage().setCode("0").setInfo("保存成功").setData(dto);

        } else {

            //需要删除的数据，逻辑删除
            if (dto != null && !CollectionUtils.isEmpty(dto.getDeleteExpTypeList())) {
                for (ExpCategoryEntity expCategoryDTO : dto.getDeleteExpTypeList()) {
                    expCategoryDTO.setStatus("1");
                    expCategoryDao.updateById(expCategoryDTO);
                }
            }

            //所有要更新的数据。
            for (int i = 0; i < listAll.size(); i++) {
                entity = listAll.get(i).getParent();
                String pid = entity.getId();
                entity.setUpdateBy(dto.getAccountId());
                expCategoryDao.updateById(entity);

                //删除子条目
                Map<String, Object> mapParams = new HashMap<String, Object>();
                mapParams.put("companyId", companyId);
                mapParams.put("pid", pid);
                // expCategoryDao.deleteByPId(mapParams);
                List<ExpCategoryEntity> list = listAll.get(i).getChild();
                for (ExpCategoryEntity child : list) {
                    child.setStatus("0");
                    child.setPid(pid);
                    child.setCompanyId(companyId);
                    if (StringUtil.isNullOrEmpty(child.getId())) {
                        ExpCategoryEntity categoryEntity = expCategoryDao.selectByName(pid, child.getName());
                        if (categoryEntity != null) {
                            categoryEntity.setExpTypeMemo(child.getExpTypeMemo());
                            categoryEntity.setStatus("0");
                            expCategoryDao.updateById(categoryEntity);
                            child.setId(categoryEntity.getId());
                        } else {
                            child.setId(StringUtil.buildUUID());
                            child.setCategoryType(categoryType);
                            expCategoryDao.insert(child);
                        }
                    } else {
                        expCategoryDao.updateById(child);
                    }
                }
            }
        }
        return new AjaxMessage().setCode("0").setInfo("保存成功").setData(dto);
    }

    @Override
    public int saveExpFixCategory(ExpCategoryDTO dto) throws Exception {
        // dto.setCategoryType(2);//固定支出类型
        return saveCategoryType(dto);
    }

    private int saveCategoryType(ExpCategoryDTO dto) throws Exception{
        ExpCategoryEntity parent = this.expCategoryDao.selectById(dto.getPid());
        if(parent==null){
            return 0;
        }
        Integer categoryType = dto.getCategoryType()==null?parent.getCategoryType():dto.getCategoryType();
        if(parent.getCategoryType()==3){
            categoryType = 3;
        }else if (categoryType==0){
            categoryType = 2;
        }

        ExpCategoryEntity category = (ExpCategoryEntity) BaseDTO.copyFields(dto,ExpCategoryEntity.class);
        if(otherIncome.equals(parent.getCode()) || parent.getPayType()==1){
            category.setPayType(CompanyBillType.DIRECTION_PAYEE);
        }else {
            category.setPayType(CompanyBillType.DIRECTION_PAYER);
        }
        if(StringUtil.isNullOrEmpty(category.getId())){
            category.initEntity();
            category.setRootId(dto.getPid());
            category.setCreateBy(dto.getAccountId());
            category.setStatus("0");
            category.setCategoryType(categoryType);
            if(StringUtil.isNullOrEmpty(dto.getCompanyId())){
                category.setCompanyId(dto.getCurrentCompanyId());
            }
            int seq = this.expCategoryDao.getMaxExpCategorySeq(dto.getCompanyId(),dto.getPid());
            if(directCost.equals(parent.getCode()) || mainIncome.equals(parent.getCode()) || directCost2.equals(parent.getCode())){
                seq = seq+5;
            }
            category.setSeq(seq);
            return this.expCategoryDao.insert(category);
        }else {
            return this.expCategoryDao.updateById(category);
        }
    }

    @Override
    public AjaxMessage deleteCategoryBaseData(String id) throws Exception {
        ExpCategoryEntity category = this.expCategoryDao.selectById(id);
        if (category==null){
            return new AjaxMessage().setCode("1").setInfo("操作失败");
        }
        Map<String,Object> map = new HashMap<>();
        map.put("pid",category.getPid());
        map.put("status","0");
        List<ExpCategoryEntity> list = expCategoryDao.getDataByParemeter(map);
        if(list!=null && list.size()<2){
            return new AjaxMessage().setCode("1").setInfo("至少保留一项");
        }
        category.setId(id);
        category.setStatus("1");
        expCategoryDao.updateById(category);
        return new AjaxMessage().setCode("0").setInfo("操作成功");
    }
    /**
     * 方法描述：根据companyId查询所有有效项目(我要报销 选择项目下拉框 )
     * 作   者：LY
     * 日   期：2016/7/27 17:39
     */
    private List<ProjectDTO> getProjectList(String companyId) {
        ProjectDTO dto = new ProjectDTO();
        dto.setCompanyId(companyId);

        return projectService.getProjectListByCompanyId(dto);
    }


    /**
     * 方法描述：查询报销类型
     * 用于我要报销界面，报销类型选择
     * 作        者：MaoSF
     * 日        期：2015年12月7日-上午11:21:49
     * @return
     */
    @Override
    public List<ExpTypeDTO> getExpCategoryTypeList(String companyId,String userId)  throws Exception{
        String rootCompanyId = companyService.getRootCompanyId(companyId);
        return this.getExpTypeList(rootCompanyId,companyId);
    }
}
