package acme.example.work_order.kafkaconsumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumer {

    @KafkaListener(topics = "new-wo", groupId = "wo-group-2")
    public void listen(String message) {
        System.out.println("Received message: " + message);
    }

}