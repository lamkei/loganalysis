package realruisheng.kpi.googleplay;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.*;

public class JoinGoogleplay extends Configured implements Tool {

    public static class KeyPartitioner extends Partitioner<TextPair, Text> {
        @Override
        public int getPartition(/*[*/TextPair key/*]*/, Text value, int numPartitions) {
            return (/*[*/key.getFirst().hashCode()/*]*/ & Integer.MAX_VALUE) % numPartitions;
        }
    }

    @Override
    public int run(String[] args) throws Exception {
        if (args.length != 3) {
            System.out.println("需要输入3个参数！");
            return -1;
        }

        Job job = Job.getInstance(getConf());
        //Job job = new Job(getConf(), "Join weather records with station names");
        job.setJarByClass(getClass());

        Path nameInputPath = new Path(args[0]);
        Path numInputPath = new Path(args[1]);
        Path outputPath = new Path(args[2]);

        MultipleInputs.addInputPath(job, nameInputPath,
                TextInputFormat.class, GoogleplayNameMapper.class);
        MultipleInputs.addInputPath(job, numInputPath,
                TextInputFormat.class, GoogleplayNumMapper.class);
        FileOutputFormat.setOutputPath(job, outputPath);

        job.setGroupingComparatorClass(TextPair.FirstComparator.class);

        job.setMapOutputKeyClass(TextPair.class);
        job.setReducerClass(GoogleplayReducer.class);
        job.setOutputKeyClass(Text.class);

        return job.waitForCompletion(true) ? 0 : 1;

    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        if (args.length == 3) {
            FileSystem hdfs = FileSystem.get(conf);
            String outputPath = args[2];
            Path findf = new Path(outputPath);
            if (hdfs.exists(findf)) {
                hdfs.delete(findf);
            }
            ToolRunner.run(new JoinGoogleplay(), args);
        } else {
            System.out.println("input two parameters: input file path , output file path");
        }
    }
}
