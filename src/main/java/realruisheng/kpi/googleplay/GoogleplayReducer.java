package realruisheng.kpi.googleplay;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class GoogleplayReducer
        extends Reducer<TextPair, Text, Text, Text> {
    @Override
    protected void reduce(TextPair key, Iterable<Text> values, Context context)
            throws IOException, InterruptedException {
        Iterator<Text> iter = values.iterator();
        Text name = new Text(iter.next());
        /*
        while (iter.hasNext()) {
            Text num = iter.next();
            Text outValue = new Text(name.toString()+"\t"+num.toString());
            context.write(key.getFirst(),outValue);
        }
        */
        if(iter.hasNext()){
            while (iter.hasNext()) {
                Text num = iter.next();
                Text outValue = new Text(name.toString()+"\t"+num.toString());
                context.write(name,num);
            }
        }else if(key.getSecond().toString().equals("1")){
            context.write(key.getFirst(),new Text(name.toString()+"\t"+"1"));
        }else {
            context.write(key.getFirst(),new Text(name.toString()+"\t"+"0"));
        }
        /*
        context.write(key.getFirst(),name);
        while (iter.hasNext()) {
            Text num = iter.next();
            context.write(key.getFirst(),num);
        }
        */
    }
}
