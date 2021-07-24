package ru.redsoft.dao;

import ru.redsoft.datamodel.Department;
import ru.redsoft.datamodel.Employee;
import ru.redsoft.datamodel.Position;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("SqlNoDataSourceInspection")
public class EmployeeAccountingDAO {

    private final Connection connection;

    public EmployeeAccountingDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Employee> getAllEmployees(){
        List<Employee> employees = new ArrayList<>();
        ResultSet rs;
        try (Statement statement = connection.createStatement()){

            rs = statement.executeQuery("SELECT E.ID_EMPLOYEE, E.FIRST_NAME, E.LAST_NAME, D.DEPARTMENT_NAME, P.POSITION_NAME\n" +
                    "FROM EMPLOYEES E \n" +
                    "LEFT JOIN DEPARTMENTS D ON (E.ID_DEPARTMENT = D.ID_DEPARTMENT)\n" +
                    "LEFT JOIN POSITIONS P ON (E.ID_POSITION = P.ID_POSITION)");
            while (rs.next()) {

                employees.add(new Employee(rs.getInt(1), rs.getString(2),
                        rs.getString(3),rs.getString(4), rs.getString(5)));
            }
        }
        catch (SQLException e) {
            System.out.println(e.getErrorCode());
        }
        return employees;
    }

    public List<Department> getAllDepartments(){
        List<Department> departments = new ArrayList<>();
        ResultSet rs;
        try (Statement statement = connection.createStatement()){
            rs = statement.executeQuery("SELECT D.ID_DEPARTMENT, D.DEPARTMENT_NAME, E.FIRST_NAME, E.LAST_NAME, D.PHONE, D.EMAIL\n" +
                    "FROM DEPARTMENTS D\n" +
                    "LEFT JOIN EMPLOYEES E ON (D.ID_HEAD_OF_DEPARTMENT = E.ID_EMPLOYEE)");
            while (rs.next()) {

                departments.add(new Department(rs.getInt(1), rs.getString(2),
                        rs.getString(3) + ' ' + rs.getString(4),
                        rs.getString(5), rs.getString(6)));
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return departments;
    }

    public List<Position> getAllPositions() {
        List<Position> positions = new ArrayList<>();
        ResultSet rs;
        try (Statement statement = connection.createStatement()){
            rs = statement.executeQuery("SELECT P.ID_POSITION, P.POSITION_NAME, P.SALARY\n" +
                    "FROM POSITIONS P");
            while (rs.next()) {

                positions.add(new Position(rs.getInt(1), rs.getString(2),
                        rs.getFloat(3)));
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return positions;
    }

    public void addPosition(Position position){
        if (position != null) {
            String sql = "INSERT INTO POSITIONS (ID_POSITION, POSITION_NAME, SALARY)\n" +
                    "VALUES (NEXT VALUE FOR POSITION_SEQUENCE, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

                preparedStatement.setString(1, position.getPositionName());
                preparedStatement.setFloat(2, position.getSalary());
                preparedStatement.executeUpdate();
            }
            catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

    }

    public void addDepartment(Department department){
        if (department != null) {
            String sql = "INSERT INTO DEPARTMENTS (ID_DEPARTMENT, DEPARTMENT_NAME, ID_HEAD_OF_DEPARTMENT,PHONE, EMAIL)\n" +
                    "VALUES (NEXT VALUE FOR DEPARTMENT_SEQUENCE, ?, " +
                    "(SELECT ID_EMPLOYEE FROM EMPLOYEES WHERE FIRST_NAME = ? AND LAST_NAME = ?),?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

                 preparedStatement.setString(1, department.getDepartmentName());
                 preparedStatement.setString(2, department.getDepartmentHeadFullName().split("\\s+",2)[0]);
                 preparedStatement.setString(3, department.getDepartmentHeadFullName().split("\\s+",2)[1]);
                 preparedStatement.setString(4, department.getPhone());
                 preparedStatement.setString(5, department.getEmail());
                 preparedStatement.executeUpdate();
            }
            catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void addEmployee(String firstName, String lastName, Integer id_department, Integer id_position){
        if ((firstName != null) && (lastName != null) && (id_department != null) &&(id_position != null)) {
            String sql = "INSERT INTO EMPLOYEES (ID_EMPLOYEE, FIRST_NAME, LAST_NAME, ID_DEPARTMENT, ID_POSITION)\n" +
                "VALUES (NEXT VALUE FOR EMPLOYEE_SEQUENCE, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

                preparedStatement.setString(1, firstName);
                preparedStatement.setString(2, lastName);
                preparedStatement.setString(3, id_department.toString());
                preparedStatement.setString(4, id_position.toString());
                preparedStatement.executeUpdate();
            }
            catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void updatePositionName(String positionName, Position position){
        if ((positionName != null) && (position != null)) {
            String sql = "UPDATE POSITIONS\n" +
                    "SET POSITION_NAME = ?\n" +
                    "WHERE ID_POSITION = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

                preparedStatement.setString(1, positionName);
                preparedStatement.setString(2, position.getId().toString());
                preparedStatement.executeUpdate();
            }
            catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        }
    }

    public void updatePositionSalary(Float positionSalary, Position position){
        if ((positionSalary != null) && (position != null)) {
            String sql = "UPDATE POSITIONS\n" +
                    "SET SALARY = ?\n" +
                    "WHERE ID_POSITION = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

                preparedStatement.setString(1, positionSalary.toString());
                preparedStatement.setString(2, position.getId().toString());
                preparedStatement.executeUpdate();
            }
            catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        }
    }

    public void updateDepartmentName (String departmentName, Department department){
        if ((departmentName != null) && (department != null)) {
            String sql = "UPDATE DEPARTMENTS\n" +
                    "SET DEPARTMENT_NAME = ?\n" +
                    "WHERE ID_DEPARTMENT = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

                preparedStatement.setString(1, departmentName);
                preparedStatement.setString(2, department.getId().toString());
                preparedStatement.executeUpdate();
            }
            catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        }
    }

    public void updateHeadOfDepartment (String departmentHeadFullName, Department department){
        if ((departmentHeadFullName != null) && (department != null)) {
            String sql = "UPDATE DEPARTMENTS\n" +
                    "SET ID_HEAD_OF_DEPARTMENT = (SELECT ID_EMPLOYEE FROM EMPLOYEES WHERE FIRST_NAME = ? AND LAST_NAME = ?)\n" +
                    "WHERE ID_DEPARTMENT = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

                preparedStatement.setString(1, departmentHeadFullName.split("\\s+",2)[0]);
                preparedStatement.setString(2, departmentHeadFullName.split("\\s+",2)[1]);
                preparedStatement.setString(3, department.getId().toString());
                preparedStatement.executeUpdate();
            }
            catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        }
    }

    public void updateDepartmentPhone (String departmentPhone, Department department){
        if ((departmentPhone != null) && (department != null)) {
            String sql = "UPDATE DEPARTMENTS\n" +
                    "SET PHONE = ?\n" +
                    "WHERE ID_DEPARTMENT = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

                preparedStatement.setString(1, departmentPhone);
                preparedStatement.setString(2, department.getId().toString());
                preparedStatement.executeUpdate();
            }
            catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        }
    }

    public void updateDepartmentEmail (String departmentEmail, Department department){
        if ((departmentEmail != null) && (department != null)) {
            String sql = "UPDATE DEPARTMENTS\n" +
                    "SET EMAIL = ?\n" +
                    "WHERE ID_DEPARTMENT = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

                preparedStatement.setString(1, departmentEmail);
                preparedStatement.setString(2, department.getId().toString());
                preparedStatement.executeUpdate();
            }
            catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        }
    }

    public void updateEmployeeFirstName (String firstName, Employee employee){
        if ((firstName != null) && (employee != null)) {
            String sql = "UPDATE EMPLOYEES\n" +
                    "SET FIRST_NAME = ?\n" +
                    "WHERE ID_EMPLOYEE = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

                preparedStatement.setString(1, firstName);
                preparedStatement.setString(2, employee.getId().toString());
                preparedStatement.executeUpdate();
            }
            catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        }
    }

    public void updateEmployeeLastName (String lastName, Employee employee){
        if ((lastName != null) && (employee != null)) {
            String sql = "UPDATE EMPLOYEES\n" +
                    "SET LAST_NAME = ?\n" +
                    "WHERE ID_EMPLOYEE = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

                preparedStatement.setString(1, lastName);
                preparedStatement.setString(2, employee.getId().toString());
                preparedStatement.executeUpdate();
            }
            catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        }
    }

    public void updateEmployeeDepartment (String departmentName, Employee employee){
        if ((departmentName != null) && (employee != null)) {
            String sql = "UPDATE EMPLOYEES\n" +
                    "SET ID_DEPARTMENT = (SELECT ID_DEPARTMENT FROM DEPARTMENTS WHERE DEPARTMENT_NAME = ?)\n" +
                    "WHERE ID_EMPLOYEE = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

                preparedStatement.setString(1, departmentName);
                preparedStatement.setString(2, employee.getId().toString());
                preparedStatement.executeUpdate();
            }
            catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        }
    }

    public void updateEmployeePosition (String positionName, Employee employee){
        if ((positionName != null) && (employee != null)) {
            String sql = "UPDATE EMPLOYEES\n" +
                    "SET ID_POSITION = (SELECT ID_POSITION FROM POSITIONS WHERE POSITION_NAME = ?)\n" +
                    "WHERE ID_EMPLOYEE = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

                preparedStatement.setString(1, positionName);
                preparedStatement.setString(2, employee.getId().toString());
                preparedStatement.executeUpdate();
            }
            catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        }
    }

    public void deleteObject(Department department){
        if (department != null) {
            String sql = "DELETE FROM DEPARTMENTS\n" +
                    "WHERE ID_DEPARTMENT = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

                preparedStatement.setString(1, department.getId().toString());
                preparedStatement.executeUpdate();
            }
            catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        }
    }

    public void deleteObject(Employee employee){
        if (employee != null) {
            String sql = "DELETE FROM EMPLOYEES\n" +
                    "WHERE ID_EMPLOYEE = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

                preparedStatement.setString(1, employee.getId().toString());
                preparedStatement.executeUpdate();
            }
            catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        }
    }

    public void deleteObject(Position position){
        if (position != null) {
            String sql = "DELETE FROM POSITIONS\n" +
                    "WHERE ID_POSITION = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

                preparedStatement.setString(1, position.getId().toString());
                preparedStatement.executeUpdate();
            }
            catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        }
    }

}
