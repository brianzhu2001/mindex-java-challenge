package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Employee create(Employee employee) {
        LOG.debug("Creating employee [{}]", employee);

        employee.setEmployeeId(UUID.randomUUID().toString());
        employeeRepository.insert(employee);

        return employee;
    }

    @Override
    public Employee read(String id) {
        LOG.debug("Reading employee with id [{}]", id);

        Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        return employee;
    }

    @Override
    public Employee update(Employee employee) {
        LOG.debug("Updating employee [{}]", employee);

        return employeeRepository.save(employee);
    }

    /**
     * Initializes and returns ReportingStructure corresponding to a particular
     * Employee. numberOfReports is equal to the number of Employees where there 
     * exists a direct or indirect link from this Employee. This behaves as expected
     * in the case of a tree but may act unexpectedly otherwise.  It is robust to 
     * cross and forward edges but is not necessarily robust to back edges and cycles.
     * 
     * @param id id of an Employee
     * @return ReportingStructure for corresponding Employee
     */
    @Override
    public ReportingStructure gReportingStructure(String id) {
        LOG.debug("Generating ReportingStructure for employee with id [{}]", id);

        Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        // initialize ReportingStructure
        ReportingStructure rs = new ReportingStructure();
        rs.setEmployee(employee);

        // case if leaf
        if(employee.getDirectReports() == null) {
            rs.setNumberOfReports(0);
            return rs;
        }

        // BF traverse and count descendants until exhausted
        Set<String> seenIds = new HashSet<String>();
        seenIds.add(employee.getEmployeeId());
        Queue<Employee> toSearch = new LinkedList<Employee>();
        // below line assumes employee not in employee.getDirectReports()
        toSearch.addAll(employee.getDirectReports()); 
        Employee e;
        while(!toSearch.isEmpty()) {
            e = toSearch.poll();
            e = employeeRepository.findByEmployeeId(e.getEmployeeId());
            if (e == null) {
                throw new RuntimeException("Invalid employeeId: " + id);
            }
            seenIds.add(e.getEmployeeId());
            if(e.getDirectReports() != null) {
                for(Employee e2 : e.getDirectReports()) {
                    // ignore edges to already seen nodes
                    // makes algorithm more robust to malformed trees
                    if(!seenIds.contains(e2.getEmployeeId())) {
                        toSearch.add(e2);
                    }
                }
            }
        }
        rs.setNumberOfReports(seenIds.size()-1);
        
        return rs;
    }
}
