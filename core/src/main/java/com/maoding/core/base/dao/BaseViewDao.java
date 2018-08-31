package com.maoding.core.base.dao;

import com.maoding.core.base.dto.CorePageDTO;
import com.maoding.core.base.dto.CoreQueryDTO;
import com.maoding.core.base.dto.BaseIdObject;
import com.maoding.core.util.DigitUtils;
import com.maoding.core.util.ObjectUtils;
import com.maoding.core.util.TraceUtils;
import org.apache.ibatis.session.RowBounds;
import tk.mybatis.mapper.common.base.BaseSelectMapper;
import tk.mybatis.mapper.common.example.SelectByExampleMapper;
import tk.mybatis.mapper.common.example.SelectCountByExampleMapper;
import tk.mybatis.mapper.common.rowbounds.SelectByExampleRowBoundsMapper;
import tk.mybatis.mapper.entity.Example;

import javax.validation.constraints.NotNull;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/8/16
 * 类名: com.maoding.core.base.BaseViewDao
 * 作者: 张成亮
 * 描述: 只读数据库访问方法，没有写操作
 **/
public interface BaseViewDao<T extends BaseIdObject> extends BaseSelectMapper<T>,SelectByExampleMapper<T>, SelectCountByExampleMapper<T>, SelectByExampleRowBoundsMapper<T> {
    //FIXME 特别注意，该接口不能被扫描到，否则会出错

    /**
     * 描述     通过编号查找记录
     * 日期     2018/8/16
     * @author  张成亮
     **/
    default T selectById(@NotNull String id){
        return selectByPrimaryKey(id);
    }

    /**
     * 描述       单表或单视图索引
     * 日期       2018/8/16
     * @author   张成亮
     **/
    default List<T> list(@NotNull CoreQueryDTO query){
        Example example = createExample(query);
        List<T> list;
        if (query.getPageSize() != null) {
            RowBounds rowBounds = new RowBounds(DigitUtils.parseInt(query.getPageIndex()),DigitUtils.parseInt(query.getPageSize()));
            list = selectByExampleAndRowBounds(example,rowBounds);
        } else {
            list = selectByExample(example);
        }
        return list;
    }

    /**
     * 描述       获取分页查询结果
     * 日期       2018/8/16
     * @author   张成亮
     **/
    default CorePageDTO<T> listPage(@NotNull CoreQueryDTO query){
        Example example = createExample(query);
        List<T> list;
        if (query.getPageSize() != null) {
            RowBounds rowBounds = new RowBounds(DigitUtils.parseInt(query.getPageIndex()),DigitUtils.parseInt(query.getPageSize()));
            list = selectByExampleAndRowBounds(example,rowBounds);
        } else {
            list = selectByExample(example);
        }
        int count = selectCountByExample(example);

        CorePageDTO<T> page = new CorePageDTO<>();
        page.setTotal(count);
        page.setData(list);
        page.setPageIndex(DigitUtils.parseInt(query.getPageIndex()));
        page.setPageSize(DigitUtils.parseInt(query.getPageSize()));

        return page;
    }

    /**
     * 描述       获取查询条数
     * 日期       2018/8/16
     * @author   张成亮
     **/
    default int count(@NotNull CoreQueryDTO query){
        Example example = createExample(query);
        return selectCountByExample(example);
    }

    /**
     * 描述       获取符合条件的首条记录
     * 日期       2018/8/16
     * @author   张成亮
     **/
    default T getFirst(@NotNull CoreQueryDTO query){
        Integer pageIndex = query.getPageIndex();
        Integer pageSize = query.getPageSize();

        query.setPageIndex(0);
        query.setPageSize(1);
        List<T> list = list(query);
        query.setPageIndex(pageIndex);
        query.setPageSize(pageSize);

        T result = null;
        if (ObjectUtils.isNotEmpty(list) && list.size() > 0) {
            //如果有多条记录符合条件，打印一条信息
            if (list.size() > 1) {
                TraceUtils.info("出现多条符合条件的记录");
            }
            //返回第一条符合条件的记录
            result = list.get(0);
        }
        return result;
    }


    /**
     * 描述       创建一个空查询器
     * 日期       2018/8/16
     * @author   张成亮
     **/
    default Example createExample(){
        Class<T> clazz = getT();
        return new Example(clazz);
    }

    /**
     * 描述       创建一个带条件的查询器
     * 日期       2018/8/17
     * @author   张成亮
     **/
    default Example createExample(@NotNull CoreQueryDTO query){
        Example example = createExample();
        example.createCriteria()
                .andEqualTo(query);
        return example;
    }

    /**
     * 描述       获取泛型类型
     * 日期       2018/8/16
     * @author   张成亮
     **/
    default Class<T> getT(){
        Class<?> thisClass = this.getClass();
        Type[] thisInterfaces = thisClass.getGenericInterfaces();
        return (Class<T>)getT(thisInterfaces);
    }

    /**
     * 描述       获取泛型类型的递归调用方法
     * 日期       2018/8/17
     * @author   张成亮
     **/
    default Type getT(Type[] interfaces){
        Type tClass = null;
        if ((interfaces != null) && (interfaces.length > 0)) {
            for (Type i : interfaces) {
                if (i instanceof ParameterizedType) {
                    Type[] pts = ((ParameterizedType) i).getActualTypeArguments();
                    if ((pts != null) && (pts.length == 1) && (BaseIdObject.class.isAssignableFrom((Class<?>)pts[0]))){
                        tClass = pts[0];
                    }
                }

                if ((tClass == null) && (i instanceof Class)){
                    Type[] subInterfaces = ((Class)i).getGenericInterfaces();
                    tClass = getT(subInterfaces);
                }

                if (tClass != null){
                    break;
                }
            }
        }
        return tClass;
    }
}
