package realruisheng.kpi.googleplay;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class GoogleplayNameMapper
        extends Mapper<LongWritable, Text, TextPair, Text> {

    @Override
    protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {
        String valueStr = value.toString();
        String []strs = valueStr.split(",");
        if(strs.length==2){
            String id = strs[0].replaceAll("\"","");
            String name = strs[1].replaceAll("\"","");
            context.write(new TextPair(id,"0"),new Text(name));
        }
    }
}
