package acme.example.work_order.api;

import acme.example.work_order.common.LocalDateTimeTypeAdapter;
import acme.example.work_order.workorder.WorkOrderDTO;
import acme.example.work_order.workorder.internal.WorkOrder;
import acme.example.work_order.workorder.internal.WorkOrderDAO;
import acme.example.work_order.workorderjob.WorkOrderJobDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Arrays;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Sql(scripts = "/sql/woTestData.sql")
public class WorkOrderControllerTest extends BaseApiTest{

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WorkOrderDAO woDAO;

    static Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
            .create();

    static String json1, json2, json3 = "";

    @BeforeAll
    static void setUp() {
        WorkOrderJobDTO woJobDTO1 = WorkOrderJobDTO.builder()
                .woNumber("testNumber")
                .jobCode("JobCode1")
                .quantity(2)
                .activeStatus('Y')
                .build();
        WorkOrderJobDTO woJobDTO2 = WorkOrderJobDTO.builder()
                .woNumber("testNumber")
                .jobCode("JobCode2")
                .quantity(1)
                .activeStatus('Y')
                .build();
        WorkOrderDTO woDTO1 = WorkOrderDTO.builder()
                .woNumber("testNumber")
                .jobTypeId(1L)
                .woJobDTOs(Arrays.asList(woJobDTO1, woJobDTO2))
                .woCreationDate(LocalDateTime.now().minusDays(3))
                .woCompletionDate(LocalDateTime.now().minusHours(4))
                .address("address1")
                .city("city1")
                .state("state1")
                .clientId("client1")
                .build();
        json1 = gson.toJson(woDTO1);
        WorkOrderDTO woDTO2 = WorkOrderDTO.builder()
                .jobTypeId(1L)
                .woJobDTOs(Arrays.asList(woJobDTO1, woJobDTO2))
                .woCreationDate(LocalDateTime.now().minusDays(3))
                .woCompletionDate(LocalDateTime.now().minusHours(4))
                .address("address1")
                .city("city1")
                .state("state1")
                .clientId("client1")
                .build();
        json2 = gson.toJson(woDTO2);
    }

    @Test
    @DisplayName("Test successful save of a work order")
    void createWorkOrder_success() throws Exception {
        // Arrange
        // Act
        mockMvc.perform(post("/workorders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json1))
                .andExpect(status().isCreated());
        WorkOrder wo = woDAO.findByWoNumber("testNumber");
        // Assert
        Assertions.assertNotNull(wo, "WO should not be null");
        Assertions.assertNotNull(wo.getId(), "WO id should not be null");
        Assertions.assertNotNull(wo.getCreationDate(), "WO creation Date should not be null");
        Assertions.assertEquals("client1", wo.getClientId(), "WO Client Id should match");
    }

    @Test
    @DisplayName("Test that WO cannot be saved without a number")
    void createJobTest_failure_noNumber() throws Exception {
        mockMvc.perform(post("/workorders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json2))
                .andExpect(status().isConflict());
    }

}
