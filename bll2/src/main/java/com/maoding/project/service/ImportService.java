package com.maoding.project.service;


import com.maoding.project.dto.*;

import java.io.InputStream;
import java.util.List;

/**
 * Created by Chengliang.zhang on 2017/7/19.
 */
public interface ImportService {
    /**
     * 导入项目数据
     */
    ImportResultDTO importProjects(InputStream in, ImportProjectDefaultParamDTO params);

    ImportResultDTO importProjects(List<ImportProjectDTO> dataList, ImportProjectDefaultParamDTO params);

    /**
     * 方法描述：费用录入导入
     * 作   者：DongLiu
     * 日   期：2018/2/26 10:39
     */
    ImportExpFixedDTO importExpFixeds(InputStream in, ImportProjectDefaultParamDTO params);

    /**
     * 正式导入到数据库
     * 作   者：DongLiu
     * 日   期：2018/2/26 10:39
     **/
    ImportExpFixedDTO importExpFixedDTOs(List<ExpFixedDTO> dataList, ImportProjectDefaultParamDTO params);

    /**
     * 方法描述：报销费用导入
     * 作   者：DongLiu
     * 日   期：2018/2/26 11:43
     *
     * @param
     * @return
     */
    ImportExpenseDTO ImportExpenses(InputStream in, ImportProjectDefaultParamDTO params);

    /**
     * 报销费用导入,正式导入到数据库
     * 作   者：DongLiu
     * 日   期：2018/2/26 10:39
     **/
    ImportExpenseDTO ImportExpenseDTOs(List<ExpenseDTO> dataList, ImportProjectDefaultParamDTO params);

    /**
     * 方法描述：项目收支导入
     * 作   者：DongLiu
     * 日   期：2018/2/26 14:48
     *
     * @param
     * @return
     */
    ImportExpenditureDTO ImportExpenditures(InputStream in, ImportProjectDefaultParamDTO params);

    /**
     * 项目收支导入,正式导入到数据库
     * 作   者：DongLiu
     * 日   期：2018/2/26 10:39
     **/
    ImportExpenditureDTO ImportExpenditureDTOs(List<ExpenditureDTO> dataList, ImportProjectDefaultParamDTO params);
}
