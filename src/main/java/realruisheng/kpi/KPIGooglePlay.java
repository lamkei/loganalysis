package realruisheng.kpi;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import realruisheng.parselog.WebLog;
import realruisheng.parselog.WebLogParser;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 将Google Play的URL上各个参数以及对应的值提取出来，统计每种参数下各种值的数量，
 * 根据参数名字输出到不同的文件
 * Created by sheng on 18-4-16.
 */
public class KPIGooglePlay extends Configured implements Tool {

    static class KPIGooglePlayMapper extends Mapper<LongWritable, Text, Text, LongWritable> {

        LongWritable one = new LongWritable(1);
        private WebLogParser webLogParser;
        private WebLog webLog;
        private Text parameterValue = new Text();
        private Pattern pattern;
        private Pattern patternURL;
        private Pattern patternParameter;
        private Matcher matcher;
        private Matcher matcherURL;
        private Matcher matcherParameter;

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            webLogParser = new WebLogParser();
            webLog = new WebLog();
            patternURL = Pattern.compile("&url=(.*)&cat");        //只匹配Google Play的URL
            pattern = Pattern.compile("(\\?|&+)(.+?)=([^&]*)");
            patternParameter = Pattern.compile("^((referrer=utm_source)|(referrer=)|(utm_source)|(utm_medium)|(utm_term)|(utm_content)|(utm_campaign)|(gclid)|(hl)|(id))$");
        }

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            webLog = webLogParser.parse(line);
            if (webLog != null && webLog.isValid()) {
                String requestStr = webLog.getRequest();
                matcherURL = patternURL.matcher(requestStr);
                if(matcherURL.find()){
                    String url = matcherURL.group().replace("&url=","").replace("&cat","");;           //Google Play 或 Amazon 的URL
                    if(url.contains("play.google.com/store/apps/details")){
                        //提取URL中的参数以及对应的参数
                        String requestDecode = URLDecoder.decode(url);
                        matcher = pattern.matcher(requestDecode);
                        while (matcher.find()) {
                            String urlParameter = matcher.group(2);        //获得request上每个参数
                            String urlValue = matcher.group(3);             //该参数对应的值
                            if(urlParameter.equals("referrer")&&urlValue.contains("utm_source=")){
                                urlParameter=urlParameter+"=utm_source";
                                urlValue = urlValue.replace("utm_source=","");
                            }
                            matcherParameter = patternParameter.matcher(urlParameter);
                            if(matcherParameter.find()){
                                parameterValue.set(urlParameter+"\t"+urlValue);
                                context.write(parameterValue, one);
                            }

                        }
                    }
                }
            }
        }

        @Override
        protected void cleanup(Context context) throws IOException, InterruptedException {
            webLogParser = null;
            webLog = null;
            patternURL = null;
            pattern = null;
        }
    }
    static class KPIGooglePlayReducer
            extends Reducer<Text, LongWritable, Text, Text> {

        private MultipleOutputs<Text, Text> multipleOutputs;
        private Text multipleKey;
        private Text multipleValue;

        @Override
        protected void setup(Context context)
                throws IOException, InterruptedException {
            multipleOutputs = new MultipleOutputs<Text, Text>(context);
            multipleKey = new Text();
            multipleValue = new Text();
        }

        @Override
        protected void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
            long sum = 0;
            for (LongWritable value : values) {
                sum += value.get();
            }
            String []strs = key.toString().split("\\t");
            if(strs.length==2){
                String urlParameter = strs[0];
                String urlValue = strs[1];
                multipleKey.set(urlValue);
                multipleValue.set(String.valueOf(sum));
                multipleOutputs.write(multipleKey, multipleValue, urlParameter);
            }
        }

        @Override
        protected void cleanup(Context context)
                throws IOException, InterruptedException {
            multipleKey = null;
            multipleValue = null;
            multipleOutputs.close();
        }
    }
    public int run(String[] allArgs) throws Exception {
        Job job = Job.getInstance(getConf());
        job.setJarByClass(KPIGooglePlay.class);
        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(LongWritable.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        job.setMapperClass(KPIGooglePlayMapper.class);
        job.setReducerClass(KPIGooglePlayReducer.class);

        String[] args = new GenericOptionsParser(getConf(), allArgs)
                .getRemainingArgs();
        FileInputFormat.setInputPaths(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        /*
        Configuration conf = new Configuration();
        ToolRunner.run(new KPIGooglePlay(), args);
        */
        Configuration conf = new Configuration();
        if(args.length==2){
            //
            FileSystem hdfs=FileSystem. get(conf);
            String outputPath = args[1];
            Path findf= new Path(outputPath );
            if(hdfs.exists(findf)){
                hdfs.delete(findf);
            }
            ToolRunner.run(new KPIGooglePlay(), args);
        }
        else {
            System.out.println("input two parameters: input file path , output file path");
        }
    }
}