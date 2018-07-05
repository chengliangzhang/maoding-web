package com.maoding.core.util;

/**
 * 生成5位顺序号单例模式类
 * @author wangrubin
 */
public class GetSxCount {
   private GetSxCount(){}
   
   private static  GetSxCount single = null;
   
   private static Integer Count = 10000;
   
   synchronized public  static GetSxCount getInstance() 
   {
	   if(single == null)
	   {
		   single = new GetSxCount();
	   }
	   return single;
   }
   
	
   synchronized public String getCount()
   {
	   if(Count == 99999)
	   {
		   Count = 10000;
	   }
	   Count ++;

	   return Count.toString();
   }
}
