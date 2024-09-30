package acme.example.work_order.integration;

import jakarta.transaction.Transactional;
import acme.example.work_order.workorderjob.WorkOrderJobDTO;
import acme.example.work_order.workorderjob.internal.WorkOrderJob;
import acme.example.work_order.workorderjob.internal.WorkOrderJobDAO;
import acme.example.work_order.workorderjob.internal.WorkOrderJobServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
@Sql(scripts = "/sql/woTestData.sql")
public class WorkOrderJobServiceIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private WorkOrderJobServiceImpl service;

    @Autowired
    private WorkOrderJobDAO woJobDAO;

    @Test
    @DisplayName("Tests if an entity is saved in the database when the parent entity exists")
    void saveTest_woExists() {
        // Arrange
        WorkOrderJobDTO woJobDTO = WorkOrderJobDTO.builder()
                .woNumber("ZZZ999")
                .jobCode("jobCode1")
                .quantity(4)
                .activeStatus('Y')
                .appliedRule("A1")
                .build();
        // Act
        boolean saved = service.save(woJobDTO);
        List<WorkOrderJob> list = woJobDAO.findByWorkOrderNumber("ZZZ999");
        Optional<WorkOrderJob> entity = woJobDAO.findById(10L);
        // Assert
        Assertions.assertTrue(saved, "The entity should have been saved successfully.");
        Assertions.assertFalse(list.isEmpty(), "There should be at least one object in the list");
        Assertions.assertTrue(entity.isPresent(), "The entity should have been saved with id = 10L.");
        Assertions.assertNotNull(entity.get().getCreationDate(), "Creation date should not be null.");
        Assertions.assertEquals(4,entity.get().getQuantity(), "Entity quantity does not match.");
        Assertions.assertEquals('Y',entity.get().getActiveStatus(), "Entity active status does not match.");
        Assertions.assertEquals("A1",entity.get().getAppliedRule(), "Entity applied rule does not match.");
    }

    @Test
    @DisplayName("Tests no entity is saved in the database when the parent entity doesn't exists")
    void saveTest_woDoesNotExists() {
        // Arrange
        WorkOrderJobDTO woJobDTO = WorkOrderJobDTO.builder()
                .woNumber("non-existent")
                .jobCode("jobCode1")
                .quantity(4)
                .activeStatus('Y')
                .appliedRule("A1")
                .build();
        // Act
        boolean saved = service.save(woJobDTO);
        // Assert
        Assertions.assertFalse(saved, "The entity should not have been saved because it`s parent doesn't exist.");
    }

}


/*
    WorkOrderJobDTO findById(Long id);
    List<WorkOrderJobDTO> findByIds(List<Long> ids);
    List<WorkOrderJobDTO> findByCodes(List<String> jobCodeList);
 */