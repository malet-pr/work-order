package acme.example.work_order.api;

import acme.example.work_order.workorderjob.WorkOrderJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/workorderjobs")
public class WorkOrderJobController {

    @Autowired
    private WorkOrderJobService woJobService;

}
