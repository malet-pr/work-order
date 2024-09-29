package acme.example.work_order.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import acme.example.work_order.service.JobService;


@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "/sql/jobTestData.sql")
public class JobControllerTest extends BaseApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JobService jobServiceMock;

    @Test
    @DisplayName("Test getJob() endpoint when the job exists")
    void getJobTest_exist() throws Exception {
        mockMvc.perform(get("/job?id=1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.code").value("JobCode1"))
                .andExpect(jsonPath("$.name").value("job name 1"))
                .andExpect(jsonPath("$.activeStatus").value("Y"));
    }

    @Test
    @DisplayName("Test getJob() endpoint when the job doesn't exists")
    void getJobTest_notExist() throws Exception {
        mockMvc.perform(get("/job?id=100"))
                .andExpect(status().isNotFound());

    }

    @Test
    @DisplayName("Test getJob() endpoint when an exception occurs (500 error)")
    void getJobTest_serverError() throws Exception {
        doThrow(new RuntimeException()).when(jobServiceMock).findById(anyLong());
        mockMvc.perform(get("/job?id=1"))
                .andExpect(status().isInternalServerError());
    }

}