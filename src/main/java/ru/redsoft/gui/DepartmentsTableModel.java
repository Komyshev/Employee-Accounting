package ru.redsoft.gui;

import ru.redsoft.dao.DAOConnection;
import ru.redsoft.dao.EmployeeAccountingDAO;
import ru.redsoft.datamodel.Department;


import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DepartmentsTableModel implements TableModel {

    private Set<TableModelListener> listeners = new HashSet<TableModelListener>();

    public List<Department> departments;

    public DepartmentsTableModel(List<Department> departments) {
        this.departments = departments;
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
                return "Отдел";
            case 1:
                return "Начальник отдела";
            case 2:
                return "Телефон";
            case 3:
                return "Электронная почта";
        }
        return "";
    }

    public int getRowCount() {
        return departments.size();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Department department = departments.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return department.getDepartmentName();
            case 1:
                return department.getDepartmentHeadFullName();
            case 2:
                return department.getPhone();
            case 3:
                return department.getEmail();
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
                employeeAccountingDAO.updateDepartmentName(value.toString(), departments.get(rowIndex));
                departments.get(rowIndex).setDepartmentName(value.toString());
                break;
            case 1:
                employeeAccountingDAO.updateHeadOfDepartment(value.toString(), departments.get(rowIndex));
                departments.get(rowIndex).setDepartmentHeadFullName(value.toString());
                break;
            case 2:
                employeeAccountingDAO.updateDepartmentPhone(value.toString(), departments.get(rowIndex));
                departments.get(rowIndex).setPhone(value.toString());
                break;
            case 3:
                employeeAccountingDAO.updateDepartmentEmail(value.toString(), departments.get(rowIndex));
                departments.get(rowIndex).setEmail(value.toString());
                break;
        }
    }

}



