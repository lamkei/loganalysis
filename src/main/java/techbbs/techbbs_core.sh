#!/bin/sh

#step1.create external table in hive
hive -e "CREATE EXTERNAL TABLE techbbs(ip string, atime string, url string) PARTITIONED BY (logdate string) ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t' LOCATION '/project/techbbs/cleaned';"

#step2.compute the days between start date and end date
s1=`date --date="$1"  +%s`
s2=`date +%s`
s3=$((($s2-$s1)/3600/24))

#step3.excute techbbs_core.sh $3 times
for ((i=$s3; i>0; i--))
do
  logdate=`date --date="$i days ago" +%Y_%m_%d`
  techbbs_core.sh $logdate
done


#step4.alter hive table and then add partition
hive -e "ALTER TABLE techbbs ADD PARTITION(logdate='${yesterday}') LOCATION '/project/techbbs/cleaned/${yesterday}';"
#step5.create hive table everyday
hive -e "CREATE TABLE hmbbs_pv_${yesterday} AS SELECT COUNT(1) AS PV FROM hmbbs WHERE logdate='${yesterday}';"
hive -e "CREATE TABLE hmbbs_reguser_${yesterday} AS SELECT COUNT(1) AS REGUSER FROM hmbbs WHERE logdate='${yesterday}' AND INSTR(url,'member.php?mod=register')>0;"
hive -e "CREATE TABLE hmbbs_ip_${yesterday} AS SELECT COUNT(DISTINCT ip) AS IP FROM hmbbs WHERE logdate='${yesterday}';"
hive -e "CREATE TABLE hmbbs_jumper_${yesterday} AS SELECT COUNT(1) AS jumper FROM (SELECT COUNT(ip) AS times FROM hmbbs WHERE logdate='${yesterday}' GROUP BY ip HAVING times=1) e;"
hive -e "CREATE TABLE hmbbs_${yesterday} AS SELECT '${yesterday}', a.pv, b.reguser, c.ip, d.jumper FROM hmbbs_pv_${yesterday} a JOIN hmbbs_reguser_${yesterday} b ON 1=1 JOIN hmbbs_ip_${yesterday} c ON 1=1 JOIN hmbbs_jumper_${yesterday} d ON 1=1;"
#step6.delete hive tables
hive -e "drop table hmbbs_pv_${yesterday};"
hive -e "drop table hmbbs_reguser_${yesterday};"
hive -e "drop table hmbbs_ip_${yesterday};"
hive -e "drop table hmbbs_jumper_${yesterday};"
#step7.export to mysql
sqoop export --connect jdbc:mysql://hadoop-master:3306/techbbs --username root --password admin --table techbbs_logs_stat --fields-terminated-by '\001' --export-dir '/hive/hmbbs_${yesterday}'
#step8.delete hive table
hive -e "drop table techbbs_${yesterday};"