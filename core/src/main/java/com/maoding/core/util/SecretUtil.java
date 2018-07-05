package com.maoding.core.util;
import com.maoding.core.constant.SystemParameters;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.KeyGenerator;
import java.io.*;
import java.nio.charset.Charset;
import java.security.*;
import java.util.Formatter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * 深圳市设计同道技术有限公司
 * 类    名：SecretUtil
 * 类描述：
 * 作    者：Chenxj
 * 日    期：2016年1月18日-下午12:36:45
 */
public final class SecretUtil {
	private static final Logger log=LoggerFactory.getLogger(SecretUtil.class);
	private static String password;
	private static Key key;
	private static Cipher cipherDecrypt;
	private static Cipher cipherEncrypt;
	private SecretUtil(){
		super();
	}
	static{
		//DES加密密码
		password="apple";
		//根据加密密码获取DES加密key
		key=getKey(password);
		try {
			//获取DES加密对象
			cipherEncrypt=Cipher.getInstance("DES");
			//初始化DES加密对象
			cipherEncrypt.init(Cipher.ENCRYPT_MODE, key);
			//获取DES解密对象
			cipherDecrypt=Cipher.getInstance("DES");
			//初始化DES解密对象
			cipherDecrypt.init(Cipher.DECRYPT_MODE, key);
		} catch (Exception e) {
			log.error("获取DES加解密对象失败", e);
		}
	}
	/**
	 * 加密String，返回String的MD5码
	 * @param encryptStr 待加密String
	 * @return 成功返回String的MD5码，失败返回输入的String
	 */
	public static String MD5(String encryptStr){
		if(StringUtil.isNullOrEmpty(encryptStr)){
			return encryptStr;
		}
		try{
			//获取MD5实例
			MessageDigest md5=MessageDigest.getInstance("MD5");
			String result="";
			byte[]temp;
			temp=md5.digest(encryptStr.getBytes(Charset.forName(SystemParameters.UTF8)));
			for(int i=0;i<temp.length;i++){
				result+=Integer.toHexString((0x000000ff & temp[i]) | 0xffffff00).substring(6);
			}
			return result.toUpperCase();
		}catch(NoSuchAlgorithmException e){
			log.error("获取信息摘要实例错误", e);
		}catch (Exception e) {
			log.error("计算MD5码失败", e);
		}
		return encryptStr;
	}
	/**
	 * 获取文件的信息摘要
	 * @param fileName 文件名
	 * @param hashType 摘要算法
	 * @return 摘要字串 失败或者文件为空则返回文件名
	 */
    public static String getHash(String fileName,String hashType){
    	String r=fileName;
    	try{
    		//如果文件为空文件，则直接返回
            File f = new File(fileName);
            if(f.length()==0){
            	return r;
            }
            InputStream ins = new FileInputStream(f);
            byte[] buffer = new byte[1024];
            MessageDigest md5 = MessageDigest.getInstance(hashType);
            int len;
            while((len = ins.read(buffer)) != -1){
                md5.update(buffer, 0, len);
            }
            ins.close();
            r= DigestUtils.md5Hex(md5.digest());
    	}catch(Exception e){
    		log.error("计算文件"+hashType+"值失败", e);
    	}
    	return r;
    }
	/**
	 * DES加密String
	 * @param encryptStr 待加密String
	 * @return 成功返回加密后的String，失败返回未加密的String
	 */
	public static String EncryptDES(String encryptStr){
		return getEncString(encryptStr, key);
	}
	/**
	 * 解密DES加密后的String
	 * @param decryptStr DES加密的String
	 * @return 成功返回解密后的String，失败返回未解密的String
	 */
	public static String DecryptDES(String decryptStr){
		return getDecString(decryptStr, key);
	}
	/**
	 * DES加密文件
	 * @param inFilePath 待加密的文件绝对路径
	 * @param outFilePath 加密后的文件存放路径
	 * @return 成功返回true，失败返回false
	 */
	public static boolean EncryptDES(String inFilePath,String outFilePath){
		try {
			FileInputStream fin=new FileInputStream(inFilePath);
			FileOutputStream fou=new FileOutputStream(outFilePath);
			return encryptFileByDES(fin, fou);
		} catch (FileNotFoundException e) {
			log.error("文件不存在",e);
		}
		return false;
	}
	/**
	 * DES解密文件
	 * @param inFilePath 待解密的DES加密文件路径
	 * @param outFilePath 解密后文件存放路径
	 * @return 成功返回true，失败返回false
	 */
	public static boolean DecryptDES(String inFilePath,String outFilePath){
		try {
			FileInputStream fin=new FileInputStream(inFilePath);
			FileOutputStream fou=new FileOutputStream(outFilePath);
			return decryptFileByDES(fin, fou);
		} catch (FileNotFoundException e) {
			log.error("文件不存在",e);
		}
		return false;
	}
	//获取DES加密所需的key
	private static Key getKey(String strKey){
		try{
			KeyGenerator kg=KeyGenerator.getInstance("DES");
			kg.init(new SecureRandom(strKey.getBytes(Charset.forName(SystemParameters.UTF8))));
			Key tmpKey=kg.generateKey();
			kg=null;
			return tmpKey;
		}catch(Exception e){
			log.error("未能获取DES加密所需的key", e);
		}
		return null;
	}
	/**
	 * 获取DES加密String
	 * @param strMing 输入的String
	 * @param key 用于DES加密的Key
	 * @return 返回加密后的string
	 */
	private static String getEncString(String strMing,Key key){
		try{
			return byte2hex(getEncCode(strMing.getBytes(Charset.forName(SystemParameters.UTF8)),key));
		}catch(Exception e){
			log.error("错误的字符编码", e);
		}
		return strMing;
	}
	/**
	 * 获取DES解密后的String
	 * @param strMi 输入的待解密string
	 * @param key 解密String的key
	 * @return 返回解密后的String
	 */
	private static String getDecString(String strMi,Key key){
		try{
			return new String(getDecCode(hex2byte(strMi.getBytes(Charset.forName(SystemParameters.UTF8))),key));
		}catch(Exception e){
			log.error("错误的字符编码", e);
		}
		return strMi;
	}
	/**
	 * 获取DES加密后的比特数组
	 * @param bytes 待加密数据的比特数组
	 * @param key 加密用的key
	 * @return 返回加密后的比特数组
	 */
	private static byte[] getEncCode(byte[]bytes,Key key){
		byte[]byteFina=null;
		Cipher cipher;
		try{
			cipher=Cipher.getInstance("DES");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byteFina=cipher.doFinal(bytes);
		}catch(Exception e){
			log.error("未能获取DES加密后的比特数组", e);
		}finally{
			cipher=null;
		}
		return byteFina;
	}
	/**
	 * 获取解密后的比特数组
	 * @param byteD 待解密的比特数组
	 * @param key 用于解密的key
	 * @return 返回解密后的比特数组
	 */
	private static byte[] getDecCode(byte[]byteD,Key key){
		Cipher cipher;
		byte[]byteFina=null;
		try{
			cipher=Cipher.getInstance("DES");
			cipher.init(Cipher.DECRYPT_MODE, key);
			byteFina=cipher.doFinal(byteD);
		}catch(Exception e){
			log.error("未能获取解密后的比特数组", e);
		}finally{
			cipher=null;
		}
		return byteFina;
	}
	/**
	 * 比特数组转为16进制字符
	 * @param bs 待转换的比特数组
	 * @return 返回转换后的String
	 */
	private static String byte2hex(byte[]bs){
		int l=bs.length;
		if(l>4000){
			//数据量大于4000室此方法效率要明显高于下面的方法
			StringBuilder sb=new StringBuilder(l*2);
			Formatter fm=new Formatter(sb);
			for(byte b:bs){
				fm.format("%02X", b);
			}
			fm.close();
			return sb.toString().toUpperCase();
		}else{
//			此种方法在处理大量数据时效率太低，在处理小数据量时效率要明显高于上面的方法。
			String hs="";
			String stmp="";
			for(int n=0;n<l;n++){
				stmp=(java.lang.Integer.toHexString(bs[n]&0XFF));
				if(stmp.length()==1)
					hs+="0"+stmp;
				else
					hs+=stmp;
			}
			return hs.toUpperCase();
		}
	}
	/**
	 * 16进制的字符转比特数组
	 * @param b 16进制字符
	 * @return 16进制的比特数组
	 */
	private static byte[] hex2byte(byte[]b){
		if((b.length%2)!=0)
			throw new IllegalArgumentException("not odds");
		byte[]b2=new byte[b.length/2];
		for(int n=0;n<b.length;n+=2){
			String item=new String(b,n,2);
			b2[n/2]=(byte)Integer.parseInt(item,16);
		}
		return b2;
	}
	/**
	 * 用DES加密文件，结果输出到指定文件中
	 * @param in 输入文件流
	 * @param out 输出文件流
	 * @return 成功返回true，失败返回false
	 */
	private static boolean encryptFileByDES(FileInputStream in,FileOutputStream out){
		boolean isOK=false;
		CipherInputStream cin=new CipherInputStream(in,cipherEncrypt);
		byte[]buffer=new byte[1024];
		int r;
		try{
			while((r=cin.read(buffer))>0){
				out.write(buffer, 0, r);
			}
			cin.close();
			in.close();
			out.close();
			isOK=true;
		}catch(Exception e){
			e.printStackTrace();
		}
		return isOK;
	}
	/**
	 * DES解密文件，解密结果输出到指定文件中
	 * @param in 输入文件流
	 * @param out 输出文件流
	 * @return 成功返回true，失败返回false;
	 */
	private static boolean decryptFileByDES(FileInputStream in,FileOutputStream out){
		boolean isOK=false;
		CipherInputStream cin=new CipherInputStream(in,cipherDecrypt);
		byte[]buffer=new byte[1024];
		int r;
		try{
			while((r=cin.read(buffer))>0){
				out.write(buffer, 0, r);
			}
			cin.close();
			in.close();
			out.close();
			isOK=true;
		}catch(Exception e){
			e.printStackTrace();
		}
		return isOK;
	}
	/*
     * 获取MessageDigest支持几种加密算法
     */
    @SuppressWarnings({ "rawtypes", "unchecked", "unused" })
    private static String[] getCryptolmpls(String serviceType){
        Set result = new HashSet();
        Provider[] providers = Security.getProviders();
        for(int i=0;i<providers.length;i++){
            Set keys = providers[i].keySet();
            for(Iterator it = keys.iterator();it.hasNext();){
                String key = it.next().toString();
                key = key.split(" ")[0];
                
                if(key.startsWith(serviceType+".")){
                    result.add(key.substring(serviceType.length()+1));
                }else if(key.startsWith("Alg.Alias."+serviceType+".")){
                    result.add(key.substring(serviceType.length()+11));
                }
            }
        }
        return (String[]) result.toArray(new String[result.size()]);
    }
}
