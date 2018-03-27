package realruisheng.hive;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import com.kumkee.userAgent.UserAgent;
import com.kumkee.userAgent.UserAgentParser;
import realruisheng.parselog.WebLog;
import realruisheng.parselog.WebLogParser;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 清洗Web日志数据
 * 清洗后的数据与Hive一起使用
 * Created by sheng on 18-2-04.
 */
public class KPICleaner extends Configured implements Tool {

    static class KPICleanerMapper extends Mapper<LongWritable, Text, LongWritable, Text> {

        LongWritable one = new LongWritable(1);
        private WebLogParser webLogParser;
        private WebLog webLog;
        private UserAgentParser userAgentParser;
        private UserAgent userAgent;
        private Text outputValue = new Text();

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            webLogParser = new WebLogParser();
            webLog = new WebLog();
            userAgentParser = new UserAgentParser();
            userAgent = new UserAgent();
        }

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            webLog = webLogParser.parse(line);
            if (webLog != null && webLog.isValid()) {
                String remoteAddr = webLog.getRemoteAddr();     //IP地址
                String remoteUser = webLog.getRemoteUser();     //用户
                Date timeLocal = webLog.getTimeLocal();        //时间
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH");
                String timeLocalString = simpleDateFormat.format(timeLocal);
                String request = webLog.getRequest();        //请求的页面
                int status = webLog.getStatus();            //状态码
                int bodyBytesSent = webLog.getBodyBytesSent();     //返回字节数
                String httpReferer = webLog.getHttpReferer();    //来源域名
                String httpUserAgent = webLog.getHttpUserAgent();  //客户端用户设备

                userAgent = userAgentParser.parse(httpUserAgent);
                String browser = userAgent.getBrowser();
                String platForm = userAgent.getPlatform();
                String isMobile = "true";
                if (userAgent.isMobile()) {
                    isMobile = "true";
                } else {
                    isMobile = "false";
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(remoteAddr);
                stringBuilder.append("\t");
                stringBuilder.append(timeLocalString);
                stringBuilder.append("\t");
                stringBuilder.append(request);
                stringBuilder.append("\t");
                stringBuilder.append(status);
                stringBuilder.append("\t");
                stringBuilder.append(bodyBytesSent);
                stringBuilder.append("\t");
                stringBuilder.append(httpReferer);
                stringBuilder.append("\t");
                stringBuilder.append(browser);
                stringBuilder.append("\t");
                stringBuilder.append(platForm);
                stringBuilder.append("\t");
                stringBuilder.append(isMobile);
                String outputValueStr = stringBuilder.toString();
                /*
                String outputValueStr = remoteAddr + "\t" + timeLocalString + "\t"
                        + request + "\t" + status + "\t" + bodyBytesSent + "\t" + httpReferer + "\t"
                        + browser + "\t" + platForm + "\t" + isMobile;
                */
                outputValue.set(outputValueStr);
                context.write(key, outputValue);
            }
        }

        @Override
        protected void cleanup(Context context) throws IOException, InterruptedException {
            webLogParser = null;
            webLog = null;
            userAgentParser = null;
            userAgent = null;
        }
    }

    static class KPICleanerReducer extends Reducer<LongWritable, Text, Text, NullWritable> {

        @Override
        protected void reduce(LongWritable key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            for (Text value : values) {
                context.write(value, NullWritable.get());
            }
        }
    }

    public int run(String[] allArgs) throws Exception {
        Job job = Job.getInstance(getConf());
        job.setJarByClass(KPICleaner.class);
        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        job.setMapOutputKeyClass(LongWritable.class);
        job.setMapOutputValueClass(Text.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);

        job.setMapperClass(KPICleanerMapper.class);
        job.setReducerClass(KPICleanerReducer.class);

        String[] args = new GenericOptionsParser(getConf(), allArgs)
                .getRemainingArgs();
        FileInputFormat.setInputPaths(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        ToolRunner.run(new KPICleaner(), args);
    }

}
