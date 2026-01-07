package com.dreamhouse.mem.model;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.dreamhouse.mem.model.MemVO;
import java.util.Properties;

import jakarta.mail.*;
import jakarta.mail.internet.*;



import jakarta.transaction.Transactional;

@Service("memberService")
public class MemService {

    @Autowired
    private MemRepository repository;

    // 新增會員：設定 createTime 和 updatedTime
    @Transactional
    public MemVO addMember(MemVO member) {
        Timestamp now = Timestamp.from(Instant.now());
        member.setCreateTime(now);
        member.setUpdatedTime(now);
        return repository.save(member);
    }
    //檢查帳號及信箱是否使用
    public boolean existsByAccount(String account) {
        return repository.existsByAccount(account);
    }

    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    //會員登入
    public MemVO findByAccountAndPassword(String account, String password) {
        return repository.findByAccountAndPassword(account, password);
    }

    public void updateLastLogin(Integer memberId) {
        MemVO member = repository.findById(memberId).orElse(null);
        if (member != null) {
        	Timestamp now = Timestamp.from(Instant.now());
            member.setLastLogin(now);
            repository.save(member);
        }
    }
    
  //查詢單一會員
    public MemVO findById(Integer memberId) {
        return repository.findById(memberId).orElse(null);
    }
    
    //會員更新資料
    public MemVO updateMember(MemVO member) {
        return repository.save(member); // JPA save 會自動更新
    }
    
    // 查詢全部會員
    public List<MemVO> findAllMembers() {
        return repository.findAll();
    }


    // 更新會員狀態 (0=停用, 1=正常)
    public void updateStatus(Integer memberId, int status) {
        MemVO mem = repository.findById(memberId).orElse(null);
        if (mem != null) {
            mem.setStatus(status);
            repository.save(mem);
        }
    }

    public List<MemVO> findActiveMem(){
    	return repository.findActiveMem(1);
    }
    @Autowired
    private MailService mailService;

    // 寄送驗證碼
    public boolean sendVerificationCode(String email, String code) {
        MemVO mem = repository.findByEmail(email);
        if (mem != null) {
            mailService.sendMail(email, "忘記密碼驗證碼", "您的驗證碼是：" + code);
            return true;
        }
        return false;
    }



    // 重設密碼
    public boolean resetPassword(String email, String newPassword) {
        MemVO mem = repository.findByEmail(email);
        if (mem != null) {
            mem.setPassword(newPassword); // 專題版可直接存，正式版建議加密
            repository.save(mem);
            return true;
        }
        return false;
    }

}

 
