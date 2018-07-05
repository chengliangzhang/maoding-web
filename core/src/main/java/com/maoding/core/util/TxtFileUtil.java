package com.maoding.core.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Calendar;

/**
 * 读写文件的工具类
 * 
 * @author wangrubin
 */
public class TxtFileUtil {
	private File file = null;

	/**
	 * 以字符为单位读取文件
	 * 
	 * @param fileName
	 * @return
	 * @throws FileNotFoundException
	 */
	public String readFileByChars(String fileName) throws FileNotFoundException {
		file = new File(fileName);
		StringBuffer fileContent = new StringBuffer();
		Reader reader = null;
		try {
			reader = new InputStreamReader(new FileInputStream(file));
			int tempchar;
			// 一次读一个字节
			while ((tempchar = reader.read()) != -1) {
				fileContent.append((char) tempchar);
			}
			// 关闭流
			reader.close();
		} catch (Exception e) {
			// 异常信息打印
			e.printStackTrace(new PrintWriter("以字符为单位读取文件异常！"));
		} finally {
			if (reader != null) {
				try {
					// 关闭流
					reader.close();
				} catch (IOException e1) {
					// 异常信息打印
					e1.printStackTrace(new PrintWriter("关闭流异常！"));
				}
			}
		}
		// 返回读取到的数据
		return fileContent.toString();
	}

	/**
	 * 读取第一行数据
	 * 
	 * @param fileName
	 * @return
	 * @throws FileNotFoundException
	 */
	public String readFileFirstLine(String fileName)
			throws FileNotFoundException {
		File filee = new File(fileName);
		String tempString = null;
		BufferedReader brr = null;
		try {
			brr = new BufferedReader(new FileReader(filee));
			tempString = brr.readLine();
			brr.close();
		} catch (IOException e) {
			// 异常信息打印
			e.printStackTrace(new PrintWriter("读取文件第一行数据异常！"));
		} finally {
			if (brr != null) {
				try {
					// 关闭流
					brr.close();
				} catch (IOException ioe) {
					ioe.printStackTrace(new PrintWriter("文件流关闭异常！"));
				}
			}
		}
		// 返回数据
		return tempString;
	}

	/**
	 * 指定编码格式写文件
	 * 
	 * @param srcFile
	 * @param srcCode
	 * @param distFile
	 * @param distCode
	 * @return
	 * @throws Exception
	 */

	public boolean writeToTxtFile(String srcFile, String srcCode,
			String distFile, String distCode) throws Exception {
		// 输出流
		Writer writer = null;
		try {
			File dist_File = new File(distFile);
			// 判断文件是否存在
			if (!dist_File.exists() != false) {
				dist_File.createNewFile();
			}
			writer = new OutputStreamWriter(new FileOutputStream(dist_File),
					"GBK");
			File src_File = new File(srcFile);
			// 输入流
			InputStreamReader read = new InputStreamReader(new FileInputStream(
					src_File));
			BufferedReader reader = new BufferedReader(read);
			String line;
			// 逐行读取
			while ((line = reader.readLine()) != null) {
				// 写入文件内
				writer.write(line);
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace(new PrintWriter("指定编码格式写文件异常！"));
			throw e;
		} finally {
			// 关闭流
			writer.close();
		}
		// 返回结果
		return true;
	}

	/**
	 * 将字符串内容写入文件
	 * 
	 * @param fileName
	 *            文件路径
	 * @param content
	 *            内容
	 * @return
	 * @throws Exception
	 */
	public boolean writeToTxtFile(String fileName, String content) {
		FileOutputStream foss = null;
		try {
			File filee = new File(fileName);
			// 判断文件是否存在，不存在就新建一个
			if (!filee.exists() != false) {
				filee.createNewFile();
			}

			// 输出流
			foss = new FileOutputStream(fileName, true);
			// 将要输入的信息转换成流
			ByteArrayInputStream baInput = new ByteArrayInputStream(
					content.getBytes("GBK"));

			byte[] buffer = new byte[8192];
			int ch = 0;
			while ((ch = baInput.read(buffer)) != -1) {
				foss.write(buffer, 0, ch);
			}
			// 关闭流
			baInput.close();
		} catch (Exception e) {
			// 将字符串内容写入文件异常！
			e.printStackTrace();

		} finally {
			if (foss != null) {
				// 关闭流
				try {
					foss.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return true;

	}

	/**
	 * 转换流字符为大写
	 * 
	 * @param ips
	 *            输入流
	 * @param ops
	 *            输出流
	 */
	public static void transform(InputStream ips, OutputStream ops) {
		int ch = 0;
		try {
			while ((ch = ips.read()) != -1) {
				int upperCh = Character.toUpperCase((char) ch);
				ops.write(upperCh);
			}

		} catch (Exception e) {
			// 转换流字符为大写异常！
			e.printStackTrace();
		}
	}

	/**
	 * 创建目录
	 * 
	 * @param dirPath
	 * @return
	 */
	public boolean makeDir(String dirPath) {
		file = new File(dirPath);
		boolean isExist = file.exists();
		if (isExist != true) {
			if (file.mkdirs() == false) {
				return false;
			}
			return true;
		}
		return false;
	}
	
	/**
	 * 返回当前年月日
	 * 
	 * @param dirName
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public String getCurNYR() throws Exception {
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DAY_OF_MONTH);
		String dirUrl = "";
		dirUrl += year + "/" + month + "/" + day + "/";
		return dirUrl;
	}

	/**
	 * 获取路径
	 * 
	 * @return
	 */
	public String getWebRoot() {
		String realUrl = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		String newUrl = "";
		if (realUrl.contains("/WEB-INF/")) {
			newUrl = realUrl.substring(0, realUrl.lastIndexOf("WEB-INF/"));
		}
		realUrl = newUrl.replace("%20", " ");// 此路径不兼容jboss
		return realUrl;
	}

	/**
	 * 返回文件全路径
	 * 
	 * @param url
	 * @return
	 */
	public String getPhysicsUrl(String url) {
		String physicsUrl = this.getWebRoot();
		physicsUrl = physicsUrl + url;
		return physicsUrl;
	}

	/**
	 * 检查文件是否存在(公共方法)
	 * 
	 * @param url
	 *            ：文件地址
	 * @return
	 */
	public boolean checkFile(String url) {

		url = this.getPhysicsUrl(url);
		File file = new File(url);
		if (file.isFile() && file.exists()) {
			return true;
		}
		return false;
	}

	/**
	 * 复制文件
	 * 
	 * @param oldPath
	 * @param newPath
	 */
	public void copyFile(String oldPath, String newPath) {
		try {
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) {
				InputStream inStream = new FileInputStream(oldPath);
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				while ((byteread = inStream.read(buffer)) != -1) {
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
				fs.close();
			}
		} catch (Exception e) {
			// 复制单个文件操作异常！
			e.printStackTrace();

		}

	}

	/**
	 * 以行为单位读取文件
	 * 
	 * @param fileName
	 *            文件路径名
	 * @return
	 */
	public String readFileByLines(String fileName) {
		String phoneStr = null;
		BufferedReader br = null;
		try {
			File file = new File(this.getWebRoot() + fileName);
			StringBuffer sb = new StringBuffer();
			br = new BufferedReader(new FileReader(file));
			String tempString = null;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = br.readLine()) != null) {
				// 将读取到的数据添加到stringBuffer
				sb.append(tempString.trim()).append(",");
			}
			// 截取字符串
			if (sb.lastIndexOf(",") != -1) {
				sb.deleteCharAt(sb.lastIndexOf(","));
			}
			phoneStr = sb.toString();
			sb.setLength(0);
		} catch (Exception e) {
			// 异常信息打印 以行为单位读取文件异常！
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					// 关闭流
					br.close();
				}
			} catch (IOException ioe) {
				// 异常信息打印 关闭文本流异常！
				ioe.printStackTrace();
			}
		}
		// 返回读到的数据
		return phoneStr;
	}

	/**
	 * delete file 删除文件
	 * 
	 * @param fileName
	 * @return
	 */
	public boolean deleteFile(String fileName) {
		File file = new File(fileName);
		// 如果文件存在并类型是文件
		if (file.isFile() && file.exists()) {
			// 删除并返回结果
			return file.delete();
		}
		return false;
	}

	/**
	 * 创建年月日层级目录
	 * 
	 * @param physicsUrl
	 *            绝对路径
	 * @param c
	 *            日历对象
	 * @return
	 */
	public String createDir(String physicsUrl, Calendar c) {
		// 文件操作类
		TxtFileUtil util = new TxtFileUtil();
		// 年
		int year = c.get(Calendar.YEAR);
		// 月
		int month = c.get(Calendar.MONTH) + 1;
		//
		int day = c.get(Calendar.DAY_OF_MONTH);

		String dirPath = year + "/" + month + "/" + day + "/";
		// 生成目录
		util.makeDir(physicsUrl + dirPath);
		return dirPath;
	}

}
