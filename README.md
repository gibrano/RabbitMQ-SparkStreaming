# RabbitMQ-SparkStreaming
Hello world message receiver to consume from RabbitMQ using Spark Streaming

You need install spark-2.1.0, golang 1.8.3, scala 2.11, sbt 1.0 and RabbitMQ 3.6

Run

`git clone https://github.com/gibrano/RabbitMQ-SparkStreaming.git`

`cd RabbitMQ-SparkStreaming`

`sbt package`

Then 

`spark-submit --master local[2] --class "SparkRabbit" --jars ./jars/rabbitmq-client.jar target/scala-2.11/spark-rabbit-project_2.11-1.0.jar`

Send message with the script

`go run main.go`
