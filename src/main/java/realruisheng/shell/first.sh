cd /opt/software/hadoop-2.9.0
start=$(date +%s)  
./bin/hadoop jar myProgram/loganalysis-1.0-SNAPSHOT-jar-with-dependencies.jar realruisheng.kpi.KPIBrowser  /input /output/KPIBrowserSSH
end=$(date +%s)  
time=$(( $end - $start ))  
echo $time