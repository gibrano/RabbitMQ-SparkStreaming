package main

import (
	"fmt"
	"os"

	"github.com/entropyx/protos"
	"github.com/entropyx/rabbitgo"
	"github.com/golang/protobuf/proto"
	"github.com/streadway/amqp"
)

func rabbitConn() (conn *rabbitgo.Connection) {
	c := &rabbitgo.Config{
		Host:     os.Getenv("AMQP_HOST"),
		Username: os.Getenv("AMQP_USER"),
		Password: os.Getenv("AMQP_PASS"),
		Vhost:    os.Getenv("AMQP_VHOST"),
		Port:     5672,
	}

	conn, err := rabbitgo.NewConnection(c)
	fmt.Println(c)
	if err != nil {
		fmt.Println(err)
	}
	return conn
}

func main() {
	conn := rabbitConn()
	producerConfig := &rabbitgo.ProducerConfig{
		Exchange:   "entropy",
		RoutingKey: "montserrat",
	}
	producer, err := conn.NewProducer(producerConfig)

	if err != nil {
		fmt.Println(err)
	}

	publishing := &amqp.Publishing{
		Body: []byte("Hello World"),
	}

	err = producer.PublishRPC(publishing, func(delivery *rabbitgo.Delivery) {
		fmt.Println(string(delivery.Body))
	})
	fmt.Println(err)
}
