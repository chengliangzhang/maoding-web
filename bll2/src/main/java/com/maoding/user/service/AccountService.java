package com.maoding.user.service;

import com.maoding.core.base.service.BaseService;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.org.dto.CompanyDTO;
import com.maoding.user.dto.AccountDTO;
import com.maoding.user.dto.RegisterCompanyDTO;
import com.maoding.user.entity.AccountEntity;

import java.util.List;

public interface AccountService extends BaseService<AccountEntity>{

	/*************************************************注册信息**************************************************************/

	/**
	 * 方法描述：创建用户
	 * 作者：MaoSF
	 * 日期：2016/11/29
	 *
	 * @param:
	 * @return:
	 */
	AccountEntity createAccount(String userName, String password, String cellphone) throws Exception;

	/**
	 * 方法描述：用户注册
	 * 作        者：MaoSF
	 * 日        期：2015年11月30日-下午5:35:15
	 * @param dto
	 * @return
	 */
	public AjaxMessage register(AccountDTO dto) throws Exception;
	
	/**
	 * 方法描述：企业注册
	 * 作        者：MaoSF
	 * 日        期：2016年7月7日-上午9:49:55
	 * @return
	 */
	public AjaxMessage registerCompany(RegisterCompanyDTO dto) throws Exception;


	/**
	 * 方法描述：企业注册(work)
	 * 作        者：MaoSF
	 * 日        期：2016年7月7日-上午9:49:55
	 * @return
	 */
	public AjaxMessage registerCompanyOfWork(RegisterCompanyDTO dto,String accountId) throws Exception;
	
	/**
	 * 方法描述：根据手机号查询注册信息
	 * 作        者：MaoSF
	 * 日        期：2016年7月7日-上午11:33:07
	 * @param cellphone
	 * @return
	 */
	public AccountEntity getAccountByCellphoneOrEmail(String cellphone) throws Exception;
	
	/**
	 * 方法描述：根据手机号查询注册信息(并且转化成DTO)
	 * 作        者：MaoSF
	 * 日        期：2016年7月7日-上午11:33:07
	 * @param cellphone
	 * @return
	 */
	public AccountDTO getAccountDtoByCellphoneOrEmail(String cellphone) throws Exception;


	/**
	 * 方法描述：根据id查询(并且转化成DTO)
	 * 作        者：MaoSF
	 * 日        期：2016年7月7日-上午11:33:07
	 * @param id
	 * @return
	 */
	public AccountDTO getAccountById(String id) throws Exception;



	/**
	 * 方法描述： 查询所有的注册人员
	 * 作        者：MaoSF
	 * 日        期：2016年7月7日-上午11:33:07
	 */
	public List<AccountEntity> selectAll()throws Exception;
}
