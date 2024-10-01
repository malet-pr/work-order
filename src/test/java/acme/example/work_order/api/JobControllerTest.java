package acme.example.work_order.api;

import acme.example.work_order.job.internal.Job;
import acme.example.work_order.job.internal.JobDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Sql(scripts = "/sql/jobTestData.sql")
public class JobControllerTest extends BaseApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JobDAO jobDAO;

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

    @Test
    @DisplayName("Test succesfull save of a job")
    void createJobTest_success() throws Exception {
        mockMvc.perform(post("/jobs")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"code\":\"newJob\", \"name\":\"test saving jobs\", \"activeStatus\":\"Y\"}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        Job job = jobDAO.findByCode("newJob");
        Assertions.assertNotNull(job, "Job should not be null");
        Assertions.assertNotNull(job.getId(), "Job id should not be null");
        Assertions.assertNotNull(job.getCreationDate(), "Job creation Date should not be null");
        Assertions.assertEquals("test saving jobs", job.getName(), "Job name should match");
    }

    @Test
    @DisplayName("Test that a job cannot be saved without a code")
    void createJobTest_failure() throws Exception {
        mockMvc.perform(post("/jobs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"code\":null, \"name\":\"test saving jobs\", \"activeStatus\":\"Y\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
        Job job = jobDAO.findByCode("newJob");
        Assertions.assertNull(job, "Job should not exist");
    }

    @Test
    @DisplayName("The name of the job should be returned when searching by an existing id")
    void getNameByJobTest_exist() throws Exception {
        MvcResult result = mockMvc.perform(get("/jobs/{id}/name", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andReturn();
        String content = result.getResponse().getContentAsString();
        Assertions.assertEquals("job name 1", content, "Job name should match");
    }

    @Test
    @DisplayName("Not found should be returned when searching by a non-existing id")
    void getNameByJobTest_notExist() throws Exception {
        mockMvc.perform(get("/jobs/{id}/name", 100L))
                .andExpect(status().isNotFound());
    }

}

