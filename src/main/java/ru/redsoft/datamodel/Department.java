package ru.redsoft.datamodel;

import java.util.Objects;

public class Department {
    private Integer id;
    private String departmentName;
    private String departmentHeadFullName;
    private String phone;
    private String email;

    public Department(Integer id, String departmentName, String departmentHeadFullName, String phone, String email) {
        this.id = id;
        this.departmentName = departmentName;
        this.departmentHeadFullName = departmentHeadFullName;
        this.phone = phone;
        this.email = email;
    }

    public Department(String departmentName, String departmentHeadFullName, String phone, String email) {
        this.departmentName = departmentName;
        this.departmentHeadFullName = departmentHeadFullName;
        this.phone = phone;
        this.email = email;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDepartmentHeadFullName() {
        return departmentHeadFullName;
    }

    public void setDepartmentHeadFullName(String departmentHeadFullName) {
        this.departmentHeadFullName = departmentHeadFullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Department that = (Department) o;
        return Objects.equals(departmentName, that.departmentName) &&
                Objects.equals(departmentHeadFullName, that.departmentHeadFullName) &&
                Objects.equals(phone, that.phone) &&
                Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(departmentName, departmentHeadFullName, phone, email);
    }

    @Override
    public String toString() {
        return departmentName + ' ' + departmentHeadFullName + ' ' + phone + ' ' + email;
    }
}
