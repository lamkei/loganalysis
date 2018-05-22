package realruisheng.kpi;

import realruisheng.hive.KPICleaner;

/**
 * 运行项目工程的
 * Created by sheng on 18-1-28.
 */
public class KPIJob {

    public static final String HDFS = "hdfs://127.0.0.1:9000";

    public static void main(String[] args) throws Exception {
        //String outputAggregateURLParameters = "/output/loganalysis/AggregateURLParameters";
        String outputBrowser = "/output/loganalysis/Browser";
        String outputGeoIP = "/output/loganalysis/GeoIP";
        String outputGooglePlay = "/output/loganalysis/GooglePlay";
        String outputGooglePlayAmazonNum = "/output/loganalysis/GooglePlayAmazonNum";
        String outputHttpReferer = "/output/loganalysis/HttpReferer";
        String outputIP = "/output/loganalysis/IP";
        String outputOneIP = "/output/loganalysis/OneIP";
        String outputPageIP = "/output/loganalysis/PageIP";
        String outputPlatForm = "/output/loganalysis/PlatForm";
        String outputPV = "/output/loganalysis/PV";
        String outputStatus = "/output/loganalysis/Status";
        String outputTime = "/output/loganalysis/Time";
        String outputCleaner = "/output/loganalysis/Cleaner";
        String inputLog = "/input/";

        //AggregateURLParameters.main(new String[]{inputLog,outputAggregateURLParameters});
        KPIBrowser.main(new String[]{inputLog,outputBrowser});
        KPIGeoIP.main(new String[]{inputLog,outputGeoIP});
        KPIGooglePlay.main(new String[]{inputLog,outputGooglePlay});
        KPIGooglePlayAmazonNum.main(new String[]{inputLog,outputGooglePlayAmazonNum});
        //KPIHttpReferer.main(new String[]{inputLog,outputHttpReferer});
        KPIIP.main(new String[]{inputLog,outputIP});
        //KPIOneIP.main(new String[]{inputLog,outputOneIP});
        //KPIPageIP.main(new String[]{inputLog,outputPageIP});
        KPIPlatForm.main(new String[]{inputLog,outputPlatForm});
        //KPIPV.main(new String[]{inputLog,outputPV});
        KPIStatus.main(new String[]{inputLog,outputStatus});
        KPITime.main(new String[]{inputLog,outputTime});

        //KPICleaner.main(new String[]{inputLog,outputCleaner});
    }
}
