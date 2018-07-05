package com.maoding.core.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 类名：StringUtil
 * 描述：字符串工具类
 * 作者： Chenxj
 * 日期：2015年6月18日 - 下午4:40:44
 */
public class StringUtil {
	public static Boolean isEmpty(String s){
		return ((s == null) || s.trim().isEmpty());
	}

	/**
	 * 判断两个字符串是否相同，视null和""为相同字符串
	 */
	public static Boolean isSame(String a,String b){
		return (isEmpty(a) && isEmpty(b)) ||
				(!isEmpty(a) && !isEmpty(b) && (a.equals(b)));
	}

	/**
	 * 方法描述：字符串格式化
	 * 作者：Chenxj
	 * 日期：2015年6月18日 - 下午4:50:07
	 * @param f
	 * @return StringBuilder
	 */
	public static StringBuilder format(StringBuilder f,Object...objs){
		StringBuilder sb=new StringBuilder();
		int count=0;
		int flag=0;
		char a="?".charAt(0);
		String b="";
		for(int i=0;i<f.length();i++){
			if(a==f.charAt(i)){
				String s= "";
				if(count<objs.length) {//2017-04-28添加此段代码，为了防止模板与参数不对应的情况，引起错误
					s = objs[count].toString();
				}
				f.replace(i,i+1,b);
				sb.append(f.substring(flag, i)).append(s);
				flag=i;
				count++;
			}
		}
		sb.append(f.substring(flag, f.length()));
		return sb;
	}
	/**
	 * 方法描述：字符串格式化
	 * 作者：Chenxj
	 * 日期：2015年6月18日 - 下午4:58:56
	 * @param string
	 * @param objs
	 * @return String
	 */
	public static String format(String string,Object...objs){
		if (isEmpty(string)) return "";
		return format(new StringBuilder(string), objs).toString();
	}
	/**
	 * 方法描述：字符串连接
	 * 作者：Chenxj
	 * 日期：2015年6月18日 - 下午5:20:32
	 * @param s
	 * @return StringBuilder
	 */
	@SafeVarargs
	public static <T>StringBuilder join(String s,T...tt){
		StringBuilder sb=new StringBuilder();
		int l=tt.length;
		if(l>0){
			Object obj0=tt[0];
			if(obj0 instanceof Object[]){
				sb.append("[").append(join(s,(Object[])obj0)).append("]");
			}else{
				sb.append(tt[0]);
			}
			for(int i=1;i<l;i++){
				Object obji=tt[i];
				if(obji instanceof Object[]){
					sb.append(s).append("[").append(join(s, (Object[])obji)).append("]");
				}else{
					sb.append(s).append(tt[i]);
				}
			}
			return sb;
		}else{
			return sb;
		}
	}
	/**
	 * 方法描述：连接字符串数组
	 * 作者：Chenxj
	 * 日期：2015年6月18日 - 下午5:22:01
	 * @param s
	 * @return String
	 */
	@SafeVarargs
	public static <T>String joinString(String s,T...tt){
		return join(s, tt).toString();
	}
	
	/**
	 * 获取32位字符串  生成数据库ID
	 * 
	 * @return
	 */
	public static final String buildUUID() {
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		return uuid;
	}
	/**
	 * 方法描述：判断字符串是否为邮箱
	 * 作        者：Chenxj
	 * 日        期：2015年7月16日-下午1:03:50
	 * @param string
	 * @return
	 */
	public static boolean isEmail(String string){
		boolean is=false;
		Pattern pattern = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
		Matcher mat=pattern.matcher(string);
		if(mat.find()){
			is=true;
		}
		return is;
	}
	/**
	 * 方法描述：判断字符串是否为数字
	 * 作        者：Chenxj
	 * 日        期：2015年7月16日-下午1:03:55
	 * @param string
	 * @return
	 */
	public static boolean isNumeric(String string){
		int l=string.length();
		for(int i=0;i<l;i++){
			if(!Character.isDigit(string.charAt(i))){
				return false;
			}
		}
		return true;
	}
	/**
	 * 方法描述：数据格式化
	 * 作        者：MaoSF
	 * 日        期：2015年7月29日-上午10:39:27
	 * @param number
	 * @param pattern
	 * @return
	 */
	public static String toNumberStr(double number, String pattern)
	{
        DecimalFormat df = null ;               // 声明一个DecimalFormat对象         
        df = new DecimalFormat(pattern) ;       // 实例化对象          
        String str = df.format(number) ;  
        return str;
	}
	/**
	 * 方法描述：格式化，并保留两位小数
	 * 作        者：MaoSF
	 * 日        期：2015年7月29日-上午10:39:46
	 * @param number
	 * @return
	 */
	public static String toNumberStr(double number)
	{
		return toNumberStr(number, "###,###,###,##0.00");
	}
	
	/**
	 * 方法描述：格式化，无小数位
	 * 作        者：MaoSF
	 * 日        期：2015年7月29日-上午10:39:46
	 * @param number
	 * @return
	 */
	public static String toNumberStr2(double number)
	{
		return toNumberStr(number, "###,###,###,##0");
	}
	/**
	 * 方法描述：保留两位小数
	 * 作        者：MaoSF
	 * 日        期：2015年7月29日-上午10:41:12
	 * @param number
	 * @return
	 */
	public static String toNumberStr3(double number)
	{
		return toNumberStr(number, "###########0.00");
	}
	/**
	 * 方法描述：无小数位
	 * 作        者：MaoSF
	 * 日        期：2015年7月29日-上午10:41:43
	 * @param number
	 * @return
	 */
	public static String toNumberStr4(double number)
	{
		return toNumberStr(number, "###########0");
	}
	
	/**
	 * 方法描述：格式化，并保留三位小数
	 * 作        者：MaoSF
	 * 日        期：2015年7月29日-上午10:42:12
	 * @param number
	 * @return
	 */
	public static String toNumberStr5(double number)
	{
		return toNumberStr(number, "###,###,###,##0.000");
	}
	/**
	 * 方法描述：首字母大写
	 * 作        者：Chenxj
	 * 日        期：2015年11月20日-上午10:43:04
	 * @param str
	 * @return
	 */
	public static String upperCaseFirstLatter(String str){
		char[] strChar=str.toCharArray();
		strChar[0]-=32;
		return String.valueOf(strChar);
	}
	
	/**  
     * 使用java正则表达式去掉多余的.与0  
     * @return
     */    
    public static String  subZeroAndDot(Object s1){  
    	String s=s1.toString();
        if(s.indexOf(".") > 0){    
            s = s.replaceAll("0+?$", "");//去掉多余的0    
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉    
        }    
        return s;    
    } 	
    
    /**
	 * 方法描述：对数据四舍五入，并且保留decimal（如：2，3，4）位小数，并且对数据后面的0去掉
	 * 作        者：MaoSF
	 * 日        期：2015年11月16日-上午10:44:45
	 * @param o 处理的对象
	 * @param decimal 保留的小数位（后面是0的去掉）
	 * @return
	 */
	public static String formateNumber(Object o,int decimal){
		BigDecimal d= new BigDecimal(o.toString()).setScale(decimal, BigDecimal.ROUND_HALF_UP); 
		return subZeroAndDot(d);
	}
	
	/**
	 * 方法描述：正则匹配
	 * 作        者：Chenxj
	 * 日        期：2016年5月24日-上午9:52:37
	 * @param regex
	 * @param str
	 * @return
	 */
    public static boolean match(String regex, String str) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);

        return matcher.lookingAt();
    }
    
    /**
     * 方法描述：对象值进行类型判断并返回
     * 作    者：wangrb
     * 日    期：2016年6月7日-上午10:33:47
     * @param param
     * @return
     */
	public static String converToString(Object param){
		if(param==null){
			return "";
		}else if (param instanceof Integer) {
			int value = ((Integer) param).intValue();
			return String.valueOf(value);
		} else if (param instanceof String) {
			String s = (String) param;
			return s;
		} else if (param instanceof Double) {
		   	double d = ((Double) param).doubleValue();
		   	return String.valueOf(d);
		} else if (param instanceof Float) {
			float f = ((Float) param).floatValue();
			return String.valueOf(f);
		} else if (param instanceof Long) {
			long l = ((Long) param).longValue();
			return String.valueOf(l);
		}
		return "";
	}
	
	/**
	 * 方法描述：非空判断
	 * 作        者：MaoSF
	 * 日        期：2016年7月7日-下午5:33:52
	 * @param o
	 * @return
	 */
	public static boolean isNullOrEmpty(Object o){
		if(o==null){
			return true;
		}else{
			try{
				if("".equals(o.toString()) ||  "".equals((o.toString()).trim())){
					return true;
				}
			}catch(Exception e){
				return false;
			}
		}
		return false;
	}

	/**
	 * 方法描述
	 * @param s
	 * @return
     */
	public static String subZeroAndDot(String s){
		if(s.indexOf(".") > 0){
			s = s.replaceAll("0+?$", "");//去掉多余的0
			s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
		}
		return s;
	}

	public static String getRealData(BigDecimal num) {
		if (num == null) {
			return "0";
		}
		String value = num.stripTrailingZeros().toPlainString();
		return value;
	}

	public static void main(String args[]){
		System.out.println("id:"+StringUtil.buildUUID());
	}
}
