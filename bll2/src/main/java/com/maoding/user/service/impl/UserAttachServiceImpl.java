package com.maoding.user.service.impl;

import com.maoding.core.base.dto.BaseDTO;
import com.maoding.core.base.service.GenericService;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.core.util.StringUtil;
import com.maoding.user.dao.UserAttachDao;
import com.maoding.user.dto.UserAttachDTO;
import com.maoding.user.entity.UserAttachEntity;
import com.maoding.user.service.UserAttachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
/**
 * 深圳市设计同道技术有限公司
 * 类    名：userAttachService
 * 类描述：人员信息Service
 * 作    者：MaoSF
 * 日    期：2015年11月23日-下午3:37:01
 */
@Service("userAttachService")
public class UserAttachServiceImpl extends GenericService<UserAttachEntity>  implements UserAttachService{
    @Autowired
	private UserAttachDao userAttachDao;
    
    private final String th1="142X107";//头像缩略图大小
	private final String th2="180X240";//头像缩略图大小
	private final String th3="245X155";

	@Value("${fastdfs.url}")
	protected String fastdfsUrl;

    @Override
	public List<UserAttachEntity> getUserAttachByUserId(String userId)throws Exception{
		 List <UserAttachEntity> userAttachList=userAttachDao.getUserAttachByUserId(userId);
		 return userAttachList;
	}

	@Override
	public List<UserAttachEntity> getAttachByType(Map<String, Object> paraMap)throws Exception {
		return  userAttachDao.getAttachByType(paraMap);
	}

	/**
	 * 方法描述：根据type获取附件
	 * 作        者：TangY
	 * 日        期：2016年3月21日-上午10:29:56
	 *
	 * @param paraMap@return
	 */
	@Override
	public List<UserAttachDTO> getAttachByTypeToDTO(Map<String, Object> paraMap) throws Exception {
		List<UserAttachEntity> list = getAttachByType(paraMap) ;
		return BaseDTO.copyFields(list,UserAttachDTO.class);
	}
	
	@Override
	public int saveOrUpdateAttach(UserAttachEntity entity)throws Exception  {
		if(null==entity.getId() || "".equals(entity.getId()))
		{
			entity.setId(StringUtil.buildUUID());
			return userAttachDao.insert(entity);
		}else{
			return userAttachDao.updateById(entity);
		}
		
	}
	
	@Override
	public Object saveUserAttach(MultipartFile file,Map<String,Object>param)throws Exception{
//		String filename=file.getOriginalFilename();
//		String sourceExtendName = filename.substring(filename.lastIndexOf('.') + 1);//原始文件的扩展名(不包含“.”)
//    	String companyId=(String) param.get("companyId");
//		String userId=(String) param.get("userId");
//		String personUrl=(String) param.get("personUrl");
//	    String 	type=(String) param.get("type");
//		String[] path = uploadService.getWorksResourceUrl(sourceExtendName, personUrl);
//		 String[] thArr = new String[1];
//		if("5".equals(type)){
//	         thArr[0]=th1+"-0-true-false";//thArr(输出名前缀(widthxheight)-wh-gp-isAll-1为不加前缀) 318X190
//		}else if("0".equals(type)){
//			 thArr[0]=th2+"-0-true-false";//thArr(输出名前缀(widthxheight)-wh-gp-isAll-1为不加前缀) 180X240
//
//		}else{
//		     thArr[0]=th3+"-0-true-true";//thArr(输出名前缀(widthxheight)-wh-gp-isAll-1为不加前缀) 164X164
//		}
//		//result = uploadService.uploadAndThumbnailToFtpByImg(file, path, thArr,1);
//
//		String resPath = uploadService.uploadImgWithByFdfs(file, path, thArr);
//		if ("1".equals(param.get("cutFlg"))&&"5".equals(type)) {
//			//String newPath =path[3]+ th1+"_"+path[0].substring(path[0].lastIndexOf("/")+1,path[0].length());
//			String cutPath = uploadService.uploadSlaveFileByFdfs(resPath, "_cut", file);
//			System.out.println(cutPath);
//		}
//		Map<String,Object>paraMap=new HashMap<String, Object>();
//		paraMap.put("companyId", companyId);
//		paraMap.put("fileType", type);
//		paraMap.put("userId", userId);
//		paraMap.put("attachType",type);
//		if("0".equals(type)){
//			List<UserAttachEntity> attcachList=this.getAttachByType(paraMap);
//			if(attcachList.size()>0){
//			  UserAttachEntity userAttEntity=attcachList.get(0);
//			  this.deleteById(userAttEntity.getId());
//			  String [] filePath=new String[4];
//			  filePath[0]=userAttEntity.getAttachPath();
//			  filePath[1]=PathUtil.pathThumbnailFdfs(userAttEntity.getAttachPath(), "142X107");
//			  filePath[2]=PathUtil.pathThumbnailFdfs(userAttEntity.getAttachPath(),"180X240");
//			  filePath[3]=PathUtil.pathThumbnailFdfs(userAttEntity.getAttachPath(),"245X155");
//
//			  for(int i=0;i<filePath.length;i++){
//			   uploadService.delFileByFdfs(filePath[i]);
//			  }
//			}
//		}
//		String pathUrl="";
//		String[] oldPathUrl = null;
//        if(resPath !=null && !resPath.equals("") ){
//        	pathUrl=path[1];
//        /*	pathUrl=PathUtil.pathThumbnail(path[1], "th_"+th3+"_");
//        	if("0".equals(type)){
//        		pathUrl=PathUtil.pathThumbnail(path[1], "th_"+th2+"_");
//        	}*/
//            UserAttachEntity attachEntity=new UserAttachEntity();
//          //  attachEntity.setId(StringUtil.buildUUID()); 在Service中设置
//            attachEntity.setUserId(userId);
//            attachEntity.setAttachPath( resPath);
//            attachEntity.setAttachType(type);
//            attachEntity.setAttachName(file.getName());
//            attachEntity.setCreateBy(userId);
//            attachEntity.setCreateDate(new Date());
//
//            if("5".equals(type)){
//            	Map<String,Object> map=new HashMap<String,Object>();
//                map.put("userId", userId);
//         		map.put("attachType", "5");
//         	//	pathUrl=PathUtil.pathThumbnail(path[1], "th_"+th1+"_");
//				//attachEntity.setAttachPath( pathUrl);
//				//发现pathUrl没有上传成功,但resPath上传成功
//         		attachEntity.setAttachPath( resPath);
//         		List<UserAttachEntity>attachList=this.getAttachByType(map);
//
//         		 if(null !=attachList && attachList.size()>0){
//         			attachEntity=attachList.get(0);
//         			oldPathUrl = new String[5];
//         			oldPathUrl[0] = attachEntity.getAttachPath();//获取原文件路径
//         			attachEntity.setAttachPath( resPath);
//         		 }
//            }
//
//            this.saveOrUpdateAttach(attachEntity);
//            if("5".equals(type) && null!=oldPathUrl){//删除原文件
//            	oldPathUrl[1]=PathUtil.pathThumbnailFdfs(oldPathUrl[0], "142X107");
//            	oldPathUrl[2]=PathUtil.pathThumbnailFdfs(oldPathUrl[0],"180X240");
//            	oldPathUrl[3]=PathUtil.pathThumbnailFdfs(oldPathUrl[0],"245X155");
//				oldPathUrl[4]=PathUtil.pathThumbnailFdfs(oldPathUrl[0],"cut");
//			    for(int i=0;i<oldPathUrl.length;i++){
//			     uploadService.delFileByFdfs(oldPathUrl[i]);
//			   }
//            }
//            return new AjaxMessage().setCode("0").setData("附件上传成功").setData(attachEntity);
//        }
        return AjaxMessage.error("上传失败");

	}

	@Override
	public String getHeadImgUrl(String userId) throws Exception {
		String url = this.userAttachDao.getHeadImg(userId);
		if(!StringUtil.isNullOrEmpty(url)){
			url = this.fastdfsUrl + url;
		}
		return url;
	}


	@Override
	public String getHeadImgNotFullPath(String userId) throws Exception {
		return this.userAttachDao.getHeadImg(userId);
	}
}
