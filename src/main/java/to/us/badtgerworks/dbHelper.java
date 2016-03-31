package to.us.badtgerworks;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class dbHelper {
	static String host = "localhost";
	static String port = "5432";
	static String db = "meta";
	static String user = "postgres";
	
	public static void DriverRegistration() {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Driver .jar not found");
			e.printStackTrace();
			return;
		}
		System.out.println("PostgreSQL JDBC Driver Registered!");
	}
	public static Connection create(){
		//String url = "?"+user;
		//String url = "jdbc:postgresql://localhost/test?user=fred&password=secret&ssl=true";
		Connection conn = null;
		try {
			 conn = DriverManager.getConnection("jdbc:postgresql://"+host+":"+port+"/"+db,user,"");
		} catch (SQLException e) {
			DriverRegistration();
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}
	
	public void closeConnection(Connection c){
		try {
			c.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void closeStatement(Statement stmt){
		try {
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
