name := "Spark Rabbit Project"

version := "1.0"

scalaVersion := "2.11.8"

val sparkVersion = "2.1.0"

libraryDependencies += "org.apache.spark" %% "spark-core" % sparkVersion
libraryDependencies += "org.apache.spark" %% "spark-sql" % sparkVersion
libraryDependencies += "org.apache.spark" %% "spark-streaming" % sparkVersion
libraryDependencies += "com.datastax.spark" %% "spark-cassandra-connector" %  "2.0.1" withSources() withJavadoc()
//libraryDependencies += "com.stratio.receiver" % "spark-rabbitmq" % "0.5.1"
libraryDependencies += "com.rabbitmq" % "amqp-client" % "4.1.0"
libraryDependencies += "com.typesafe" % "config" % "1.3.1"

resolvers += "Akka Repository" at "http://repo.akka.io/releases/"
