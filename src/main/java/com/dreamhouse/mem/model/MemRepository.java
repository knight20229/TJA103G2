// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/

package com.dreamhouse.mem.model;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface MemRepository extends JpaRepository<MemVO, Integer> {
	MemVO findByAccountAndPassword(String account, String password);
	boolean existsByAccount(String account);
	boolean existsByEmail(String email);

}

