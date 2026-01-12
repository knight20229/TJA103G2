// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/

package com.dreamhouse.mem.model;

import java.time.LocalDate;
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
    // 依帳號查詢會員
    MemVO findByAccount(String account);

    // 依驗證 Token 查詢會員
    MemVO findByVerificationToken(String verificationToken);

	
	@Query(value = "select * from member where status=?1 order by member_id", nativeQuery = true)
	List<MemVO> findActiveMem(Integer state1);

	MemVO findByEmail(String email);
	
}

