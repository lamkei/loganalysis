package realruisheng;


import realruisheng.parselog.WebLog;
import realruisheng.parselog.WebLogParser;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sheng on 18-1-22.
 */
public class Main {
    public static void main(String[] args) {
        WebLogParser webLogParser = new WebLogParser();
        WebLog webLog = new WebLog();
        Pattern patternURL = Pattern.compile("&url=(.*)&cat");        //只匹配Google Play的URL
        Pattern pattern = Pattern.compile("(\\?|&+)(.+?)=([^&]*)");
        Pattern patternParameter = Pattern.compile("^((referrer=utm_source)|(referrer=)|(utm_source)|(utm_medium)|(utm_term)|(utm_content)|(utm_campaign)|(gclid)|(hl)|(id))$");
        Matcher matcher;
        Matcher matcherURL;
        Matcher matcherParameter;
        String line = "198.91.166.34 - - [25/Oct/2017:23:24:41 +0000] \\\"GET /axis2/services/WebFilteringService/getCategoryByUrl?app=chrome_webfilter&ver=0.19.7.1&url=https%3A//play.google.com/store/apps/details%3Fid%3Dcom.google.android.youtube%26hl%3Den&cat=computer-information HTTP/1.1\\\" 200 133 \\\"-\\\" \\\"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36\"";
        webLog = webLogParser.parse(line);
        System.out.println("webLog.isValid() = " + webLog.isValid());
        if (webLog != null && webLog.isValid()) {
            String requestStr = webLog.getRequest();
            System.out.println("requestStr = " + requestStr);
            matcherURL = patternURL.matcher(requestStr);
            if(matcherURL.find()){
                String url = matcherURL.group().replace("&url=","").replace("&cat","");           //Google Play 或 Amazon 的URL
            //    String url = "https://play.google.com/store/apps/details?id=net.stevemiller.android.referrertest&referrer=utm_source%3D1%26utm_medium%3D2%26utm_term%3D3%26utm_content%3D4%26utm_campaign%3D5";
                System.out.println("url = " + url);
                if(url.contains("play.google.com/store/apps/details")){
                    //提取URL中的参数以及对应的参数
            //        String requestDecode = URLDecoder.decode(url);
                    String requestDecode = "https://play.google.com/store/apps/details?id=com.magmamobile.game.Animals&referrer=utm_source=ShareWithFriends&utm_medium2=ShareWithFriends&1utm_campaign=ShareWithFriends";
                    System.out.println("requestDecode = " + requestDecode);
                    matcher = pattern.matcher(requestDecode);
                    while (matcher.find()) {
                      //  System.out.println("matcher.find() = " + true);
                        String urlParameter = matcher.group(2);        //获得request上每个参数
                        String urlValue = matcher.group(3);             //该参数对应的值
                        if(urlParameter.equals("referrer")&&urlValue.contains("utm_source=")){
                            urlParameter=urlParameter+"=utm_source";
                            urlValue = urlValue.replace("utm_source=","");
                        }
                        matcherParameter = patternParameter.matcher(urlParameter);
                        if(matcherParameter.find()){
                  //          System.out.println("matcherParameter = " + true);
                            System.out.println(urlParameter+"\t"+urlValue);
                        }

                    }
                }
            }
        }


    }
}
