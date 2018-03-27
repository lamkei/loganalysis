package com.imooc.hadoop.project;

import com.kumkee.userAgent.UserAgent;
import com.kumkee.userAgent.UserAgentParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sheng on 18-1-21.
 */
public class UserAgentTest {

    public static void main(String[] args) throws Exception {
        UserAgentTest userAgentTest = new UserAgentTest();
        userAgentTest.testReadFile();
    }


    public void testUserAgentParser() {
        String source = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36";

        UserAgentParser userAgentParser = new UserAgentParser();
        UserAgent agent = userAgentParser.parse(source);

        String browser = agent.getBrowser();
        String engine = agent.getEngine();
        String engineVerson = agent.getEngineVersion();
        String os = agent.getOs();
        String platform = agent.getPlatform();
        boolean isMobile = agent.isMobile();

        System.out.println(browser + ", " + engine + ", " + engineVerson + ", " + os + ", "
                + platform + ", " + isMobile);

    }

    public void testReadFile() throws Exception {
        String path = "/home/sheng/IdeaProjects/hadoop/loganalysis/files/access.log.10";
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(new File(path)))
        );
        String line = "";
        int i = 0;

        Map<String, Integer> browserMap = new HashMap<String, Integer>();

        UserAgentParser userAgentParser = new UserAgentParser();

        HashMap<String,Integer> hashMap = new HashMap<>();

        while (line != null) {
            line = reader.readLine();
            i++;
            if (line != null && line.length() != 0) {
                String source = line.substring(getCharacterPosttion(line, "\"", 5) + 1);
                UserAgent agent = userAgentParser.parse(source);

                String browser = agent.getBrowser();
                String engine = agent.getEngine();
                String engineVerson = agent.getEngineVersion();
                String version = agent.getVersion();
                String os = agent.getOs();
                String platform = agent.getPlatform();
                boolean isMobile = agent.isMobile();

                Integer browserValue = browserMap.get(browser);
                if (browserValue != null) {
                    browserMap.put(browser, browserValue + 1);
                } else {
                    browserMap.put(browser, 1);
                }
                System.out.println(isMobile + ", " + browser + ", " + ", " + platform + ", "
                        + os + ", " + engine + ", " + version + ", " + engineVerson);
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            }
        }
        System.out.println("i: " + i);

    }

    public void testGetCharacterPosition() {
        String value = "127.0.0.1 - - [27/Jul/2017:22:16:22 +0800] \"GET / HTTP/1.1\" 200 612 \"-\" \"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36";
        int index = getCharacterPosttion(value, "\"", 1);
        System.out.println(index);
    }

    /**
     * 获取指定字符串中指定标识的字符串出现的索引位置
     *
     * @param value
     * @param operator
     * @param index
     * @return
     */
    private int getCharacterPosttion(String value, String operator, int index) {
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

}
