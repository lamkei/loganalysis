package realruisheng;

import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sheng on 18-4-16.
 */
public class URLAnalysisTest {
    public static void main(String[] args) {
        String aid = "/axis2/services/WebFilteringService/getCategoryByUrl?app=chrome_webfilter&ver=0.19.7.1&url=https%3A//play.google.com/store/apps/details%3Fid%3Dcom.google.android.youtube%26hl%3Den&cat=computer-information";
        String aid2 = URLDecoder.decode(aid);
        Pattern p = Pattern.compile("(\\?|&+)(.+?)=([^&]*)");
        Matcher m = p.matcher(aid2);
        while (m.find()) {
            System.out.println(m.group(2) + "=" + m.group(3));
        }
    }
}
