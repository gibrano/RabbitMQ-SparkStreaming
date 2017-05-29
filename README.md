# RabbitMQ-SparkStreaming
Hello world message receiver to consume from RabbitMQ using Spark Streaming

Run

`sbt package`

Then 

`spark-submit --master local[2] --class "SparkRabbit" --jars ./jars/rabbitmq-client.jar target/scala-2.11/spark-rabbit-project_2.11-1.0.jar`
