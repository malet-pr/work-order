package acme.example.work_order.api;

import acme.example.work_order.workorderjob.internal.WorkOrderJobDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Sql(scripts = "/sql/woTestData.sql")
public class WorkOrderJobControllerTest extends BaseApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WorkOrderJobDAO woJobDAO;

}
