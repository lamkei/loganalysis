package realruisheng.parselog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sheng on 18-1-22.
 */
public class WebLogParser {

//    Set<String> pages = new HashSet<String>();

    public WebLogParser() {
        /*
        pages.add("/about/");
        pages.add("/axis2/services/WebFilteringService/getCategoryByUrl");
        */
    }

    public WebLog parse(String webLogString) {
        WebLog webLog = new WebLog();
        if(isValid(webLogString)){
            webLog.setRemoteAddr(parseRemoteAddr(webLogString));
            webLog.setRemoteUser(parseRemoteUser(webLogString));
            webLog.setTimeLocal(parseTimeLocal(webLogString));
            webLog.setRequest(parseRequest(webLogString));
            webLog.setStatus(parseStatus(webLogString));
            webLog.setBodyBytesSent(parseBodyBytesSent(webLogString));
            webLog.setHttpReferer(parseHttpReferer(webLogString));
            webLog.setHttpUserAgent(parseHttpUserAgent(webLogString));
            webLog.setValid(true);
        }else {
            webLog.setValid(false);
        }
        return webLog;
    }

    /**
     * 每行日志中有6个双引号。
     * 如果没有6个则返回false，表示该行日志无效
     * @param webLogString
     * @return
     */
    public boolean isValid(String webLogString) {
        int doubleQuoteNum = webLogString.length() - webLogString.replace("\"", "").length();
        if (doubleQuoteNum != 6) {
            return false;
        }
        /* 判断是否该网站的页面
        String requestPage = parseRequest(webLogString);
        if (pages.contains(requestPage)) {
            return true;
        }
        */
        return true;

    }

    public String parseRemoteAddr(String webLogString) {
        String remoteAddr = webLogString.split(" +")[0].trim();
        return remoteAddr;
    }

    /**
     * 在“-”后面的是remoteUser，“-”是历史原因导致还存在
     * @param webLogString
     * @return
     */
    public String parseRemoteUser(String webLogString) {
        String remoteUser = webLogString.split(" +")[2].trim();
        return remoteUser;
    }

    //TODO 了解SimpleDateFormat使用
    public Date parseTimeLocal(String webLogString) {
        /*
        int firstIndex = webLogString.indexOf("[");
        int lastIndex = webLogString.indexOf(" +0800]");
        String timeLocalStr = webLogString.substring(firstIndex + 1, lastIndex).trim();
        */
        String timeLocalStr = webLogString.split(" +")[3].trim().replace("[","");
        //System.out.println("timeLocalStr = " + timeLocalStr);
        SimpleDateFormat dateFormat = new SimpleDateFormat("d/MMM/yyyy:HH:mm:ss", Locale.ENGLISH);
        Date date = null;
        try {
            date = dateFormat.parse(timeLocalStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;

    }


    public String parseRequest(String webLogString) {
        int firstIndex = getCharacterPosttion(webLogString, "\"", 1);
        int lastIndex = getCharacterPosttion(webLogString, "\"", 2);
        String request = webLogString.substring(firstIndex + 1, lastIndex).trim();
        if(request.split(" +").length==3){
            return request.split(" +")[1];
        }else {
            return request;   // 当request为“-”时，即直接访问
        }
    }

    public int parseStatus(String webLogString) {
        int firstIndex = getCharacterPosttion(webLogString, "\"", 2);
        int lastIndex = getCharacterPosttion(webLogString, "\"", 3);
        String intercept = webLogString.substring(firstIndex + 1, lastIndex - 1).trim();
        String parseStatusStr = intercept.split(" +")[0];
        int status = Integer.parseInt(parseStatusStr);
        return status;
    }

    public int parseBodyBytesSent(String webLogString) {
        int firstIndex = getCharacterPosttion(webLogString, "\"", 2);
        int lastIndex = getCharacterPosttion(webLogString, "\"", 3);
        String intercept = webLogString.substring(firstIndex + 1, lastIndex - 1).trim();
        String parseStatusStr = intercept.split(" +")[1];
        int bodyBytesSent = Integer.parseInt(parseStatusStr);
        return bodyBytesSent;
    }

    public String parseHttpReferer(String webLogString) {
        int firstIndex = getCharacterPosttion(webLogString, "\"", 3);
        int lastIndex = getCharacterPosttion(webLogString, "\"", 4);
        String http_referer = webLogString.substring(firstIndex + 1, lastIndex).trim();
        return http_referer;
    }

    public String parseHttpUserAgent(String webLogString) {
        int firstIndex = getCharacterPosttion(webLogString, "\"", 5);
        int lastIndex = getCharacterPosttion(webLogString, "\"", 6);
        String httpUserAgent = webLogString.substring(firstIndex + 1, lastIndex).trim();
        return httpUserAgent;
    }


    public static int getCharacterPosttion(String value, String operator, int index) {
        Matcher slashMatcher = Pattern.compile(operator).matcher(value);
        int mIdx = 0;
        while (slashMatcher.find()) {
            mIdx++;
            if (mIdx == index) {
                break;
            }
        }
        return slashMatcher.start();
    }


    public static void main(String[] args) {
        WebLogParser webLogParser = new WebLogParser();
        String webLogString = "198.91.166.34 - - [25/Oct/2017:23:24:41 +0000] \"GET /axis2/services/WebFilteringService/getCategoryByUrl?app=chrome_webfilter&ver=0.19.7.1&url=https%3A//play.google.com/store/apps/details%3Fid%3Dcom.google.android.youtube%26hl%3Den&cat=computer-information HTTP/1.1\" 200 133 \"-\" \"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36\"";
        String remoteAddr = webLogParser.parseRemoteAddr(webLogString);     //IP地址
        String remoteUser = webLogParser.parseRemoteUser(webLogString);     //用户
        Date timeLocal = webLogParser.parseTimeLocal(webLogString);        //时间
        String request = webLogParser.parseRequest(webLogString);        //请求的页面
        int status = webLogParser.parseStatus(webLogString);            //状态码
        int bodyBytesSent = webLogParser.parseBodyBytesSent(webLogString);     //返回字节数
        String httpReferer = webLogParser.parseHttpReferer(webLogString);    //来源域名
        String httpUserAgent = webLogParser.parseHttpUserAgent(webLogString);  //客户端用户设备
        boolean valid = webLogParser.isValid(webLogString);  //该条日志记录是否有效

        System.out.println("remoteAddr = " + remoteAddr);
        System.out.println("remoteUser = " + remoteUser);
        System.out.println("request = " + request);
        System.out.println("status = " + status);
        System.out.println("bodyBytesSent = " + bodyBytesSent);
        System.out.println("httpReferer = " + httpReferer);
        System.out.println("httpUserAgent = " + httpUserAgent);
        System.out.println("valid = " + valid);

    }
}
