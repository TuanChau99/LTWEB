package vn.iotstar.configs;

import java.sql.Connection;
import java.sql.DriverManager;

import vn.iotstar.models.Category;

public class DBConnect {
    private final String serverName = "localhost\\TUAN";
    private final String dbName = "LTWEB";
    private final String userID = "sa";
    private final String password = "123456";

    public Connection getConnection() throws Exception {
        String url = "jdbc:sqlserver://" + serverName
                   + ";databaseName=" + dbName
                   + ";encrypt=true;trustServerCertificate=true;";
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        return DriverManager.getConnection(url, userID, password);
    }

    public static void main(String[] args) {
        try {
            System.out.println(new DBConnect().getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	public void insert(Category cate) {
		// TODO Auto-generated method stub

	}
}
