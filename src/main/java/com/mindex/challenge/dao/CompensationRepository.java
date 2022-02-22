package com.mindex.challenge.dao;

import java.util.Date;
import java.util.List;

import com.mindex.challenge.data.Compensation;

import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;

@Repository
public interface CompensationRepository extends MongoRepository<Compensation, String> {
    List<Compensation> findByEmployeeIdAndEffectiveDateBeforeOrderByEffectiveDateDesc(String employeeId, Date date);
}

