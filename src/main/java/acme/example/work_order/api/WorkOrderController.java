package acme.example.work_order.api;

import acme.example.work_order.workorder.WorkOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/workorders")
public class WorkOrderController {

    @Autowired
    private WorkOrderService woService;

}
