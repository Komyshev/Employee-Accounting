package ru.redsoft.gui;

import ru.redsoft.dao.DAOConnection;
import ru.redsoft.dao.EmployeeAccountingDAO;
import ru.redsoft.datamodel.Position;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddPositionWindow extends JFrame {

    private JButton button = new JButton("Создать");
    private JTextField inputPositionName = new JTextField("", 20);
    private JTextField inputPositionSalary = new JTextField("", 20);

    private JLabel labelPositionName = new JLabel("Должность:");
    private JLabel labelPositionSalary = new JLabel("Зарплата:");



    public AddPositionWindow() {
        super("Создать должность");

        this.setBounds(100,100,250,100);

        Container container = this.getContentPane();
        container.setLayout(new GridLayout(3,2,2,2));
        container.add(labelPositionName);
        container.add(inputPositionName);
        container.add(labelPositionSalary);
        container.add(inputPositionSalary);


        button.addActionListener(new ButtonEventListener());
        container.add(button);
    }

    class ButtonEventListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            EmployeeAccountingDAO employeeAccountingDAO =
                    new EmployeeAccountingDAO(DAOConnection.getDAOConnection().getConnection());
            Position position = new Position(inputPositionName.getText(), new Float(inputPositionSalary.getText()));
            employeeAccountingDAO.addPosition(position);
            setVisible(false);
            dispose();
        }
    }
}
