package realruisheng.kpi;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Country;
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
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import realruisheng.parselog.WebLog;
import realruisheng.parselog.WebLogParser;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;

/**
 * 利用GeoIP，将IP转化为 国家+"\t"+省份
 * Created by sheng on 18-4-11.
 */
public class KPIGeoIP extends Configured implements Tool {

    static class KPIGeoIPMapper extends Mapper<LongWritable, Text, Text, LongWritable> {

        LongWritable one = new LongWritable(1);
        private WebLogParser webLogParser;
        private WebLog webLog;
        private Text location = new Text();
        private File database;
        private DatabaseReader reader ;

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {

            String fileName = "GeoLite2-City.mmdb";
            database = new File(fileName);
            reader = new DatabaseReader.Builder(database).build();
            webLogParser = new WebLogParser();
            webLog = new WebLog();
        }

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            webLog = webLogParser.parse(line);
            if (webLog != null && webLog.isValid()) {
                String remoteAddr = webLog.getRemoteAddr();
                InetAddress ipAddress = InetAddress.getByName(remoteAddr);
                CityResponse response = null;
                try {
                    response = reader.city(ipAddress);
                    Country country = response.getCountry();
                    City city = response.getCity();
                    location.set(country.getName()+"\t"+city.getName());
                    context.write(location, one);
                } catch (GeoIp2Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected void cleanup(Context context) throws IOException, InterruptedException {
            webLogParser = null;
            webLog = null;
            database = null;
            reader = null;
        }
    }


    static class KPIGeoIPReducer extends Reducer<Text, LongWritable, Text, LongWritable> {

        @Override
        protected void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
            long sum = 0;
            for (LongWritable value : values) {
                sum += value.get();
            }
            context.write(key, new LongWritable(sum));
        }
    }

    public int run(String[] allArgs) throws Exception {
        Job job = Job.getInstance(getConf());
        job.setJarByClass(KPIGeoIP.class);
        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(LongWritable.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);

        job.setMapperClass(KPIGeoIPMapper.class);
        job.setReducerClass(KPIGeoIPReducer.class);

        //TODO 为了在docker上运行，修改一下
       // job.addCacheFile(new URI("hdfs://localhost:9000/user/sheng/GeoLite2-City.mmdb#GeoLite2-City.mmdb"));
        job.addCacheFile(new URI("hdfs://master:9000/geoIP/GeoLite2-City.mmdb#GeoLite2-City.mmdb"));
        String[] args = new GenericOptionsParser(getConf(), allArgs)
                .getRemainingArgs();
        FileInputFormat.setInputPaths(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        /*
        Configuration conf = new Configuration();
        ToolRunner.run(new KPIGeoIP(), args);
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
            ToolRunner.run(new KPIGeoIP(), args);
        }
        else {
            System.out.println("input two parameters: input file path , output file path");
        }
    }
}
