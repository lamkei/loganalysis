#!/usr/bin/env bash
cd /usr/local/hadoop-2.9.0
start=$(date +%s)
./bin/hadoop jar myProgram/loganalysis-1.0-SNAPSHOT-jar-with-dependencies.jar realruisheng.kpi.AggregateURLParameters  /input /output/AggregateURLParameters
end=$(date +%s)
time=$(( $end - $start ))
echo $time