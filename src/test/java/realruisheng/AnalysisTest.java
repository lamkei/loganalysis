package realruisheng;

import com.kumkee.userAgent.UserAgent;
import com.kumkee.userAgent.UserAgentParser;
import org.apache.hadoop.yarn.webapp.hamlet.Hamlet;
import realruisheng.parselog.WebLog;
import realruisheng.parselog.WebLogParser;

import java.io.*;
import java.util.*;

/**
 * 采用Java测试程序对1000条数据进行测试
 * 再与Hadoop集群上运行的结果进行对比
 * 用来判断Hadoop集群上的程序是否正确
 */
public class AnalysisTest {
    public static void main(String[] args) {
        String fileName = "files/access_log_sample_1000";
        browserTest();
    }

    public static void browserTest() {
        String fileName1 =  "files/access_log_sample_1000";
        File file1 = new File(fileName1);
        WebLogParser webLogParser = new WebLogParser();
        WebLog webLog = new WebLog();
        UserAgentParser userAgentParser = new UserAgentParser();
        UserAgent userAgent = new UserAgent();
        HashMap<String,Integer> browserMap = new HashMap<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file1));
            String line = null;
            // 一次读入一行，直到读入null为文件结束
            while ((line = reader.readLine()) != null) {
                webLog = webLogParser.parse(line);
                if (webLog != null && webLog.isValid()) {
                    String httpUserAgent = webLog.getHttpUserAgent();
                    userAgent = userAgentParser.parse(httpUserAgent);
                    String browserStr = userAgent.getBrowser();
                    if(browserMap.containsKey(browserStr)){
                        int temp = browserMap.get(browserStr);
                        browserMap.put(browserStr,temp+1);
                    }else {
                        browserMap.put(browserStr,1);
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        System.out.println("Browser:");
        Iterator iter = browserMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            Object val = entry.getValue();
            System.out.println(key+" : "+val);
        }
    }
}
