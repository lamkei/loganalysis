package realruisheng;

import com.kumkee.userAgent.UserAgent;
import com.kumkee.userAgent.UserAgentParser;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.yarn.webapp.hamlet.Hamlet;
import realruisheng.kpi.googleplay.TextPair;
import realruisheng.parselog.WebLog;
import realruisheng.parselog.WebLogParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GoogleplayScrapy {
    public static void main(String[] args) {
        scrapyTest();
    }

    public static void scrapyTest() {
        String fileName1 =  "files/googleplay-id-r-00000";
        File file1 = new File(fileName1);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file1));
            String line = null;
            // 一次读入一行，直到读入null为文件结束
            while ((line = reader.readLine()) != null) {
                String valueStr = line.toString();
                String []strs = valueStr.split("\\t");
                if(strs.length==2){
                    System.out.println(strs[0]+" : "+strs[1]);
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
    }
}
