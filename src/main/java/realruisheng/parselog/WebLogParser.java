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

    Set<String> pages = new HashSet<String>();

    public WebLogParser() {
        pages.add("/about/");
        pages.add("/black-ip-clustor/");
        pages.add("/cassandra-clustor/");
        pages.add("/finance-rhive-repurchase/");
        pages.add("/hadoop-familiy-roadmap/");
        pages.add("/hadoop-hive-intro/");
        pages.add("/hadoop-zookeeper-intro/");
        pages.add("/hadoop-mahout-roadmap/");
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

    public boolean isValid(String webLogString) {
        // TODO 还需要修改
        int doubleQuoteNum = webLogString.length() - webLogString.replace("\"", "").length();
        if (doubleQuoteNum != 6) {
            return false;
        }
        String requestPage = parseRequest(webLogString);
        if (pages.contains(requestPage)) {
            return true;
        } else {
            return true;
        }
    }

    public String parseRemoteAddr(String webLogString) {
        String remoteAddr = webLogString.split(" +")[0].trim();
        return remoteAddr;
    }

    public String parseRemoteUser(String webLogString) {
        String remoteUser = webLogString.split(" +")[1].trim();
        return remoteUser;
    }

    //TODO 了解SimpleDateFormat使用
    //TODO "+0800]"需要修改
    public Date parseTimeLocal(String webLogString) {
        int firstIndex = webLogString.indexOf("[");
        int lastIndex = webLogString.indexOf(" +0800]");
        String timeLocalStr = webLogString.substring(firstIndex + 1, lastIndex).trim();
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
        String webLogString = "127.0.0.1 - - [30/May/2013:17:38:20 +0800] \"GET / HTTP/1.1\" 200 612 \"-\" \"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36\"";
        boolean valid1 = webLogParser.isValid(webLogString);
        boolean valid2 = webLogParser.isValid("fdsfsafas");
        //System.out.println("valid1 = " + valid1);
        //System.out.println("valid2 = " + valid2);
    }
}
