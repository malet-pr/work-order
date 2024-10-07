package acme.example.work_order.kafkaconsumer;

import acme.example.work_order.api.JobController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumer {

    private static final Logger log = LoggerFactory.getLogger(JobController.class);

    @KafkaListener(topics = "new-wo", groupId = "wo-group-2")
    public void listen(String message) {
        log.info("Received message: {}" , message);
    }

}