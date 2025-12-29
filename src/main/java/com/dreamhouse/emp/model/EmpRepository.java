// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/

package com.dreamhouse.emp.model;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface EmpRepository extends JpaRepository<EmpVO, Integer> {
    EmpVO findByAccount(String account);
}
