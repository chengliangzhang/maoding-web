package com.maoding.companybill.service.impl;

import com.maoding.companybill.dao.CompanyBillDao;
import com.maoding.companybill.dao.CompanyBillDetailDao;
import com.maoding.companybill.dao.CompanyBillRelationDao;
import com.maoding.companybill.dto.SaveCompanyBillDTO;
import com.maoding.companybill.entity.CompanyBillDetailEntity;
import com.maoding.companybill.entity.CompanyBillEntity;
import com.maoding.companybill.entity.CompanyBillRelationEntity;
import com.maoding.companybill.service.CompanyBillService;
import com.maoding.core.base.dto.BaseDTO;
import com.maoding.core.base.service.NewBaseService;
import com.maoding.core.constant.CompanyBillType;
import com.maoding.core.constant.ExpCategoryConst;
import com.maoding.core.util.DateUtils;
import com.maoding.core.util.DigitUtils;
import com.maoding.core.util.StringUtil;
import com.maoding.dynamicForm.dao.DynamicFormFieldValueDao;
import com.maoding.dynamicForm.dto.DynamicFormFieldValueDTO;
import com.maoding.dynamicForm.dto.FormFieldQueryDTO;
import com.maoding.dynamicForm.entity.DynamicFormFieldValueEntity;
import com.maoding.dynamicForm.service.DynamicFormFieldValueService;
import com.maoding.exception.CustomException;
import com.maoding.financial.dao.ExpDetailDao;
import com.maoding.financial.dao.ExpFixedDao;
import com.maoding.financial.dao.ExpMainDao;
import com.maoding.financial.dto.ExpFixedDataDTO;
import com.maoding.financial.dto.ExpMainDTO;
import com.maoding.financial.entity.ExpDetailEntity;
import com.maoding.financial.entity.ExpMainEntity;
import com.maoding.org.dao.CompanyDao;
import com.maoding.org.dao.CompanyUserDao;
import com.maoding.org.service.CompanyService;
import com.maoding.project.dao.ProjectDao;
import com.maoding.project.entity.ProjectEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("companyBillService")
public class CompanyBillServiceImpl extends NewBaseService implements CompanyBillService {

    @Autowired
    private CompanyUserDao companyUserDao;

    @Autowired
    private CompanyDao companyDao;

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private ExpMainDao expMainDao;

    @Autowired
    private ExpDetailDao expDetailDao;

    @Autowired
    private CompanyBillDao companyBillDao;

    @Autowired
    private ExpFixedDao expfixedDao;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CompanyBillRelationDao companyBillRelationDao;

    @Autowired
    private CompanyBillDetailDao companyBillDetailDao;

    @Autowired
    private DynamicFormFieldValueDao dynamicFormFieldValueDao;

    @Autowired
    private DynamicFormFieldValueService dynamicFormFieldValueService;

    @Override
    public synchronized int saveCompanyBill(SaveCompanyBillDTO dto) throws Exception {

        CompanyBillEntity bill = new CompanyBillEntity();
        BaseDTO.copyFields(dto, bill);
        bill.initEntity();
        bill.setDeleted(0);
        ProjectEntity project = projectDao.selectById(dto.getProjectId());
        bill.setOperatorName(companyUserDao.getUserName(dto.getOperatorId()));
        bill.setProjectName(project != null ? project.getProjectName() : null);
        bill.setPayeeName(companyDao.getCompanyName(dto.getToCompanyId()));
        bill.setPayerName(companyDao.getCompanyName(dto.getFromCompanyId()));
        if (dto.getFeeType() == CompanyBillType.FEE_TYPE_EXPENSE || dto.getFeeType() == CompanyBillType.FEE_TYPE_EXP_APPLY) {//报销，费用
            this.saveExpFee(dto, bill);
        } else if (dto.getFeeType() == CompanyBillType.FEE_TYPE_EXP_FIX || dto.getFeeType() == CompanyBillType.FEE_TYPE_FIX_OTHER_INCOME) { //固定支出,其他收入
            this.saveFixFee(dto, bill);
        } else if (dto.getFeeType() == CompanyBillType.FEE_TYPE_DYNAMIC) {//自定义审批表单
            this.saveDynamicFee(dto, bill);
        } else {
            if (dto.getFeeType() == CompanyBillType.DIRECTION_PAYEE) {//合同回款
                //合同回款的付款方为甲方
                bill.setPayerName(this.projectDao.getEnterpriseNameByProjectId(dto.getProjectId()));
                dto.setFromCompanyId(project != null ? project.getConstructCompany() : null);
            }
            this.saveProjectFee(dto, bill);
        }
        if (!StringUtil.isNullOrEmpty(bill.getPaymentDate())) {
            bill.setPaymentDate(DateUtils.formatDateString(bill.getPaymentDate()));
        }

        //查询该记录是否已经存在，若存在则不添加，防止网络差，引起前端反应慢，用户多点的情形
        if (bill.getFeeType() != CompanyBillType.FEE_TYPE_EXP_FIX
                && bill.getFeeType() != CompanyBillType.FEE_TYPE_FIX_OTHER_INCOME
                && !CollectionUtils.isEmpty(companyBillDao.getCompanyBillByTargetId(dto))) {
            return 0;
        }
        //保存主表数据
        companyBillDao.insert(bill);
        //保存关联的字段
        CompanyBillRelationEntity billRelation = new CompanyBillRelationEntity();
        BaseDTO.copyFields(dto, billRelation);
        billRelation.initEntity();
        billRelation.setId(bill.getId());//必须设置bill.getId(),主键关联//
        return companyBillRelationDao.insert(billRelation);
    }

    public synchronized int saveCompanyBillForFix(SaveCompanyBillDTO dto) throws Exception {
        //从这里设置日期，如果是历史数据的话，创建日期为输入月份的最后一天，如果是当前月的话，创建日期为当下的时间
        Date create = null;
        if (DateUtils.isThisMonth(DateUtils.str2Date(dto.getPaymentDate() + "-01"))) {
            create = DateUtils.getDate();
        } else {
            create = DateUtils.getLastDayOfMonth(dto.getPaymentDate());
        }
        //todo 支出部分
        dto.setFeeType(CompanyBillType.FEE_TYPE_EXP_FIX);
        dto.setPayType(CompanyBillType.DIRECTION_PAYER);
        dto.setFromCompanyId(dto.getCompanyId());
        dto.setToCompanyId(null);
        dto.setCreateDate(create);
        int i = this.saveCompanyBill(dto);
        //todo 收入部分
        dto.setFeeType(CompanyBillType.FEE_TYPE_FIX_OTHER_INCOME);
        dto.setPayType(CompanyBillType.DIRECTION_PAYEE);
        dto.setFromCompanyId(null);
        dto.setToCompanyId(dto.getCompanyId());
        dto.setCreateDate(create);
        this.saveCompanyBill(dto);
        return i;
    }

    @Override
    public void deleteCompanyBill(String targetId) throws Exception {
        companyBillDao.deletedCompanyBill(targetId);
    }


    /**
     * 报销，费用申请，业务处理
     */
    private void saveExpFee(SaveCompanyBillDTO dto, CompanyBillEntity bill) throws Exception {

        ExpMainDTO exp = expMainDao.selectByIdWithUserName(dto.getTargetId());
        List<ExpDetailEntity> detailList = expDetailDao.selectByMainId(dto.getTargetId());
        BigDecimal sum = new BigDecimal("0");
        String projectName = "";
        String desc = "";
        for (ExpDetailEntity detail : detailList) {
            CompanyBillDetailEntity billDetail = new CompanyBillDetailEntity();
            billDetail.initEntity();
            billDetail.setBillId(bill.getId());
            billDetail.setFee(detail.getExpAmount() == null ? new BigDecimal("0") : detail.getExpAmount());
            billDetail.setExpTypeParentName(detail.getExpPName());
            billDetail.setExpTypeName(detail.getExpName());
            billDetail.setFeeDescription(detail.getExpUse());
            billDetail.setSeq(detail.getSeq());
            desc += detail.getExpPName() + "-" + detail.getExpName() + ";";
            if (!StringUtil.isNullOrEmpty(detail.getProjectId())) {
                billDetail.setProjectName(projectDao.getProjectName(detail.getProjectId()));
                projectName += billDetail.getProjectName() + ",";
            }
            sum = sum.add(billDetail.getFee());
            companyBillDetailDao.insert(billDetail);
        }
        bill.setFee(sum);
        bill.setProjectName(StringUtil.isNullOrEmpty(projectName) ? null : projectName.substring(0, projectName.length() - 1));
        bill.setBillDescription(StringUtil.isNullOrEmpty(desc) ? null : desc.substring(0, desc.length() - 1));
        bill.setPayeeName(exp.getUserName());
        bill.setFeeUnit(2);
    }

    /**
     * 项目费用（合同回款，合作设计费，技术审查费，其他收支）业务处理
     */
    private void saveProjectFee(SaveCompanyBillDTO dto, CompanyBillEntity bill) throws Exception {
        CompanyBillDetailEntity billDetail = new CompanyBillDetailEntity();
        billDetail.initEntity();
        billDetail.setBillId(bill.getId());
        billDetail.setProjectName(bill.getProjectName());
        billDetail.setFee(bill.getFee().multiply(new BigDecimal("10000"))); //万元 转成 元
        billDetail.setFeeDescription(bill.getBillDescription());
        if (dto.getPayType() == CompanyBillType.DIRECTION_PAYEE) {
            billDetail.setExpTypeParentName(ExpCategoryConst.EXP_MAIN_INCOME);
        } else {
            billDetail.setExpTypeParentName(ExpCategoryConst.EXP_DIRECT_COST);
        }
        switch (bill.getFeeType()) {
            case 1:
                billDetail.setExpTypeName(ExpCategoryConst.EXP_PROJECT_CONTRACT);
                break;
            case 2:
                billDetail.setExpTypeName(ExpCategoryConst.EXP_PROJECT_TECHNICAL);
                break;
            case 3:
                billDetail.setExpTypeName(ExpCategoryConst.EXP_PROJECT_COOPERATIVE);
                break;
            case 4:
                billDetail.setExpTypeName(ExpCategoryConst.EXP_PROJECT_OTHER);
                break;
            default:
                ;
        }
        companyBillDetailDao.insert(billDetail);
        bill.setFeeUnit(2);
        bill.setFee(bill.getFee().multiply(new BigDecimal("10000")));
    }

    /**
     * 固定支出 业务处理
     */
    private void saveFixFee(SaveCompanyBillDTO dto, CompanyBillEntity mainBill) throws Exception {
        Date createDate = dto.getCreateDate();
        if (createDate == null) {
            createDate = DateUtils.getDate();
        }
        List<ExpFixedDataDTO> detailList = expfixedDao.getExpFixedByExpDate(dto.getCompanyId(), dto.getPaymentDate());
        BigDecimal sum = new BigDecimal("0");
        //先删除原来的数据
        deleteCompanyBillForFixData(dto);
        //处理收入部分的数据
        if (dto.getFeeType() == CompanyBillType.FEE_TYPE_FIX_OTHER_INCOME) {
            for (ExpFixedDataDTO detail : detailList) {
                if (detail.getExpTypeParentName().contains("收入") && detail.getExpAmount() != null) {
                    sum = sum.add(saveCompanyBillDetail(detail, ExpCategoryConst.EXP_OTHER_INCOME, detail.getExpTypeParentName(), mainBill.getId(), createDate));
                }
            }
            mainBill.setFee(sum);
            mainBill.setBillDescription(ExpCategoryConst.EXP_OTHER_INCOME);
            mainBill.setFeeUnit(2);
            mainBill.setCreateDate(createDate);
        } else {//处理支出部分的数据
            List<ExpFixedDataDTO> shareExpList = new ArrayList<>();
            for (ExpFixedDataDTO detail : detailList) {
                if (detail.getExpTypeParentName().contains("分摊") && detail.getExpAmount() != null) {
                    shareExpList.add(detail);
                    continue;
                }
                if (detail.getExpTypeParentName().contains("收入") && detail.getExpAmount() != null) {
                    continue;
                }
                sum = sum.add(saveCompanyBillDetail(detail, ExpCategoryConst.EXP_FIXED_COST, detail.getExpTypeParentName(), mainBill.getId(), createDate));
            }
            mainBill.setFee(sum);
            mainBill.setBillDescription(ExpCategoryConst.EXP_FIXED_COST);
            mainBill.setFeeUnit(2);
            mainBill.setCreateDate(createDate);
            //todo 处理分摊数据,把数据保存到对方的收入方
            if (!CollectionUtils.isEmpty(shareExpList)) {
                //查询分摊到那个组织下
                String toCompanyId = this.companyService.getRootCompanyId(dto.getCompanyId());
                if (!StringUtil.isNullOrEmpty(toCompanyId)) {
                    String fromCompanyName = companyDao.getCompanyName(dto.getCompanyId());
                    String toCompanyName = companyDao.getCompanyName(toCompanyId);
                    BigDecimal sum1 = new BigDecimal("0");
                    CompanyBillEntity bill = new CompanyBillEntity();
                    bill.initEntity();
                    bill.setPaymentDate(mainBill.getPaymentDate());
                    bill.setCompanyId(dto.getCompanyId());
                    bill.setFeeType(CompanyBillType.FEE_TYPE_EXP_FIX);
                    bill.setPayType(CompanyBillType.DIRECTION_PAYER);
                    bill.setDeleted(0);
                    bill.setOperatorName(mainBill.getOperatorName());
                    bill.setPayerName(fromCompanyName);
                    bill.setPayeeName(toCompanyName);

                    for (ExpFixedDataDTO detail : shareExpList) {
                        sum1 = sum1.add(saveCompanyBillDetail(detail, ExpCategoryConst.EXP_FIXED_COST, detail.getExpTypeParentName(), bill.getId(), createDate));

                    }
                    bill.setFee(sum1);
                    bill.setBillDescription(ExpCategoryConst.EXP_FIXED_COST);
                    bill.setFeeUnit(2);
                    bill.setCreateDate(createDate);
                    companyBillDao.insert(bill);
                    //保存关联的字段
                    CompanyBillRelationEntity billRelation = new CompanyBillRelationEntity();
                    billRelation.initEntity();
                    billRelation.setId(bill.getId());//必须设置bill.getId(),主键关联//
                    billRelation.setFromCompanyId(dto.getCompanyId());
                    billRelation.setToCompanyId(toCompanyId);
                    billRelation.setOperatorId(dto.getOperatorId());
                    billRelation.setCreateDate(createDate);
                    companyBillRelationDao.insert(billRelation);

                    //备份一份财务数据给收款方
                    String targetId = bill.getId();
                    sum1 = new BigDecimal("0");
                    bill.initEntity();
                    bill.setFeeType(CompanyBillType.FEE_TYPE_FIX_OTHER_INCOME);
                    bill.setPayType(CompanyBillType.DIRECTION_PAYEE);

                    bill.setCompanyId(toCompanyId);
                    //下面的收付方组织和上面的对应
                    bill.setPayerName(fromCompanyName);
                    bill.setPayeeName(toCompanyName);
                    for (ExpFixedDataDTO detail : shareExpList) {
                        sum1 = sum1.add(saveCompanyBillDetail(detail, ExpCategoryConst.EXP_OTHER_INCOME, ExpCategoryConst.EXP_OTHER_INCOME, bill.getId(), createDate));
                    }
                    bill.setFee(sum1);
                    bill.setBillDescription(ExpCategoryConst.EXP_OTHER_INCOME);
                    bill.setFeeUnit(2);
                    bill.setCreateDate(createDate);
                    companyBillDao.insert(bill);
                    billRelation.setTargetId(targetId);//为了删除方便
                    billRelation.setId(bill.getId());//id重新设置，其他一样
                    billRelation.setCreateDate(createDate);
                    companyBillRelationDao.insert(billRelation);
                }
            }
        }
    }

    /**
     * 保存费用详情数据
     */
    private BigDecimal saveCompanyBillDetail(ExpFixedDataDTO detail, String desc, String expTypeParentName, String billId, Date createDate) throws Exception {
        CompanyBillDetailEntity billDetail = new CompanyBillDetailEntity();
        billDetail.initEntity();
        billDetail.setBillId(billId);
        billDetail.setFee(detail.getExpAmount() == null ? new BigDecimal("0") : detail.getExpAmount());
        billDetail.setExpTypeParentName(expTypeParentName);
        billDetail.setExpTypeName(detail.getExpTypeName());
        billDetail.setFeeDescription(desc);
        billDetail.setSeq(detail.getSeq());
        billDetail.setCreateDate(createDate);
        companyBillDetailDao.insert(billDetail);
        return billDetail.getFee();
    }

    /**
     * 处理删除固定支出费用
     */
    private void deleteCompanyBillForFixData(SaveCompanyBillDTO dto) throws Exception {
        List<CompanyBillEntity> list = null;
        if (dto.getFeeType() == CompanyBillType.FEE_TYPE_FIX_OTHER_INCOME) {
            dto.setIgnoreCompanyId("0");
        }
        list = companyBillDao.getCompanyBillIgnoreCompanyRelation(dto);
        if (!CollectionUtils.isEmpty(list)) {
            for (CompanyBillEntity bill : list) {
                //先删targetId = bill.getId（）的数据
                SaveCompanyBillDTO billQuery = new SaveCompanyBillDTO();
                billQuery.setTargetId(bill.getId());
                List<CompanyBillEntity> list1 = companyBillDao.getCompanyBillByTargetId(billQuery);
                for (CompanyBillEntity bill2 : list1) {
                    deleteByBillId(bill2.getId());
                }
                //再删除直接关联的数据
                deleteByBillId(bill.getId());
            }
        }
    }

    private void deleteByBillId(String billId) {
        companyBillDao.deleteById(billId);
        companyBillRelationDao.deleteById(billId);
        companyBillDetailDao.deleteCompanyBillDetailByBillId(billId);
    }

    /**
     * 保存自定义审批单，金额相关至bill财务表
     *
     * @param dto
     * @param mainBill
     * @throws Exception
     */
    private void saveDynamicFee(SaveCompanyBillDTO dto, CompanyBillEntity mainBill) throws Exception {

        if (dto.getTargetId() == null) {
            throw new CustomException("参数错误");
        }
        //1.封装保存CompanyBillEntity
        FormFieldQueryDTO queryDTO = new FormFieldQueryDTO();
        ExpMainEntity mainEntity = expMainDao.selectById(dto.getTargetId());//dto.getTargetId()就是mainId
        queryDTO.setId(mainEntity.getId());//mainId
        queryDTO.setFormId(mainEntity.getType());//formId
        double feeSum = 0;
        Integer seq = 0;
        boolean flag = false;

        //1.查询模板+数据获取feeSum
        List<DynamicFormFieldValueDTO> fieldList = dynamicFormFieldValueService.listFormFieldValueByFormId(queryDTO);
        for (DynamicFormFieldValueDTO field : fieldList) {
            List<List<DynamicFormFieldValueDTO>> detailFieldList = field.getDetailFieldList();
            for (List<DynamicFormFieldValueDTO> detailList : detailFieldList) {
                for (DynamicFormFieldValueDTO list : detailList) {
                    //list.getFieldType()==5 代表是数字类型的 list.getIsStatistics()==1 代表是参与统计的
                    if(list.getFieldType()==5 && list.getIsStatistics()==1) {
                        flag = true;
                    }
                }
                if (flag) {//如果有统计金额数据，则封装组织账单详情
                    for (DynamicFormFieldValueDTO list : detailList) {
                        // 2.封装保存CompanyBillDetailEntity组织账单详情
                        CompanyBillDetailEntity billDetail = new CompanyBillDetailEntity();
                        billDetail.initEntity();
                        billDetail.setBillId(mainBill.getId());//主记录id
                        feeSum += DigitUtils.parseDouble(list.getFieldValue());
                        double billFee = 0;
                        billFee = DigitUtils.parseDouble(list.getFieldValue());
                        BigDecimal billDetailFee = new BigDecimal(billFee);
                        billDetail.setFee(billDetailFee);

                        String value = list.getFieldValue();
                        if (list.getFieldType() == 6 && null != list.getFieldType()){
                            if (value.contains("-")){
                                String[] result = value.split("-");
                                billDetail.setExpTypeName(result[0]);//费用父类型
                                billDetail.setExpTypeName(result[1]);//费用类型
                            }else{
                                billDetail.setExpTypeName("");
                                billDetail.setExpTypeName(value);
                            }
                        }
                        if (list.getFieldType() == 2){//费用描述
                            billDetail.setFeeDescription(list.getFieldValue());
                        }
                        if (list.getFieldType() == 12){//关联项目
                            ProjectEntity projectEntity = projectDao.selectById(list.getFieldValue());
                            if (null != projectEntity.getProjectName()) {
                                billDetail.setProjectName(projectEntity.getProjectName());//关联的项目名称
                            }
                        }
                        billDetail.setSeq(seq++);//排序
                        companyBillDetailDao.insert(billDetail);
                    }
                }
            }
        }
        mainBill.setCompanyId(dto.getCompanyId());
        BigDecimal fee = new BigDecimal(feeSum);
        mainBill.setFee(fee);//总金额
        mainBill.setFeeType(CompanyBillType.FEE_TYPE_DYNAMIC);//费用类型9（参考CompanyBillType）
        mainBill.setFeeUnit(2);//金额的单位（1：万元，2：元）目前统一用元
        mainBill.setPayType(dto.getPayType());//收款or支出
        mainBill.setBillDescription(dto.getBillDescription());//费用描述
        mainBill.setPaymentDate(dto.getPaymentDate());//费用日期
    }
}
