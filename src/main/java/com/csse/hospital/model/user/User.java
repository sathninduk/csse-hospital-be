// User.java
package com.csse.hospital.model.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email")
        })
public class User {

    // attributes
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @GeneratedValue
    @Column(name = "userkey", insertable = false, updatable = false, nullable = false, columnDefinition = "uuid DEFAULT uuid_generate_v4()")
    private UUID userkey;

    @NotBlank
    @Size(max = 50)
    @Email
    @Column(name = "email")
    private String username;

    @NotBlank
    @Size(max = 100)
    private String first_name;

    @NotBlank
    @Size(max = 100)
    private String last_name;

    @NotBlank
    @Size(max = 20)
    private String phone;

    @NotNull(message = "Date of birth must not be null")
    private LocalDate date_of_birth;

    @NotBlank
    @Size(max = 20)
    private String gender;

    @NotBlank
    @Size(max = 120)
    private String password;

    // status
    private int status;

    @Size(max = 120)
    private String code;

    @Size(max = 120)
    @Column(name = "forgot_code")
    private String forgotCode;

    @Column(name = "verification_exp")
    private Timestamp verificationExp;

    @Column(name = "forgot_exp")
    private Timestamp forgotExp;

    // init date
    @Column(name = "init_date", insertable = false, updatable = false, nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp initDate;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    // Constructors
    public User() {
    }

    public User(String first_name, String last_name, String email, String phone, LocalDate date, String gender, String password) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.username = email;
        this.phone = phone;
        this.date_of_birth = date;
        this.gender = gender;
        this.password = password;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUserkey() {
        return userkey;
    }

    public void setUserkey(UUID userkey) {
        this.userkey = userkey;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(LocalDate date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getForgotCode() {
        return forgotCode;
    }

    public void setForgotCode(String forgotCode) {
        this.forgotCode = forgotCode;
    }

    public Timestamp getVerificationExp() {
        return verificationExp;
    }

    public void setVerificationExp(Timestamp verificationExp) {
        this.verificationExp = verificationExp;
    }

    public Timestamp getForgotExp() {
        return forgotExp;
    }

    public void setForgotExp(Timestamp forgotExp) {
        this.forgotExp = forgotExp;
    }

    public Timestamp getInitDate() {
        return initDate;
    }

    public void setInitDate(Timestamp initDate) {
        this.initDate = initDate;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}