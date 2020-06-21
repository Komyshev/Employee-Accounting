package ru.redsoft.gui;

import ru.redsoft.dao.DAOConnection;
import ru.redsoft.dao.EmployeeAccountingDAO;
import ru.redsoft.datamodel.Position;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PositionsTableModel implements TableModel {

    private Set<TableModelListener> listeners = new HashSet<TableModelListener>();

    public List<Position> positions;

    public PositionsTableModel(List<Position> positions) {
        this.positions = positions;
    }

    public void addTableModelListener(TableModelListener listener) {
        listeners.add(listener);
    }

    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return String.class;
            case 1:
                return Float.class;
        }
        return String.class;
    }

    public int getColumnCount() {
        return 2;
    }

    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return "Должность";
            case 1:
                return "Зарплата";
        }
        return "";
    }

    public int getRowCount() {
        return positions.size();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Position position = positions.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return position.getPositionName();
            case 1:
                return position.getSalary();
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
                employeeAccountingDAO.updatePositionName(value.toString(), positions.get(rowIndex));
                positions.get(rowIndex).setPositionName(value.toString());
                break;
            case 1:
                employeeAccountingDAO.updatePositionSalary(new Float (value.toString()), positions.get(rowIndex));
                positions.get(rowIndex).setSalary((Float) value);
                break;
        }

    }

}


