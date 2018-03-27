package realruisheng.parselog;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by sheng on 18-1-22.
 */
public class WebLog {
    private String remoteAddr;     //IP地址
    private String remoteUser;     //用户
    private Date timeLocal;        //时间
    private String request;        //请求的页面
    private int status;            //状态码
    private int bodyBytesSent;     //返回字节数
    private String httpReferer;    //来源域名
    private String httpUserAgent;  //客户端用户设备
    private boolean valid = true;  //该条日志记录是否有效
    public WebLog(){
    }

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    public String getRemoteUser() {
        return remoteUser;
    }

    public void setRemoteUser(String remoteUser) {
        this.remoteUser = remoteUser;
    }

    public Date getTimeLocal() {
        return timeLocal;
    }

    public void setTimeLocal(Date timeLocal) {
        this.timeLocal = timeLocal;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getBodyBytesSent() {
        return bodyBytesSent;
    }

    public void setBodyBytesSent(int bodyBytesSent) {
        this.bodyBytesSent = bodyBytesSent;
    }

    public String getHttpReferer() {
        return httpReferer;
    }

    public void setHttpReferer(String httpReferer) {
        this.httpReferer = httpReferer;
    }

    public String getHttpUserAgent() {
        return httpUserAgent;
    }

    public void setHttpUserAgent(String httpUserAgent) {
        this.httpUserAgent = httpUserAgent;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    @Override
    public String toString() {
        return "WebLog{" +
                "remoteAddr='" + remoteAddr + '\'' +
                ", remoteUser='" + remoteUser + '\'' +
                ", timeLocal=" + timeLocal +
                ", request='" + request + '\'' +
                ", status=" + status +
                ", bodyBytesSent=" + bodyBytesSent +
                ", httpReferer='" + httpReferer + '\'' +
                ", httpUserAgent='" + httpUserAgent + '\'' +
                ", valid=" + valid +
                '}';
    }
}
