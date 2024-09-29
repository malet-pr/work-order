package acme.example.work_order.jobType;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobTypeDTO {
    private String code;
    private String name;
    private Character activeStatus;
    private String clientType;
}
