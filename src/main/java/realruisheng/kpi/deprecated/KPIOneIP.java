package realruisheng.kpi.deprecated;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import realruisheng.parselog.WebLog;
import realruisheng.parselog.WebLogParser;

import java.io.IOException;

/**
 * 记录每个IP的访问量
 * Created by sheng on 18-1-23.
 */
public class KPIOneIP extends Configured implements Tool {
   /*
    enum IP{
        IPJumper,     //跳出数，表示只出现一次的IP的数量
        IPNum          //IP数量，表示来访IP的数量（去重）
    }
  */
    static class KPIOneIPMapper extends Mapper<LongWritable, Text, Text, LongWritable> {

        LongWritable one = new LongWritable(1);
        private WebLogParser webLogParser;
        private WebLog webLog;
        private Text remoteAddr = new Text();

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            webLogParser = new WebLogParser();
            webLog = new WebLog();
        }

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            webLog = webLogParser.parse(line);
            if (webLog != null && webLog.isValid()) {
                remoteAddr.set(webLog.getRemoteAddr());
                context.write(remoteAddr, one);
            }
        }

        @Override
        protected void cleanup(Context context) throws IOException, InterruptedException {
            webLogParser = null;
            webLog = null;
        }
    }


    static class KPIOneIPReducer extends Reducer<Text, LongWritable, Text, LongWritable> {

        @Override
        protected void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
            long sum = 0;
            for (LongWritable value : values) {
                sum += value.get();
            }
            /*
            if(sum==1){                //该IP只访问一次
                context.getCounter(IP.IPJumper).increment(1);
            }
            context.getCounter(IP.IPNum).increment(1);   //独立IP数
            */
            context.write(key, new LongWritable(sum));
        }
        /*
        @Override
        protected void cleanup(Context context) throws IOException, InterruptedException {
            long oneTimeIP = context.getCounter(IP.IPJumper).getValue();
            long sumIP = context.getCounter(IP.IPNum).getValue();
            context.write(new Text("IPJumper"),new LongWritable(oneTimeIP));
            context.write(new Text("IPNum"),new LongWritable(sumIP));
        }
        */
    }

    public int run(String[] allArgs) throws Exception {
        Job job = Job.getInstance(getConf());
        job.setJarByClass(KPIOneIP.class);
        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(LongWritable.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);

        job.setMapperClass(KPIOneIPMapper.class);
        job.setReducerClass(KPIOneIPReducer.class);

        String[] args = new GenericOptionsParser(getConf(), allArgs)
                .getRemainingArgs();
        FileInputFormat.setInputPaths(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        /*
        //输出计数器
        if(job.isComplete()){
            Counters counters = job.getCounters();
            long oneTimeIP = counters.findCounter(IP.IPJumper).getValue();
            long sumIP = counters.findCounter(IP.IPNum).getValue();
            System.out.println("oneTimeIP = " + oneTimeIP);
            System.out.println("sumIP = " + sumIP);
        }
        */
        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        /*
        Configuration conf = new Configuration();
        ToolRunner.run(new KPIOneIP(), args);
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
            ToolRunner.run(new KPIOneIP(), args);
        }
        else {
            System.out.println("input two parameters: input file path , output file path");
        }
    }
}

















