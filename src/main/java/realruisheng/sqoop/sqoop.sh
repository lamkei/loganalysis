

sqoop export --connect jdbc:mysql://localhost:3306/logAnalysisVisual --username root --password 1234 --table browserMR --export-dir hdfs://localhost:9000/output/output1/part-r-00000 --input-fields-terminated-by '\t'

sqoop export --connect jdbc:mysql://localhost:3306/logAnalysisVisual --username root --password 1234 --table httpRefererMR --export-dir hdfs://localhost:9000/output/output2/part-r-00000 --input-fields-terminated-by '\t'

sqoop export --connect jdbc:mysql://localhost:3306/logAnalysisVisual --username root --password 1234 --table oneIPMR --export-dir hdfs://localhost:9000/output/output4/part-r-00000 --input-fields-terminated-by '\t'

sqoop export --connect jdbc:mysql://localhost:3306/logAnalysisVisual --username root --password 1234 --table pageIPMR --export-dir hdfs://localhost:9000/output/output5/part-r-00000 --input-fields-terminated-by '\t'


sqoop export --connect jdbc:mysql://localhost:3306/logAnalysisVisual --username root --password 1234 --table platFormMR --export-dir hdfs://localhost:9000/output/output6/part-r-00000 --input-fields-terminated-by '\t'

sqoop export --connect jdbc:mysql://localhost:3306/logAnalysisVisual --username root --password 1234 --table pvMR --export-dir hdfs://localhost:9000/output/output7/part-r-00000 --input-fields-terminated-by '\t'

sqoop export --connect jdbc:mysql://localhost:3306/logAnalysisVisual --username root --password 1234 --table statusMR --export-dir hdfs://localhost:9000/output/output8/part-r-00000 --input-fields-terminated-by '\t'

sqoop export --connect jdbc:mysql://localhost:3306/logAnalysisVisual --username root --password 1234 --table timeMR --export-dir hdfs://localhost:9000/output/output9/part-r-00000 --input-fields-terminated-by '\t'

