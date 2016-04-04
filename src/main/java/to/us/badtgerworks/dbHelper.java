package to.us.badtgerworks;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
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
	
	public static void addPage(Domain d){
		int parent_id = d.getParent().getId();
		Connection c = create();
		Statement stmt = null;
		try {
			stmt = c.createStatement();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String sql = "INSERT INTO domain (hostname, page, bk_link) values ('"+d.getHostname()+"','"+d.getPage()+"','"+parent_id+"') RETURNING id;";
		System.out.println(sql);
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int id = -1;
		try {
			if(rs.next()){
				id = rs.getInt(1);
			}			
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} //TODO remove hardcode
		d.setId(id);
		try {
			stmt.close();
			c.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
				
	}

}
