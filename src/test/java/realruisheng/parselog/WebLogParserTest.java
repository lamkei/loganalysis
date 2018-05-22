package realruisheng.parselog;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * WebLogParser Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>一月 22, 2018</pre>
 */
public class WebLogParserTest {

    private WebLogParser webLogParser;
    private String webLogString = "127.0.0.1 - - [30/May/2013:17:38:20 +0800] \"GET / HTTP/1.1\" 200 612 \"-\" \"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36\"";

    @Before
    public void before() throws Exception {
        webLogParser = new WebLogParser();
    }

    @After
    public void after() throws Exception {

    }

    /**
     * Method: parse(String webLogString)
     */
    @Test
    public void testParse() throws Exception {
        WebLog webLog = new WebLog();
        webLog = webLogParser.parse(webLogString);
        System.out.println(webLog);
    }

    @Test
    public void testIsValid() throws Exception {
        boolean valid = webLogParser.isValid(webLogString);
        //System.out.println("valid = " + valid);
        assertEquals(true,valid);
    }

    /**
     * Method: parseRemoteAddr(String webLogString)
     */
    @Test
    public void testParseRemoteAddr() throws Exception {
        String remoteAddr = webLogParser.parseRemoteAddr(webLogString);
        //System.out.println("remoteAddr = " + remoteAddr);
        assertEquals("127.0.0.1", remoteAddr);
    }


    /**
     * Method: parseRemoteUser(String webLogString)
     */
    @Test
    public void testParseRemoteUser() throws Exception {
        String remoteUser = webLogParser.parseRemoteUser(webLogString);
        //System.out.println("remoteUser = " + remoteUser);
        assertEquals("-", remoteUser);
    }

    /**
     * Method: parseTimeLocal(String webLogString)
     */
    @Test
    public void testParseTimeLocal() throws Exception {
        Date timeLocal = webLogParser.parseTimeLocal(webLogString);
        //System.out.println("timeLocal = " + timeLocal);
    }

    /**
     * Method: parseRequest(String webLogString)
     */
    @Test
    public void testParseRequest() throws Exception {
        String request = webLogParser.parseRequest(webLogString);
        //System.out.println("request = " + request);
        assertEquals("/", request);
    }

    /**
     * Method: parseStatus(String webLogString)
     */
    @Test
    public void testParseStatus() throws Exception {
        int status = webLogParser.parseStatus(webLogString);
        //System.out.println("status = " + status);
        assertEquals(200, status);

    }

    /**
     * Method: parseBodyBytesSent(String webLogString)
     */
    @Test
    public void testParseBodyBytesSent() throws Exception {
        int bodyBytesSent = webLogParser.parseBodyBytesSent(webLogString);
        //System.out.println("bodyBytesSent = " + bodyBytesSent);
        assertEquals(612, bodyBytesSent);
    }

    /**
     * Method: parseHttpReferer(String webLogString)
     */
    @Test
    public void testParseHttpReferer() throws Exception {
        String httpReferer = webLogParser.parseHttpReferer(webLogString);
        //System.out.println("httpReferer = " + httpReferer);
        assertEquals("-", httpReferer);
    }

    /**
     * Method: parseHttpUserAgent(String webLogString)
     */
    @Test
    public void testParseHttpUserAgent() throws Exception {
        String httpUserAgent = webLogParser.parseHttpUserAgent(webLogString);
        //System.out.println("httpUserAgent = " + httpUserAgent);
        String expectValue = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36";
        assertEquals(expectValue, httpUserAgent);
    }

    /**
     * Method: getCharacterPosttion(String value, String operator, int index)
     */
    @Test
    public void testGetCharacterPosttion() throws Exception {

    }

    @Test
    public void testReadFile() throws Exception{
        String path = "/home/sheng/IdeaProjects/hadoop/loganalysis/files/access100.log";
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(new File(path)))
        );
        String line = "";
        int i = 0;

        Map<String, Integer> browserMap = new HashMap<String,Integer>();

        WebLogParser webLogParser = new WebLogParser();
        while (line!=null){
            line = reader.readLine();
            System.out.println("line = " + line);
            i++;
            if(StringUtils.isNotBlank(line)){
                webLogString = line;
                testParse();
            }
        }
        System.out.println("i: "+i);

    }

} 
