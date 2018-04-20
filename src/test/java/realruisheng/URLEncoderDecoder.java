package realruisheng;

import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Created by sheng on 18-4-16.
 */
public class URLEncoderDecoder {
    public static void main(String[] args) {

    }

    public void encode(){
        String url="http://192.168.0.19:8888/cas/login";
        String urlEncodee= URLEncoder.encode(url);
        System.out.println(urlEncodee);
    }
    public void decode(){
        String url="https://play.google.com/store/apps/details?id=123&referrer=utm_source%3Dgoogle%26utm_medium%3Dcpc%26utm_term%3D456%26utm_content%3D789%26utm_campaign%3D0%26anid%3Dadmob";
        String urlDecode= URLDecoder.decode(url);
        System.out.println(urlDecode);
    }
}
