import com.maoding.core.util.StringUtil;

/**
 * Created by Idccapp22 on 2016/7/20.
 */
public class test {

    public static void  main(String args[]){

        String strings = "我;;你; ";
        String[] strings1 = strings.split(";");
       String userName="neid?nlfdfdf?";
       userName = StringUtil.format(userName,strings1);

       System.out.println(userName);
        System.out.println(strings1);
    }

    public  String hello(){
        String res = "3";
        int i =1;
        while (true){
            if(i==5){
               /* res = "2";
                break;*/
                return res;
            }
            i++;
            System.out.println(i);
        }

    }
}
