package com.maoding.shiro.realm;

import com.maoding.core.base.dto.BaseDTO;
import com.maoding.core.util.DateUtils;
import com.maoding.core.util.StringUtil;
import com.maoding.role.service.PermissionService;
import com.maoding.shiro.ShiroSession;
import com.maoding.shiro.ShiroSessionDao;
import com.maoding.system.service.SystemService;
import com.maoding.user.dto.AccountDTO;
import com.maoding.user.entity.AccountEntity;
import com.maoding.user.service.AccountService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Xx on 2016/7/14.
 */
public class UserRealm extends AuthorizingRealm {

    @Autowired
    private AccountService  accountService;

    @Autowired
    private SystemService systemService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private ShiroSessionDao sessionDao;

    /**
     * 方法描述：权限授权
     * 作者：MaoSF
     * 日期：2016/8/16
     * @param:
     * @return:
     */
    @Override
    public AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String userId = (String)  SecurityUtils.getSubject().getSession().getAttribute("userId");
        String companyId = (String)  SecurityUtils.getSubject().getSession().getAttribute("companyId");

        String keyString = userId+"-"+companyId+"-permissions";
        byte key[] = keyString.getBytes();
        //角色名的集合

        List<String> permissions=new ArrayList<String>();

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("userId",userId);
        map.put("companyId",companyId);
        String permission =    permissionService.getPermissionCodeByUserId(map);

        if(!StringUtil.isNullOrEmpty(permission)){
            String[] permissionList = permission.split(",");
            for(String r:permissionList){
                permissions.add(r);
            }
        }
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.addStringPermissions(permissions);
        return authorizationInfo;
    }


    public void resetRole(AccountDTO account) throws AuthenticationException{
        RealmSecurityManager rsm = (RealmSecurityManager) SecurityUtils.getSecurityManager();
        UserRealm shiroRealm = (UserRealm)rsm.getRealms().iterator().next();
        Subject subject = SecurityUtils.getSubject();
        String realmName = subject.getPrincipals().getRealmNames().iterator().next();
        //logger.info("oper.user="+user.getEmail()+",login.user="+SecurityUtils.getSubject().getPrincipal().toString());

        //shiroRealm.clearAllCachedAuthorizationInfo2();//清楚所有用户权限
        //第一个参数为用户名,第二个参数为realmName,test想要操作权限的用户
        SimplePrincipalCollection principals = new SimplePrincipalCollection(account.getId(),account.getCellphone());
        subject.runAs(principals);
        shiroRealm.getAuthorizationCache().remove(subject.getPrincipals());
        shiroRealm.getAuthorizationCache().remove(account.getId());
        subject.releaseRunAs();
    }
    /**
     * 方法描述：身份验证
     * 作者：MaoSF
     * 日期：2016/8/16
     * @param:
     * @return:
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException  {
        UsernamePasswordToken tok=(UsernamePasswordToken)token;
        String cellphone = (String)token.getPrincipal();
        AccountDTO account = null;
        String password = null;
        try {
            account = accountService.getAccountDtoByCellphoneOrEmail(cellphone);
            password = String.valueOf(tok.getPassword());

            if (account == null) {
                throw new UnknownAccountException("帐号或密码错误");// 没找到帐号
            }

            if(!account.getPassword().equals(password)){
                throw new UnknownAccountException("帐号或密码错误");// 密码错误
            }

            if("1".equals(account.getStatus())){
                account.setStatus("0");//如果是未激活状态，则激活账户
                AccountEntity accountEntity = new AccountEntity();
                BaseDTO.copyFields(account,accountEntity);
                accountEntity.setActiveTime(DateUtils.date2Str(DateUtils.datetimeFormat));
                accountService.updateById(accountEntity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<String, Object> m=new HashMap<String, Object>();

        try {
           m = systemService.getUserSessionObjOfWork(account,httpServletRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }


        Subject subject = SecurityUtils.getSubject();
        Serializable sessionId = subject.getSession().getId();
        ShiroSession session = (ShiroSession) sessionDao.doReadSession(sessionId);

        setSession(session,m);
        sessionDao.update(session);

        return new SimpleAuthenticationInfo(account.getId(), account.getPassword(), account.getCellphone());
    }


    public void setSession(ShiroSession session,Map<String,Object> m){
       // SecurityUtils.getSubject().getSession().setTimeout(10000);
        session.setAttribute("user", m.get("user"));
        session.setAttribute("userId", m.get("userId"));
        //当前组织， 切换组织的时候，companyId需要更换
        session.setAttribute("companyId", m.get("companyId"));
        session.setAttribute("companyUserId", m.get("companyUserId"));
        session.setAttribute("company", m.get("company"));
        session.setAttribute("roleCompany", m.get("roleCompany"));
        session.setAttribute("role", m.get("role"));
        session.setAttribute("orgType", "company");
    }

    public Session getSession() {
        return SecurityUtils.getSubject().getSession();
    }

    public void setSession(Map<String, Object> m) {
        getSession().setAttribute("user", m.get("user"));
        getSession().setAttribute("userId", m.get("userId"));
        getSession().setAttribute("companyUserId", m.get("companyUserId"));
        //当前组织， 切换组织的时候，companyId需要更换
        getSession().setAttribute("companyId", m.get("companyId"));
        getSession().setAttribute("company", m.get("company"));
        getSession().setAttribute("adminCompanyId", m.get("companyId"));
        getSession().setAttribute("adminCompany", m.get("company"));
        getSession().setAttribute("roleCompany", m.get("roleCompany"));
        getSession().setAttribute("role", m.get("role"));
        getSession().setAttribute("orgType", "company");
    }

    /**
     * 方法描述：设置session
     * 作        者：MaoSF
     * 日        期：2016年7月9日-下午5:29:12
     *
     * @param m
     */
    public void setSession(HttpServletRequest request, HttpServletResponse response, Map<String, Object> m) {
        setSession(m);
        if (response == null) {
            return;
        }
        try {
            Cookie cookie = this.getCookieByName(request, m.get("userId").toString());
            if (cookie == null)
                cookie = new Cookie(m.get("userId").toString(), m.get("companyId").toString());

            cookie.setMaxAge(2592000);
            cookie.setPath("/");
            cookie.setValue(m.get("companyId").toString());
            response.addCookie(cookie);

        } catch (Exception e) {
            System.out.println("-----------Exception catch response----------:" + response);
        }
    }


    private Cookie getCookieByName(HttpServletRequest request, String name) {
        Map<String, Cookie> cookieMap = ReadCookieMap(request);
        if (cookieMap.containsKey(name)) {
            Cookie cookie = (Cookie) cookieMap.get(name);
            return cookie;
        } else {
            return null;
        }
    }

    /**
     * 将cookie封装到Map里面
     *
     * @param request
     *
     * @return
     */
    private Map<String, Cookie> ReadCookieMap(HttpServletRequest request) {
        Map<String, Cookie> cookieMap = new HashMap<String, Cookie>();
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                cookieMap.put(cookie.getName(), cookie);
            }
        }
        return cookieMap;
    }
}
