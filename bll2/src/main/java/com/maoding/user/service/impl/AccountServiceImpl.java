package com.maoding.user.service.impl;

import com.maoding.core.base.dto.BaseDTO;
import com.maoding.core.base.service.GenericService;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.core.util.DateUtils;
import com.maoding.core.util.SecretUtil;
import com.maoding.core.util.StringUtil;
import com.maoding.hxIm.service.ImService;
import com.maoding.org.entity.CompanyEntity;
import com.maoding.org.service.CompanyService;
import com.maoding.user.dao.AccountDao;
import com.maoding.user.dao.UserDao;
import com.maoding.user.dto.AccountDTO;
import com.maoding.user.dto.RegisterCompanyDTO;
import com.maoding.user.entity.AccountEntity;
import com.maoding.user.entity.UserEntity;
import com.maoding.user.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：UserServiceImpl
 * 类描述：账号信息Service
 * 作    者：MaoSF
 * 日    期：2015年11月23日-下午3:37:01
 */
@Service("accountService")
public class AccountServiceImpl extends GenericService<AccountEntity> implements AccountService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ImService imService;

    /**
     * 方法描述：创建用户
     * 作者：MaoSF
     * 日期：2016/11/29
     */
    @Override
    public AccountEntity createAccount(String userName, String password, String cellphone) throws Exception {
        return createAccount(userName,password,cellphone,"1");
    }

    private AccountEntity createAccount(String userName, String password, String cellphone,String status) throws Exception {
        AccountEntity account = new AccountEntity();
        account.setId(StringUtil.buildUUID());
        account.setUserName(userName);
        if(StringUtil.isNullOrEmpty(password)){
            password = SecretUtil.MD5("123456");//默认密码
        }
        account.setPassword(password);
        account.setCellphone(cellphone);
        account.setStatus(status==null?"1":status);
        if("0".equals(status)){
            account.setActiveTime(DateUtils.date2Str(DateUtils.datetimeFormat));
        }

        accountDao.insert(account);
        UserEntity user = new UserEntity();
        BaseDTO.copyFields(account, user);
        userDao.insert(user);

        //创建im账号
        imService.createImAccount(account.getId(),account.getUserName(),account.getPassword());
        return account;
    }

    @Override
    public AjaxMessage register(AccountDTO dto) throws Exception {
        if (null == dto.getPassword() || "".equals(dto.getPassword())) {
            return new AjaxMessage().setCode("1").setInfo("密码不能为空！");
        }
        //状态设置为激活状态
        dto.setStatus("0");
        dto.setActiveTime(DateUtils.date2Str(DateUtils.datetimeFormat));
        //判断当前手机是否已经注册
        AccountEntity account = this.getAccountByCellphoneOrEmail(dto.getCellphone());
        String flag = "0";

        UserEntity user = null;
        if (account == null) {
            //如果没有注册
            account =this.createAccount(dto.getUserName(),dto.getPassword(),dto.getCellphone(),"0");
            dto.setId(account.getId());
        } else {//如果注册，更新
            dto.setId(account.getId());
            BaseDTO.copyFields(dto, account);
            accountDao.updateById(account);
            user = userDao.selectById(account.getId());
            if(user==null){//理论上user是不会为空的，只怕错误删除数据库中的数据，导致user记录被删除，此处判断为null的时候新增，否则更新
                user = new UserEntity();
                BaseDTO.copyFields(account, user);
                userDao.insert(user);
            }else {
                user.setUserName(dto.getUserName());
                userDao.updateById(user);
            }
            //修改im账号密码
            imService.updateImAccount(account.getId(),account.getPassword());
        }

        return new AjaxMessage().setCode("0").setInfo("注册成功，欢迎进入卯丁！").setData(dto);
    }

    /**
     * 方法描述：创建公司（验证方法）
     * 作者：MaoSF
     * 日期：2016/11/30
     *
     * @param:
     * @return:
     */
    public AjaxMessage validateRegisterCompany(String companyName, String companyShortName) throws Exception {

        if (StringUtil.isNullOrEmpty(companyName)) {
            return new AjaxMessage().setCode("1").setInfo("组织名称不能为空");
        }
//        if (StringUtil.isNullOrEmpty(companyShortName)) {
//            return new AjaxMessage().setCode("1").setInfo("公司简称不能为空");
//        }
        //公司名可以重名
        /*if(companyDao.getCompanyByCompanyName(companyName) != null){
			return new AjaxMessage().setCode("1").setInfo(companyName+"：公司名已经被注册了");
		}*/
//        if (companyDao.getCompanyByCompanyShortName(companyShortName) != null) {
//            return new AjaxMessage().setCode("1").setInfo(companyShortName + "：公司简称已经被占用");
//        }
        return new AjaxMessage().setCode("0");
    }

    @Override
    public AjaxMessage registerCompany(RegisterCompanyDTO dto) throws Exception {
        AccountEntity account = null;
        //判断该公司名是否存在
        AjaxMessage ajaxMessage = this.validateRegisterCompany(dto.getCompanyName(), dto.getCompanyShortName());
        if ("0".equals(ajaxMessage.getCode())) {
            account = this.getAccountByCellphoneOrEmail(dto.getCellphone());
            //判断当前手机是否已经注册
            if (account == null) {
                 account = this.createAccount(dto.getUserName(),SecretUtil.MD5(dto.getPassword()),dto.getCellphone(),"0");
            } else {
                account = new AccountEntity();
                account.setUserName(dto.getUserName());
                account.setPassword(SecretUtil.MD5(dto.getPassword()));
                accountDao.updateById(account);
            }
            String currUserId = account.getId();

            //创建公司
            CompanyEntity company = new CompanyEntity();
            BaseDTO.copyFields(dto, company);
            String companyId = this.companyService.saveCompany(company, dto.getUserName(), currUserId, currUserId);

            //返回userId，companyId,以便直接登录，获取数据保存到session
            AccountDTO returnDto = new AccountDTO();
            BaseDTO.copyFields(account,returnDto);
            returnDto.setDefaultCompanyId(companyId);
            return ajaxMessage.setCode("0").setData(returnDto);

        }

        return ajaxMessage;
    }

    @Override
    public AjaxMessage registerCompanyOfWork(RegisterCompanyDTO dto, String currUserId) throws Exception {
        //判断该公司名是否存在
        AjaxMessage ajaxMessage = this.validateRegisterCompany(dto.getCompanyName(), dto.getCompanyShortName());
        if ("0".equals(ajaxMessage.getCode())) {
            AccountDTO accountDTO = this.getAccountById(currUserId);
            if (accountDTO == null) {
                return new AjaxMessage().setCode("1").setInfo("创建失败");
            }
            //创建公司
            CompanyEntity company = new CompanyEntity();
            BaseDTO.copyFields(dto, company);
            String companyId = this.companyService.saveCompany(company,  accountDTO.getUserName(),currUserId,currUserId);
            //返回userId，companyId,以便直接登录，获取数据保存到session
            AccountDTO returnDto = new AccountDTO();
            returnDto.setId(accountDTO.getId());
            returnDto.setDefaultCompanyId(companyId);

            return ajaxMessage.setCode("0").setData(returnDto);
        }
        return ajaxMessage;
    }

    @Override
    public AccountDTO getAccountDtoByCellphoneOrEmail(String cellphone) throws Exception {

        AccountEntity account = this.getAccountByCellphoneOrEmail(cellphone);
        if (account == null) {
            return null;
        }
        AccountDTO dto = new AccountDTO();
        BaseDTO.copyFields(account, dto);
        return dto;
    }

    /**
     * 方法描述：根据id查询(并且转化成DTO)
     * 作        者：MaoSF
     * 日        期：2016年7月7日-上午11:33:07
     *
     * @param id@return
     */
    @Override
    public AccountDTO getAccountById(String id) throws Exception {
        AccountEntity account = this.selectById(id);
        if (account == null) {
            return null;
        }
        AccountDTO dto = new AccountDTO();
        BaseDTO.copyFields(account, dto);
        return dto;
    }

    @Override
    public AccountEntity getAccountByCellphoneOrEmail(String cellphone)
            throws Exception {
        return accountDao.getAccountByCellphoneOrEmail(cellphone);
    }

    public List<AccountEntity> selectAll() throws Exception {
        return accountDao.selectAll();
    }

}
