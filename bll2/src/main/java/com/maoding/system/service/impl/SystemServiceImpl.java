package com.maoding.system.service.impl;

import com.maoding.core.base.dto.BaseDTO;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.core.util.StringUtil;
import com.maoding.hxIm.service.ImService;
import com.maoding.message.dao.MessageDao;
import com.maoding.notice.service.NoticeService;
import com.maoding.org.dao.CompanyDao;
import com.maoding.org.dao.OrgAuthDao;
import com.maoding.org.dao.TeamOperaterDao;
import com.maoding.org.dto.CompanyDTO;
import com.maoding.org.dto.LoginAdminDTO;
import com.maoding.org.entity.CompanyEntity;
import com.maoding.org.entity.CompanyUserEntity;
import com.maoding.org.entity.OrgAuthEntity;
import com.maoding.org.entity.TeamOperaterEntity;
import com.maoding.org.service.CompanyService;
import com.maoding.org.service.CompanyUserService;
import com.maoding.role.dao.PermissionDao;
import com.maoding.role.dao.RoleDao;
import com.maoding.role.entity.RoleEntity;
import com.maoding.role.service.PermissionService;
import com.maoding.shiro.realm.UserRealm;
import com.maoding.system.service.SystemService;
import com.maoding.user.dao.AccountDao;
import com.maoding.user.dto.AccountDTO;
import com.maoding.user.entity.AccountEntity;
import com.maoding.user.service.AccountService;
import com.maoding.user.service.UserAttachService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：SystemServiceImpl
 * 类描述：系统（公共）serviceImpl
 * 作    者：MaoSF
 * 日    期：2016年7月7日-下午6:07:14
 */
@Service("systemService")
public class SystemServiceImpl implements SystemService {

    @Value("${server.url}")
    protected String serverUrl;
    @Value("${fastdfs.url}")
    protected String fastdfsUrl;
    @Value("${fastdfs.fileCenterUrl}")
    protected String fileCenterUrl;
    @Value("${yongyoucloud.enterpriseUrl}")
    protected String enterpriseUrl;
    @Value("${cdn.url}")
    protected String cdnUrl;

    @Autowired
    private AccountDao accountDao;
    @Autowired
    private TeamOperaterDao teamOperaterDao;
    @Autowired
    private CompanyDao companyDao;
    @Autowired
    private AccountService accountService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private CompanyUserService companyUserService;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private MessageDao messageDao;
    @Autowired
    private UserAttachService userAttachService;
    @Autowired
    private UserRealm userRealm;
    @Autowired
    private OrgAuthDao orgAuthDao;
    @Autowired
    private ImService imService;
    @Autowired
    private NoticeService noticeService;
    @Value("${webscoket.url}")
    private String scoketUrl;

    @Override
    public Map<String, Object> getUserSessionObjOfWork(AccountDTO dto, HttpServletRequest request) throws Exception {
        String userId = dto.getId();
        String companyId = null;
        /********下面一段代码*******/
        AccountEntity account = accountDao.selectById(userId);
        BaseDTO.copyFields(account, dto);
        //从cookie中查找此用户的cookie
        Cookie companyCookie = getCookieByName(request, account.getId());
        String companyUserId = null;
        if (companyCookie != null) {
            companyId = companyCookie.getValue();
            CompanyUserEntity companyUser = companyUserService.getCompanyUserByUserIdAndCompanyId(userId, companyId);
            CompanyEntity companyEntity = companyDao.selectById(companyId);
            if (!(companyUser != null && "1".equals(companyUser.getAuditStatus()) && companyEntity != null && "0".equals(companyEntity.getStatus()))) {//如果cookie中的公司是失效的。则companyId设置为null
                companyId = null;
            }else {
                companyUserId = companyUser.getId();
            }
        }
        Map<String, Object> m = new HashMap<String, Object>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userId", dto.getId());
        List<CompanyDTO> companies = companyDao.getCompanyByUserId(userId);
        if (companyId == null || "".equals(companyId)) {// 如果
            for (CompanyDTO c : companies) {
                companyId = c.getId();
                break;
            }
        }
        if (companyId != null && !"".equals(companyId)) {
            for (CompanyDTO c : companies) {
                if (companyId.equals(c.getId())) {
                    // 设置默认公司
                    m.put("company", c);//当前组织， 切换组织的时候，company需要更换
                    m.put("defaultCompany", c);//默认组织， 切换组织的时候，defaultCompany不需要更换
                }
            }

            if(StringUtil.isNullOrEmpty(companyUserId)){//如果在cookie中没有获取到，则在此处获取
                CompanyUserEntity companyUser = companyUserService.getCompanyUserByUserIdAndCompanyId(userId, companyId);
                if(companyUser!=null){
                    companyUserId = companyUser.getId();
                }
            }
        }

        /*************权限设置**************/
        List<RoleEntity> roles;
        // 此处执行用户查询操作，并将用户信息保存到session中
        roles = roleDao.getUserRolesByOrgId(userId, companyId);

        m.put("companies", companies);
        m.put("user", dto);
        Map<String, Object> role = new HashMap<String, Object>();
        role.put("roles", roles);
        m.put("roleCompany", role);
        m.put("role", roles);
        m.put("userId", userId);
        m.put("companyUserId", companyUserId);
        m.put("companyId", companyId);
        setServerUrl(m,null);
        return m;
    }

    public Cookie getCookieByName(HttpServletRequest request, String name) {
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

    /**
     * 方法描述：登录时，获取相关信息
     * 作        者：MaoSF
     * 日        期：2016年7月7日-下午6:03:38
     */
    @Override
    public Map<String, Object> getUserSessionObjOfAdmin(AccountDTO dto) throws Exception {
        String userId = dto.getId();
        String companyId = dto.getDefaultCompanyId();//此处是注册公司带过来的CompanyId
        /********下面一段代码*******/
        AccountEntity account = accountDao.selectById(userId);
        BaseDTO.copyFields(account, dto);
        if (StringUtil.isNullOrEmpty(companyId)) {
            companyId = account.getDefaultCompanyId();
        }
        Map<String, Object> m = new HashMap<String, Object>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userId", dto.getId());
        List<CompanyDTO> companies = companyDao.getCompanyByUserId(userId);

        if (companyId == null || "".equals(companyId)) {// 如果
            for (CompanyDTO c : companies) {
                companyId = c.getId();
                break;
            }
        }
        if (companyId != null && !"".equals(companyId)) {
            for (CompanyDTO c : companies) {
                if (companyId.equals(c.getId())) {
                    // 设置默认公司
                    m.put("company", c);//当前组织， 切换组织的时候，company需要更换
                    m.put("defaultCompany", c);//默认组织， 切换组织的时候，defaultCompany不需要更换
                }
            }
        }
        /*************权限设置**************/
        List<RoleEntity> roles;
        // 此处执行用户查询操作，并将用户信息保存到session中
        roles = roleDao.getUserRolesByOrgId(userId, companyId);

        m.put("companies", companies);
        m.put("user", dto);
        Map<String, Object> role = new HashMap<String, Object>();
        role.put("roles", roles);
        m.put("roleCompany", role);
        m.put("role", roles);
        m.put("userId", userId);
        m.put("companyId", companyId);
        setServerUrl(m,null);
        return m;
    }

    @Override
    public AjaxMessage login(AccountDTO dto) throws Exception {
        AjaxMessage ajax = new AjaxMessage();
        if (null == dto.getPassword() || "".equals(dto.getPassword())) {
            return ajax.setCode("1").setInfo("密码不能为空！");
        }
        AccountEntity account = accountDao.getAccountByCellphoneOrEmail(dto.getCellphone());
        //如果用户不存在
        if (account == null) {
            return ajax.setCode("1").setInfo("手机号或密码无效");
        }
        //如果密码不正确
        if (!account.getPassword().equals(dto.getPassword())) {
            return ajax.setCode("1").setInfo("手机号或密码无效");
        }
        //用户名密码都正确
        BaseDTO.copyFields(account, dto);
        return ajax.setCode("0").setInfo("登录成功").setData(dto);
    }


    @Override
    public AjaxMessage loginAdmin(LoginAdminDTO dto) throws Exception {

        AjaxMessage ajax = new AjaxMessage();

        if (null == dto.getAdminPassword() || "".equals(dto.getAdminPassword())) {
            return ajax.setCode("1").setInfo("密码不能为空");
        }

        AccountEntity account = accountDao.selectById(dto.getId());

        AccountDTO accountDto = new AccountDTO();
        BaseDTO.copyFields(account, accountDto);

        TeamOperaterEntity teamOperater = teamOperaterDao.getTeamOperaterByCompanyId(dto.getCompanyId(), account.getId());
        //非管理员
        if (teamOperater == null) {
            return ajax.setCode("1").setInfo("密码错误");
        }

        //如果密码不正确
        if (!teamOperater.getAdminPassword().equals(dto.getAdminPassword())) {
            return ajax.setCode("1").setInfo("密码错误");
        }

        Map<String, Object> m = new HashMap<String, Object>();

        m.put("userId", accountDto.getId());
        m.put("user", accountDto);
        m.put("companyId", dto.getCompanyId());
        m.put("company", companyService.getCompanyById(dto.getCompanyId()));

        //密码正确
        return ajax.setCode("0").setInfo("登录成功").setData(m);
    }

    @Override
    public AccountDTO updateAccount(AccountDTO dto) throws Exception {
        AccountEntity entity = new AccountEntity();
        BaseDTO.copyFields(dto, entity);
        accountDao.updateById(entity);
        this.imService.updateImAccount(entity.getId(), entity.getPassword());
        return dto;
    }


    @Override
    public AjaxMessage switchCompany(HttpServletRequest request, HttpServletResponse response, String companyId, String userId) throws Exception {
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("userId", userId);
        m.put("companyId", companyId);
        CompanyUserEntity user = this.companyUserService.getCompanyUserByUserIdAndCompanyId(userId, companyId);
        if (user == null) {
            return AjaxMessage.failed("切换组织失败");
        }
        List<CompanyDTO> companies = companyDao.getCompanyByUserId(userId);
        for (CompanyDTO c : companies) {
            if (companyId.equals(c.getId())) {
                // 设置默认公司
                m.put("company", c);//当前组织， 切换组织的时候，company需要更换
            }
        }
        Map<String, Object> role = new HashMap<String, Object>();
        /*************权限设置**************/
        List<RoleEntity> roles = roleDao.getUserRolesByOrgId(userId, companyId);;
        //获取在当前公司下的部门
        // 此处执行用户查询操作，并将用户信息保存到session中
        m.put("companies", companies);
        m.put("user", (AccountDTO) (userRealm.getSession().getAttribute("user")));
        role.put("roles", roles);
        m.put("roleCompany", role);
        m.put("role", roles);
        m.put("userId", userId);
        m.put("companyUserId",user.getId());
        setServerUrl(m,null);
        userRealm.setSession(request, response, m);
        userRealm.resetRole((AccountDTO) (userRealm.getSession().getAttribute("user")));
        //userRealm.doGetAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());
        return AjaxMessage.succeed(null).setInfo("切换组织成功");
    }

    /**
     * 方法描述：admin模块获取用户相关信息
     * 作        者：MaoSF
     * 日        期：2016年7月13日-上午10:58:10
     */
    @Override
    public ModelMap getCurrUserInfoOfAdmin(ModelMap model, Session getSession) throws Exception {
        String userId = (String) getSession.getAttribute("userId");
        String companyId = (String) getSession.getAttribute("adminCompanyId");
        //组织列表
        List<CompanyDTO> orgList;
        orgList = companyService.getCompanyByUserId(userId);

        //用户信息
        //AccountDTO userInfo = (AccountDTO) getSession.getAttribute("user");
        AccountDTO userInfo = accountService.getAccountById(userId);

        //当前组织信息
        CompanyDTO company = companyService.getCompanyById(companyId);

        CompanyUserEntity companyUser = companyUserService.getCompanyUserByUserIdAndCompanyId(userId, companyId);

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userId", userId);
        param.put("companyId", companyId);
        String roleCodes = permissionService.getPermissionCodeByUserId(param);
        getSession.setAttribute("roleCodes", roleCodes);
        if (null != companyUser) {
            getSession.setAttribute("companyUserId", companyUser.getId());
        }
        model.addAttribute("userInfo", userInfo);
        model.addAttribute("orgList", orgList);
        model.addAttribute("companyInfo", company);
        model.addAttribute("companyUser", companyUser);
        model.addAttribute("roleCodes", roleCodes);
        setServerUrl(model,getSession);
        return model;
    }

    /**
     * 方法描述：admin模块获取用户相关信息
     * 作        者：MaoSF
     * 日        期：2016年7月13日-上午10:58:10
     */
    @Override
    public ModelMap getCurrUserInfoOfAdmin2(ModelMap model, Session getSession) throws Exception {
        String userId = (String) getSession.getAttribute("userId");
        String companyId = (String) getSession.getAttribute("companyId");
        //组织列表
        List<CompanyDTO> orgList = companyService.getCompanyByUserId(userId);

        //当前组织信息
        CompanyDTO company = null;
        if (!StringUtil.isNullOrEmpty(companyId)) {
            company = companyService.getCompanyById(companyId);
        }
        if ((company == null || !"0".equals(company.getStatus()))) {
            if (!CollectionUtils.isEmpty(orgList)) {
                company = orgList.get(0);
                companyId = orgList.get(0).getId();
                getSession.setAttribute("companyId", companyId);
                getSession.setAttribute("company", company);
            } else {
                company = null;
            }
        }

        //用户信息
        //AccountDTO userInfo = (AccountDTO) getSession.getAttribute("user");
        AccountDTO userInfo = accountService.getAccountById(userId);

        CompanyUserEntity companyUser = companyUserService.getCompanyUserByUserIdAndCompanyId(userId, companyId);
        getSession.setAttribute("adminCompanyId", companyId);
        getSession.setAttribute("adminCompany", company);
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userId", userId);
        param.put("companyId", companyId);
        String roleCodes = permissionService.getPermissionCodeByUserId(param);
        getSession.setAttribute("roleCodes", roleCodes);
        //管理员数据查询
        TeamOperaterEntity teamOperaterEntity = teamOperaterDao.getTeamOperaterByCompanyId(companyId, userId);
        if (teamOperaterEntity != null) {
            model.addAttribute("adminFlag", "1");
        } else {
            model.addAttribute("adminFlag", "0");
        }
        if (null != companyUser) {
            getSession.setAttribute("companyUserId", companyUser.getId());
        }
        if (null != companyUser) {
            getSession.setAttribute("companyUserName", companyUser.getUserName());
        }
        model.addAttribute("userInfo", userInfo);
        model.addAttribute("orgList", orgList);
        model.addAttribute("companyInfo", company);
        model.addAttribute("companyUser", companyUser);
        model.addAttribute("roleCodes", roleCodes);
        setServerUrl(model,getSession);
        return model;
    }

    /**
     * 方法描述：work模块获取用户相关信息
     * 作        者：MaoSF
     * 日        期：2016年7月13日-上午10:58:10
     */
    @Override
    public ModelMap getCurrUserInfoOfWork(ModelMap model, Session getSession) throws Exception {
        String userId = (String) getSession.getAttribute("userId");
        String companyId = (String) getSession.getAttribute("companyId");
        //组织列表
        List<CompanyDTO> orgList;
        orgList = companyService.getCompanyByUserId(userId);
        //账号信息
        AccountDTO userInfo = accountService.getAccountById(userId);
        model.addAttribute("userInfo", userInfo);
        //当前组织信息
        CompanyDTO company = companyService.getCompanyById(companyId);
        //用户所在公司信息
        CompanyUserEntity companyUser = companyUserService.getCompanyUserByUserIdAndCompanyId(userId, companyId);
        //管理员数据查询
        TeamOperaterEntity teamOperaterEntity = teamOperaterDao.getTeamOperaterByCompanyId(companyId, userId);
        //String roleCodes =roleDao.getUserRoleCodes(userId,companyId);
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userId", userId);
        param.put("companyId", companyId);
        String roleCodes = permissionService.getPermissionCodeByUserId(param);

        if (teamOperaterEntity != null) {
            model.addAttribute("adminFlag", "1");
        } else {
            model.addAttribute("adminFlag", "0");
        }
        model.addAttribute("userInfo", userInfo);
        model.addAttribute("orgList", orgList);
        model.addAttribute("companyInfo", company);
        model.addAttribute("companyUser", companyUser);
        if (null != companyUser) {
            model.addAttribute("companyUserId", companyUser.getId());
        }
        if (null != companyUser) {
            getSession.setAttribute("companyUserId", companyUser.getId());
        }
        if (null != companyUser) {
            getSession.setAttribute("companyUserName", companyUser.getUserName());
        }
        getSession.setAttribute("roleCodes", roleCodes);
        model.addAttribute("roleCodes", roleCodes);
        setServerUrl(model,getSession);
        return model;
    }


    @Override
    public AjaxMessage forgotPassword(AccountDTO accountDto) throws Exception {
        AjaxMessage ajax = new AjaxMessage();
        if (StringUtil.isNullOrEmpty(accountDto.getPassword())) {
            return ajax.setCode("1").setInfo("密码不能为空");
        }
        AccountEntity account = accountService.getAccountByCellphoneOrEmail(accountDto.getCellphone());
        account.setPassword(accountDto.getPassword());
        accountDao.updateById(account);
        this.imService.updateImAccount(account.getId(), account.getPassword());
        return ajax.setCode("0").setInfo("保存成功");
    }


    @Override
    public AjaxMessage changePassword(AccountDTO accountDto) throws Exception {
        AjaxMessage ajax = new AjaxMessage();
        if (StringUtil.isNullOrEmpty(accountDto.getPassword())) {
            return ajax.setCode("1").setInfo("密码不能为空");
        }
        AccountEntity accountEntity = accountDao.selectById(accountDto.getId());
        if (!accountEntity.getPassword().equals(accountDto.getOldPassword())) {
            return ajax.setCode("1").setInfo("旧密码错误");
        }
        if (accountEntity.getPassword().equals(accountDto.getPassword())) {
            return ajax.setCode("1").setInfo("新旧密码不能一致");
        }
        this.updateAccount(accountDto);

        return ajax.setCode("0").setInfo("保存成功");
    }


    @Override
    public AjaxMessage changeCellphone(AccountDTO accountDto) throws Exception {
        AjaxMessage ajax = new AjaxMessage();
        if (StringUtil.isNullOrEmpty(accountDto.getCellphone())) {
            return ajax.setCode("1").setInfo("手机号不能为空");
        }
        //注册前验证是否已经注册过
        if (null != accountService.getAccountByCellphoneOrEmail(accountDto.getCellphone())) {
            return ajax.setCode("1").setInfo(accountDto.getCellphone() + "已经注册过了！");
        }
        this.updateAccount(accountDto);
        return ajax.setCode("0").setInfo("保存成功");
    }


    @Override
    public AjaxMessage bindMailbox(AccountDTO accountDto) throws Exception {
        AjaxMessage ajax = new AjaxMessage();
        if (StringUtil.isNullOrEmpty(accountDto.getEmail())) {
            return ajax.setCode("1").setInfo("邮箱不能为空！");
        }

        if (null != accountService.getAccountByCellphoneOrEmail(accountDto.getEmail())) {
            return ajax.setCode("1").setInfo(accountDto.getEmail() + "已经注册过了！");
        }
        this.updateAccount(accountDto);
        return ajax.setCode("0").setInfo("保存成功");
    }

    //=============================================================新接口2.0================================================

    /**
     * 方法描述：work模块获取用户相关信息
     * 作        者：MaoSF
     * 日        期：2016年7月13日-上午10:58:10
     */
    @Override
    public Map<String, Object> getCurrUserOfWork(Map<String, Object> model, Session getSession) throws Exception {
        String userId = (String) getSession.getAttribute("userId");
        String companyId = (String) getSession.getAttribute("companyId");

        if (null == userId || "".equals(userId)) {
            return null;
        }

        //当前组织信息
        CompanyDTO company = null;
        if (!StringUtil.isNullOrEmpty(companyId)) {
            company = companyService.getCompanyById(companyId);
        }
        //组织列表
        List<CompanyDTO> orgList;
        orgList = companyService.getCompanyByUserId(userId);

        if ((company == null || !"0".equals(company.getStatus()))) {
            if (!CollectionUtils.isEmpty(orgList)) {
                company = orgList.get(0);
                companyId = orgList.get(0).getId();
                getSession.setAttribute("companyId", companyId);
                getSession.setAttribute("company", company);
            } else {
                company = null;
            }
        }
        //账号信息
        AccountDTO userInfo = accountService.getAccountById(userId);
        //查询头像地址
        userInfo.setImgUrl(userAttachService.getHeadImgUrl(userId));
        model.put("userInfo", userInfo);

        //用户所在公司信息
        CompanyUserEntity companyUser = companyUserService.getCompanyUserByUserIdAndCompanyId(userId, companyId);
        //管理员数据查询
        TeamOperaterEntity teamOperaterEntity = teamOperaterDao.getTeamOperaterByCompanyId(companyId, userId);
        //String roleCodes =roleDao.getUserRoleCodes(userId,companyId);

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userId", userId);
        param.put("companyId", companyId);
        String roleCodes = permissionService.getPermissionCodeByUserId(param);
        getSession.setAttribute("roleCodes", roleCodes);
        if (teamOperaterEntity != null) {
            model.put("adminFlag", "1");
        } else {
            model.put("adminFlag", "0");
        }
        if (null != companyUser) {
            getSession.setAttribute("companyUserId", companyUser.getId());
        }
        //获取消息未读数量
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userId", userId);
        map.put("param2", "0");
        int unReadMessage = messageDao.getMessageCount(map);

        //获取消息未读数量
        map.clear();
        map.put("userId", userId);
        map.put("companyId", companyId);
        int unReadNotice = noticeService.getNotReadNoticeCount(map);

        //查询公司是否认证
        OrgAuthEntity orgAuth = this.orgAuthDao.selectById(companyId);
        if (orgAuth != null) {
            model.put("isAuth", orgAuth.getAuthenticationStatus());//企业是否认证（认证状态(0.默认状态，1.提交审核，2认证通过，3，认证不通过)）
        }
        model.put("userInfo", userInfo);
        model.put("orgList", orgList);
        model.put("companyInfo", company);
        model.put("companyUser", companyUser);
        model.put("roleCodes", roleCodes);
        model.put("unReadMessage", unReadMessage);
        model.put("unReadNotice", unReadNotice);
        setServerUrl(model,getSession);
        return model;
    }

    private void setServerUrl(Map<String, Object> model,Session getSession){
        if(model!=null){
            model.put("scoketUrl", scoketUrl);
            model.put("serverUrl", serverUrl);
            model.put("fastdfsUrl", fastdfsUrl);
            model.put("fileCenterUrl", fileCenterUrl);
            model.put("enterpriseUrl", enterpriseUrl);
            model.put("cdnUrl",cdnUrl);
        }
        if(getSession!=null){
            getSession.setAttribute("scoketUrl", scoketUrl);
            getSession.setAttribute("serverUrl", serverUrl);
            getSession.setAttribute("fastdfsUrl", fastdfsUrl);
            getSession.setAttribute("fileCenterUrl", fileCenterUrl);
            getSession.setAttribute("enterpriseUrl", enterpriseUrl);
            getSession.setAttribute("cdnUrl",cdnUrl);
        }
    }
}
