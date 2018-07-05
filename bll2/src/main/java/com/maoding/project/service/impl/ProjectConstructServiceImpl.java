package com.maoding.project.service.impl;

import com.maoding.core.base.dto.BaseDTO;
import com.maoding.core.base.service.GenericService;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.core.util.StringUtil;
import com.maoding.project.dao.ProjectConstructDao;
import com.maoding.project.dao.ProjectConstructDetailDao;
import com.maoding.project.dto.ProjectConstructDTO;
import com.maoding.project.dto.ProjectConstructDetailDTO;
import com.maoding.project.dto.ProjectConstructDetailGroupByProjectDTO;
import com.maoding.project.entity.ProjectConstructDetailEntity;
import com.maoding.project.entity.ProjectConstructEntity;
import com.maoding.project.service.ProjectConstructService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：ProjectService
 * 类描述：建设单位Service
 * 作    者：:LY
 * 日    期：2016年7月20日- 09:38:54
 */
@Service("projectConstructService")
public class ProjectConstructServiceImpl extends GenericService<ProjectConstructEntity> implements ProjectConstructService {

    @Autowired
    private ProjectConstructDao projectConstructDao;

    @Autowired
    private ProjectConstructDetailDao projectConstructDetailDao;


    @Override
    public List<ProjectConstructDTO> getConstructByCompanyId(String companyId) throws Exception{
         return projectConstructDao.getConstructByCompanyId(companyId);
    }

    /**
     * 方法描述：根据当前组织查找客户管理列表（模糊查询）
     * 日   期：2016/7/22 10:04
     *
     * @param map@return
     */
    @Override
    public List<ProjectConstructDTO> getConstructByParam(Map<String, Object> map) throws Exception{
        return projectConstructDao.getConstructByParam(map);
    }


    public  AjaxMessage validateSaveOrUpdateProjectConstruct(ProjectConstructDTO dto) throws Exception{
           /*  验证start  */
        if(StringUtil.isNullOrEmpty(dto.getCompanyName()))
        {
            return new AjaxMessage().setCode("1").setInfo("公司名不能为空");
        }
        String city = dto.getContractCity();
        String province = dto.getContractProvince();
        if(!StringUtil.isNullOrEmpty(province)){
            if(!("北京".equals(province) || "天津".equals(province) || "重庆".equals(province)
                    || "上海".equals(province) || "香港".equals(province) || "台湾".equals(province))
                    && (StringUtil.isNullOrEmpty(city) || "请选择".equals(city)))
            {
                return new AjaxMessage().setCode("1").setInfo("所在地区(市区)不能为空");
            }
        }else{
            return new AjaxMessage().setCode("1").setInfo("所在地区(省份)不能为空");
        }

        List<ProjectConstructDetailDTO> proConstrDetList = dto.getProConstrDetList();
        if(!CollectionUtils.isEmpty(proConstrDetList)) {
            for (ProjectConstructDetailDTO detailDTO : proConstrDetList) {
                if (StringUtil.isNullOrEmpty(detailDTO.getName())) {
                    return new AjaxMessage().setCode("1").setInfo("项目联系人姓名不能为空");
                }
            }
        }
        return new AjaxMessage().setCode("0");
        /*  验证end  */
    }
    /**
     * 方法描述：新增或者修改建设单位
     * 作        者：LY
     * 日        期：2016年7月20日-下午17:58:09
     * @param dto
     * @return
     */
    @Override
    public AjaxMessage saveOrUpdateProjectConstruct(ProjectConstructDTO dto) throws Exception{

        AjaxMessage ajaxMessage = this.validateSaveOrUpdateProjectConstruct(dto);
        if(!"0".equals(ajaxMessage.getCode()))
        {
            return ajaxMessage;
        }

        /* 持久化建设单位start */
        ProjectConstructEntity entity = new ProjectConstructEntity();
        ProjectConstructDetailEntity detailEntity = new ProjectConstructDetailEntity();
        BaseDTO.copyFields(dto, entity);
        if(StringUtil.isNullOrEmpty(dto.getId()))
        {//新增
            String id = StringUtil.buildUUID();
            dto.setId(id);
            entity.setId(id);
            entity.setCreateBy(dto.getAccountId());
            projectConstructDao.insert(entity);
        }
        else
        {//修改
            entity.setUpdateBy(dto.getAccountId());
            projectConstructDao.updateById(entity);
            //删除之前的联系人
            //projectConstructDetailDao.delConstructDetByConsId(dto.getId());
        }
        /* 持久化建设单位end */
        List<ProjectConstructDetailDTO> proConstrDetList = dto.getProConstrDetList();
        /* 持久化建设单位联系人start */
        if(!CollectionUtils.isEmpty(proConstrDetList)) {
            int i = 0;
            for (ProjectConstructDetailDTO detailDTO : proConstrDetList) {
                BaseDTO.copyFields(detailDTO, detailEntity);
                String dId = detailDTO.getId();
                if (StringUtil.isNullOrEmpty(dId)){
                    dId = StringUtil.buildUUID();
                    detailDTO.setId(dId);
                    detailEntity.setId(dId);
                    detailEntity.setConstructId(dto.getId());
                    detailEntity.setSeq(String.valueOf(++i));
                    detailEntity.setCreateBy(dto.getAccountId());
                    projectConstructDetailDao.insert(detailEntity);
                }else {
                    detailEntity.setUpdateBy(dto.getAccountId());
                    projectConstructDetailDao.updateById(detailEntity);
                }
               // detailDTO.setSeq(String.valueOf(i));

            }
        }
        /* 持久化建设单位联系人end */

        return ajaxMessage.setCode("0").setInfo("保存成功").setData(dto);
    }

    /**
     * 方法描述：根据id获取建设单位的详细信息
     * 作者：MaoSF
     * 日期：2016/7/28
     */
    @Override
    public ProjectConstructDTO getProjectConstructById(String id) throws Exception {
        ProjectConstructDTO projectConstructDTO = new ProjectConstructDTO();
        ProjectConstructEntity projectConstructEntity = projectConstructDao.selectById(id);
        if(projectConstructEntity!=null){
            BaseDTO.copyFields(projectConstructEntity,projectConstructDTO);
        }
        List<ProjectConstructDetailEntity> detaiList = projectConstructDetailDao.getDetailByConstructId(id);
        projectConstructDTO.setProConstrDetList(BaseDTO.copyFields(detaiList,ProjectConstructDetailDTO.class));
        return projectConstructDTO;
    }

    /**
     * 方法描述：根据id获取建设单位的详细信息
     * 作者：MaoSF
     * 日期：2016/7/28
     */
    @Override
    public ProjectConstructDTO getProjectConstructByIdAndOtherDetail(String id, String companyId, String projectId) throws Exception {
        ProjectConstructDTO projectConstructDTO = new ProjectConstructDTO();
        ProjectConstructEntity projectConstructEntity = projectConstructDao.selectById(id);
        if(projectConstructEntity!=null){
            BaseDTO.copyFields(projectConstructEntity,projectConstructDTO);
            if(!StringUtil.isNullOrEmpty(projectId)){
                List<ProjectConstructDetailEntity> projectConstructDetailEntityList = projectConstructDetailDao.getDetailByProjectId(id,projectId);
                projectConstructDTO.setProConstrDetList(BaseDTO.copyFields(projectConstructDetailEntityList,ProjectConstructDetailDTO.class));
            }
        }

        if(StringUtil.isNullOrEmpty(id)){//如果是新增
            return projectConstructDTO;
        }
        //编辑的时候，查询当前客户其他项目的联系人
        //获取其他联系人
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("companyId",companyId);
        if(null!=projectConstructEntity&&null!=projectConstructEntity.getId()) {
            map.put("constructId", projectConstructEntity.getId());
        }
        if(!StringUtil.isNullOrEmpty(projectId)){
            map.put("projectId",projectId);
        }

        List<ProjectConstructDetailGroupByProjectDTO> otherList = projectConstructDetailDao.getOtherConstructDetailGroupByProject(map);
        projectConstructDTO.setOtherList(otherList);
        return projectConstructDTO;
    }


    /**
     * 方法描述：获取5个常用的建设单位
     * 作者：MaoSF
     * 日期：2016/8/26
     */
    @Override
    public List<ProjectConstructDTO> getUsedConstructByCompanyId(String companyId) throws Exception{
        List<ProjectConstructEntity> list = projectConstructDao.selectUsedConstructByCompanyId(companyId);
        if(!CollectionUtils.isEmpty(list)){
            return BaseDTO.copyFields(list,ProjectConstructDTO.class);
        }
        return new ArrayList<ProjectConstructDTO>();
    }

}
