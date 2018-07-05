package com.maoding.core.util;

import java.util.Random;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：SecurityCodeUtil
 * 类描述：验证码生成
 * 作    者：Chenxj
 * 日    期：2015年7月14日-上午9:59:45
 */
public class SecurityCodeUtil {
	//private final static String[]codes={"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
	private final static String[]codes={"0","1","2","3","4","5","6","7","8","9"};
	/**
	 * 方法描述：创建验证码
	 * 作        者：Chenxj
	 * 日        期：2015年7月14日-上午10:11:10
	 * @param length 验证码长度
	 * @return 验证码
	 */
	public static String createSecurityCode(int length){
		length=length<4?4:length;
		Random random=new Random();
		StringBuilder code=new StringBuilder();
		for(int i=0;i<length;i++){
			code.append(codes[random.nextInt(10)]);
		}
		return code.toString();
	}
	public static String createSecurityCode(){
		return createSecurityCode(4);
	}
	public static void main(String[] args) {
		System.out.println(SecurityCodeUtil.createSecurityCode(666));
	}
}
