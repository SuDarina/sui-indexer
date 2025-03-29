use rdkafka::producer::{FutureProducer, FutureRecord};
use rdkafka::ClientConfig;
use std::time::Duration;

#[tokio::main]
async fn main() {
    // Создаем продюсера в main
    let producer: FutureProducer = ClientConfig::new()
        .set("bootstrap.servers", "localhost:9092") // Укажите адрес вашего Kafka брокера
        .set("message.timeout.ms", "15000")
        .create()
        .expect("Producer creation error");

    // Топик и сообщение, которое мы хотим отправить
    let topic = "my_topic";
    let message = "Hello, Kafka from main!";

    // Создаем запись для отправки
    let record = FutureRecord::to(topic)
        .payload(message)
        .key("some_key");

    // Отправляем сообщение в Kafka
    match producer.send(record, Duration::from_secs(0)).await {
        Ok(_) => {
            println!(
                "Message sent!",
            );
        }
        Err((e, _)) => {
            eprintln!("Error sending message: {:?}", e);
        }
    }
}