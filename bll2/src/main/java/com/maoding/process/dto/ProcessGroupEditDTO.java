package com.maoding.process.dto;

import com.maoding.dynamicForm.dto.FormGroupEditDTO;
import org.springframework.util.StringUtils;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/9/14
 * 类名: com.maoding.process.entity.ProcessGroupEditDTO
 * 作者: 张成亮
 * 描述: 流程群组更改信息
 *      由于原有审批管理的查询接口在ProcessService内实现，因此保留一个接口，保持一致性
 *      但根据目前的设计，审批信息使用动态表单表来存储，因此在动态表单服务内进行具体实现，并在动态表单内也提供相同功能的接口
 **/
public class ProcessGroupEditDTO extends FormGroupEditDTO {
}
