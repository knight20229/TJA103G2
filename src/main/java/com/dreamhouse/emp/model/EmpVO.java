package com.dreamhouse.emp.model;

import java.io.Serializable;
import java.sql.Timestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Table;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

/**
 * 對應資料表: employee
 */
@Entity
@Table(name = "employee")
public class EmpVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer employeeId;
    private String account;
    private String password;
    private String name;
    private String email;
    private Byte status;
    private Timestamp createTime;
    private Timestamp updatedTime;

    public EmpVO() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id", nullable = false, updatable = false)
    public Integer getEmployeeId() {
        return employeeId;
    }
    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
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

    @Column(name = "status", nullable = false)
    @NotNull(message = "帳號狀態: 請勿空白")
    public Byte getStatus() {
        return status;
    }
    public void setStatus(Byte status) {
        this.status = status;
    }

    @Column(name = "create_time", nullable = false, updatable = false, insertable = false)
    public Timestamp getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Column(name = "updated_time", nullable = false, insertable = false)
    public Timestamp getUpdatedTime() {
        return updatedTime;
    }
    public void setUpdatedTime(Timestamp updatedTime) {
        this.updatedTime = updatedTime;
    }
}