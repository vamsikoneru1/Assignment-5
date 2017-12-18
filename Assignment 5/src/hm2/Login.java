package hm2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.sql.*;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.mysql.jdbc.Driver;
import com.mysql.jdbc.Statement;
import com.sun.faces.config.DbfFactory;
import com.sun.net.httpserver.Authenticator.Failure;
import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;

import stockapi.StockApiBean;

//import stockapi.StockApiBean;

import javax.faces.bean.ManagedBean;

@ManagedBean
@SessionScoped
public class Login {
	
	List<StockApiBean> stock= new ArrayList<StockApiBean>();
	List<StockApiBean> soldstock= new ArrayList<StockApiBean>();
	List<StockApiBean> stockrequest= new ArrayList<StockApiBean>();
	List<RegManager> reqmanager = new ArrayList<RegManager>();
	List<Login> details =new ArrayList<Login>();
	
	


	Integer userid;
	Double accountbalance;
	Integer managerid;
	int uid;
	Integer pid;
	Integer qty;

	private String password;
	private String email;
	private String firstname; 
	private String username; 
	
	RegManager man;
	


	public Integer getQty() {
		return qty;
	}



	public void setQty(Integer qty) {
		this.qty = qty;
	}



	public Integer getPid() {
		return pid;
	}



	public void setPid(Integer pid) {
		this.pid = pid;
	}



	public Integer getManagerid() {
		return managerid;
	}



	public void setManagerid(Integer managerid) {
		this.managerid = managerid;
	}



	public Double getAccountbalance() {
		return accountbalance;
	}



	public void setAccountbalance(Double accountbalance) {
		this.accountbalance = accountbalance;
	}



	
	

	
			
	public Integer getUserid() {
		return userid;
	}



	public void setUserid(Integer userid) {
		this.userid = userid;
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
	
	public Integer getuid()
	{
		return userid;
	}
	
	public String login() {
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
			
			String sql = "SELECT name,username,password,userid from reg where username= ?";
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, this.username);
			
			 
			
			ResultSet rs = st.executeQuery();
			String un="";
			String pass="";
			
			
			while (rs.next()) {
				un=rs.getString("username");
				pass=rs.getString("password");
				uid=rs.getInt("userid");	
				this.firstname=rs.getString("name");
				
				if(pass.equals(password)&& un.equals(username))
				{
					res="sucess";
					FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("username", un);
	                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("userid", uid);
					//hs.setAttribute("username", username);
					System.out.println(userid);
				}
				else
				{
					FacesMessage fm= new FacesMessage("incorrect username or password", "ERROR MESSAGE");
					fm.setSeverity(FacesMessage.SEVERITY_ERROR);
					FacesContext.getCurrentInstance().addMessage(null, fm);
					//res="failure";
					
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

	public List<Login> getDetails() throws SQLException {
		details.clear();
		
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
		
		String sql = "select * from reg where userid ='" + uid + "'";
		PreparedStatement st = con.prepareStatement(sql);
		
		
		
		
		details.clear();
		ResultSet rs=st.executeQuery(sql);
		
		while(rs.next())
		{
			
			Login det=new Login();
			det.setFirstname(rs.getString("name"));
			det.setUsername(rs.getString("username"));
			det.setUserid(uid);
			det.setAccountbalance(rs.getDouble("accountbalance"));
			det.setEmail(rs.getString("email"));
			det.setManagerid(rs.getInt("managerid"));
			
			details.add(det);
			
			
			//System.out.println(rs.getDouble("amt")+" " +rs.getDouble("price")+""+rs.getString("id"));
			
			
			
			
			
			
			
			
		}
		
		
		
		con.close();
		st.close();
		return details;
		
	}


	//}
	public String Regi() throws SQLException {
		String res1="";
		Connection con=null;
		String s="";
		if(this.email.contains("@") && this.firstname.matches("[A-Z][a-zA-Z]*") && (this.email.contains(".com")||this.email.contains(".edu")))
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

			
			String sql = "INSERT INTO reg "+"(userid, name, email, password,username,accountbalance) values (NULL,?,?,?,?,10000)";
			PreparedStatement st = con.prepareStatement(sql);
			//st.setInt(1, userid);
			st.setString(1 ,firstname);
			st.setString(2, email);
			st.setString(3, password);
			st.setString(4, username);
			

			st.executeUpdate();
			s="true";
			System.out.println("Inserted 1 ");
			

			
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
		return "regsuc?faces-redirect=true";
		
		
	}
		else
		{
			
			return "failure2";
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
			
			String x=username;
			com.mysql.jdbc.jdbc2.optional.MysqlDataSource ds = new com.mysql.jdbc.jdbc2.optional.MysqlDataSource();
			ds.setServerName(System.getenv("ICSI518_SERVER"));
			ds.setPortNumber(Integer.parseInt(System.getenv("ICSI518_PORT")));
			ds.setDatabaseName(System.getenv("ICSI518_DB").toString());
			ds.setUser(System.getenv("ICSI518_USER").toString());
			ds.setPassword(System.getenv("ICSI518_PASSWORD").toString());
			
			
			con = ds.getConnection();

			
			String sql = "update  reg set name=?,email=?,password=? where username=?";
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, firstname);
			
			st.setString(2, email);
			
			st.setString(3, password);
			st.setString(4, username);
			
			

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
		return "sucess?faces-redirect=true";
		
		
	}
		
	}
	
	public String managerlist() throws SQLException
	{
		
		Connection con = null;
		com.mysql.jdbc.jdbc2.optional.MysqlDataSource ds = new com.mysql.jdbc.jdbc2.optional.MysqlDataSource();
		ds.setServerName(System.getenv("ICSI518_SERVER"));
		ds.setPortNumber(Integer.parseInt(System.getenv("ICSI518_PORT")));
		ds.setDatabaseName(System.getenv("ICSI518_DB").toString());
		ds.setUser(System.getenv("ICSI518_USER").toString());
		ds.setPassword(System.getenv("ICSI518_PASSWORD").toString());
		
		
		con = ds.getConnection();

	
	
	
	
	//con = ds.getConnection();

	
	String sql = "select accountbalance from from manager where username=?";
	
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
		
		System.out.println("hi");
		
		
		
		
		
		
		
	}
	con.close();
	stm.close();
	
	return "";
	
	}
	
	
	public String requestmanager() throws SQLException 
	{
		Connection con = null;
		try {
			System.out.println("entered");

		com.mysql.jdbc.jdbc2.optional.MysqlDataSource ds = new com.mysql.jdbc.jdbc2.optional.MysqlDataSource();
		ds.setServerName(System.getenv("ICSI518_SERVER"));
		ds.setPortNumber(Integer.parseInt(System.getenv("ICSI518_PORT")));
		ds.setDatabaseName(System.getenv("ICSI518_DB").toString());
		ds.setUser(System.getenv("ICSI518_USER").toString());
		ds.setPassword(System.getenv("ICSI518_PASSWORD").toString());
		
	
		
		con = ds.getConnection();
		String sql1 = "select managerid from reg where userid='"+ uid+"'";
		PreparedStatement st = con.prepareStatement(sql1);
		Integer mid=0;;
		System.out.println("1");

		ResultSet rs = st.executeQuery();
		while(rs.next())
		{
			mid=rs.getInt("managerid");
		}
		System.out.println(mid);

		String sql2 = "INSERT INTO request (managerid, userid, amount) values ('"+ mid +"','"+ uid +"',?)";
		PreparedStatement stx = con.prepareStatement(sql2);
		stx.setInt(1, qty);
		stx.executeUpdate();
		System.out.print("requested");
		
		return "sucess";
	}finally {
		try {
			if(con!=null)
				con.close();
		} catch (SQLException e) {

		}
		return "sucess";
	}
	}
	
	public String request() throws SQLException 
	{
		Connection con = null;
		try {
		
			
			com.mysql.jdbc.jdbc2.optional.MysqlDataSource ds = new com.mysql.jdbc.jdbc2.optional.MysqlDataSource();
			ds.setServerName(System.getenv("ICSI518_SERVER"));
			ds.setPortNumber(Integer.parseInt(System.getenv("ICSI518_PORT")));
			ds.setDatabaseName(System.getenv("ICSI518_DB").toString());
			ds.setUser(System.getenv("ICSI518_USER").toString());
			ds.setPassword(System.getenv("ICSI518_PASSWORD").toString());
			
			con = ds.getConnection();
			
				String sql = "update  reg "+" set managerid='" + managerid + "'  where userid='" + uid + "'";
				String sql2 = "update  manager "+" set userid=NULL  where userid='" + uid + "'";
			String sql1 = "update  manager "+" set userid='" + uid + "'  where id='" + managerid + "'";
			//String sql2="update manager set userid=Null where id<> '" + uid + "'";
			//String sql2 = "update  manager "+" set userid='" + 0 + "'  where id <>'" + managerid + "'";
			PreparedStatement st = con.prepareStatement(sql);
			PreparedStatement stx = con.prepareStatement(sql1);
			PreparedStatement sty = con.prepareStatement(sql2);

			
			//PreparedStatement sty = con.prepareStatement(sql2);
			st.executeUpdate();
			sty.executeUpdate();
			stx.executeUpdate();
			//sty.executeUpdate();
			//sty.executeUpdate();
			System.out.println("managerid updated");
			
			return "sucess";
			
		
		
	}catch (Exception e) {
		System.err.println("Exception: " + e.getMessage());
	} finally {
		try {
			if(con!=null)
				con.close();
		} catch (SQLException e) {

		}
		return "sucess";
}
	}

		
	
	public List<StockApiBean> getstocks() throws SQLException 
	{
		stock.clear();
		
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
		
		String sql = "select * from purchase where uid ='" + uid + "' and pors=0 and sell=0 ";
		PreparedStatement st = con.prepareStatement(sql);
		
		
		
		
		stock.clear();
		ResultSet rs=st.executeQuery(sql);
		
		while(rs.next())
		{
			
			StockApiBean stoc=new StockApiBean();
			stoc.setPid(rs.getInt("id"));
			stoc.setAmt(rs.getDouble("amt"));
			stoc.setPrice(rs.getDouble("price"));
			stoc.setQty(rs.getInt("qty"));
			stoc.setSymbol(rs.getString("stock_symbol"));
			
			stock.add(stoc);
			
			
			System.out.println(rs.getDouble("amt")+" " +rs.getDouble("price")+""+rs.getString("id"));
			
			
			
			
			
			
			
			
		}
		
		
		
		con.close();
		st.close();
		
		return stock;

		
	}
	
	

	
	
	public List<StockApiBean> getStockrequest() throws SQLException {
		stockrequest.clear();
		
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
		
		String sql = "select * from purchase where uid ='" + uid + "' ";
		PreparedStatement st = con.prepareStatement(sql);
		
		
		
		
		stock.clear();
		ResultSet rs=st.executeQuery(sql);
		return stockrequest;
	}

	
	public List<RegManager> getReqmanager() throws SQLException {

		
	 {
			System.out.println("entered");
			
			Connection con = null;
		com.mysql.jdbc.jdbc2.optional.MysqlDataSource ds = new com.mysql.jdbc.jdbc2.optional.MysqlDataSource();
		ds.setServerName(System.getenv("ICSI518_SERVER"));
		ds.setPortNumber(Integer.parseInt(System.getenv("ICSI518_PORT")));
		ds.setDatabaseName(System.getenv("ICSI518_DB").toString());
		ds.setUser(System.getenv("ICSI518_USER").toString());
		ds.setPassword(System.getenv("ICSI518_PASSWORD").toString());
		
		String sql = "";
		
		con = ds.getConnection();
		String sql1 = "select managerid from reg where userid='"+ uid+"'";
		PreparedStatement st = con.prepareStatement(sql1);
		Integer mid=0;;
		System.out.println("1");

		ResultSet rs = st.executeQuery();
		while(rs.next())
		{
			mid=rs.getInt("managerid");
		}
		System.out.println(mid);

		String sql2="select * from manager where id='"+ mid +"'";
		//String sql2 = "INSERT INTO request (managerid, userid, amount) values ('"+ mid +"','"+ uid +"',?)";
		PreparedStatement stx = con.prepareStatement(sql2);
		
		ResultSet s=stx.executeQuery();
		reqmanager.clear();
		while (s.next())
		{
			RegManager RM=new RegManager();
			RM.setFirstname(s.getString("name"));
			RM.setUsername(s.getString("username"));
			RM.setEmail(s.getString("email"));
			RM.setManagerid(s.getInt("id"));
			RM.setPassword(s.getString("password"));
			RM.setRole(s.getString("role"));
			RM.setFee(s.getInt("fee"));
			reqmanager.add(RM);
		}
		
		System.out.print("requested");
		
		
		}
		return reqmanager;
	}

	public List<StockApiBean> getSoldstock() throws SQLException {
		soldstock.clear();
		
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
		
		String sql = "select * from sell where userid ='" + uid + "'";
		PreparedStatement st = con.prepareStatement(sql);
		
		
		
		
		soldstock.clear();
		ResultSet rs=st.executeQuery(sql);
		
		while(rs.next())
		{
			
			StockApiBean stoc=new StockApiBean();
			stoc.setPid(rs.getInt("id"));
			stoc.setUseridentity(rs.getInt("userid"));
			stoc.setAmt(rs.getDouble("bought_price"));
			stoc.setPrice(rs.getDouble("sold_price"));
			stoc.setQty(rs.getInt("qty"));
			stoc.setSymbol(rs.getString("stock_symbol"));
			stoc.setProfit(rs.getDouble("profit"));
			
			soldstock.add(stoc);
			
			
			//System.out.println(rs.getDouble("amt")+" " +rs.getDouble("price")+""+rs.getString("id"));
			
			
			
			
			
			
			
			
		}
		
		
		
		con.close();
		st.close();
		
		return soldstock;
	}
	
	public String managersell() throws SQLException 
	{
		
		Connection con = null;
		try {
			System.out.println("entered");

		com.mysql.jdbc.jdbc2.optional.MysqlDataSource ds = new com.mysql.jdbc.jdbc2.optional.MysqlDataSource();
		ds.setServerName(System.getenv("ICSI518_SERVER"));
		ds.setPortNumber(Integer.parseInt(System.getenv("ICSI518_PORT")));
		ds.setDatabaseName(System.getenv("ICSI518_DB").toString());
		ds.setUser(System.getenv("ICSI518_USER").toString());
		ds.setPassword(System.getenv("ICSI518_PASSWORD").toString());
		
	
		
		con = ds.getConnection();
		System.out.println(pid);
		String up="Update purchase set sell=1,managerid='"+managerid+"' where id='"+pid +"'";
		PreparedStatement st = con.prepareStatement(up);
		st.executeUpdate();
//		String sql1 = "select managerid from reg where userid='"+ uid+"'";
//		PreparedStatement st = con.prepareStatement(sql1);
//		Integer mid=0;;
//		System.out.println("1");
//
//		ResultSet rs = st.executeQuery();
//		while(rs.next())
//		{
//			mid=rs.getInt("managerid");
//		}
//		System.out.println(mid);
//
//		String sql2 = "INSERT INTO request (managerid, userid, amount) values ('"+ mid +"','"+ uid +"',?)";
//		PreparedStatement stx = con.prepareStatement(sql2);
//		stx.setInt(1, qty);
//		stx.executeUpdate();
		System.out.print("requested");
		
		return "sucess";
	}finally {
		try {
			if(con!=null)
				con.close();
		} catch (SQLException e) {

		}
		return "sucess";
	}
	}

	public void logout() throws IOException {
//		HttpSession hs=Util.getSession();
//		hs.invalidate();
		 FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
	      FacesContext.getCurrentInstance().getExternalContext().redirect("front.xhtml");
		//return "front";
	}
	}
