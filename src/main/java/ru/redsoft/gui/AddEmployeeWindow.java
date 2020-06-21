package ru.redsoft.gui;

import ru.redsoft.dao.DAOConnection;
import ru.redsoft.dao.EmployeeAccountingDAO;
import ru.redsoft.datamodel.Department;
import ru.redsoft.datamodel.Employee;
import ru.redsoft.datamodel.Position;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddEmployeeWindow extends JFrame {
    private JButton button = new JButton("Добавить");
    private JTextField inputEmployeeFirstName = new JTextField("", 20);
    private JTextField inputEmployeeLastName = new JTextField("", 20);
    private JComboBox comboBoxDepartment;
    private JComboBox comboBoxPosition;


    private JLabel labelEmployeeFirstName = new JLabel("Имя:");
    private JLabel labelEmployeeLastName = new JLabel("Фамилия:");
    private JLabel labelEmployeeDepartment = new JLabel("Отдел:");
    private JLabel labelEmployeePosition = new JLabel("Должность:");

    private HashMap<Integer, String> departmentsMap;
    private HashMap<Integer, String> positionMap;



    public AddEmployeeWindow() {
        super("Добавить сотрудника");

        this.setBounds(100,100,450,200);

        Container container = this.getContentPane();
        container.setLayout(new GridLayout(5,2,2,2));


        container.add(labelEmployeeFirstName);
        container.add(inputEmployeeFirstName);
        container.add(labelEmployeeLastName);
        container.add(inputEmployeeLastName);

        EmployeeAccountingDAO employeeAccountingDAO =
                new EmployeeAccountingDAO(DAOConnection.getDAOConnection().getConnection());

        departmentsMap = new HashMap<>();
        for (Department department: employeeAccountingDAO.getAllDepartments()){
            departmentsMap.put(department.getId(), department.getDepartmentName());
        }

        comboBoxDepartment = new JComboBox(departmentsMap.values().toArray());

        container.add(labelEmployeeDepartment);
        container.add(comboBoxDepartment);

        positionMap = new HashMap<>();
        for (Position position: employeeAccountingDAO.getAllPositions()){
            positionMap.put(position.getId(), position.getPositionName());
        }

        comboBoxPosition = new JComboBox(positionMap.values().toArray());

        container.add(labelEmployeePosition);
        container.add(comboBoxPosition);

        button.addActionListener(new AddEmployeeWindow.ButtonEventListener());
        container.add(button);
    }

    class ButtonEventListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            EmployeeAccountingDAO employeeAccountingDAO =
                    new EmployeeAccountingDAO(DAOConnection.getDAOConnection().getConnection());

            Integer id_department = null, id_position = null;
            for(Map.Entry entry: departmentsMap.entrySet()){
                if (entry.getValue().equals((String) comboBoxDepartment.getSelectedItem())){
                    id_department = (Integer) entry.getKey();
                }

            }

            for(Map.Entry entry: positionMap.entrySet()){
                if (entry.getValue().equals((String) comboBoxPosition.getSelectedItem())){
                    id_position = (Integer) entry.getKey();
                }

            }


            employeeAccountingDAO.addEmployee(inputEmployeeFirstName.getText(),
                    inputEmployeeLastName.getText(), id_department, id_position);



            setVisible(false);
            dispose();
        }
    }


}
