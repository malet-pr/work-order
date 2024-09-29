package acme.example.work_order.api;

import acme.example.work_order.job.JobDTO;
import jdk.jfr.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import acme.example.work_order.service.JobService;


@RestController
@RequestMapping("/job")
public class JobController {

    @Autowired
    private JobService jobService;

    @GetMapping(name="/job/{id}", produces = "application/json")
    public ResponseEntity<JobDTO> getJob(@RequestParam("id") Long id) {
        try {
            JobDTO job = jobService.findById(id);
            if (job != null) {
                return ResponseEntity.ok(job);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
