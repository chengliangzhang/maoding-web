package com.maoding.enterprise.service.impl;

import com.maoding.core.base.service.NewBaseService;
import com.maoding.core.bean.ApiResult;
import com.maoding.core.bean.ResponseBean;
import com.maoding.core.util.JsonUtils;
import com.maoding.core.util.OkHttpUtils;
import com.maoding.enterprise.dao.EnterpriseDao;
import com.maoding.enterprise.service.EnterpriseService;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("enterpriseService")
public class EnterpriseServiceImpl extends NewBaseService implements EnterpriseService {

    @Autowired
    private EnterpriseDao enterpriseDao;

    protected final Logger logger= LoggerFactory.getLogger(this.getClass());
    /**
     * 方法描述：获取用用放数据甲方
     */
    public ResponseBean getRemoteData(String url, Object param) throws Exception {
        Response res = null;
        try {
            res = OkHttpUtils.postJson(url, param);
            if (res.isSuccessful()) {
                ApiResult<Map<String,Object>> apiResult = JsonUtils.json2pojo(res.body().string(), ApiResult.class);
                ResponseBean responseBean = new ResponseBean();
                responseBean.setError(apiResult.getCode());
                responseBean.setMsg(apiResult.getMsg());
                responseBean.setData(apiResult.getData());
                return responseBean;
            }
        } catch (Exception e) {
            logger.error("getRemoteData 请求工商数据异常", e);
            return ResponseBean.responseError("数据异常");
        }
        return ResponseBean.responseError("数据异常");
    }

    @Override
    public String getEnterpriseName(String id) {
        return enterpriseDao.getEnterpriseNameById(id);
    }
}
