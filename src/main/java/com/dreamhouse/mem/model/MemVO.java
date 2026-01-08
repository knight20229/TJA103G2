package com.dreamhouse.mem.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import com.dreamhouse.memcoupon.model.MemCouponVO;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "member")
public class MemVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer memberId;
    private String account;
    private String password;
    private String name;
    private String email;
    private String address;
    private String phone;
    private Set<MemCouponVO> memCoup;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
    private Integer status = 1;   // 預設啟用
    private Timestamp createTime;
    private Timestamp updatedTime;
    private Timestamp lastlogin;

    public MemVO() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", nullable = false, updatable = false)
    public Integer getMemberId() {
        return memberId;
    }
    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    @Column(name = "account", nullable = false, length = 50)
    @NotEmpty(message = "帳號: 請勿空白")
    @Size(min = 8, max = 50, message = "帳號: 長度需介於8~50字元")
    public String getAccount() {
        return account;
    }
    public void setAccount(String account) {
        this.account = account;
    }

    @Column(name = "password", nullable = false, length = 50)
    @NotEmpty(message = "密碼: 請勿空白")
    @Size(min = 8, max = 50, message = "密碼: 長度需介於8~50字元")
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "name", nullable = false, length = 20)
    @NotEmpty(message = "姓名: 請勿空白")
    @Size(max = 20, message = "姓名: 長度不能超過20字元")
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "email", nullable = false, length = 50)
    @NotEmpty(message = "電子信箱: 請勿空白")
    @Email(message = "電子信箱: 格式不正確")
    @Size(max = 50, message = "電子信箱: 長度不能超過50字元")
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "address", nullable = false, length = 100)
    @NotEmpty(message = "地址: 請勿空白")
    @Size(max = 100, message = "地址: 長度不能超過100字元")
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    @Column(name = "phone", nullable = false, length = 20)
    @NotEmpty(message = "電話: 請勿空白")
    @Size(max = 20, message = "電話: 長度不能超過20字元")
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Column(name = "birthday", nullable = false)
    @NotNull(message = "生日: 請勿空白")
    public LocalDate getBirthday() { return birthday; }   // ✅ LocalDate getter
    public void setBirthday(LocalDate birthday) { this.birthday = birthday; }


    @Column(name = "status", nullable = false)
    @NotNull(message = "帳號狀態: 請勿空白")
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }

    @Column(name = "create_time", nullable = false, updatable = false, insertable = false)
    public Timestamp getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Column(name = "update_time", nullable = false, insertable = false)
    public Timestamp getUpdatedTime() {
        return updatedTime;
    }
    public void setUpdatedTime(Timestamp updatedTime) {
        this.updatedTime = updatedTime;
    }

    @Column(name = "last_login", insertable = false)
    public Timestamp getLastLogin() {
        return lastlogin;
    }
    public void setLastLogin(Timestamp lastlogin) {
        this.lastlogin = lastlogin;
    }

	@Override
	public String toString() {
		return "MemVO [memberId=" + memberId + ", account=" + account + ", password=" + password + ", name=" + name
				+ ", email=" + email + ", address=" + address + ", phone=" + phone + ", birthday=" + birthday
				+ ", status=" + status + ", createTime=" + createTime + ", updatedTime=" + updatedTime + ", lastlogin="
				+ lastlogin + "]";
	}

	@OneToMany(mappedBy = "memVO")
	@JsonIgnore
	public Set<MemCouponVO> getMemCoup() {
		return memCoup;
	}

	public void setMemCoup(Set<MemCouponVO> memCoup) {
		this.memCoup = memCoup;
	}
    
}