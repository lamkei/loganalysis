# 在做前后端时
sqoop export --connect jdbc:mysql://localhost:3306/logAnalysisVisual --username root --password 1234 --table geoIPMR --export-dir hdfs://localhost:9000/loganalysis/GeoIP/part-r-00000 --input-fields-terminated-by '\t'

sqoop export --connect jdbc:mysql://localhost:3306/logAnalysisVisual --username root --password 1234 --table geoIPResult --columns country,city,num --export-dir hdfs://localhost:9000/loganalysis/GeoIP/part-r-00000 --input-fields-terminated-by '\t'


# TODO
sqoop export --connect jdbc:mysql://localhost:3306/logAnalysisVisual --username root --password 1234 --table timeMR  --export-dir hdfs://localhost:9000/loganalysis/Time/part-r-00000 --input-fields-terminated-by '\t'

sqoop export --connect jdbc:mysql://localhost:3306/logAnalysisVisual --username root --password 1234 --table googleHlResult --columns name,num --export-dir hdfs://localhost:9000/loganalysis/GooglePlay/hl-r-00000 --input-fields-terminated-by '\t'

sqoop export --connect jdbc:mysql://localhost:3306/logAnalysisVisual --username root --password 1234 --table googleIdResult --columns name,num --export-dir hdfs://localhost:9000/loganalysis/GooglePlay/id-r-00000 --input-fields-terminated-by '\t'

sqoop export --connect jdbc:mysql://localhost:3306/logAnalysisVisual --username root --password 1234 --table googleReferrerSourceResult --columns name,num --export-dir hdfs://localhost:9000/loganalysis/GooglePlay/referrer=utm_source-r-00000 --input-fields-terminated-by '\t'

sqoop export --connect jdbc:mysql://localhost:3306/logAnalysisVisual --username root --password 1234 --table googleCampaignResult --columns name,num --export-dir hdfs://localhost:9000/loganalysis/GooglePlay/utm_campaign-r-00000 --input-fields-terminated-by '\t'

sqoop export --connect jdbc:mysql://localhost:3306/logAnalysisVisual --username root --password 1234 --table googleContentResult --columns name,num --export-dir hdfs://localhost:9000/loganalysis/GooglePlay/utm_content-r-00000 --input-fields-terminated-by '\t'

sqoop export --connect jdbc:mysql://localhost:3306/logAnalysisVisual --username root --password 1234 --table googleMediumResult --columns name,num --export-dir hdfs://localhost:9000/loganalysis/GooglePlay/utm_medium-r-00000 --input-fields-terminated-by '\t'

sqoop export --connect jdbc:mysql://localhost:3306/logAnalysisVisual --username root --password 1234 --table googleSourceResult --columns name,num --export-dir hdfs://localhost:9000/loganalysis/GooglePlay/utm_source-r-00000 --input-fields-terminated-by '\t'

sqoop export --connect jdbc:mysql://localhost:3306/logAnalysisVisual --username root --password 1234 --table googleTermResult --columns name,num --export-dir hdfs://localhost:9000/loganalysis/GooglePlay/utm_term-r-00000 --input-fields-terminated-by '\t'

sqoop export --connect jdbc:mysql://localhost:3306/logAnalysisVisual --username root --password 1234 --table bounceRateMR  --export-dir hdfs://localhost:9000/loganalysis/IP/part-r-00000 --input-fields-terminated-by '\t'

sqoop export --connect jdbc:mysql://localhost:3306/logAnalysisVisual --username root --password 1234 --table browserMR  --export-dir hdfs://localhost:9000/loganalysis/Browser/part-r-00000 --input-fields-terminated-by '\t'

sqoop export --connect jdbc:mysql://localhost:3306/logAnalysisVisual --username root --password 1234 --table platFormMR  --export-dir hdfs://localhost:9000/loganalysis/PlatForm/part-r-00000 --input-fields-terminated-by '\t'

sqoop export --connect jdbc:mysql://localhost:3306/logAnalysisVisual --username root --password 1234 --table statusMR  --export-dir hdfs://localhost:9000/loganalysis/Status/part-r-00000 --input-fields-terminated-by '\t'