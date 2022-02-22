package com.mindex.challenge.service.impl;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompensationServiceImpl implements CompensationService {

    private static final Logger LOG = LoggerFactory.getLogger(CompensationServiceImpl.class);

    @Autowired
    CompensationRepository compensationRepository;
    
    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Compensation create(Compensation c) {
        LOG.debug("Creating compensation [{}]", c);

        String id = c.getEmployee().getEmployeeId();

        if (employeeRepository.findByEmployeeId(id) == null) {
            throw new RuntimeException("No employee with employeeId: " + id);
        }

        c.setEmployeeId(id); // not required to be input; only used internally as indexing key

        compensationRepository.insert(c);

        return c;
    }

    @Override
    public Compensation read(String id) {
        LOG.debug("Reading compensation with employee id [{}]", id);

        List<Compensation> cList = compensationRepository.findByEmployeeIdAndEffectiveDateBeforeOrderByEffectiveDateDesc(
            id, 
            Date.from(Instant.now()));

        if(cList.size() == 0){
            throw new RuntimeException("Invalid employeeId " + id + " or no matching Compensation");
        }

        Compensation c = cList.get(0);

        return c;
    }
    
}
