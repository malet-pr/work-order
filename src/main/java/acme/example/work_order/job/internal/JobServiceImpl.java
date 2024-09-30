package acme.example.work_order.job.internal;


import acme.example.work_order.job.JobDTO;
import acme.example.work_order.job.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class JobServiceImpl implements JobService {

    @Autowired
    JobDAO jobDAO;

    @Autowired
    JobMapper jobMapper = new JobMapper();

    @Override
    public JobDTO findById(Long id) {
        Job entity = jobDAO.findById(id).orElse(null);
        return jobMapper.convertToDto(entity);
    }

    @Override
    public boolean save(JobDTO job){
        try{
            Job entity = jobMapper.convertToEntity(job);
            jobDAO.save(entity);
            return true;
        } catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public String getNameByJob(Long id) {
        Optional<Job> entity = jobDAO.findById(id);
        return entity.map(Job::getName).orElse("");
    }

    @Override
    public Character getActiveStatusById(Long id) {
        Optional<Job> entity = jobDAO.findById(id);
        return entity.map(Job::getActiveStatus).orElse(null);
    }

    @Override
    public JobDTO findByCode(String code) {
        Job entity = jobDAO.findByCode(code);
        return jobMapper.convertToDto(entity);
    }

    @Override
    public List<Long> findByCodes(List<String> codes) {
        List<Job> list = jobDAO.findByCodes(codes);
        return list.stream().map(Job::getId).toList();
    }

    @Override
    public JobDTO findByCodeAndActiveStatus(String code, Character activeStatus) {
        Job entity = jobDAO.findByCodeAndActiveStatus(code,activeStatus);
        return jobMapper.convertToDto(entity);
    }

    @Override
    public List<JobDTO> findByCodesAndActiveStatus(List<String> codes, Character activeStatus) {
        List<Job> actividades = jobDAO.findByCodesAndActiveStatus(codes,activeStatus);
        return actividades.stream().map(jobMapper::convertToDto).toList();
    }

}

