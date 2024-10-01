package acme.example.work_order.api;

import acme.example.work_order.job.JobDTO;
import acme.example.work_order.job.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/jobs")
public class JobController {

    @Autowired
    private JobService jobService;


    @GetMapping("/{id}")
    public ResponseEntity<JobDTO> getJob(@PathVariable("id") Long id) {
        try {
            JobDTO job = jobService.findById(id);
            return job != null ? new ResponseEntity<>(job, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/codes/{code}")
    public ResponseEntity<JobDTO> getJobByCode(@PathVariable("code") String code) {
        try {
            JobDTO job = jobService.findByCode(code);
            return job != null ? new ResponseEntity<>(job, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping()
    public ResponseEntity<Boolean> createJob(@RequestBody JobDTO jobDTO) {
        try{
            boolean saved = jobService.save(jobDTO);
            return saved ? new ResponseEntity<>(true, HttpStatus.CREATED) : new ResponseEntity<>(false, HttpStatus.CONFLICT);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}/name")
    public ResponseEntity<String> getNameByJob(@PathVariable("id") Long id) {
        try {
            String name = jobService.getNameByJob(id);
            return !name.isBlank() ?  new ResponseEntity<>(name, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

}

/*
    public Character getActiveStatusById(Long id);
    public JobDTO findByCodeAndActiveStatus(String code, Character activeStatus);
    public List<JobDTO> findByCodesAndActiveStatus(List<String> codes, Character activeStatus);
    public List<Long> findByCodes(List<String> codes);
 */