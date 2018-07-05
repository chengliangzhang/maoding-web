package com.maoding.core.common.service.impl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.maoding.core.common.service.UploadService;
import com.maoding.core.component.fastdfs.StorageClientFactory;
import com.maoding.core.component.fastdfs.common.FastDFSException;
import com.maoding.core.constant.SystemParameters;
import com.maoding.core.util.CmykConvert;
import com.maoding.core.util.GetSxCount;
import com.maoding.core.util.ThumbnailUtil;
import com.maoding.core.util.TxtFileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service("uploadService")
public class UploadServiceImpl implements UploadService{
	
	private final Logger log=LoggerFactory.getLogger(getClass());
	
	@Value("${file.temp}")
	private String fileTemp;
	
	@Value("${ueditor.upload}")
	private String uePrefix;

	
	@Value("${fastdfs.url}")
	private String fastdfsUrl;
	
	@Autowired
	private StorageClientFactory storageClientFactory;


	@Override
	public String[] getImgPaths(String content,String pathPrefix) {
		
		if("".equals(content) || null==content){
			return null;
		}
		if(pathPrefix==null){
			pathPrefix = "";
		}
        Pattern p = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]("+pathPrefix+")([^'\"]+)['\"][^>]*>");
        Matcher m = p.matcher(content);
        List<String> list = new ArrayList<String>();
        while(m.find()){
            //System.out.println(m.group());
        	list.add(pathPrefix+m.group(2));
        }
        String[] imgPath = null;
        if(list.size()>0){
        	imgPath = (String[])list.toArray(new String[list.size()]);
        }
        return imgPath;
	}
	

	
	@Override
	public boolean delFileByService(String[] path) {
		
		try {
			if(null!=path && path.length>0){
				TxtFileUtil txtFileUtil = new TxtFileUtil();
				String webRoot = txtFileUtil.getWebRoot();
				
				for (int i = 0; i < path.length; i++) {
					txtFileUtil.deleteFile(webRoot  +  path[i]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public String uploadFileByFdfs(MultipartFile file) {
		String result = "";
		try {
		    String fileExtName = ""; 
			String fileName=file.getOriginalFilename();
		    if (fileName.contains(".")) { 
		        fileExtName = fileName.substring(fileName.lastIndexOf(".") + 1); 
		    } else { 
		        log.error("Fail to upload file, because the format of filename is illegal."); 
		    }
			String[] resultInfo = storageClientFactory.createClient().upload_file(file.getBytes(), fileExtName, null);
			result = resultInfo[0] + "/" + resultInfo[1];
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FastDFSException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	public String uploadFileByFdfs(String filePath) {
		String result = "";
		try {
		    String fileExtName = ""; 
		    if (filePath.contains(".")) { 
		        fileExtName = filePath.substring(filePath.lastIndexOf(".") + 1); 
		    } else { 
		        log.error("Fail to upload file, because the format of filename is illegal."); 
		    }

			String[] resultInfo = storageClientFactory.createClient().upload_file(filePath, fileExtName, null);
			if(resultInfo!=null){
				result = resultInfo[0] + "/" + resultInfo[1];
			}else{
				log.error("Fail to upload file");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FastDFSException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}	
	
	@Override
	public String uploadSlaveFileByFdfs(String masterFilePath, String prefixName, String slaveFilePath){
		String result = "";
	    String slaveFileExtName = ""; 
	    String groupName = masterFilePath.substring(0,masterFilePath.indexOf("/"));//组名
	    String masterFileName = masterFilePath.substring(masterFilePath.indexOf("/")+1,masterFilePath.length());//主文件ID
	    if (slaveFilePath.contains(".")) { 
	        slaveFileExtName = slaveFilePath.substring(slaveFilePath.lastIndexOf(".") + 1); 
	    } else { 
	        log.error("Fail to upload file, because the format of filename is illegal."); 
	        return result; 
	    } 
	    //上传文件 
	    try { 
	    	String[] slaveFileId = storageClientFactory.createClient().upload_file(groupName, masterFileName, prefixName, slaveFilePath,slaveFileExtName, null);
	    	if(slaveFileId!=null){
	    		result = slaveFileId[0] + "/" + slaveFileId[1];
	    	}else{
	    		log.error("Fail to upload file");
	    	}
	    } catch (Exception e) { 
	        log.error("Upload file \"" + slaveFilePath + "\"fails"); 
	    }finally{
	    }
	    return result;
	}

	@Override
	public String uploadSlaveFileByFdfs(String masterFilePath, String prefixName, MultipartFile file){
		String result = "";
		String slaveFileExtName = "";
		System.out.println("masterFilePath==>"+masterFilePath);
		System.out.println("masterFilePath.index==>"+masterFilePath.indexOf("/"));
		System.out.println("masterFilePath.length==>"+masterFilePath.length());
		String groupName = masterFilePath.substring(0,masterFilePath.indexOf("/"));//组名
		String masterFileName = masterFilePath.substring(masterFilePath.indexOf("/")+1,masterFilePath.length());//主文件ID
		String fileName=file.getOriginalFilename();
		if (fileName.contains(".")) {
			slaveFileExtName = fileName.substring(fileName.lastIndexOf(".") + 1);
		} else {
			log.error("Fail to upload file, because the format of filename is illegal.");
		}
		//上传文件
		try {
			String[] slaveFileId = storageClientFactory.createClient().upload_file(groupName, masterFileName, prefixName, file.getBytes(),slaveFileExtName, null);
			if(slaveFileId!=null){
				result = slaveFileId[0] + "/" + slaveFileId[1];
			}else{
				log.error("Fail to upload file");
			}
		} catch (Exception e) {
			log.error("Upload file \"" + result + "\"fails");
		}finally{
		}
		return result;
	}
	@Override
	public int delFileByFdfs(String masterFilePath) {
	    try {
			String groupName = masterFilePath.substring(0,masterFilePath.indexOf("/"));//组名
			String masterFileName = masterFilePath.substring(masterFilePath.indexOf("/")+1,masterFilePath.length());//主文件ID
			return storageClientFactory.createClient().delete_file(groupName, masterFileName);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FastDFSException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 1;
	}

	@Override
	public int downLoadFileByFdfs(HttpServletResponse response,String masterFilePath, String localFileName) {
	    try { 
			String groupName = masterFilePath.substring(0,masterFilePath.indexOf("/"));//组名
			String masterFileName = masterFilePath.substring(masterFilePath.indexOf("/")+1,masterFilePath.length());//主文件ID
            byte[] b = storageClientFactory.createClient().download_file(groupName, masterFileName);

            //1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
            response.setContentType("multipart/form-data");
            //2.设置文件头：最后一个参数是设置下载文件名(假如我们叫a.pdf)

            String fileName1=java.net.URLEncoder.encode(localFileName, "UTF-8");//这是根本要需要转的
            response.setHeader("Content-Disposition", "attachment;fileName="+fileName1);
            ServletOutputStream out;

            try {


                //3.通过response获取ServletOutputStream对象(out)
                out = response.getOutputStream();
                //4.写到输出流(out)中
                out.write(b);
                out.close();
                out.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }

	    } catch (Exception e) { 
	        log.error("Download file \"" + localFileName + "\"fails"); 
	    }
		return 1;
	}
	
	@Override
	public String[] getWorksResourceUrl(String type,String path) {
        String[] url = new String[6];
        try
        {
            String strNYR = "";
            TxtFileUtil txtFileUtil = new TxtFileUtil();
            try
            {
                strNYR = txtFileUtil.getCurNYR();
            }
            catch (Exception e)
            {
        	    e.printStackTrace(new PrintWriter("UploadUtil.getWorksResourceUrl.getCurNYR Is Error"));
                return null;
            }
            String fileDirUrl = "";
            String fileType = "";
            if("image".equals(type))
            {
                // 图片
                fileDirUrl = path + strNYR;
                fileType = "jpg";
            }
            else if("voice".equals(type))
            {
                // 多媒体声音
                fileDirUrl = path + strNYR;
                fileType = "amr";
            }
            else if("video".equals(type))
            {
                // 多媒体视频
                fileDirUrl = path + strNYR;
                fileType = "mp4";
            }
            else if("qrcode".equals(type))
            {
                // 二维码
                fileDirUrl = path + strNYR;
                fileType = "jpg";
            }
            else if("thumb".equals(type))
            {
                // 缩略图
                fileDirUrl = path + strNYR;
                fileType = "jpg";
            }
            else
            {
                try{
                	  // 缩略图
                    fileDirUrl = path + strNYR;
                    fileType = type;
                    
                }catch(Exception e){
                	e.printStackTrace(new PrintWriter("UploadUtil.getWorksResourceUrl.type Is Error"));
                }
            }
            
            String webRoot = txtFileUtil.getWebRoot();
            
            GetSxCount sx = GetSxCount.getInstance();
            Date time = Calendar.getInstance().getTime();
            String saveName = type + "_" + (new SimpleDateFormat("yyyyMMddHHmmssS")).format(time) + "_" + sx.getCount() + "." + fileType;
            String logicUrl = fileDirUrl + saveName;
            String physicsUrl = webRoot + logicUrl;
            url[0] = physicsUrl;
            url[1] = logicUrl;
            url[2] = webRoot + fileDirUrl;
            url[3] = webRoot + fileTemp+fileDirUrl;//临时文件目录
            url[4] = webRoot + fileTemp + logicUrl;//临时文件
            url[5] = saveName;
            
        }
        catch (Exception e)
        {
        	log.error("生成路径失败",e);
        }
        return url;
	}

	
	/**
	 * 方法描述：图片处理方法
	 * 作        者：wangrb
	 * 日        期：2016年1月19日-上午10:44:02
     * @param @param inputDir 大图片路径
     * @param @param outputDir 生成小图片路径
     * @param @param inputFileName 大图片文件名
     * @param @param outputFileName 生成小图片文名
     * @param @param width 生成小图片宽度
     * @param @param height 生成小图片高度
     * @param @param wh 以宽或高为基准(1＝以宽为基准等比例缩放，2＝以高为基准等比例缩放,0=默认按输入的宽高等比例缩放以gp为准)
     * @param @param gp 是否等比缩放(默认为true)
	 * @param isAll 当isAll为true直接生成缩图，当为false先缩略再裁剪
	 * @return
	 * @throws Exception
	 */
	private String compressPics(String inputDir, String outputDir, String inputFileName, String outputFileName,
			int width, int height,int wh, boolean gp,boolean isAll) throws Exception{
		String result="";
        try {
            //获得源文件    
    		File file = new File(inputDir + inputFileName);    
            if (!file.exists()) {    
                return "false";    
            }    
			Image img = ImageIO.read(file);
			
            // 判断图片格式是否正确    
            if (img.getWidth(null) == -1) {   
                log.error(" can't read,retry!" + "<BR>");    
                return "no";    
            } else {
            	result = this.dealCompressPic(isAll, inputDir, outputDir, inputFileName, outputFileName, width, height, img);
            }
			
		} catch (Exception e) {//异常,作cmyk处理
			try{
				String filename = inputDir + inputFileName;
				CmykConvert cm = new CmykConvert();
	          	cm.readImage(filename);
	          	//获得源文件  
	          	File file = new File(inputDir + inputFileName);
	            if (!file.exists()) {    
	                return "false";    
	            }    
				Image img = ImageIO.read(file);	          	
				result = this.dealCompressPic(isAll, inputDir, outputDir, inputFileName, outputFileName, width, height, img);

			}catch (Exception e1){ 
				e1.printStackTrace(); 
			} 
		} 
		return result;
	}
	
	
	private String dealCompressPic(boolean isAll,String inputDir,String outputDir,String inputFileName, String outputFileName,int width,int height,Image img){
		ThumbnailUtil thumbnailUtil = new ThumbnailUtil();
    	if(isAll){
    		if(width==0){
    			if((Integer) img.getHeight(null)<height){//图片比缩略图小，不作处理,使用原图
    				return "original";
    			}else{
    				thumbnailUtil.compressPic(inputDir, outputDir, inputFileName, outputFileName, width, height,2,true);
    			}            			
    		}else if(height==0){
    			if((Integer) img.getWidth(null)<width){//图片比缩略图小，不作处理,使用原图
    				return "original";
    			}else{
    				thumbnailUtil.compressPic(inputDir, outputDir, inputFileName, outputFileName, width, height,1,true);
    			}
    			
    		}else if((Integer) img.getWidth(null)>width || (Integer) img.getHeight(null)>height){
    			thumbnailUtil.compressPic(inputDir, outputDir, inputFileName, outputFileName, width, height,0,true);
    		}else{
    			thumbnailUtil.compressPic(inputDir, outputDir, inputFileName, outputFileName, (Integer) img.getWidth(null), (Integer) img.getHeight(null),0,true);
    		}
    		
    	}else{
            // 为等比缩放计算输出的图片宽度及高度    
            double rate1 = ((double) img.getWidth(null)) / ((double) img.getHeight(null));    
            double rate2 =  (double) width/ (double) height;  
        		if((Integer) img.getWidth(null)<=width && (Integer) img.getHeight(null)<=height){
        			thumbnailUtil.cutCenterImage(inputDir+inputFileName, outputDir+outputFileName, width, height);
        		}else{
        			
        			if(rate2>rate1){
        				thumbnailUtil.compressPic(inputDir, outputDir, inputFileName, outputFileName, width, height,1,true);
        			}else{
        				thumbnailUtil.compressPic(inputDir, outputDir, inputFileName, outputFileName, width, height,2,true);
        			}
            		thumbnailUtil.cutCenterImage(inputDir+outputFileName, outputDir+outputFileName, width, height);
        		}
    	}
    	return "true";
	}
	
	@Override
	public String uploadImgWithByFdfs(MultipartFile file,String path[],String[] thArr) {
		TxtFileUtil txtfileUtil = new TxtFileUtil();
		String masterFilePath = "";
		try {
			
			//上传原图
			masterFilePath = this.uploadFileByFdfs(file);
			if("".equals(masterFilePath)){
				log.error("上传图片失败!");
				return "";
			}
			
			/**生成临时文件目录,并保存到本地-----开始---------------------------------------**/
			File tmpFile = new File(path[3]);
			if(!tmpFile.exists())
			{
				tmpFile.mkdirs();
			} 			
			BufferedInputStream in = new BufferedInputStream(file.getInputStream());//获得文件输入流
			File tmpFile1 = new File(path[4]);
			FileOutputStream outputStream = new FileOutputStream(tmpFile1);
			byte [] bytes = new byte[1024];
			int v;
			//写入文件流
			while((v=in.read(bytes))>0){
				outputStream.write(bytes,0,v);
			}
			outputStream.close();
			/**生成临时文件目录,并保存到本地-----结束---------------------------------------**/
			
			//处理缩略图并上传到fastdfs文件服务
	        String inputFileName = path[0].substring(path[0].lastIndexOf("/")+1,path[0].length());
	        String outputFileName = "";
	        
	        if(null!=thArr && thArr.length>0){
		        for(int i=0;i<thArr.length;i++){
		        	
		        	if(thArr[i]==null){
		        		break;
		        	}
		        	String[] th = thArr[i].split("-");
		        	outputFileName = th[0]+"_"+inputFileName;//存放在本地的缩略图
		        	String[] whArr = th[0].split("X"); 
		        	try {
						String re = this.compressPics(path[3], path[3], inputFileName, outputFileName, Integer.valueOf(whArr[0]), Integer.valueOf(whArr[1]),Integer.valueOf(th[1]),Boolean.valueOf(th[2]),Boolean.valueOf(th[3]));
						if(re.equals("original")){//返回原图处理,即缩略图为原图,上传到fastdfs
							outputFileName = inputFileName;
						}
					} catch (NumberFormatException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
					if(!"".equals(this.uploadSlaveFileByFdfs(masterFilePath, "_"+th[0], path[3]+outputFileName))){//上传成功
						if(!inputFileName.equals(outputFileName)){//非保存在本地的原图,作删除处理
							txtfileUtil.deleteFile(path[3]+outputFileName);//删除本地缩略图	
						}
					}else{
						log.error("上传缩略图失败!");
					}
		        }			        	
		        txtfileUtil.deleteFile(path[3]+inputFileName);//删除本地原图
	        }
			
		} catch (IOException e) {
			log.error("上传失败",e);
		} 
		return masterFilePath;		
	}

	@Override
	public String pathReplaceByFdfs(String content) {
		
		String pathPrefix = SystemParameters.PATHPREFIX;
		String[] path = this.getImgPaths(content,pathPrefix);
		
		TxtFileUtil txtfileUtil = new TxtFileUtil();
		String webRoot = txtfileUtil.getWebRoot();
		
		if(null!=path && path.length>0){
			
			for (int i = 0; i < path.length; i++) {
				
				String newPath = this.uploadFileByFdfs(webRoot+path[i]);
				//替换路径
				if(!"".equals(newPath)){
					content = content.replaceAll(path[i], fastdfsUrl+newPath);	
				}
			}
		}
		return content;
	}

	@Override
	public int delUnwantedFilesByUeByFdfs(String content,String oldContent) {
		int re = 0;
		try {
			String[] path = this.getImgPaths(content,null);
			List<String> newPathName = new ArrayList<String>();
			if(null!=path && path.length>0){
				for (int i = 0; i < path.length; i++) {
					if(path[i].indexOf(fastdfsUrl)>-1){//fastdfs服务器上的文件
						newPathName.add(path[i].replace(fastdfsUrl, "").trim());//group+主文件ID
					}
				}		
			}	
			List<String> oldPathNames = new ArrayList<String>();
			String[] allOldPaths = getImgPaths(oldContent,null);
			if(null!=allOldPaths && allOldPaths.length>0){
				for (int i = 0; i < allOldPaths.length; i++) {
					if(allOldPaths[i].indexOf(fastdfsUrl)>-1){//fastdfs服务器上的文件
						oldPathNames.add(allOldPaths[i].replace(fastdfsUrl, "").trim());//group+主文件ID
					}
				}		
			}	
			if(null!=oldPathNames && oldPathNames.size()>0){//循环处理不存在新内容下的图片,作删除处理
				for (int i = 0; i < oldPathNames.size(); i++) {
					if(null!=newPathName && newPathName.contains(oldPathNames.get(i))){//新内容存在,不作处理
						continue;
					}else{
						re = this.delFileByFdfs(oldPathNames.get(i));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return re;
	}

	@Override
	public String cutImg(String src, String dest, int x, int y, int w, int h,String suffix) {
		try {
			ThumbnailUtil.cutImage(src, dest, x, y, w, h, suffix);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dest;
	}

	@Override
	public void downLoadForLocal(HttpServletResponse response,String filePath) {
		 //1.设置文件ContentType类型，这样设置，会自动判断下载文件类型  
        response.setContentType("multipart/form-data");  
        //2.设置文件头：最后一个参数是设置下载文件名(假如我们叫a.pdf)  
        OutputStream os=null;
		response.reset();	  
        //通过文件路径获得File对象(假如此路径中有一个download.pdf文件)  
        File file = new File(filePath);  
        String fileName=filePath.substring(filePath.lastIndexOf("/")+1, filePath.length());
        try {  
        	response.setHeader("Content-Disposition", "attachment; filename="+fileName);// 设定输出文件头				
			response.setContentType("application/vnd.ms-excel");
			os = response.getOutputStream();
            FileInputStream inputStream = new FileInputStream(file);  
            int b = 0;  
            byte[] buffer = new byte[512];  
            while (b != -1){  
                b = inputStream.read(buffer);  
                //4.写到输出流(out)中  
                os.write(buffer,0,b);  
            }  
            inputStream.close();  
            os.close();  
            os.flush();  
  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
	}
	@Override
	public boolean uploadToServer(MultipartFile imgObj, String[] path,
			String[] thArr) {
		
        try
        {
            if(path == null || path.length == 0)
            {
                return false;
            }
			File file = new File(path[3]);
			if(!file.exists())
			{
			    file.mkdirs();
			}            
            
			BufferedInputStream in = new BufferedInputStream(imgObj.getInputStream());//获得文件输入流

			File file1 = new File(path[4]);
			FileOutputStream outputStream = new FileOutputStream(file1);
			byte [] bytes = new byte[1024];
			int v;
			//写入文件流
			while((v=in.read(bytes))>0){
				outputStream.write(bytes,0,v);
			}
			outputStream.close();
			
			if(null!=thArr && thArr.length>0){//有缩略图要求，进行缩略处理
				
		        String inputFileName = path[0].substring(path[0].lastIndexOf("/")+1,path[0].length());
		        String outputFileName = "";				
		        //生成缩略图并上传
		        for(int i=0;i<thArr.length;i++){
		        	
		        	String[] th = thArr[i].split("-");
		        	outputFileName = th[0]+"_"+inputFileName;//存放在本地的缩略图
		        	String[] whArr = th[0].split("X"); 
		        	
		        	try {
						String re = this.compressPics(path[3], path[3], inputFileName, outputFileName, Integer.valueOf(whArr[0]), Integer.valueOf(whArr[1]),Integer.valueOf(th[1]),Boolean.valueOf(th[2]),Boolean.valueOf(th[3]));
					} catch (NumberFormatException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
		        }						
			}
			
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
		return true;
	}

	@Override
	public void downLoadFile(HttpServletResponse response, String fileName, String filePath) throws UnsupportedEncodingException {

		//获取网站部署路径(通过ServletContext对象)，用于确定下载文件位置，从而实现下载
		String webRoot = new TxtFileUtil().getWebRoot();
		if(filePath.indexOf("http")>-1){//说明是读nginx的地址
			webRoot = "";//不需要此路径
		}

		//1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
		response.setContentType("multipart/form-data");
		//2.设置文件头：最后一个参数是设置下载文件名(假如我们叫a.pdf)

		String fileName1=java.net.URLEncoder.encode(fileName, "UTF-8");//这是根本要需要转的
		response.setHeader("Content-Disposition", "attachment;fileName="+fileName1);
		ServletOutputStream out;
		//通过文件路径获得File对象(假如此路径中有一个download.pdf文件)
		File file = new File(webRoot+filePath + fileName);

		try {
			FileInputStream inputStream = new FileInputStream(file);

			//3.通过response获取ServletOutputStream对象(out)
			out = response.getOutputStream();

			int b = 0;
			byte[] buffer = new byte[512];
			while (b != -1){
				b = inputStream.read(buffer);
				//4.写到输出流(out)中
				out.write(buffer,0,b);
			}
			inputStream.close();
			out.close();
			out.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 方法描述：生成二维码，并保持到fastdfs
	 * 作者：MaoSF
	 * 日期：2016/11/25
	 *
	 * @param url
	 * @param:
	 * @return:
	 */
	@Override
	public String createQrcode(String url,String imgUrl) {
		String qrcodeFilePath = "";
		try {
			int qrcodeWidth = 200;
			int qrcodeHeight = 200;
			String qrcodeFormat = "jpg";

			String[] path = this.getWorksResourceUrl("qrcode", imgUrl);

			HashMap<EncodeHintType, String> hints = new HashMap<EncodeHintType, String>();
			hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
			BitMatrix bitMatrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, qrcodeWidth, qrcodeHeight, hints);

			BufferedImage image = new BufferedImage(qrcodeWidth, qrcodeHeight, BufferedImage.TYPE_INT_RGB);

			File tmpFile = new File(path[3]);
			if(!tmpFile.exists())
			{
				tmpFile.mkdirs();
			}
			File QrcodeFile = new File(path[4]);
			ImageIO.write(image, qrcodeFormat, QrcodeFile);
			MatrixToImageWriter.writeToFile(bitMatrix, qrcodeFormat, QrcodeFile);
			qrcodeFilePath = this.uploadFileByFdfs(path[4]);
			//qrcodeFilePath = QrcodeFile.getAbsolutePath();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return qrcodeFilePath;
	}


//    public void testDownload() {
//        try {
//
//            byte[] b = storageClientFactory.createClient().download_file("group1", "M00/00/00/wKgRcFV_08OAK_KCAAAA5fm_sy874.conf");
//            System.out.println(b);
//            IOUtils.write(b, new FileOutputStream("D:/"+UUID.randomUUID().toString()+".conf"));
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
