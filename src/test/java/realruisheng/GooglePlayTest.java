package realruisheng;

import realruisheng.parselog.WebLog;
import realruisheng.parselog.WebLogParser;

import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sheng on 18-4-19.
 */
public class GooglePlayTest {
    public static void main(String[] args) {
        WebLogParser webLogParser = new WebLogParser();
        WebLog webLog = new WebLog();
        Pattern pattern = Pattern.compile("(\\?|&+)(.+?)=([^&]*)");
        Pattern patternURL = Pattern.compile("&url=(.*)&cat");        //只匹配Google Play的URL
        Matcher matcher;
        Matcher matcherURL;
        String value = "198.91.166.34 - - [25/Oct/2017:23:24:41 +0000] \"GET /axis2/services/WebFilteringService/getCategoryByUrl?app=chrome_webfilter&ver=0.19.7.1&url=https%3A//play.google.com/store/apps/details%3Fid%3Dcom.google.android.youtube%26hl%3Den&cat=computer-information HTTP/1.1\" 200 133 \"-\" \"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36\"";
        String line = value.toString();
        webLog = webLogParser.parse(line);
        String str ;
        if (webLog != null && webLog.isValid()) {
            String requestStr = webLog.getRequest();
            matcherURL = patternURL.matcher(requestStr);
            if(matcherURL.find()){
                String url = matcherURL.group();           //Google Play 或 Amazon 的URL
                //TODO 在这里添加统计Amazon与Google的数量
                //提取URL中的参数以及对应的参数
                String requestDecode = URLDecoder.decode(url);
                matcher = pattern.matcher(requestDecode);
                while (matcher.find()) {
                    String urlParameter = matcher.group(2);        //获得request上每个参数
                    String urlValue = matcher.group(3);             //该参数对应的值
                    if(urlParameter.equals("url")){
                        String id = urlValue.replace("https://play.google.com/store/apps/details?id=","");
                        str = "id"+"\t"+id;
                    }else {
                        str = urlParameter+"\t"+urlValue;
                    }
                    String[] a = str.split("\\t");
                    System.out.println("a.length = " + a.length);
                    System.out.println(a[0] +"  "+a[1]);

                }

            }

        }
    }
}
