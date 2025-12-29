package com.dreamhouse.emp.model;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("empService")
public class EmpService {

    @Autowired
    private EmpRepository repository;

    // 新增員工：設定 createTime 和 updatedTime
    public EmpVO addEmployee(EmpVO employee) {
        Timestamp now = Timestamp.from(Instant.now());
        employee.setCreateTime(now);
        employee.setUpdatedTime(now);
        return repository.save(employee);
    }

    // 更新員工：保留原本的 createTime，只更新 updatedTime
    public EmpVO updateEmployee(EmpVO employee) {
        EmpVO existing = repository.findById(employee.getEmployeeId()).orElse(null);
        if (existing != null) {
            // 保留原本的 createTime
            employee.setCreateTime(existing.getCreateTime());
        }
        employee.setUpdatedTime(Timestamp.from(Instant.now()));
        return repository.save(employee);
    }

    // 查詢單筆
    public EmpVO getOneEmployee(Integer employeeId) {
        return repository.findById(employeeId).orElse(null);
    }

    // 查詢全部
    public List<EmpVO> getAllEmployees() {
        return repository.findAll();
    }

    

    

}