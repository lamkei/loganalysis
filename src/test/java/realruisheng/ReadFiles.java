package realruisheng;

import java.io.*;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sheng on 18-4-11.
 */
public class ReadFiles {
    public static void main(String[] args) {

        String fileName = "files/part-r-00000";
        readFileByLines();
    }

    public static void readFileByLines() {
        String fileName1 =  "files/part-r-00000.txt";
        String fileName2 =  "files/part-r-00001.txt";
        File file1 = new File(fileName1);
        File file2 = new File(fileName2);
        BufferedReader reader = null;
        BufferedWriter writer = null;
        try {
            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file1));
            writer = new BufferedWriter(new FileWriter(file2));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                String strs[] = tempString.split("\\t");
                writer.write(strs[0]+":00:00"+"\t"+strs[1]);
                writer.newLine();
            }
            reader.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }
}
