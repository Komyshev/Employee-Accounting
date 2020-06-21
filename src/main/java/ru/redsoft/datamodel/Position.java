package ru.redsoft.datamodel;

import java.util.Objects;

public class Position {
    private Integer id;
    private String positionName;
    private float salary;

    public Position(Integer id, String positionName, float salary) {
        this.id = id;
        this.positionName = positionName;
        this.salary = salary;
    }

    public Position(String positionName, float salary) {
        this.positionName = positionName;
        this.salary = salary;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public float getSalary() {
        return salary;
    }

    public void setSalary(float salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return positionName + ' ' + salary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return id == position.id &&
                Float.compare(position.salary, salary) == 0 &&
                Objects.equals(positionName, position.positionName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, positionName, salary);
    }
}
