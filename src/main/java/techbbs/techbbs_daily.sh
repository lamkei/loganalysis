#!/bin/sh
#   改写crontab定时任务配置：crontab -e
#   * 1 * * * /usr/local/files/apache_logs/techbbs_daily.sh

yesterday=`date --date='1 days ago' +%Y_%m_%d`
hmbbs_core.sh $yesterday