/**
 * 深圳市设计同道技术有限公司
 * 类    名：UploadService
 * 类描述：TODO
 * 作    者：wangrb
 * 日    期：2015年11月17日-下午6:29:08
 */
package com.maoding.core.common.service;


import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;


public interface UploadService {
	/**
	* 方法描述：删除服务器文件
	* 作        者：wangrb
	* 日        期：2016年3月7日-上午10:42:59
	* @param path
	*/
	public boolean delFileByService(String[] path);


	/**
	* 方法描述：获取URL
	* 作        者：wangrb
	* 日        期：2015年11月18日-上午11:11:35
	* @param type(image,voice,video,qrcode,thumb,本身文件后缀类型)
	* @param path 文件存放的路径（例work/img/）
	* @return
	*/
	public String[] getWorksResourceUrl(String type,String path);
	/**
	* 方法描述：从内容解析img标签　的src，并返回
	* 作        者：wangrb
	* 日        期：2016年2月22日-下午4:32:49
	* @param content 内容
	* @param 例pathPrefix = SystemParameters.PATHPREFIX;
	* @return
	*/
	public String[] getImgPaths(String content,String pathPrefix);

	/**
	* 方法描述：上传文件到fastdfs文件服务器
	* 作    者：wangrb
	* 日    期：2016年3月30日-下午6:30:00
	* @param imgObj 文件对象
	* @return 返回fastdfs生成的路径
	*/
	public String uploadFileByFdfs(MultipartFile imgObj);

	/**
	* 方法描述：上传文件到fastdfs文件服务器
	* 作    者：wangrb
	* 日    期：2016年3月30日-下午6:30:00
	* @param path 文件路径
	* @return 返回fastdfs生成的路径
	*/
	public String uploadFileByFdfs(String filePath);

	/**
	* 方法描述：上传 缩略图到fastdfs文件服务器
	* 作    者：wangrb
	* 日    期：2016年3月30日-下午7:09:21
	* @param masterFilePath 主文件完整路径(组+主文件ID)
	* @param prefixName 缩略图前缀
	* @param slaveFilePath 缩略图文件路径(缩略图是经过处理保存在本地)
	* @return 返回fastdfs生成的路径
	*/
	public String uploadSlaveFileByFdfs(String masterFilePath, String prefixName, String slaveFilePath);

	/**
	* 方法描述：上传 缩略图到fastdfs文件服务器
	* 作    者：wangrb
	* 日    期：2016年3月30日-下午7:09:21
	* @param masterFilePath 主文件完整路径(组+主文件ID)
	* @param prefixName 缩略图前缀
	* @param slaveFilePath 缩略图文件路径(缩略图是经过处理保存在本地)
	* @return 返回fastdfs生成的路径
	*/
	public String uploadSlaveFileByFdfs(String masterFilePath, String prefixName, MultipartFile file);
	/**
	* 方法描述：从fastdfs删除文件
	* 作    者：wangrb
	* 日    期：2016年3月30日-下午9:12:15
	* @param masterFilePath 主文件完整路径(组+主文件ID)
	* @return 0 成功
	*/
	public int delFileByFdfs(String masterFilePath);

	/**
	* 方法描述：下载文件
	* 作    者：wangrb
	* 日    期：2016年3月31日-上午10:07:40
	* @param masterFilePath
	* @param localFileName
	* @return 0成功
	*/
	public int downLoadFileByFdfs(HttpServletResponse response,String masterFilePath, String localFileName);

	/**
	* 方法描述：
	* 作        者：wangrb
	* 日        期：2015年11月18日-上午11:11:30
	* @param file 文件对象
	* @param thArr生成缩略图的路径数组(例：thArr[0]="56X36-0-false-false";//thArr(输出名前缀-wh-gp-isAll))
	* 	   wh 以宽或高为基准(1＝以宽为基准等比例缩放，2＝以高为基准等比例缩放,0=默认按输入的宽高等比例缩放以gp为准)
	* 	   gp 是否等比缩放(默认为true)
	* 	   isAll 为true直接生成缩图，当为false先缩略再裁剪
	* @return 返加主文件的目录(group+主文件ID)
	*/
	public String uploadImgWithByFdfs(MultipartFile file,String path[],String[] thArr);
	/**
	* 方法描述：富文本内容替换图片路径,并上传到fastdfs文件服务器
	* 作    者：wangrb
	* 日    期：2016年3月31日-下午2:46:43
	* @param content 富文本内容
	* @return 返回图片替换后的内容
	*/
	public String pathReplaceByFdfs(String content);

	/**
	* 方法描述：删除富文本中不存在的图片
	* 作    者：wangrb
	* 日    期：2016年3月31日-下午2:47:40
	* @param content 新内容(替换fastdfs路径后的内容)
	* @param oldContent 老内容(未更新前的内容)
	* @return 0即成功
	*/
	public int delUnwantedFilesByUeByFdfs(String content,String oldContent);
	 
	public String cutImg(String src, String dest, int x, int y, int w,int h,String suffix);

	/**
	* 方法描述：本地文件下载
	* 作        者：MaoSF
	* 日        期：2016年4月16日-下午3:24:25
	* @param response
	* @param filePath
	*/
	public void downLoadForLocal(HttpServletResponse response,String filePath);
	/**
	 * 方法描述：上传到服务器
	 * 作        者：wangrb
	 * 日        期：2016年2月22日-上午11:10:28
	 * @param imgObj 文件对象
	 * @param path 路径
	 * @param thArr 缩略图
	 * @return
	 */
	public boolean uploadToServer(MultipartFile imgObj,String[] path,String[] thArr);

	/**
	 * 方法描述：下载到本地
	 * 作        者：wangrb
	 * 日        期：2016年2月22日-上午11:10:28
	 * @param imgObj 文件对象
	 * @param path 路径
	 * @param thArr 缩略图
	 * @return
	 */
	public void downLoadFile(HttpServletResponse response,String fileName,String filePath) throws UnsupportedEncodingException;


	/**
	 * 方法描述：生成二维码，并保持到fastdfs
	 * 作者：MaoSF
	 * 日期：2016/11/25
	 * @param:url:要生成二维码的地址，imgUrl:文件保持的路径，
	 * @return:
	 */
	public  String createQrcode(String url,String imgUrl);
}
