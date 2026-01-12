package com.dreamhouse.mem.model;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.transaction.Transactional;

@Service("memberService")
public class MemService {

    @Autowired
    private MemRepository repository;

    @Autowired
    private MailService mailService;

    @Autowired
    private TemplateEngine templateEngine; // 用來渲染 HTML 模板

    // 新增會員：設定 createTime、updatedTime，並產生驗證 Token
    @Transactional
    public MemVO addMember(MemVO member) {
        Timestamp now = Timestamp.from(Instant.now());
        member.setCreateTime(now);
        member.setUpdatedTime(now);

        // 註冊時預設未驗證
        member.setEmailVerified(false);

        // 產生驗證 Token 與有效期限
        String token = UUID.randomUUID().toString();
        member.setVerificationToken(token);
        member.setTokenExpireTime(Timestamp.valueOf(LocalDateTime.now().plusHours(24)));

        MemVO saved = repository.save(member);

        // 寄送驗證信（使用 Thymeleaf 模板）
        String verifyLink = "http://localhost:8080/mem/verify?token=" + token;
        Context context = new Context();
        context.setVariable("verifyUrl", verifyLink);
        String htmlContent = templateEngine.process("front-end/mem/email_verify", context);

        try {
            mailService.sendHtmlMail(saved.getEmail(), "帳號驗證", htmlContent);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return saved;
    }

    // 檢查帳號及信箱是否使用
    public boolean existsByAccount(String account) {
        return repository.existsByAccount(account);
    }

    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    // 會員登入（需檢查是否已驗證）
    public MemVO findByAccountAndPassword(String account, String password) {
        MemVO mem = repository.findByAccountAndPassword(account, password);
        if (mem != null && mem.getEmailVerified()) {
            return mem;
        }
        return null; // 未驗證或帳號密碼錯誤
    }

    public void updateLastLogin(Integer memberId) {
        MemVO member = repository.findById(memberId).orElse(null);
        if (member != null) {
            Timestamp now = Timestamp.from(Instant.now());
            member.setLastLogin(now);
            repository.save(member);
        }
    }

    // 查詢單一會員
    public MemVO findById(Integer memberId) {
        return repository.findById(memberId).orElse(null);
    }

    // 會員更新資料
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

    // 查詢會員狀態=1
    public List<MemVO> findActiveMem() {
        return repository.findActiveMem(1);
    }

    // 驗證帳號
    public boolean verifyEmail(String token) {
        MemVO mem = repository.findByVerificationToken(token);
        if (mem != null && mem.getTokenExpireTime() != null
                && mem.getTokenExpireTime().after(new Timestamp(System.currentTimeMillis()))) {
            mem.setEmailVerified(true);
            mem.setVerificationToken(null);
            mem.setTokenExpireTime(null);
            repository.save(mem);
            return true;
        }
        return false;
    }

    // 重寄驗證信（使用 HTML 模板）
    public boolean resendVerification(String email) {
        MemVO mem = repository.findByEmail(email);
        if (mem != null && !mem.getEmailVerified()) {
            String newToken = UUID.randomUUID().toString();
            mem.setVerificationToken(newToken);
            mem.setTokenExpireTime(Timestamp.valueOf(LocalDateTime.now().plusHours(24)));
            repository.save(mem);

            String verifyLink = "http://localhost:8080/mem/verify?token=" + newToken;
            Context context = new Context();
            context.setVariable("verifyUrl", verifyLink);
            String htmlContent = templateEngine.process("front-end/mem/email_verify", context);

            try {
                mailService.sendHtmlMail(mem.getEmail(), "帳號驗證", htmlContent);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

            return true;
        }
        return false;
    }

    // 忘記密碼寄送驗證碼（純文字）
    public boolean sendVerificationCode(String email, String code) {
        MemVO mem = repository.findByEmail(email);
        if (mem != null) {
            String textContent = "您的驗證碼是：" + code + "\n請在 10 分鐘內使用完成驗證。";
            try {
                mailService.sendTextMail(email, "忘記密碼驗證碼", textContent);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
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