package hm2;
import hm2.*;
import java.util.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.omg.CORBA.PUBLIC_MEMBER;

import com.mysql.jdbc.Statement;
import stockapi.StockApiBean;

@ManagedBean
@SessionScoped
public class RegManager {
	
	

	List<RegManager> mang=new ArrayList<RegManager>();
	List<RegManager> mang1=new ArrayList<RegManager>();
	List<Login> login= new ArrayList<Login>();
	List<StockApiBean> managersell= new ArrayList<StockApiBean>();
	List<StockApiBean> managersellhistory= new ArrayList<StockApiBean>();
	List<StockApiBean> managerpurchasehistory= new ArrayList<StockApiBean>();
	List<RegManager> managerprofile= new ArrayList<RegManager>();
	//List<RegManager> manger=new ArrayList<RegManager>();
	

	

	Integer managerid;
	

	Integer mid;
	

	Double accountbalance;
	public Double getAccountbalance() {
		return accountbalance;
	}
	public void setAccountbalance(Double accountbalance) {
		this.accountbalance = accountbalance;
	}

	private String role;
	private Integer fee;
	private String password;
	public Integer getManagerid() {
		return managerid;
	}
	public void setManagerid(Integer managerid) {
		this.managerid = managerid;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public Integer getFee() {
		return fee;
	}
	public void setFee(Integer fee) {
		this.fee = fee;
	}

	private String email;
	private String firstname; 
	private String username; 
	
	
	
	public String log() {
		Connection con=null;
		String res="";
		if(this.username.equals(null)&& this.password.equals(null))
		{
				return "failure";
		}
		else
		{
		try {
			HttpSession hs= Util.getSession();
			
			// Setup the DataSource object
			System.out.println("Inside database");
			com.mysql.jdbc.jdbc2.optional.MysqlDataSource ds = new com.mysql.jdbc.jdbc2.optional.MysqlDataSource();
			ds.setServerName(System.getenv("ICSI518_SERVER"));
			ds.setPortNumber(Integer.parseInt(System.getenv("ICSI518_PORT")));
			ds.setDatabaseName(System.getenv("ICSI518_DB").toString());
			ds.setUser(System.getenv("ICSI518_USER").toString());
			ds.setPassword(System.getenv("ICSI518_PASSWORD").toString());
			
			
			con = ds.getConnection();
			
			String sql = "SELECT name,username,password,id,approved from manager where username= ?";
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, this.username);
			
			 
			
			ResultSet rs = st.executeQuery();
			String un="";
			String pass="";
			Integer app=0;
			
			while (rs.next()) {
				un=rs.getString("username");
				pass=rs.getString("password");
				app=rs.getInt("approved");
				mid=rs.getInt("id");
				this.firstname=rs.getString("name");
				if(app==1)
				{
				if(pass.equals(password)&& un.equals(username))
				{
					res="Manageraccount";
					FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("username", un);
	                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("managerid", mid);
					hs.setAttribute("username", username);
					
				}
				else
				{
					FacesMessage fm= new FacesMessage("incorrect username or password", "ERROR MESSAGE");
					fm.setSeverity(FacesMessage.SEVERITY_ERROR);
					FacesContext.getCurrentInstance().addMessage(null, fm);
					//res="failure";
					
				}
				}
				else
				{
					FacesMessage fm= new FacesMessage("Manager did not approve yet", "ERROR MESSAGE");
					fm.setSeverity(FacesMessage.SEVERITY_ERROR);
					FacesContext.getCurrentInstance().addMessage(null, fm);
				}
				
				
			}
			
		}catch (Exception e) {
			System.err.println("Exception: " + e.getMessage());
		} finally {
			try {
				if(con!=null)
					con.close();
			} catch (SQLException e) {

			}
			return res;
	}
		}
	}
	
	
	public String Regi() throws SQLException {
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

			
			String sql = "INSERT INTO manager "+"( name,username, email,id, password,role,fee,accountbalance,userid,approved) values (?,?,?,NULL,?,?,?,NULL,NULL,0)";
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, firstname);
			st.setString(2 ,username);
			st.setString(3, email);
			
			st.setString(4, password);
			st.setString(5, role);
			st.setInt(6, fee);
			
			

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
		return "ManagerRegSucc?faces-redirect=true";
		
		
	}
	}
		
	public String Upd() throws SQLException 
	{
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

			
			String sql = "update  manager set name=?,email=?,role=?,fee=? where username=?";
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, firstname);
			
			st.setString(2, email);
			
			
			st.setString(3, role);
			st.setInt(4, fee);
			
			st.setString(5, username);

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
		return "Manageraccount?faces-redirect=true";
		
		
	}
		
	}
	
	


	
	public List<RegManager> getMang() throws SQLException {
		mang.clear();
		Connection con = null;
		com.mysql.jdbc.jdbc2.optional.MysqlDataSource ds = new com.mysql.jdbc.jdbc2.optional.MysqlDataSource();
		ds.setServerName(System.getenv("ICSI518_SERVER"));
		ds.setPortNumber(Integer.parseInt(System.getenv("ICSI518_PORT")));
		ds.setDatabaseName(System.getenv("ICSI518_DB").toString());
		ds.setUser(System.getenv("ICSI518_USER").toString());
		ds.setPassword(System.getenv("ICSI518_PASSWORD").toString());
		
		
		con = ds.getConnection();

	
	
	
	
	//con = ds.getConnection();

	
	String sql = "select * from manager where approved='"+ 1 +"' ";
	
	//PreparedStatement st = con.prepareStatement(sql);
	Statement stm=null;
	stm=(Statement) con.createStatement();
	
	
	
	
	ResultSet rs=stm.executeQuery(sql);
	while(rs.next())
	{
		RegManager rm=new RegManager();
		rm.setFirstname(rs.getString("name"));
		rm.setUsername(rs.getString("username"));
		rm.setEmail(rs.getString("email"));
		rm.setManagerid(rs.getInt("id"));
		rm.setPassword(rs.getString("password"));
		rm.setRole(rs.getString("role"));
		rm.setFee(rs.getInt("fee"));
		//System.out.println(rs.getInt("id")+" " +rs.getString("name"));
		mang.add(rm);
		//System.out.println("hi");
		
		
		
		
		
		
		
	}
	con.close();
	stm.close();
	
	
	return mang;
	}
	
	
	
	
	
//	public List<RegManager> getManger() {
//		return manger;
//	}
	
	public List<RegManager> getMang1() throws SQLException {
		mang1.clear();
		Connection con = null;
		com.mysql.jdbc.jdbc2.optional.MysqlDataSource ds = new com.mysql.jdbc.jdbc2.optional.MysqlDataSource();
		ds.setServerName(System.getenv("ICSI518_SERVER"));
		ds.setPortNumber(Integer.parseInt(System.getenv("ICSI518_PORT")));
		ds.setDatabaseName(System.getenv("ICSI518_DB").toString());
		ds.setUser(System.getenv("ICSI518_USER").toString());
		ds.setPassword(System.getenv("ICSI518_PASSWORD").toString());
		
		
		con = ds.getConnection();

	
	
	
	
	//con = ds.getConnection();

	
	String sql = "select * from manager where approved='"+ 0 +"' ";
	
	//PreparedStatement st = con.prepareStatement(sql);
	Statement stm=null;
	stm=(Statement) con.createStatement();
	
	
	
	
	ResultSet rs=stm.executeQuery(sql);
	while(rs.next())
	{
		RegManager rm=new RegManager();
		rm.setFirstname(rs.getString("name"));
		rm.setUsername(rs.getString("username"));
		rm.setEmail(rs.getString("email"));
		rm.setManagerid(rs.getInt("id"));
		rm.setPassword(rs.getString("password"));
		rm.setRole(rs.getString("role"));
		rm.setFee(rs.getInt("fee"));
		//System.out.println(rs.getInt("id")+" " +rs.getString("name"));
		mang1.add(rm);
		//System.out.println("hi");
		
		
		
		
		
		
		
	}
	con.close();
	stm.close();
	
	
	return mang1;
		
	}
	
	
	
	public String approve() throws SQLException 
	{
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

			
			String sql = "update  manager set approved= '"+ 1 +"' where id='" + managerid +"'";
			PreparedStatement st = con.prepareStatement(sql);
			

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
		return "Admindash?faces-redirect=true";
		
		
	}
		
	}
	
public List<Login> getLogin() throws SQLException {
		
		login.clear();
		
		Connection con = null;
		com.mysql.jdbc.jdbc2.optional.MysqlDataSource ds = new com.mysql.jdbc.jdbc2.optional.MysqlDataSource();
		ds.setServerName(System.getenv("ICSI518_SERVER"));
		ds.setPortNumber(Integer.parseInt(System.getenv("ICSI518_PORT")));
		ds.setDatabaseName(System.getenv("ICSI518_DB").toString());
		ds.setUser(System.getenv("ICSI518_USER").toString());
		ds.setPassword(System.getenv("ICSI518_PASSWORD").toString());
		con = ds.getConnection();
		Integer uid = (Integer) ( FacesContext.getCurrentInstance()
                .getExternalContext()
                .getSessionMap().get("userid"));
		
		String sql = "select userid,amount from request where managerid ='" + mid + "' ";
		PreparedStatement st = con.prepareStatement(sql);
		
		
		
		
		login.clear();
		ResultSet rs=st.executeQuery(sql);
		while(rs.next())
		{
			
			Login log=new Login();
			log.setUserid(rs.getInt("userid"));
			log.setQty(rs.getInt("amount"));
			
			
			login.add(log);
		
	}
		return login;
		
}


public List<StockApiBean> getManagersell() throws SQLException {
	managersell.clear();
	
	Connection con = null;
	com.mysql.jdbc.jdbc2.optional.MysqlDataSource ds = new com.mysql.jdbc.jdbc2.optional.MysqlDataSource();
	ds.setServerName(System.getenv("ICSI518_SERVER"));
	ds.setPortNumber(Integer.parseInt(System.getenv("ICSI518_PORT")));
	ds.setDatabaseName(System.getenv("ICSI518_DB").toString());
	ds.setUser(System.getenv("ICSI518_USER").toString());
	ds.setPassword(System.getenv("ICSI518_PASSWORD").toString());
	con = ds.getConnection();
	
	
	String sql = "select * from purchase where managerid ='" + mid + "' and pors=0 and sell=1";
	PreparedStatement st = con.prepareStatement(sql);
	
	
	
	
	managersell.clear();
	ResultSet rs=st.executeQuery(sql);
	
	while(rs.next())
	{
		
		StockApiBean stoc=new StockApiBean();
		stoc.setPid(rs.getInt("id"));
		stoc.setUseridentity(rs.getInt("uid"));
		
		stoc.setAmt(rs.getDouble("amt"));
		stoc.setPrice(rs.getDouble("price"));
		stoc.setQty(rs.getInt("qty"));
		stoc.setSymbol(rs.getString("stock_symbol"));
		
		managersell.add(stoc);
		
		
		System.out.println(rs.getDouble("amt")+" " +rs.getDouble("price")+""+rs.getString("id"));
		
		
		
		
		
		
		
		
	}
	
	
	
	con.close();
	st.close();
	
	return managersell;

}




public List<StockApiBean> getManagersellhistory() throws SQLException {
	
	managersellhistory.clear();
	Connection con = null;
	com.mysql.jdbc.jdbc2.optional.MysqlDataSource ds = new com.mysql.jdbc.jdbc2.optional.MysqlDataSource();
	ds.setServerName(System.getenv("ICSI518_SERVER"));
	ds.setPortNumber(Integer.parseInt(System.getenv("ICSI518_PORT")));
	ds.setDatabaseName(System.getenv("ICSI518_DB").toString());
	ds.setUser(System.getenv("ICSI518_USER").toString());
	ds.setPassword(System.getenv("ICSI518_PASSWORD").toString());
	
	
	con = ds.getConnection();





//con = ds.getConnection();


String sql = "select * from purchase where managerid='"+ mid +"' and pors=1 and sell=1 ";

//PreparedStatement st = con.prepareStatement(sql);
Statement stm=null;
stm=(Statement) con.createStatement();




ResultSet rs=stm.executeQuery(sql);
while(rs.next())
{
	StockApiBean stoc=new StockApiBean();
	stoc.setPid(rs.getInt("id"));
	stoc.setAmt(rs.getDouble("amt"));
	stoc.setPrice(rs.getDouble("price"));
	stoc.setQty(rs.getInt("qty"));
	stoc.setSymbol(rs.getString("stock_symbol"));
	stoc.setUseridentity(rs.getInt("uid"));
	managersellhistory.add(stoc);
	
	
	
	
	
	
}
con.close();
stm.close();


	
	return managersellhistory;
}
public List<StockApiBean> getManagerpurchasehistory() throws SQLException {
	
	managerpurchasehistory.clear();
	Connection con = null;
	com.mysql.jdbc.jdbc2.optional.MysqlDataSource ds = new com.mysql.jdbc.jdbc2.optional.MysqlDataSource();
	ds.setServerName(System.getenv("ICSI518_SERVER"));
	ds.setPortNumber(Integer.parseInt(System.getenv("ICSI518_PORT")));
	ds.setDatabaseName(System.getenv("ICSI518_DB").toString());
	ds.setUser(System.getenv("ICSI518_USER").toString());
	ds.setPassword(System.getenv("ICSI518_PASSWORD").toString());
	
	
	con = ds.getConnection();





//con = ds.getConnection();


String sql = "select * from purchase where managerid='"+ mid +"' and pors=0 and sell=0 ";

//PreparedStatement st = con.prepareStatement(sql);
Statement stm=null;
stm=(Statement) con.createStatement();




ResultSet rs=stm.executeQuery(sql);
while(rs.next())
{
	StockApiBean stoc=new StockApiBean();
	stoc.setPid(rs.getInt("id"));
	stoc.setAmt(rs.getDouble("amt"));
	stoc.setPrice(rs.getDouble("price"));
	stoc.setQty(rs.getInt("qty"));
	stoc.setSymbol(rs.getString("stock_symbol"));
	stoc.setUseridentity(rs.getInt("uid"));
	managerpurchasehistory.add(stoc);
	
	
	
	
	
	
}
con.close();
stm.close();


	return managerpurchasehistory;
}
public List<RegManager> getManagerprofile() throws SQLException {
		
	managerprofile.clear();
	Connection con = null;
	com.mysql.jdbc.jdbc2.optional.MysqlDataSource ds = new com.mysql.jdbc.jdbc2.optional.MysqlDataSource();
	ds.setServerName(System.getenv("ICSI518_SERVER"));
	ds.setPortNumber(Integer.parseInt(System.getenv("ICSI518_PORT")));
	ds.setDatabaseName(System.getenv("ICSI518_DB").toString());
	ds.setUser(System.getenv("ICSI518_USER").toString());
	ds.setPassword(System.getenv("ICSI518_PASSWORD").toString());
	
	
	con = ds.getConnection();





//con = ds.getConnection();


String sql = "select * from manager where id='"+ mid +"' ";

//PreparedStatement st = con.prepareStatement(sql);
Statement stm=null;
stm=(Statement) con.createStatement();




ResultSet rs=stm.executeQuery(sql);
while(rs.next())
{
	RegManager rm=new RegManager();
	rm.setFirstname(rs.getString("name"));
	rm.setUsername(rs.getString("username"));
	rm.setEmail(rs.getString("email"));
	rm.setManagerid(rs.getInt("id"));
	rm.setPassword(rs.getString("password"));
	rm.setRole(rs.getString("role"));
	rm.setFee(rs.getInt("fee"));
	rm.setAccountbalance(rs.getDouble("accountbalance"));
	//System.out.println(rs.getInt("id")+" " +rs.getString("name"));
	managerprofile.add(rm);
	//System.out.println("hi");
	
	
	
	
	
	
	
}
con.close();
stm.close();


	
	return managerprofile;
}
public void logout() throws IOException {
	
	FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
    FacesContext.getCurrentInstance().getExternalContext().redirect("front.xhtml");
//	HttpSession hs=Util.getSession();
//	
//	
//	hs.invalidate();
//	return "front";
}

}
