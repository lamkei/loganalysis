package realruisheng;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sheng on 18-4-11.
 */
public class ReadFiles {
    public static void main(String[] args) {

        String fileName = "files/access_log_sample_temp.txt";
        readFileByLines(fileName);
        /*
        String str = "/axis2/services/WebFilteringService/getCategoryByUrl?app=chrome_webfilter&ver=0.19.7.1&url=https%3A//play.google.com/store/apps/details%3Fid%3Dcom.google.android.youtube%26hl%3Den&cat=computer-information";
        Pattern pattern = Pattern.compile("&url=.*&cat=");
        Matcher matcher = pattern.matcher(str);
        matcher.find();               //要先find(),才能group()、start()、end()
        System.out.println(matcher.start());
        System.out.println(matcher.end());
        System.out.println(matcher.group());
*/

    }

    public static void readFileByLines(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                if(tempString.contains("www.amazon.com")){
                    Pattern pattern = Pattern.compile("&url=.*&cat=");
                    Matcher matcher = pattern.matcher(tempString);
                    matcher.find();
                    String str = matcher.group().replace("&url=","").replace("&cat=","");
                    String urlDecode= URLDecoder.decode(str);
                    System.out.println(urlDecode);
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
