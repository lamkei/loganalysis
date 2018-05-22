package realruisheng.kpi.googleplay;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class GoogleplayNumMapper
        extends Mapper<LongWritable, Text, TextPair, Text> {

    @Override
    protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {
        String valueStr = value.toString();
        String []strs = valueStr.split("\\t");
        if(strs.length==2){
            context.write(new TextPair(strs[0],"1"),new Text(strs[1]));
        }
    }
}
