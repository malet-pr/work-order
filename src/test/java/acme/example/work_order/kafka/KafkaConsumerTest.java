package acme.example.work_order.kafka;

import acme.example.work_order.common.LocalDateTimeTypeAdapter;
import acme.example.work_order.kafkaconsumer.MessageConsumer;
import acme.example.work_order.workorder.WorkOrderDTO;
import acme.example.work_order.workorder.WorkOrderService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.containers.KafkaContainer;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.times;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class KafkaConsumerTest extends BaseIntegrationKafka{

    @Autowired
    public KafkaTemplate<String, String> template;

    @Autowired
    private MessageConsumer msgConsumer;

    @MockBean
    private WorkOrderService serviceMock;

    private static final String TOPIC = "wo-new";
    private static WorkOrderDTO dto1,dto2,dto3;
    private Gson gson = new GsonBuilder()
                        .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                        .create();

    @BeforeEach
    void setUp() {
        dto1 = WorkOrderDTO.builder().woNumber("test1").build();
        dto2 = WorkOrderDTO.builder().woNumber("test2").build();
    }

    @Test
    public void messageConsumerTest_success() throws Exception {
        // Arrange
        String message = gson.toJson(List.of(dto1,dto2));
        template.send(TOPIC, message);
        // Act
        msgConsumer.listen(message);
        Type listType = new TypeToken<List<WorkOrderDTO>>(){}.getType();
        List<WorkOrderDTO> dtos = gson.fromJson(message, listType);
        // Assert
        Assertions.assertNotNull(dtos);
        Assertions.assertEquals(2, dtos.size());
        Assertions.assertEquals(List.of(dto1,dto2),dtos);
        Mockito.verify(serviceMock,times(1)).save(dto1);
        Mockito.verify(serviceMock,times(1)).save(dto2);
    }

}
