package com.maoding.user.service.impl;

import com.maoding.core.base.dto.BaseDTO;
import com.maoding.core.base.dto.BaseShowDTO;
import com.maoding.core.base.service.GenericService;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.core.util.JsonUtils;
import com.maoding.core.util.OkHttpUtils;
import com.maoding.core.util.StringUtil;
import com.maoding.system.dao.DataDictionaryDao;
import com.maoding.system.entity.DataDictionaryEntity;
import com.maoding.user.dao.*;
import com.maoding.user.dto.AccountDTO;
import com.maoding.user.dto.AppUseDTO;
import com.maoding.user.dto.UserDTO;
import com.maoding.user.dto.UserQueryDTO;
import com.maoding.user.entity.*;
import com.maoding.user.service.UserAttachService;
import com.maoding.user.service.UserService;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 深圳市设计同道技术有限公司
 * 类    名：UserServiceImpl
 * 类描述：人员信息Service
 * 作    者：MaoSF
 * 日    期：2015年11月23日-下午3:37:01
 */
@Service("userService")
public class UserServiceImpl extends GenericService<UserEntity>  implements UserService{

	@Autowired
	private UserDao userDao;

	@Autowired
	private AppUseDao appUseDao;
	
	@Autowired
	private AccountDao accountDao;
	

	@Autowired
	private UserAttachDao userAttachDao;
	
	@Autowired
	private UserAttachService userAttachService;


	@Autowired
	private DataDictionaryDao dataDictionaryDao;


	@Value("${fastdfs.fileCenterUrl}")
	protected String fileCenterUrl;

	@Override
	public AjaxMessage saveOrUpdateUser(AccountDTO entity)throws Exception{
		UserEntity userEntity=new UserEntity();
		BaseDTO.copyFields(entity, userEntity);
		if(null==entity.getId() || "".equals(entity.getId())){
			userEntity.setId(StringUtil.buildUUID());
			userDao.insert(userEntity);
			return new AjaxMessage().setCode("0").setInfo("保存成功");
		}else{
			userDao.updateById(userEntity);
			AccountEntity accountEntity = new AccountEntity();
			accountEntity.setId(entity.getId());
			accountEntity.setUserName(entity.getUserName());
			accountDao.updateById(accountEntity);
			return new AjaxMessage().setCode("0").setInfo("保存成功");
		}
	}

	@Override
	public AjaxMessage saveOrUpdateAppUse(AppUseDTO entity)throws Exception{
		AppUseEntity appUseEntity=new AppUseEntity();
		BaseDTO.copyFields(entity, appUseEntity);
		if(null==entity.getId() || "".equals(entity.getId())){
			appUseEntity.setId(StringUtil.buildUUID());
			appUseEntity.setCreateDate(new Date());
			appUseDao.insert(appUseEntity);
			return new AjaxMessage().setCode("0").setInfo("AppUse信息保存成功");
		}else{
			appUseDao.updateById(appUseEntity);
			return new AjaxMessage().setCode("0").setInfo("AppUse信息修改成功");
		}
	}
	@Override
	public UserDTO getUserById(String id) throws Exception {
		
		UserEntity user = userDao.selectById(id);
		UserDTO dto = new UserDTO();
		BaseDTO.copyFields(user, dto);

		if(null!=dto.getMajor() && !"".equals(dto.getMajor())){//查询专业名称
			DataDictionaryEntity dataDictionaryEntity = dataDictionaryDao.selectById(dto.getMajor());
			if(dataDictionaryEntity!=null){
				dto.setMajorName(dataDictionaryEntity.getName());
			}
		}

		List<UserAttachEntity> list = userAttachService.getUserAttachByUserId(id);
		if(list!=null && list.size()>0){
			dto.setAttachPath(list.get(0).getAttachPath());	
		}
		return dto;
	}
	

	@Override
	public List<UserAttachEntity> getAttachByType(Map<String, Object> paraMap) {
	   
		return userAttachDao.getAttachByType(paraMap);
	}
	@Override
	public List<AppUseEntity> getAppUseList() {

		return appUseDao.getAppUseList();
	}
	@Override
	public int saveOrUpdateAttach(UserAttachEntity entity)  {
		if(null==entity.getId() || "".equals(entity.getId()))
		{
			entity.setId(StringUtil.buildUUID());
			return userAttachDao.insert(entity);
		}else{
			return userAttachDao.updateById(entity);
		}
		
	}
	
	@Override
	public AjaxMessage uploadHeadOriginalImg(MultipartFile file,Map<String,String> param) throws Exception {
//		String fileName=file.getOriginalFilename();
//		String sourceExtendName = fileName.substring(fileName.lastIndexOf('.') + 1);//原始文件的扩展名(不包含“.”)
//		String[] pathArr = uploadService.getWorksResourceUrl(sourceExtendName, param.get("personUrl"));
//		String[] thArr = new String[1];
//		thArr[0]=param.get("th1")+"-0-true-false";//thArr(输出名前缀(widthxheight)-wh-gp-isAll-1为不加前缀) 318X190
//		String path = uploadService.uploadFileByFdfs(file);//上传到fastdfs
//		uploadService.uploadToServer(file, pathArr, thArr);//上传到tomcat
//		Map<String,Object> returnObj = new HashMap<String, Object>();
//		returnObj.put("pathArr", pathArr);
//		returnObj.put("path", path);
//		returnObj.put("fastdfsUrl", param.get("fastdfsUrl"));
		return new AjaxMessage().setCode("0").setData(null);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public AjaxMessage uploadHeadImg(Map<String,Object> param) throws Exception {
//		//MultipartFile file = (MultipartFile)param.get("file");
//		String type = (String)param.get("type");
//		String userId = (String)param.get("userId");
//		String th1 = (String)param.get("th1");
//		if(userId==null || "".equals(userId)){
//			return new AjaxMessage().setCode("1").setData("上传失败");
//		}
//
//		String fileName=(String)param.get("fileName");
//		if(fileName==null || "".equals(fileName)){
//			return new AjaxMessage().setCode("1").setData("请选择文件");
//		}
//
//		int x = (int)Float.parseFloat((String)param.get("x"));
//		int y = (int)Float.parseFloat((String)param.get("y"));
//		int w = (int)Float.parseFloat((String)param.get("w"));
//		int h = (int)Float.parseFloat((String)param.get("h"));
//
//
//		List pathList = (List)param.get("pathArr");
//		String[] path = (String[])pathList.toArray(new String[pathList.size()]);
//		String resPath = (String)param.get("headOriginaImg");
//		String[] thArr = new String[1];
//
//		String sourceExtendName = fileName.substring(fileName.lastIndexOf('.') + 1);//原始文件的扩展名(不包含“.”)
//
//		thArr[0]=th1+"-0-true-false";//thArr(输出名前缀(widthxheight)-wh-gp-isAll-1为不加前缀) 318X190
//		String newPath = path[3]+path[5].substring(0,path[5].lastIndexOf("."))+"_cut."+sourceExtendName;
//		//uploadService.uploadToServer(file, path, thArr);
//	    uploadService.cutImg(path[3]+path[5], newPath, x, y, w, h,sourceExtendName);
//		//String resPath = uploadService.uploadFileByFdfs(path[4]);
//		String cutPath = uploadService.uploadSlaveFileByFdfs(resPath, "_cut", newPath);
//
//		String[] oldPathUrl = null;
//        if(resPath !=null && !resPath.equals("") ){
//        	UserAttachEntity attachEntity=new UserAttachEntity();
//        	Map<String,Object> map=new HashMap<String,Object>();
//            map.put("userId", userId);
//     		map.put("attachType", "5");
//     		List<UserAttachEntity>attachList=this.getAttachByType(map);
//     		 if(null !=attachList && attachList.size()>0){
//     			attachEntity=attachList.get(0);
//     			oldPathUrl = new String[1];
//     			oldPathUrl[0] = attachEntity.getAttachPath();//获取原文件路径
//     			attachEntity.setAttachPath( resPath);
//     		 }else{
//     			attachEntity.setUserId(userId);
//	            attachEntity.setAttachPath( resPath);
//	            attachEntity.setAttachType(type);
//	            attachEntity.setAttachName(fileName);
//	            attachEntity.setCreateBy(userId);
//     		 }
//
//            this.saveOrUpdateAttach(attachEntity);
//
//        //    uploadService.delFtpFile(oldPathUrl);
//            return new AjaxMessage().setCode("0").setData(attachEntity);
//        }
        return new AjaxMessage().setCode("1").setInfo("上传失败");
	}

	@Override
   public AjaxMessage saveOrUpdateUserAttach(Map<String,Object> param)throws Exception{
		String fileGroup=param.get("fileGroup").toString();
		String filePath=param.get("filePath").toString();
	    String fileName=param.get("fileName").toString();
		String userId=param.get("userId").toString();
		param.clear();
		param.put("userId",userId);
		param.put("attachType","5");
		List<UserAttachEntity> userAttachEntityList= userAttachDao.getAttachByType(param);
	    if(CollectionUtils.isEmpty(userAttachEntityList)){
			UserAttachEntity attachEntity=new UserAttachEntity();
			attachEntity.setId(StringUtil.buildUUID());
			attachEntity.setAttachName(fileName);
			attachEntity.setAttachPath(filePath);
			attachEntity.setFileGroup(fileGroup);
			attachEntity.setAttachType("5");
			attachEntity.setUserId(userId);
			attachEntity.set4Base(userId,null,new Date(),null);
			userAttachDao.insert(attachEntity);
		}
		else {
			UserAttachEntity attachEntity=userAttachEntityList.get(0);
			param.put("group",attachEntity.getFileGroup());
			param.put("path",attachEntity.getAttachPath().replaceAll(attachEntity.getFileGroup()+"/",""));

			//TODO 异步删除上一个头像（暂时忽略是否删除失败的策略）
			String json=JsonUtils.obj2json(param);
			RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),json);
			Request request = new Request.Builder()
					.url(fileCenterUrl+"/fastDelete")
					.post(body)
					.build();
			OkHttpUtils.enqueue(request);

			attachEntity.setFileGroup(fileGroup);
			attachEntity.setAttachName(fileName);
			attachEntity.setAttachPath(filePath);
			userAttachDao.updateById(attachEntity);
		}
		Map<String,Object> returnObj = new HashMap<String, Object>();
		returnObj.put("path", fileGroup+"/"+filePath);
		returnObj.put("fastdfsUrl", param.get("fastdfsUrl"));
      return  new AjaxMessage().setCode("0").setInfo("保存成功");

   }

	/**
	 * @param query 查询条件
	 * @return 用户列表
	 * @author 张成亮
	 * @date 2018/7/19
	 * @description 查询用户，id存放的是companyUserId
	 **/
	@Override
	public List<BaseShowDTO> listWithCompanyUserIdByQuery(UserQueryDTO query) {
		return accountDao.listWithCompanyUserIdByQuery(query);
	}
}
