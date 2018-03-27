package realruisheng.kpi;

import realruisheng.hive.KPICleaner;

/**
 * 运行项目工程的
 * Created by sheng on 18-1-28.
 */
public class KPIJob {

    public static final String HDFS = "hdfs://127.0.0.1:9000";

    public static void main(String[] args) throws Exception {
        String outputBrowser = "/loganalysis/Browser";
        String outputCleaner = "/loganalysis/Cleaner";
        String outputHttpReferer = "/loganalysis/HttpReferer";
        String outputIP = "/loganalysis/IP";
        String outputOneIP = "/loganalysis/OneIP";
        String outputPageIP = "/loganalysis/PageIP";
        String outputPlatForm = "/loganalysis/PlatForm";
        String outputPV = "/loganalysis/PV";
        String outputStatus = "/loganalysis/Status";
        String outputTime = "/loganalysis/Time";
        String inputLog = "/loganalysis/inputLog/";

        KPIBrowser.main(new String[]{inputLog,outputBrowser});
        KPICleaner.main(new String[]{inputLog,outputCleaner});
        KPIHttpReferer.main(new String[]{inputLog,outputHttpReferer});
        KPIIP.main(new String[]{inputLog,outputIP});
        KPIOneIP.main(new String[]{inputLog,outputOneIP});
        KPIPageIP.main(new String[]{inputLog,outputPageIP});
        KPIPlatForm.main(new String[]{inputLog,outputPlatForm});
        KPIPV.main(new String[]{inputLog,outputPV});
        KPIStatus.main(new String[]{inputLog,outputStatus});
        KPITime.main(new String[]{inputLog,outputTime});


    }
}
