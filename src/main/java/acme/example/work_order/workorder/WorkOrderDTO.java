package acme.example.work_order.workorder;

import acme.example.work_order.workorderjob.WorkOrderJobDTO;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkOrderDTO {
    private String woNumber;
    private List<WorkOrderJobDTO> woJobDTOs;
    private Long jobTypeId;
    private String address;
    private String city;
    private String state;
    private LocalDateTime woCreationDate;
    private LocalDateTime woCompletionDate;
    private String clientID;
    private String appliedRule;
    
}
