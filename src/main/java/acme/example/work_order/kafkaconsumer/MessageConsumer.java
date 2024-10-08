package acme.example.work_order.kafkaconsumer;

import acme.example.work_order.api.JobController;
import acme.example.work_order.common.LocalDateTimeTypeAdapter;
import acme.example.work_order.workorder.WorkOrderDTO;
import acme.example.work_order.workorder.WorkOrderService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Transactional
public class MessageConsumer {

    private static final Logger log = LoggerFactory.getLogger(JobController.class);

    @Autowired
    private WorkOrderService woService;

    Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
            .create();

    @KafkaListener(topics = "new-wo", groupId = "wo-group-2")
    public void listen(String message) {
        log.info("Received message: {}" , message);
        AtomicInteger savedCount = new AtomicInteger();
        AtomicInteger errorCount = new AtomicInteger();
        Type listType = new TypeToken<List<WorkOrderDTO>>() {}.getType();
        List<WorkOrderDTO> dtos = gson.fromJson(message, listType);
        dtos.forEach(dto -> {
            boolean saved = woService.save(dto);
            if (saved) {
                savedCount.getAndIncrement();
            } else {
                errorCount.getAndIncrement();
            }
        });
        log.info("Work Orders:  Saved {} - Not saved {} ", savedCount, errorCount);
    }

}