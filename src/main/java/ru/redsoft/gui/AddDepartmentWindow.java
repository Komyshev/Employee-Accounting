package ru.redsoft.gui;

import ru.redsoft.dao.DAOConnection;
import ru.redsoft.dao.EmployeeAccountingDAO;
import ru.redsoft.datamodel.Department;
import ru.redsoft.datamodel.Employee;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;


public class AddDepartmentWindow extends JFrame {




    private JButton button = new JButton("Создать");
    private JTextField inputDepartmentName = new JTextField("", 20);
    private JComboBox comboBoxHeadOfDepartment;
    private JTextField inputDepartmentPhone = new JTextField("", 20);
    private JTextField inputDepartmentEmail = new JTextField("", 20);


    private JLabel labelВepartmentName = new JLabel("Назваание отдела:");
    private JLabel labelHeadOfDepartment = new JLabel("Начальник отдела:");
    private JLabel labelDepartmentPhone = new JLabel("Телефон:");
    private JLabel labelDepartmentEmail = new JLabel("Электронная почта:");



    public AddDepartmentWindow() {
        super("Создать отдел");

        this.setBounds(100,100,450,200);

        Container container = this.getContentPane();
        container.setLayout(new GridLayout(5,2,2,2));


        container.add(labelВepartmentName);
        container.add(inputDepartmentName);
        container.add(labelHeadOfDepartment);

        EmployeeAccountingDAO employeeAccountingDAO =
                new EmployeeAccountingDAO(DAOConnection.getDAOConnection().getConnection());

        List<Employee> allEmployees = employeeAccountingDAO.getAllEmployees();
        List<String> allEmployeesNames = new ArrayList<String>();

        for (Employee employee: allEmployees){
            allEmployeesNames.add(employee.getFirstName() + ' ' + employee.getLastName());
        }

        comboBoxHeadOfDepartment = new JComboBox(allEmployeesNames.toArray());

        container.add(comboBoxHeadOfDepartment);
        container.add(labelDepartmentPhone);
        container.add(inputDepartmentPhone);
        container.add(labelDepartmentEmail);
        container.add(inputDepartmentEmail);


        button.addActionListener(new ButtonEventListener());
        container.add(button);
    }

    class ButtonEventListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            EmployeeAccountingDAO employeeAccountingDAO =
                    new EmployeeAccountingDAO(DAOConnection.getDAOConnection().getConnection());
            Department department = new Department(inputDepartmentName.getText(), (String) comboBoxHeadOfDepartment.getSelectedItem(),
                    inputDepartmentPhone.getText(), inputDepartmentEmail.getText());


                employeeAccountingDAO.addDepartment(department);



            setVisible(false);
            dispose();
        }
    }
}
