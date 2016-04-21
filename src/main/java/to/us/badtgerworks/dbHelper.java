package to.us.badtgerworks;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class dbHelper {
	 String host = "localhost";
	 String port = "5432";
	 String db = "meta";
	 String user = "";
	
	public dbHelper(String user){
		this.user = user;
	}
	
	public void DriverRegistration() {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Driver .jar not found");
			e.printStackTrace();
			return;
		}
		System.out.println("DB connection ...");
	}
	public  Connection create(){
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
	
	public  void addPage(Domain d){
		int parent_id = d.getParent().getId();
		int id = -1;
		Connection c = create();
		Statement stmt = null;
		try {
			stmt = c.createStatement();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		ResultSet rs1 = null;
		String sql1 = "SELECT id FROM domain WHERE hostname = '"+ d.getHostname() +"' AND page = '"+d.getPage() + "';";
		//System.out.println(sql1);
		try {
			rs1 = stmt.executeQuery(sql1);
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			if(rs1.next()){
				id = rs1.getInt(1);
				d.setId(id);
				d.setToCrawl(0);
				String sql2 = "INSERT INTO link values ("+ d.getParent().getId() +","+ d.getId()+");";
				System.out.println(d.getParent().getUrl() + " ---> "+d.getHostname()+d.getPage());
				//System.out.println(sql2);
				stmt.executeQuery(sql2);
				return;
			}
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			return;
			//e2.printStackTrace();
		}
		String sql = "INSERT INTO domain (hostname, page) values ('"+d.getHostname()+"','"+d.getPage()+"') RETURNING id;";
		d.setToCrawl(1);
		System.out.println("[NEW] "+d.getHostname()+d.getPage());
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			if(rs.next()){
				id = rs.getInt(1);
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} //TODO remove hardcode
		d.setId(id);
		d.setToCrawl(1);
		String sql3 = "INSERT INTO link values ("+ d.getParent().getId() +","+ d.getId()+");";
		System.out.println(d.getParent().getUrl() + " ---> [NEW]"+d.getHostname()+d.getPage());
		//System.out.println(sql3);
		try {
			stmt.executeQuery(sql3);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			//e1.printStackTrace();
		}
		try {
			stmt.close();
			c.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
	}

}
