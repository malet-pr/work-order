package acme.example.work_order.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Sql(scripts = "/sql/jobTestData.sql")
public class JobControllerTest extends BaseApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Test getJob() endpoint when the job exists")
    void getJobTest_exist() throws Exception {
        mockMvc.perform(get("/jobs/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.code").value("JobCode1"))
                .andExpect(jsonPath("$.name").value("job name 1"))
                .andExpect(jsonPath("$.activeStatus").value("Y"));
    }

    @Test
    @DisplayName("Test getJob() endpoint when the job doesn't exists")
    void getJobTest_notExist() throws Exception {
        mockMvc.perform(get("/jobs/{id}", 100L))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Test getJobByCode() endpoint when the job exists")
    void getJobByCodeTest_exist() throws Exception {
        mockMvc.perform(get("/jobs/codes/{code}", "JobCode1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.code").value("JobCode1"))
                .andExpect(jsonPath("$.name").value("job name 1"))
                .andExpect(jsonPath("$.activeStatus").value("Y"));
    }

    @Test
    @DisplayName("Test getJobByCode() endpoint when the job doesn't exists")
    void getJobByCodeTest_notExist() throws Exception {
        mockMvc.perform(get("/jobs/codes/{code}", "non-existent"))
                .andExpect(status().isNotFound());
    }

}