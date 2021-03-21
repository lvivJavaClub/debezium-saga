 kafka-console-consumer.sh --topic fulfillment.public.cafe_order --bootstrap-server localhost:9092 --from-beginning


 docker exec -it order_service_kafka_1  kafka-console-consumer.sh --topic fulfillment.public.cafe_order --bootstrap-server localhost:9092 --from-beginning

docker exec -it jug_kafka_1  kafka-console-consumer.sh --topic orderdb.public.cafe_order --bootstrap-server localhost:9092 --from-beginning

docker exec -it jug_kafka_1  kafka-console-consumer.sh --topic paymentdb.public.payment_order --bootstrap-server localhost:9092 --from-beginning
docker exec -it jug_kafka_1  kafka-console-consumer.sh --topic paymentdb.public.payment_order --bootstrap-server localhost:9092 --from-beginning
docker exec -it jug_kafka_1  kafka-console-consumer.sh --topic outbox.event.order --bootstrap-server localhost:9092 --from-beginning
docker exec -it jug_kafka_1  kafka-console-consumer.sh --topic outbox.event.payment --bootstrap-server localhost:9092 --from-beginning

# topics-list
docker exec -it jug_kafka_1  kafka-topics.sh --list --bootstrap-server localhost:9092


docker volume rm $(docker volume ls -q)
