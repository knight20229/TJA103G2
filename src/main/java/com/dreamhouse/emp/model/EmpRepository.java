// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/

package com.dreamhouse.emp.model;

import org.springframework.data.jpa.repository.JpaRepository;


public interface EmpRepository extends JpaRepository<EmpVO, Integer> {
	EmpVO findByAccountAndPassword(String account, String password);
	boolean existsByAccount(String account);
	boolean existsByEmail(String email);
}
