package realruisheng.kpi;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
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
import realruisheng.parselog.WebLog;
import realruisheng.parselog.WebLogParser;

import java.io.IOException;

/**
 * 页面PV量
 * Created by sheng on 18-1-23.
 */
public class KPIPV extends Configured implements Tool {
    static class KPIPVMapper extends Mapper<LongWritable, Text, Text, LongWritable> {

        LongWritable one = new LongWritable(1);
        private WebLogParser webLogParser;
        private WebLog webLog;
        private Text request = new Text();

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
                request.set(webLog.getRequest());
                context.write(request, one);
            }
        }

        @Override
        protected void cleanup(Context context) throws IOException, InterruptedException {
            webLogParser = null;
            webLog = null;
        }
    }

    /*
        static class KPIPVReducer extends Reducer<Text, LongWritable, Text, LongWritable> {

            @Override
            protected void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
                long sum = 0;
                for (LongWritable value : values) {
                    sum += value.get();
                }
                context.write(key, new LongWritable(sum));
            }
        }
    */
    public int run(String[] allArgs) throws Exception {
        Job job = Job.getInstance(getConf());
        job.setJarByClass(KPIPV.class);
        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(LongWritable.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);

        job.setMapperClass(KPIPVMapper.class);
        job.setReducerClass(SumReducer.class);

        String[] args = new GenericOptionsParser(getConf(), allArgs)
                .getRemainingArgs();
        FileInputFormat.setInputPaths(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        ToolRunner.run(new KPIPV(), args);
    }
}
