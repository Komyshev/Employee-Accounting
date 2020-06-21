package ru.redsoft.gui;

import ru.redsoft.dao.DAOConnection;
import ru.redsoft.dao.EmployeeAccountingDAO;
import ru.redsoft.datamodel.Department;
import ru.redsoft.datamodel.Employee;
import ru.redsoft.datamodel.Position;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class GUI extends JFrame implements ItemListener{

    private static JPanel cards;
    private String currentTable = "Отделы";



    Connection connection = DAOConnection.getDAOConnection().getConnection();

    EmployeeAccountingDAO employeeAccountingDAO = new EmployeeAccountingDAO(connection);

    final   static String DEPARTMENTPANEL = "Отделы"       ;
    final   static String EMPLOYEEPANEL   = "Сотрудники";
    final   static String POSITIONPANEL = "Должности"       ;

    Container container = this.getContentPane();
    private TableModel departmentsModel = new DepartmentsTableModel(employeeAccountingDAO.getAllDepartments());
    private JTable departmentsTable = new JTable(departmentsModel);
    private TableModel employeesModel = new EmployeesTableModel(employeeAccountingDAO.getAllEmployees());
    private JTable employeesTable = new JTable(employeesModel);
    private TableModel positionsModel = new PositionsTableModel(employeeAccountingDAO.getAllPositions());
    private JTable positionsTable = new JTable(positionsModel);


    public GUI() {
        super("EmployeeAccounting");
        this.setBounds(100,100,900,600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                DAOConnection.getDAOConnection().closeConnection();
            }
        });


        container.setLayout(new BorderLayout());

        JComboBox<String> combobox = new JComboBox<String>(
                new String[] {DEPARTMENTPANEL, EMPLOYEEPANEL, POSITIONPANEL});
        combobox.setEditable    (false);
        combobox.addItemListener(this);

        JButton addPositionButton = new JButton("Добавить должность");
        addPositionButton.addActionListener(new AddPositionButtonEventListener());

        JButton addEmployeeButton = new JButton("Добавить сотрудника");
        addEmployeeButton.addActionListener(new AddEmployeeButtonEventListener());

        JButton addDepartmentButton = new JButton("Добавить отдел");
        addDepartmentButton.addActionListener(new AddDepartmentButtonEventListener());

        JButton refreshButton = new JButton("Обновить");
        refreshButton.addActionListener(new RefreshButtonEventListener());

        JButton deleteButton = new JButton("Удалить");
        deleteButton.addActionListener(new DeleteButtonEventListener());


        JPanel cbPanel = new JPanel();
        cbPanel.add(combobox);
        cbPanel.add(addPositionButton);
        cbPanel.add(addEmployeeButton);
        cbPanel.add(addDepartmentButton);
        cbPanel.add(refreshButton);
        cbPanel.add(deleteButton);

        JPanel departmentsPanel = new JPanel();
        departmentsPanel.setLayout(new BoxLayout(departmentsPanel, BoxLayout.Y_AXIS));


        List<Employee> allEmployees = employeeAccountingDAO.getAllEmployees();
        List<String> allEmployeesNames = new ArrayList<String>();

        for (Employee employee: allEmployees){
            allEmployeesNames.add(employee.getFirstName() + ' ' + employee.getLastName());
        }

        JComboBox comboBoxHeadOfDepartment = new JComboBox(allEmployeesNames.toArray());
        DefaultCellEditor editorHeadOfDepartment = new DefaultCellEditor(comboBoxHeadOfDepartment);
        departmentsTable.getColumnModel().getColumn(1).setCellEditor(editorHeadOfDepartment);
        departmentsTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        departmentsPanel.add(new JScrollPane(departmentsTable));


        JPanel employeesPanel = new JPanel();
        employeesPanel.setLayout(new BoxLayout(employeesPanel, BoxLayout.Y_AXIS));
        HashMap<Integer, String> departmentsMap = new HashMap<>();
        HashMap<Integer, String> positionMap = new HashMap<>();
        for (Department department: employeeAccountingDAO.getAllDepartments()){
            departmentsMap.put(department.getId(), department.getDepartmentName());
        }

        for (Position position: employeeAccountingDAO.getAllPositions()){
            positionMap.put(position.getId(), position.getPositionName());
        }

        JComboBox comboBoxDepartment = new JComboBox(departmentsMap.values().toArray());
        JComboBox comboBoxPosition = new JComboBox(positionMap.values().toArray());

        DefaultCellEditor editorEmployeeDepartment = new DefaultCellEditor(comboBoxDepartment);
        DefaultCellEditor editorEmployeePosition = new DefaultCellEditor(comboBoxPosition);
        employeesTable.getColumnModel().getColumn(2).setCellEditor(editorEmployeeDepartment);
        employeesTable.getColumnModel().getColumn(3).setCellEditor(editorEmployeePosition);
        employeesTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);




        employeesPanel.add(new JScrollPane(employeesTable));


        JPanel positionsPanel = new JPanel();
        positionsTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        positionsPanel.setLayout(new BoxLayout(positionsPanel, BoxLayout.Y_AXIS));

        positionsPanel.add(new JScrollPane(positionsTable));




        cards = new JPanel(new CardLayout());

        cards.add(departmentsPanel, DEPARTMENTPANEL);
        cards.add(employeesPanel, EMPLOYEEPANEL);
        cards.add(positionsPanel, POSITIONPANEL);

        container.add(cbPanel, BorderLayout.NORTH);
        container.add(cards  , BorderLayout.CENTER);
    }

    public void itemStateChanged(ItemEvent event)
    {
        CardLayout layout = (CardLayout)(cards.getLayout());
        layout.show(cards, (String)event.getItem());
        currentTable = (String)event.getItem();

    }

    class AddPositionButtonEventListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            AddPositionWindow addPositionWindow = new AddPositionWindow();
            addPositionWindow.setVisible(true);
        }
    }

    class AddEmployeeButtonEventListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            AddEmployeeWindow addEmployeeWindow = new AddEmployeeWindow();
            addEmployeeWindow.setVisible(true);
        }
    }

    class AddDepartmentButtonEventListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            AddDepartmentWindow addDepartmentWindow = new AddDepartmentWindow();
            addDepartmentWindow.setVisible(true);
        }
    }

    class RefreshButtonEventListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            positionsModel = new PositionsTableModel(employeeAccountingDAO.getAllPositions());
            positionsTable.setModel(positionsModel);

            departmentsModel = new DepartmentsTableModel(employeeAccountingDAO.getAllDepartments());
            List<Employee> allEmployees = employeeAccountingDAO.getAllEmployees();
            List<String> allEmployeesNames = new ArrayList<String>();

            for (Employee employee: allEmployees){
                allEmployeesNames.add(employee.getFirstName() + ' ' + employee.getLastName());
            }

            JComboBox comboBoxHeadOfDepartment = new JComboBox(allEmployeesNames.toArray());
            DefaultCellEditor editorHeadOfDepartment = new DefaultCellEditor(comboBoxHeadOfDepartment);
            departmentsTable.setModel(departmentsModel);
            departmentsTable.getColumnModel().getColumn(1).setCellEditor(editorHeadOfDepartment);

            employeesModel = new EmployeesTableModel(employeeAccountingDAO.getAllEmployees());
            HashMap<Integer, String> departmentsMap = new HashMap<>();
            HashMap<Integer, String> positionMap = new HashMap<>();
            for (Department department: employeeAccountingDAO.getAllDepartments()){
                departmentsMap.put(department.getId(), department.getDepartmentName());
            }

            for (Position position: employeeAccountingDAO.getAllPositions()){
                positionMap.put(position.getId(), position.getPositionName());
            }

            JComboBox comboBoxDepartment = new JComboBox(departmentsMap.values().toArray());
            JComboBox comboBoxPosition = new JComboBox(positionMap.values().toArray());

            DefaultCellEditor editorEmployeeDepartment = new DefaultCellEditor(comboBoxDepartment);
            DefaultCellEditor editorEmployeePosition = new DefaultCellEditor(comboBoxPosition);
            employeesTable.setModel(employeesModel);
            employeesTable.getColumnModel().getColumn(2).setCellEditor(editorEmployeeDepartment);
            employeesTable.getColumnModel().getColumn(3).setCellEditor(editorEmployeePosition);
        }
    }

    class DeleteButtonEventListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JTable table = departmentsTable;
            Object objectForDelete = null;
            EmployeeAccountingDAO employeeAccountingDAO =
                    new EmployeeAccountingDAO(DAOConnection.getDAOConnection().getConnection());

            switch (currentTable){
                case "Отделы":
                    table = departmentsTable;
                    DepartmentsTableModel departmentsTableModel = (DepartmentsTableModel) departmentsModel;
                    if (table.getSelectedRows().length > 0) {
                        employeeAccountingDAO.deleteObject(departmentsTableModel.departments.get(table.getSelectedRows()[0]));
                    }
                    break;
                case "Сотрудники":
                    table = employeesTable;
                    EmployeesTableModel employeesTableModel = (EmployeesTableModel) employeesModel;
                    if (table.getSelectedRows().length > 0) {
                        employeeAccountingDAO.deleteObject(employeesTableModel.employees.get(table.getSelectedRows()[0]));
                    }
                    break;
                case "Должности":
                    table = positionsTable;
                    PositionsTableModel positionsTableModel = (PositionsTableModel) positionsModel;
                    if (table.getSelectedRows().length > 0) {
                        employeeAccountingDAO.deleteObject(positionsTableModel.positions.get(table.getSelectedRows()[0]));
                    }
                    break;
            }
//            if (table.getSelectedRows().length > 0) {
//                ((DefaultTableModel) table.getModel()).removeRow(table.getSelectedRows()[0]);
//            }
        }
    }

    public static void main(String[] args) {
        GUI app = new GUI();
        app.setVisible(true);
    }
}
