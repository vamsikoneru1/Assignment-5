package hm2;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.*;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.mysql.jdbc.Driver;
import com.sun.net.httpserver.Authenticator.Failure;
import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;

import javax.faces.bean.ManagedBean;

@ManagedBean
@SessionScoped
public class request {
	
	Integer managerid;
	Integer userid;
	public Integer getManagerid() {
		return managerid;
	}
	public void setManagerid(Integer managerid) {
		this.managerid = managerid;
	}
	public Integer getUserid() {
		return userid;
	}
	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	
	public String request() {
		String res1="";
		Connection con=null;
		String s="";
		//if(this.email.contains("@") && this.firstname.matches("[A-Z][a-zA-Z]*") && (this.email.contains(".com")||this.email.contains(".edu")))
		{
		try {
			// Setup the DataSource object
			
			
			com.mysql.jdbc.jdbc2.optional.MysqlDataSource ds = new com.mysql.jdbc.jdbc2.optional.MysqlDataSource();
			ds.setServerName(System.getenv("ICSI518_SERVER"));
			ds.setPortNumber(Integer.parseInt(System.getenv("ICSI518_PORT")));
			ds.setDatabaseName(System.getenv("ICSI518_DB").toString());
			ds.setUser(System.getenv("ICSI518_USER").toString());
			ds.setPassword(System.getenv("ICSI518_PASSWORD").toString());
			
			
			con = ds.getConnection();

			
			String sql = "INSERT INTO request "+"(managerid, userid) values (?,?)";
			PreparedStatement st = con.prepareStatement(sql);
			st.setInt(1, managerid);
			st.setInt(2 ,userid);
	
			

			st.executeUpdate();
			s="true";
			System.out.println("Inserted");
			

			
		} catch (Exception e) {
			System.err.println("Exception: " + e.getMessage());
		} finally {
			try {
				if(con!=null)
					con.close();
			} catch (SQLException e) {
			}
		}
		
		
		
		//res1="login";
		return " Admindash?faces-redirect=true";
		
		
	}
		
	}
	public request(Integer managerid, Integer userid) {
		super();
		this.managerid = managerid;
		this.userid = userid;
	}
}
