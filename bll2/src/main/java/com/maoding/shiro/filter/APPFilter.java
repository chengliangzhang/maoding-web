package com.maoding.shiro.filter;


import com.maoding.core.bean.JsonResponse;
import com.maoding.core.util.HTTPHelper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by Xx on 2016/7/14.
 */
public class APPFilter extends PathMatchingFilter {

    private String loginUrl = "/iWork/sys/login";

    private void saveRequestAndRedirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
        WebUtils.saveRequest(request);
        WebUtils.issueRedirect(request, response, loginUrl);
    }

    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            //需要认证
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            if (HTTPHelper.isAjaxRequest(httpServletRequest)) {
                JsonResponse jsonResponse = JsonResponse.returnFail(401, "需要验证");
                HTTPHelper.returnJson(response, jsonResponse);
            } else {
                saveRequestAndRedirectToLogin(request, response);
            }
            return false;
        } else {
            return true;
        }
    }
}
