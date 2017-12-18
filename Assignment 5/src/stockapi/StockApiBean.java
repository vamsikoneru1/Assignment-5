package stockapi;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.login.DataConnect;

import hm2.Login;

@ManagedBean
@SessionScoped
public class StockApiBean {

    private static final long serialVersionUID = 1L;
    static final String API_KEY = "AF93E6L5I01EA39O";

    private String symbol;
    private double price;
    private int qty;
    private double amt;
    private String table1Markup;
    private String table2Markup;
    private Integer pid;
    private Integer useridentity;
    private Integer fees;
    private Integer managerid;
    private Integer stockid;
    private double profit;
    

    public double getProfit() {
		return profit;
	}

	public void setProfit(double profit) {
		this.profit = profit;
	}

	public Integer getStockid() {
		return stockid;
	}

	public void setStockid(Integer stockid) {
		this.stockid = stockid;
	}

	public Integer getManagerid() {
		return managerid;
	}

	public void setManagerid(Integer managerid) {
		this.managerid = managerid;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	

	public Integer getUseridentity() {
		return useridentity;
	}

	public void setUseridentity(Integer useridentity) {
		this.useridentity = useridentity;
	}

	public Integer getFees() {
		return fees;
	}

	public void setFees(Integer fee) {
		this.fees = fees;
	}

	private String selectedSymbol;
    private List<SelectItem> availableSymbols;

    public String getPurchaseSymbol() {
        if (getRequestParameter("symbol") != null) {
            symbol = getRequestParameter("symbol");
        }
        return symbol;
    }
    
    public void setPurchaseSymbol(String purchaseSymbol) {
        System.out.println("func setPurchaseSymbol()");  //check
    }

    public double getPurchasePrice() {
        if (getRequestParameter("price") != null) {
            price = Double.parseDouble(getRequestParameter("price"));
            System.out.println("price: " + price);
        }
        return price;
    }

    public void setPurchasePrice(double purchasePrice) {
        System.out.println("func setPurchasePrice()");  //check
    }
    
    private String getRequestParameter(String name) {
        return ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getParameter(name);
    }

    @PostConstruct
    public void init() {
        //initially populate stock list
        availableSymbols = new ArrayList<SelectItem>();
        availableSymbols.add(new SelectItem("AAPL", "Apple"));
        availableSymbols.add(new SelectItem("AMZN", "Amazon LLC"));
        availableSymbols.add(new SelectItem("AR", "Antero Resources"));
        availableSymbols.add(new SelectItem("EBAY", "Ebay"));
        availableSymbols.add(new SelectItem("FB", "Facebook, Inc."));
        availableSymbols.add(new SelectItem("GOLD", "Gold"));
        availableSymbols.add(new SelectItem("GOOGL", "Google"));
        availableSymbols.add(new SelectItem("MSFT", "Microsoft"));
        availableSymbols.add(new SelectItem("SLV", "Silver"));
        availableSymbols.add(new SelectItem("TWTR", "Twitter, Inc."));

        //initially populate intervals for stock api
        availableIntervals = new ArrayList<SelectItem>();
        availableIntervals.add(new SelectItem("1min", "1min"));
        availableIntervals.add(new SelectItem("5min", "5min"));
        availableIntervals.add(new SelectItem("15min", "15min"));
        availableIntervals.add(new SelectItem("30min", "30min"));
        availableIntervals.add(new SelectItem("60min", "60min"));
    }

    private String selectedInterval;
    private List<SelectItem> availableIntervals;

    public String getSelectedInterval() {
        return selectedInterval;
    }

    public void setSelectedInterval(String selectedInterval) {
        this.selectedInterval = selectedInterval;
    }

    public List<SelectItem> getAvailableIntervals() {
        return availableIntervals;
    }

    public void setAvailableIntervals(List<SelectItem> availableIntervals) {
        this.availableIntervals = availableIntervals;
    }

    public String getSelectedSymbol() {
        return selectedSymbol;
    }

    public void setSelectedSymbol(String selectedSymbol) {
        this.selectedSymbol = selectedSymbol;
    }

    public List<SelectItem> getAvailableSymbols() {
        return availableSymbols;
    }

    public void setAvailableSymbols(List<SelectItem> availableSymbols) {
        this.availableSymbols = availableSymbols;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getAmt() {
        return amt;
    }

    public void setAmt(double amt) {
        this.amt = amt;
    }

    public String getTable1Markup() {
        return table1Markup;
    }

    public void setTable1Markup(String table1Markup) {
        this.table1Markup = table1Markup;
    }

    public String getTable2Markup() {
        return table2Markup;
    }

    public void setTable2Markup(String table2Markup) {
        this.table2Markup = table2Markup;
    }

    public String createDbRecord(String symbol, double price, int qty, double amt) {
        try {
            //System.out.println("symbol: " + this.symbol + ", price: " + this.price + "\n");
            //System.out.println("qty: " + this.qty + ", amt: " + this.amt + "\n");
        	Connection con=null;
        	com.mysql.jdbc.jdbc2.optional.MysqlDataSource ds = new com.mysql.jdbc.jdbc2.optional.MysqlDataSource();
			ds.setServerName("localhost");
			ds.setPortNumber(3306);
			ds.setDatabaseName("se_proj");
			ds.setUser("root");
			ds.setPassword("prasad");
			
			
			con = ds.getConnection();
        	
            //Connection conn = DataConnect.getConnection();
            Statement statement = con.createStatement();
            
            //get userid
            Integer uid = (Integer) ( FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getSessionMap().get("userid"));
            Login l= new Login();
          //  Integer uid = 1;
           
            System.out.println(uid);
            System.out.println("symbol:" + symbol);
            System.out.println("price:" + price);
            System.out.println("qty:" + qty);
            System.out.println("amt:" + amt);
            String sql1="INSERT INTO purchase (id, uid, stock_symbol, qty, price, amt,pors,sell) "
                    + "VALUES (NULL,'" + uid + "','" + symbol + "','" + qty + "','" + price + "','" + amt +"',0,0)";
            String sql="INSERT INTO purchase (id, uid, stock_symbol, qty, price, amt) values (?,?,?,?,?)";
			PreparedStatement st = con.prepareStatement(sql1);
			st.executeUpdate();
			String acc="select accountbalance from reg where userid='" + uid + "'" ;
			
			//PreparedStatement stm = con.prepareStatement(acc);
			
			Statement stm=null;
			stm=(Statement) con.createStatement();
			
			
			
			Double total=0.0;
			
			ResultSet rs=stm.executeQuery(acc);
			
			while(rs.next())
			{
				total=(double) rs.getInt("accountbalance");
			}
			
			
			
			
			//total=(double) rs.getInt("accountbalance");
			total=total-amt;
            System.out.println(total);

			String upd="update reg set accountbalance='" + total + "' where userid='"+ uid+ "'";
			
			PreparedStatement stx = con.prepareStatement(upd);
			

			stx.executeUpdate();
			
			
//			st.setString(1, null);
//			st.setInt(2, uid);
//			st.setString(3 ,symbol);
//			st.setDouble(4,price );
//			st.setDouble(5,amt);
			
			
			
            st.close();
            con.close();
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully purchased stock",""));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "sucess";
    }
    
    public String createDbRecordmanager(String symbol, double price, int qty, double amt) {
        try {
            //System.out.println("symbol: " + this.symbol + ", price: " + this.price + "\n");
            //System.out.println("qty: " + this.qty + ", amt: " + this.amt + "\n");
        	Connection con=null;
        	com.mysql.jdbc.jdbc2.optional.MysqlDataSource ds = new com.mysql.jdbc.jdbc2.optional.MysqlDataSource();
			ds.setServerName("localhost");
			ds.setPortNumber(3306);
			ds.setDatabaseName("se_proj");
			ds.setUser("root");
			ds.setPassword("prasad");
			
			
			con = ds.getConnection();
        	
            //Connection conn = DataConnect.getConnection();
           // Statement statement = con.createStatement();
            
            //get userid
            Integer mid = (Integer) ( FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getSessionMap().get("managerid"));
           // Login l= new Login();
          //  Integer uid = 1;
           
            System.out.println(useridentity);
            System.out.println(mid);
            System.out.println("symbol:" + symbol);
            System.out.println("price:" + price);
            System.out.println("qty:" + qty);
            System.out.println("amt:" + amt);
            
            
            
            
          
            String sql1="INSERT INTO purchase (id, uid, stock_symbol, qty, price, amt,managerid,pors,sell) "
                    + "VALUES (NULL,'"+ useridentity +"','" + symbol + "','" + qty + "','" + price + "','" + amt +"','"+ mid+"', 0,0)";
            //String sql="INSERT INTO purchase (id, uid, stock_symbol, qty, price, amt) values (?,?,?,?,?)";
			PreparedStatement st = con.prepareStatement(sql1);
			st.executeUpdate();
			String acc="select accountbalance from reg where userid='" + useridentity + "'" ;
			String f="select fee,accountbalance from manager where id='" + mid + "'" ;
			
			//PreparedStatement stm = con.prepareStatement(acc);
			
			Statement stm=null;
			Statement stx=null;
			stm=(Statement) con.createStatement();
			stx=(Statement) con.createStatement();
			Double total=0.0;
			Double fees=0.0;
			Double ab=0.0;
			ResultSet rs=stm.executeQuery(acc);
			ResultSet q=stx.executeQuery(f);
			while(rs.next())
			{
				total=(double) rs.getInt("accountbalance");
			}
			
			System.out.println(total);
			
			while(q.next())
			{
				fees=q.getDouble("fee");
				ab=q.getDouble("accountbalance")
;				
			}
			System.out.println(fees);
			System.out.println(ab);
			//total=(double) rs.getInt("accountbalance");
			total=total-amt-fees;
			ab=ab+fees;
			
			String upd="update reg set accountbalance='" + total + "' where userid='"+ useridentity+ "'";
			String updfee="update manager set accountbalance='" + ab + "' where id='"+ mid+ "'";
			String del="delete from request where managerid='"+mid+"' and userid='"+useridentity+"' and amount='"+qty+"'";
			PreparedStatement x= con.prepareStatement(upd);
			PreparedStatement y = con.prepareStatement(updfee);
			PreparedStatement z = con.prepareStatement(del);
			x.executeUpdate();
			y.executeUpdate();
			z.executeUpdate();
//			st.setString(1, null);
//			st.setInt(2, uid);
//			st.setString(3 ,symbol);
//			st.setDouble(4,price );
//			st.setDouble(5,amt);
			
			z.close();
			y.close();
			x.close();
			stx.close();
			stm.close();
            st.close();
            
            con.close();
          
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully purchased stock",""));
        } catch (SQLException e) {
            e.printStackTrace();
        }
		return "Manageraccount";
        
    }
    
    
    public String createusersell(String symbol, double price, int qty, double amt) {
        try {
            //System.out.println("symbol: " + this.symbol + ", price: " + this.price + "\n");
            //System.out.println("qty: " + this.qty + ", amt: " + this.amt + "\n");
        	Connection con=null;
        	com.mysql.jdbc.jdbc2.optional.MysqlDataSource ds = new com.mysql.jdbc.jdbc2.optional.MysqlDataSource();
			ds.setServerName("localhost");
			ds.setPortNumber(3306);
			ds.setDatabaseName("se_proj");
			ds.setUser("root");
			ds.setPassword("prasad");
			
			
			con = ds.getConnection();
        	
            //Connection conn = DataConnect.getConnection();
            Statement statement = con.createStatement();
            
            //get userid
            Integer uid = (Integer) ( FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getSessionMap().get("userid"));
            Login l= new Login();
          //  Integer uid = 1;
           
            System.out.println(uid);
            System.out.println("symbol:" + symbol);
            System.out.println("price:" + price);
            System.out.println("qty:" + qty);
            System.out.println("amt:" + amt);
            
            String sql1 = "update  purchase  set sell=1 where id='"+ stockid+"' and qty='"+ qty+ "'";
			PreparedStatement st = con.prepareStatement(sql1);
			st.executeUpdate();
			String acc="select amt from purchase where id='" + stockid + "'" ;
			Statement stm=null;
			stm=(Statement) con.createStatement();
			Double puramount=0.0;
			Double bp=0.0;
			
			ResultSet rs=stm.executeQuery(acc);
			
			while(rs.next())
			{
				puramount=rs.getDouble("amt");
			}
			
			bp=puramount;

			puramount=amt-puramount;
			Double profit=puramount;
            System.out.println("bought amount"+bp);
            System.out.println("profit"+profit);
            String upd="INSERT INTO sell (id,userid,qty, profit, sold_price,bought_price,stock_symbol)"+ "values ('"+stockid+"','"+uid+"','"+qty+"','"+profit+"','"+amt+"','"+bp+"','"+symbol+"')"; 
			//String upd="update reg set accountbalance='" + total + "' where userid='"+ uid+ "'";
			PreparedStatement stx = con.prepareStatement(upd);
			stx.executeUpdate();
	
			
			String addpro="select accountbalance from reg where userid='" + uid + "'" ;
			
			//PreparedStatement stm = con.prepareStatement(acc);
			
			Statement stp=null;
			stp=(Statement) con.createStatement();
			Double ab=0.0;
			
			ResultSet  pro=stp.executeQuery(addpro);
			
			while(pro.next())
			{
				ab=pro.getDouble("accountbalance");
			}
			
			
//			st.setString(1, null);
//			st.setInt(2, uid);
//			st.setString(3 ,symbol);
//			st.setDouble(4,price );
//			st.setDouble(5,amt);
			
			ab=ab+profit;
			String abupp="update reg set accountbalance='" + ab + "' where userid='"+ uid+ "'";
			
			PreparedStatement stg = con.prepareStatement(abupp);
			stg.executeUpdate();
			
			stg.close();
			stp.close();
			stx.close();
			stm.close();
            st.close();
            con.close();
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully purchased stock",""));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "sucess";
    }
    
    
    public String managersellrequest(String symbol, double price, int qty, double amt) {
        try {
            //System.out.println("symbol: " + this.symbol + ", price: " + this.price + "\n");
            //System.out.println("qty: " + this.qty + ", amt: " + this.amt + "\n");
        	Connection con=null;
        	com.mysql.jdbc.jdbc2.optional.MysqlDataSource ds = new com.mysql.jdbc.jdbc2.optional.MysqlDataSource();
			ds.setServerName("localhost");
			ds.setPortNumber(3306);
			ds.setDatabaseName("se_proj");
			ds.setUser("root");
			ds.setPassword("prasad");
			
			
			con = ds.getConnection();
        	
            //Connection conn = DataConnect.getConnection();
            Statement statement = con.createStatement();
            
            //get userid
            Integer mid = (Integer) ( FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getSessionMap().get("managerid"));
            Login l= new Login();
          //  Integer uid = 1;
           
            System.out.println(mid);
            System.out.println("symbol:" + symbol);
            System.out.println("price:" + price);
            System.out.println("qty:" + qty);
            System.out.println("amt:" + amt);
            Double fees=0.0;
            Double managerbalance=0.0;
            
            
			String f="select fee,accountbalance from manager where id='" + mid + "'" ;
			Statement stf=null;
			stf=(Statement) con.createStatement();
			ResultSet fe=stf.executeQuery(f);
			while(fe.next())
			{
				fees=fe.getDouble("fee");
				managerbalance=fe.getDouble("accountbalance");
			}
			
			String acc="select amt from purchase where id='" + stockid + "'" ;
			Statement stm=null;
			stm=(Statement) con.createStatement();
			Double puramount=0.0;
			Double bpsp=0.0;
			Double boughtprice=0.0;
			Double managerab=0.0;
			
			
			ResultSet rs=stm.executeQuery(acc);
			
			while(rs.next())
			{
				puramount=rs.getDouble("amt");
			}
			
			boughtprice=puramount;
			

			bpsp=amt-puramount-fees;
			Double profit=bpsp;
			managerbalance=managerbalance+fees;
            System.out.println("bought amount"+boughtprice);
            System.out.println("profit"+profit);
            System.out.println("manager balance"+managerbalance);
            String upd="INSERT INTO sell (id,userid,qty, profit, sold_price,bought_price,stock_symbol)"+ "values ('"+stockid+"','"+useridentity+"','"+qty+"','"+profit+"','"+amt+"','"+boughtprice+"','"+symbol+"')"; 
			//String upd="update reg set accountbalance='" + total + "' where userid='"+ uid+ "'";
			PreparedStatement stx = con.prepareStatement(upd);
			stx.executeUpdate();
	
			
			String addpro="select accountbalance from reg where userid='" + useridentity + "'" ;
			
			//PreparedStatement stm = con.prepareStatement(acc);
			
			Statement stp=null;
			stp=(Statement) con.createStatement();
			Double ab=0.0;
			
			ResultSet  pro=stp.executeQuery(addpro);
			
			while(pro.next())
			{
				ab=pro.getDouble("accountbalance");
			}
			

			ab=ab+profit;
			String abupp="update reg set accountbalance='" + ab + "' where userid='"+ useridentity+ "'";
			PreparedStatement stg = con.prepareStatement(abupp);
			stg.executeUpdate();
			String updfee="update manager set accountbalance='" + managerbalance + "' where id='"+ mid+ "'";
			PreparedStatement stman = con.prepareStatement(updfee);
			stman.executeUpdate();
			String sql1 = "update  purchase  set pors=1 where id='"+ stockid+"' and qty='"+ qty+ "'";
			PreparedStatement st = con.prepareStatement(sql1);
			st.executeUpdate();
			
			
			stman.close();
			stg.close();
			stp.close();
			stx.close();
			stm.close();
            st.close();
            con.close();
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully purchased stock",""));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Manageraccount";
    }
    
    
    
    
    
    
    
    public String createDbRecordrequest(String symbol, double price, int qty, double amt)
    {
    	
		return symbol;
    	
    }

    public void installAllTrustingManager() {
        TrustManager[] trustAllCerts;
        trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }};

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            System.out.println("Exception :" + e);
        }
        return;
    }

    public void timeseries() throws MalformedURLException, IOException {

        installAllTrustingManager();

        //System.out.println("selectedItem: " + this.selectedSymbol);
        //System.out.println("selectedInterval: " + this.selectedInterval);
        String symbol = this.selectedSymbol;
        String interval = this.selectedInterval;
        String url = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=" + symbol + "&interval=" + interval + "&apikey=" + API_KEY;

        this.table1Markup += "URL::: <a href='" + url + "'>Data Link</a><br>";
        InputStream inputStream = new URL(url).openStream();

        // convert the json string back to object
        JsonReader jsonReader = Json.createReader(inputStream);
        JsonObject mainJsonObj = jsonReader.readObject();
        for (String key : mainJsonObj.keySet()) {
            if (key.equals("Meta Data")) {
                this.table1Markup = null; // reset table 1 markup before repopulating
                JsonObject jsob = (JsonObject) mainJsonObj.get(key);
                this.table1Markup += "<style>#detail >tbody > tr > td{ text-align:center;}</style><b>Stock Details</b>:<br>";
                this.table1Markup += "<table>";
                this.table1Markup += "<tr><td>Information</td><td>" + jsob.getString("1. Information") + "</td></tr>";
                this.table1Markup += "<tr><td>Symbol</td><td>" + jsob.getString("2. Symbol") + "</td></tr>";
                this.table1Markup += "<tr><td>Last Refreshed</td><td>" + jsob.getString("3. Last Refreshed") + "</td></tr>";
                this.table1Markup += "<tr><td>Interval</td><td>" + jsob.getString("4. Interval") + "</td></tr>";
                this.table1Markup += "<tr><td>Output Size</td><td>" + jsob.getString("5. Output Size") + "</td></tr>";
                this.table1Markup += "<tr><td>Time Zone</td><td>" + jsob.getString("6. Time Zone") + "</td></tr>";
                this.table1Markup += "</table>";
            } else {
                this.table2Markup = null; // reset table 2 markup before repopulating
                JsonObject dataJsonObj = mainJsonObj.getJsonObject(key);
                this.table2Markup += "<table class='table table-hover'>";
                this.table2Markup += "<thead><tr><th>Timestamp</th><th>Open</th><th>High</th><th>Low</th><th>Close</th><th>Volume</th></tr></thead>";
                this.table2Markup += "<tbody>";
                int i = 0;
                for (String subKey : dataJsonObj.keySet()) {
                    JsonObject subJsonObj = dataJsonObj.getJsonObject(subKey);
                    this.table2Markup
                            += "<tr>"
                            + "<td>" + subKey + "</td>"
                            + "<td>" + subJsonObj.getString("1. open") + "</td>"
                            + "<td>" + subJsonObj.getString("2. high") + "</td>"
                            + "<td>" + subJsonObj.getString("3. low") + "</td>"
                            + "<td>" + subJsonObj.getString("4. close") + "</td>"
                            + "<td>" + subJsonObj.getString("5. volume") + "</td>";
                    if (i == 0) {
                       // String path = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
                        this.table2Markup += "<td><a class='btn btn-success' href='" + "/Assignment_5//purchase.xhtml?symbol=" + symbol + "&price=" + subJsonObj.getString("4. close") + "'>Buy Stock</a></td>";
                    }
                    this.table2Markup += "</tr>";
                    i++;
                }
                this.table2Markup += "</tbody></table>";
            }
        }
        return;
    }
    
    public void timeseriesmanager() throws MalformedURLException, IOException {

        installAllTrustingManager();

        //System.out.println("selectedItem: " + this.selectedSymbol);
        //System.out.println("selectedInterval: " + this.selectedInterval);
        String symbol = this.selectedSymbol;
        String interval = this.selectedInterval;
        String url = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=" + symbol + "&interval=" + interval + "&apikey=" + API_KEY;

        this.table1Markup += "URL::: <a href='" + url + "'>Data Link</a><br>";
        InputStream inputStream = new URL(url).openStream();

        // convert the json string back to object
        JsonReader jsonReader = Json.createReader(inputStream);
        JsonObject mainJsonObj = jsonReader.readObject();
        for (String key : mainJsonObj.keySet()) {
            if (key.equals("Meta Data")) {
                this.table1Markup = null; // reset table 1 markup before repopulating
                JsonObject jsob = (JsonObject) mainJsonObj.get(key);
                this.table1Markup += "<style>#detail >tbody > tr > td{ text-align:center;}</style><b>Stock Details</b>:<br>";
                this.table1Markup += "<table>";
                this.table1Markup += "<tr><td>Information</td><td>" + jsob.getString("1. Information") + "</td></tr>";
                this.table1Markup += "<tr><td>Symbol</td><td>" + jsob.getString("2. Symbol") + "</td></tr>";
                this.table1Markup += "<tr><td>Last Refreshed</td><td>" + jsob.getString("3. Last Refreshed") + "</td></tr>";
                this.table1Markup += "<tr><td>Interval</td><td>" + jsob.getString("4. Interval") + "</td></tr>";
                this.table1Markup += "<tr><td>Output Size</td><td>" + jsob.getString("5. Output Size") + "</td></tr>";
                this.table1Markup += "<tr><td>Time Zone</td><td>" + jsob.getString("6. Time Zone") + "</td></tr>";
                this.table1Markup += "</table>";
            } else {
                this.table2Markup = null; // reset table 2 markup before repopulating
                JsonObject dataJsonObj = mainJsonObj.getJsonObject(key);
                this.table2Markup += "<table class='table table-hover'>";
                this.table2Markup += "<thead><tr><th>Timestamp</th><th>Open</th><th>High</th><th>Low</th><th>Close</th><th>Volume</th></tr></thead>";
                this.table2Markup += "<tbody>";
                int i = 0;
                for (String subKey : dataJsonObj.keySet()) {
                    JsonObject subJsonObj = dataJsonObj.getJsonObject(subKey);
                    this.table2Markup
                            += "<tr>"
                            + "<td>" + subKey + "</td>"
                            + "<td>" + subJsonObj.getString("1. open") + "</td>"
                            + "<td>" + subJsonObj.getString("2. high") + "</td>"
                            + "<td>" + subJsonObj.getString("3. low") + "</td>"
                            + "<td>" + subJsonObj.getString("4. close") + "</td>"
                            + "<td>" + subJsonObj.getString("5. volume") + "</td>";
                    if (i == 0) {
                       // String path = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
                        this.table2Markup += "<td><a class='btn btn-success' href='" + "/Assignment_5//managerpurchase.xhtml?symbol=" + symbol + "&price=" + subJsonObj.getString("4. close") + "'>Buy Stock</a></td>";
                    }
                    this.table2Markup += "</tr>";
                    i++;
                }
                this.table2Markup += "</tbody></table>";
            }
        }
        return;
    }
    
    
    public void timeseriesusersell() throws MalformedURLException, IOException {

        installAllTrustingManager();

        //System.out.println("selectedItem: " + this.selectedSymbol);
        //System.out.println("selectedInterval: " + this.selectedInterval);
        String symbol = this.selectedSymbol;
        String interval = this.selectedInterval;
        String url = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=" + symbol + "&interval=" + interval + "&apikey=" + API_KEY;

        this.table1Markup += "URL::: <a href='" + url + "'>Data Link</a><br>";
        InputStream inputStream = new URL(url).openStream();

        // convert the json string back to object
        JsonReader jsonReader = Json.createReader(inputStream);
        JsonObject mainJsonObj = jsonReader.readObject();
        for (String key : mainJsonObj.keySet()) {
            if (key.equals("Meta Data")) {
                this.table1Markup = null; // reset table 1 markup before repopulating
                JsonObject jsob = (JsonObject) mainJsonObj.get(key);
                this.table1Markup += "<style>#detail >tbody > tr > td{ text-align:center;}</style><b>Stock Details</b>:<br>";
                this.table1Markup += "<table>";
                this.table1Markup += "<tr><td>Information</td><td>" + jsob.getString("1. Information") + "</td></tr>";
                this.table1Markup += "<tr><td>Symbol</td><td>" + jsob.getString("2. Symbol") + "</td></tr>";
                this.table1Markup += "<tr><td>Last Refreshed</td><td>" + jsob.getString("3. Last Refreshed") + "</td></tr>";
                this.table1Markup += "<tr><td>Interval</td><td>" + jsob.getString("4. Interval") + "</td></tr>";
                this.table1Markup += "<tr><td>Output Size</td><td>" + jsob.getString("5. Output Size") + "</td></tr>";
                this.table1Markup += "<tr><td>Time Zone</td><td>" + jsob.getString("6. Time Zone") + "</td></tr>";
                this.table1Markup += "</table>";
            } else {
                this.table2Markup = null; // reset table 2 markup before repopulating
                JsonObject dataJsonObj = mainJsonObj.getJsonObject(key);
                this.table2Markup += "<table class='table table-hover'>";
                this.table2Markup += "<thead><tr><th>Timestamp</th><th>Open</th><th>High</th><th>Low</th><th>Close</th><th>Volume</th></tr></thead>";
                this.table2Markup += "<tbody>";
                int i = 0;
                for (String subKey : dataJsonObj.keySet()) {
                    JsonObject subJsonObj = dataJsonObj.getJsonObject(subKey);
                    this.table2Markup
                            += "<tr>"
                            + "<td>" + subKey + "</td>"
                            + "<td>" + subJsonObj.getString("1. open") + "</td>"
                            + "<td>" + subJsonObj.getString("2. high") + "</td>"
                            + "<td>" + subJsonObj.getString("3. low") + "</td>"
                            + "<td>" + subJsonObj.getString("4. close") + "</td>"
                            + "<td>" + subJsonObj.getString("5. volume") + "</td>";
                    if (i == 0) {
                       // String path = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
                        this.table2Markup += "<td><a class='btn btn-success' href='" + "/Assignment_5//usersell.xhtml?symbol=" + symbol + "&price=" + subJsonObj.getString("4. close") + "'>Sell stock</a></td>";
                    }
                    this.table2Markup += "</tr>";
                    i++;
                }
                this.table2Markup += "</tbody></table>";
            }
        }
        return;
    }
    
    
    public void timeseriesmanagersell() throws MalformedURLException, IOException {

        installAllTrustingManager();

        //System.out.println("selectedItem: " + this.selectedSymbol);
        //System.out.println("selectedInterval: " + this.selectedInterval);
        String symbol = this.selectedSymbol;
        String interval = this.selectedInterval;
        String url = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=" + symbol + "&interval=" + interval + "&apikey=" + API_KEY;

        this.table1Markup += "URL::: <a href='" + url + "'>Data Link</a><br>";
        InputStream inputStream = new URL(url).openStream();

        // convert the json string back to object
        JsonReader jsonReader = Json.createReader(inputStream);
        JsonObject mainJsonObj = jsonReader.readObject();
        for (String key : mainJsonObj.keySet()) {
            if (key.equals("Meta Data")) {
                this.table1Markup = null; // reset table 1 markup before repopulating
                JsonObject jsob = (JsonObject) mainJsonObj.get(key);
                this.table1Markup += "<style>#detail >tbody > tr > td{ text-align:center;}</style><b>Stock Details</b>:<br>";
                this.table1Markup += "<table>";
                this.table1Markup += "<tr><td>Information</td><td>" + jsob.getString("1. Information") + "</td></tr>";
                this.table1Markup += "<tr><td>Symbol</td><td>" + jsob.getString("2. Symbol") + "</td></tr>";
                this.table1Markup += "<tr><td>Last Refreshed</td><td>" + jsob.getString("3. Last Refreshed") + "</td></tr>";
                this.table1Markup += "<tr><td>Interval</td><td>" + jsob.getString("4. Interval") + "</td></tr>";
                this.table1Markup += "<tr><td>Output Size</td><td>" + jsob.getString("5. Output Size") + "</td></tr>";
                this.table1Markup += "<tr><td>Time Zone</td><td>" + jsob.getString("6. Time Zone") + "</td></tr>";
                this.table1Markup += "</table>";
            } else {
                this.table2Markup = null; // reset table 2 markup before repopulating
                JsonObject dataJsonObj = mainJsonObj.getJsonObject(key);
                this.table2Markup += "<table class='table table-hover'>";
                this.table2Markup += "<thead><tr><th>Timestamp</th><th>Open</th><th>High</th><th>Low</th><th>Close</th><th>Volume</th></tr></thead>";
                this.table2Markup += "<tbody>";
                int i = 0;
                for (String subKey : dataJsonObj.keySet()) {
                    JsonObject subJsonObj = dataJsonObj.getJsonObject(subKey);
                    this.table2Markup
                            += "<tr>"
                            + "<td>" + subKey + "</td>"
                            + "<td>" + subJsonObj.getString("1. open") + "</td>"
                            + "<td>" + subJsonObj.getString("2. high") + "</td>"
                            + "<td>" + subJsonObj.getString("3. low") + "</td>"
                            + "<td>" + subJsonObj.getString("4. close") + "</td>"
                            + "<td>" + subJsonObj.getString("5. volume") + "</td>";
                    if (i == 0) {
                       // String path = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
                        this.table2Markup += "<td><a class='btn btn-success' href='" + "/Assignment_5//managersellpurchase.xhtml?symbol=" + symbol + "&price=" + subJsonObj.getString("4. close") + "'>Sell stock</a></td>";
                    }
                    this.table2Markup += "</tr>";
                    i++;
                }
                this.table2Markup += "</tbody></table>";
            }
        }
        return;
    }
    

    public void purchaseStock() {
        System.out.println("Calling function purchaseStock()");
        System.out.println("stockSymbol: " + FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("stockSymbol"));
        System.out.println("stockPrice" + FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("stockPrice"));
        return;
    }
}