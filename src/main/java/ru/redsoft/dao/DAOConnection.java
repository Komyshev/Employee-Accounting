package ru.redsoft.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DAOConnection {
    private static DAOConnection instance;
    private Connection connection;

    private DAOConnection(){
        FileInputStream fis;
        Properties property = new Properties();
        try {
            fis = new FileInputStream("src/main/db.properties");
            property.load(fis);
            String url = property.getProperty("url");
            String login = property.getProperty("login");
            String password = property.getProperty("password");

            connection = DriverManager.getConnection(url, login, password);
        }
        catch (SQLException e) {
            System.out.println("По данному пути база данных не найдена");
        }
        catch (IOException e) {
            System.err.println("ОШИБКА: Файл свойств отсутствует!");
        }
    }

    public static DAOConnection getDAOConnection() {
        if (instance == null){
            instance = new DAOConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection(){
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
