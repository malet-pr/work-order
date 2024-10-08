package acme.example.work_order.workorder.internal;


import acme.example.work_order.workorder.WorkOrderDTO;
import acme.example.work_order.workorder.WorkOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkOrderServiceImpl implements WorkOrderService {

    private final WorkOrderMapper woMapper;
    private final WorkOrderDAO woDAO;
    //private final WorkOrderJobDAO woJobDAO;
    //private final JobTypeDAO typeDAO;
    //private final JobDAO jobDAO;

    @Autowired
    public WorkOrderServiceImpl(WorkOrderMapper woMapper, WorkOrderDAO woDAO) {
        this.woMapper = woMapper;
        this.woDAO = woDAO;
        //this.woJobDAO = woJobDAO;
        //this.typeDAO = typeDAO;
        //this.jobDAO = jobDAO;
    }

    @Override
    public Boolean save(WorkOrderDTO dto) {
        WorkOrder entity = woMapper.convertToEntity(dto);
        if(entity == null) {return false;}
        if(woDAO.existsByWoNumber(entity.getWoNumber())) {
            System.out.println(STR."A work order with id \{entity.getId()} already exists");
            return false;
        }
        try {
            entity.getJobs().forEach(j -> j.setWorkOrder(entity));
            woDAO.save(entity);
            return true;
        }catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public WorkOrderDTO findById(Long id) {
        return woMapper.convertToDTO(woDAO.findById(id).orElse(null));
    }

    @Override
    public WorkOrderDTO findByWoNumber(String woNumber) {
        return woMapper.convertToDTO(woDAO.findByWoNumber(woNumber));
    }
}
