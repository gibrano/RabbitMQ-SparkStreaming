import org.apache.spark.SparkConf
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.log4j.{Level, Logger}
import org.apache.spark.internal.Logging
import org.apache.spark.streaming.receiver.Receiver
import com.rabbitmq.client.{ Connection, Channel, ConnectionFactory, QueueingConsumer }
import org.apache.spark.storage.StorageLevel
import scala.reflect.ClassTag

class RMQProcessor[T: ClassTag] extends Receiver[String](StorageLevel.MEMORY_AND_DISK_SER_2) {
    def fromBytes(x: Array[Byte]) = new String(x, "UTF-8")
    override def onStart() {
      val queueName = "montserrat"
      val exchangeName = "entropy"
      val routingKey = "montserrat"
      val factory = new ConnectionFactory()
      factory.setUsername(sys.env("AMQP_USER"))
      factory.setPassword(sys.env("AMQP_PASS"))
      factory.setVirtualHost(sys.env("AMQP_VHOST"))
      factory.setHost(sys.env("AMQP_HOST"))
      factory.setPort(5672)
      val connection = factory.newConnection()
      val channel = connection.createChannel()
      channel.queueDeclare("monserrat", true, false, false, null)
      val consumer = new QueueingConsumer(channel)
      channel.basicConsume(queueName, false, consumer)
      new Thread("RabbitMQ Processor") {
        override def run() {
          while (!isStopped) {
            var delivery = consumer.nextDelivery()
            var message = fromBytes(delivery.getBody())
            println(message)
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false)
            var response = "Hello World! is a code. Hi Hello World!".getBytes
            channel.basicPublish("", delivery.getProperties.getReplyTo, delivery.getProperties,response)
          }
        }
      }.start()
    }
    override def onStop() {}
}

object SparkRabbit {
   def main(args: Array[String]) {
      val rootLogger = Logger.getRootLogger()
      rootLogger.setLevel(Level.ERROR)

      val SparkConf = new SparkConf().setAppName("SparkRabbit")
      val ssc = new StreamingContext(SparkConf, Seconds(1))

      val numStreams = 5
      val rabbitStreams = (1 to numStreams).map { i => ssc.receiverStream[String](new RMQProcessor()) }
      val unifiedStream = ssc.union(rabbitStreams)
      unifiedStream.reduceByWindow(_ + "\r\n" + _, Seconds(1), Seconds(1)).print()

      ssc.start()
      ssc.awaitTermination()
   }
}
