package acme.example.work_order.api;

import acme.example.work_order.workorder.internal.WorkOrder;
import acme.example.work_order.workorderjob.WorkOrderJobDTO;
import acme.example.work_order.workorderjob.internal.WorkOrderJob;
import acme.example.work_order.workorderjob.internal.WorkOrderJobDAO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.function.BooleanSupplier;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Sql(scripts = "/sql/woTestData.sql")
public class WorkOrderJobControllerTest extends BaseApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WorkOrderJobDAO woJobDAO;

    Gson gson = new Gson();

    @Test
    @DisplayName("Test that an entity will be saved when the WO exists")
    void createWorkOrderJobTest_success() throws Exception {
        // Arrange
        WorkOrderJobDTO woJobDTO = WorkOrderJobDTO.builder()
                .woNumber("ZZZ999")
                .jobCode("jobCode1")
                .quantity(4)
                .activeStatus('Y')
                .appliedRule("A1")
                .build();
        String json = gson.toJson(woJobDTO);
        // Act
        MvcResult result = mockMvc.perform(post("/workorderjobs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andReturn();
        boolean saved = Boolean.parseBoolean(result.getResponse().getContentAsString());
        List<WorkOrderJob> woJobs = woJobDAO.findByWorkOrderNumber("ZZZ999");
        List<WorkOrderJob> target = woJobs.stream().filter(w -> w.getWorkOrder().getWoNumber().equals("ZZZ999")).toList();
        // Assert
        Assertions.assertTrue(saved, "The entity should have been saved successfully.");
        Assertions.assertFalse(woJobs.isEmpty(), "There should be at least one object in the list");
        Assertions.assertFalse(target.isEmpty(), "There should be at least one object in the list with the required WO number");
        Assertions.assertEquals(1,target.size(), "There should be exactly one object in the list");
        Assertions.assertNotNull(target.getFirst().getCreationDate(), "The object should have a creation date");
        Assertions.assertNotNull(target.getFirst().getId(), "The object should have an id");
        Assertions.assertEquals(10,target.getFirst().getId(), "The object's id should match");
        Assertions.assertEquals(4,target.getFirst().getQuantity(), "The object's quantity should match");
    }

    @Test
    @DisplayName("Test that an entity will not be saved when the WO does not exists")
    void createWorkOrderJobTest_failure() throws Exception {
        // Arrange
        WorkOrderJobDTO woJobDTO = WorkOrderJobDTO.builder()
                .woNumber("non-existent")
                .jobCode("jobCode1")
                .quantity(4)
                .activeStatus('Y')
                .appliedRule("A1")
                .build();
        String json = gson.toJson(woJobDTO);
        // Act
        MvcResult result = mockMvc.perform(post("/workorderjobs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isConflict())
                .andReturn();
        boolean saved = Boolean.parseBoolean(result.getResponse().getContentAsString());
        // Assert
        Assertions.assertFalse(saved, "The entity should not have been saved because it`s parent doesn't exist.");
    }

}

