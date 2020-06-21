package ru.redsoft.gui;

import ru.redsoft.dao.DAOConnection;
import ru.redsoft.dao.EmployeeAccountingDAO;
import ru.redsoft.datamodel.Employee;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EmployeesTableModel implements TableModel {

    private Set<TableModelListener> listeners = new HashSet<TableModelListener>();

    public List<Employee> employees;

    public EmployeesTableModel(List<Employee> employees) {
        this.employees = employees;
    }

    public void addTableModelListener(TableModelListener listener) {
        listeners.add(listener);
    }

    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    public int getColumnCount() {
        return 4;
    }

    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return "Имя";
            case 1:
                return "Фамилия";
            case 2:
                return "Отдел";
            case 3:
                return "Должность";
        }
        return "";
    }

    public int getRowCount() {
        return employees.size();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Employee employee = employees.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return employee.getFirstName();
            case 1:
                return employee.getLastName();
            case 2:
                return employee.getDepartmentName();
            case 3:
                return employee.getPositionName();
        }
        return "";
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    public void removeTableModelListener(TableModelListener listener) {
        listeners.remove(listener);
    }

    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        EmployeeAccountingDAO employeeAccountingDAO =
                new EmployeeAccountingDAO(DAOConnection.getDAOConnection().getConnection());

        switch (columnIndex) {
            case 0:
                employeeAccountingDAO.updateEmployeeFirstName(value.toString(), employees.get(rowIndex));
                employees.get(rowIndex).setFirstName(value.toString());
                break;
            case 1:
                employeeAccountingDAO.updateEmployeeLastName(value.toString(), employees.get(rowIndex));
                employees.get(rowIndex).setLastName(value.toString());
                break;
            case 2:
                employeeAccountingDAO.updateEmployeeDepartment(value.toString(), employees.get(rowIndex));
                employees.get(rowIndex).setDepartmentName(value.toString());
                break;
            case 3:
                employeeAccountingDAO.updateEmployeePosition(value.toString(), employees.get(rowIndex));
                employees.get(rowIndex).setPositionName(value.toString());
                break;
        }

    }

}



