logDate=$(basename `pwd`)
logDate2="2018-03-04"
echo $logDate
mysql -uroot  -p1234 <<EOF
use learning;
INSERT  INTO test VALUES ( '$logDate' );
EOF
echo $logDate
