package com.maoding.core.util;

import com.maoding.core.constant.SystemParameters;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：PathUtil
 * 类描述：路径处理工具类
 * 作    者：Chenxj
 * 日    期：2015年7月21日-下午5:14:35
 */
public class PathUtil {
	/**
	 * 标准化路径（所有路径分隔都用"/"）
	 * @param filePath 路径
	 * @return 标准后的路径
	 */
	public static String pathCalibration(String filePath) {
		filePath=filePath.replaceAll("\\\\", "/");
		return filePath;
	}
	/**
	 * 本地化路径(windows系统"\",其他系统"/")
	 * @param filePath 路径
	 * @return 本地化后的路径
	 */
	public static String pathLocal(String filePath) {
		if(SystemParameters.FILE_SEPARATOR.equals("\\")){
			filePath=filePath.replaceAll("/", "\\\\");
		}else{
			filePath=filePath.replaceAll("\\\\", "/");
		}
		return filePath;
	}
	
	/**
	 * 方法描述：传入图片路径返回图片缩略图路径
	 * 作        者：wangrb
	 * 日        期：2015年8月11日-下午5:52:42
	 * @param path 图片路径
	 * @param prefix 缩略图前缀
	 * @return
	 */
	public static String pathThumbnail(String path,String prefix){
		
		String newPath = "";
		if(null!=path && !"".equals(path)){
			String name = path.substring(path.lastIndexOf("/")+1,path.length());
			String thName = prefix+name; 
			newPath = path.substring(0,path.lastIndexOf("/")+1)+thName;			
		}
		return newPath;
	}
	
	/**
	 * 方法描述：传入图片路径返回图片缩略图路径Fdfs
	 * 作        者：wangrb
	 * 日        期：2015年8月11日-下午5:52:42
	 * @param path 图片路径
	 * @param prefix 缩略图前缀
	 * @return
	 */
	public static String pathThumbnailFdfs(String path,String prefix){
		
		String newPath = "";
		if(null!=path && !"".equals(path)){
			String[] arrName=path.split("[.]");
			newPath=arrName[0]+"_"+prefix+"."+arrName[1];
		}
		return newPath.toString();
	}
	/**
	 * 测试主函数
	 * @param args
	 */
	public static void main(String[] args) {
		String a="/hello/test\\bj\\jj/aaa";
		a=PathUtil.pathThumbnailFdfs("group1/M00/00/01/rBAGSFcPYViAVsXuAAiQfKHDaaQ979.jpg","294X378");
		System.out.println(a);
		a=pathLocal(a);
		System.out.println(a);
	}
}
