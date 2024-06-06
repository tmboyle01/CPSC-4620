package cpsc4620;

import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.Date;

/*
 * This file is where most of your code changes will occur You will write the code to retrieve
 * information from the database, or save information to the database
 * 
 * The class has several hard coded static variables used for the connection, you will need to
 * change those to your connection information
 * 
 * This class also has static string variables for pickup, delivery and dine-in. If your database
 * stores the strings differently (i.e "pick-up" vs "pickup") changing these static variables will
 * ensure that the comparison is checking for the right string in other places in the program. You
 * will also need to use these strings if you store this as boolean fields or an integer.
 * 
 * 
 */

/**
 * A utility class to help add and retrieve information from the database
 */

public final class DBNinja {
	private static Connection conn;

	// Change these variables to however you record dine-in, pick-up and delivery, and sizes and crusts
	public final static String pickup = "pickup";
	public final static String delivery = "delivery";
	public final static String dine_in = "dinein";

	public final static String size_s = "Small";
	public final static String size_m = "Medium";
	public final static String size_l = "Large";
	public final static String size_xl = "XLarge";

	public final static String crust_thin = "Thin";
	public final static String crust_orig = "Original";
	public final static String crust_pan = "Pan";
	public final static String crust_gf = "Gluten-Free";



	
	private static boolean connect_to_db() throws SQLException, IOException {

		try {
			conn = DBConnector.make_connection();
			return true;
		} catch (SQLException e) {
			return false;
		} catch (IOException e) {
			return false;
		}

	}

	// not tested // updated
	public static void addOrder(Order o) throws SQLException, IOException
    {
        connect_to_db();
        /*
         * add code to add the order to the DB. Remember that we're not just
         * adding the order to the order DB table, but we're also recording
         * the necessary data for the delivery, dinein, and pickup tables
         *
         */
        String query = "INSERT INTO `order`(Order_ID,OrderCust_ID,OrderType,OrderTimeStamp,OrderTotalPrice,OrderTotalCostBusiness,OrderStatus) VALUES(?,?,?,?,?,?,?); ";
        PreparedStatement statement = conn.prepareStatement(query);

        int oID = o.getOrderID();//OrderID
        int cID = o.getCustID(); //Figure out how to get? or Try Null?
        String oType = o.getOrderType();//dine, pickup, delivery.
        String oDate = o.getDate();
        Double oCusPrice = o.getCustPrice();
        Double oBusPrice = o.getBusPrice();
        int oComplete = o.getIsComplete(); //Boolean to int?

        statement.setInt(1,oID);
        statement.setInt(2,cID);
        statement.setString(3, oType);
        statement.setString(4, oDate);
        statement.setDouble(5, oCusPrice);
        statement.setDouble(6, oBusPrice);
        statement.setInt(7,oComplete);//Is this alright? passing in an boolean or int?
       
        statement.executeUpdate();
        conn.close();
    }

	
	public static void addPizza(Pizza p) throws SQLException, IOException
    {
        connect_to_db();
        /*
         * Add the code needed to insert the pizza into into the database.
         * Keep in mind adding pizza discounts and toppings associated with the pizza,
         * there are other methods below that may help with that process.
         *
         */
       
         String query = "INSERT INTO pizza(PizzaID,PizzaOrderID,PizzaSize,PizzaCostCompany,PizzaAssoicatedPrice,PizzaCrustType,PizzaStatus) VALUES (?,?,?,?,?,?,?);";
         PreparedStatement statement = conn.prepareStatement(query);

         int pID = p.getPizzaID();
         int pOrderID = p.getOrderID();
         String pSize = p.getSize();
         Double pCustPrice = p.getCustPrice();
         Double pBusPrice = p.getBusPrice();
         String pCrustT = p.getCrustType();
         String pState = p.getPizzaState();
         
         statement.setInt(1, pID);
         statement.setInt(2,pOrderID);
        //  statement.setInt(3,null); //No PizzaBasePriceID?
         statement.setString(3, pSize);
         statement.setDouble(4,pBusPrice);
         statement.setDouble(5,pCustPrice);
         statement.setString(6,pCrustT);
         statement.setString(7,pState);

         statement.executeUpdate();
 
         conn.close();
    }

	
	
	public static void useTopping(Pizza p, Topping t, boolean isDoubled) throws SQLException, IOException //this method will update toppings inventory in SQL and add entities to the Pizzatops table. Pass in the p pizza that is using t topping
	{
		connect_to_db();
		/*
		 * This method should do 2 two things.
		 * - update the topping inventory every time we use t topping (accounting for extra toppings as well)
		 * - connect the topping to the pizza
		 *   What that means will be specific to your yimplementatinon.
		 * 
		 * Ideally, you should't let toppings go negative....but this should be dealt with BEFORE calling this method.
		 * 
		 */
		
		Connection conn = DBConnector.make_connection();

		double amt = t.getPerAMT();
		if (isDoubled) {
			amt = amt * 2;
		}
	 
		String updateQuery = "UPDATE topping SET ToppingCurrentInventory = ToppingCurrentInventory - ? WHERE ToppingID = ?";
		PreparedStatement Stmt = conn.prepareStatement(updateQuery);
		Stmt.setDouble(1, amt);
		Stmt.setInt(2, t.getTopID());
		Stmt.executeUpdate();
	
		conn.close();;
	}
	
	// not tested
	public static void usePizzaDiscount(Pizza p, Discount d) throws SQLException, IOException
	{
		connect_to_db();
		/*
		 * This method connects a discount with a Pizza in the database.
		 * 
		 * What that means will be specific to your implementatinon.
		 */
		// Insert the pizza discount into the PizzaDiscount table

		String Query = "INSERT INTO pizzadiscount (PizzaDiscountPizzaID, PizzaDiscountDiscountID) VALUES (?, ?)";
		PreparedStatement Stmt = conn.prepareStatement(Query);
		Stmt.setInt(1, p.getPizzaID());
		Stmt.setInt(2, d.getDiscountID());
		Stmt.executeUpdate();
		
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
		conn.close();
	}
	
	// not tested
	public static void useOrderDiscount(Order o, Discount d) throws SQLException, IOException
	{
		connect_to_db();
		/*
		 * This method connects a discount with an order in the database
		 * 
		 * You might use this, you might not depending on where / how to want to update
		 * this information in the dabast
		 */
		   // Insert the order discount into the OrderDiscount table
		   String Query = "INSERT INTO orderdiscount (OrderDiscountOrderID, OrderDiscountDiscountID) VALUES (?, ?)";
		   PreparedStatement Stmt = conn.prepareStatement(Query);
		   Stmt.setInt(1, o.getOrderID());
		   Stmt.setInt(2, d.getDiscountID());
		   Stmt.executeUpdate();

		   //DO NOT FORGET TO CLOSE YOUR CONNECTION
		   conn.close();
	}
	
	// works
	public static void addCustomer(Customer c) throws SQLException, IOException {
        connect_to_db();
        /*
         * This method adds a new customer to the database.
         *
         */
        String query = "INSERT INTO customer (Customer_ID,CustomerFirstName,CustomerLastName,CustomerPhoneNum) VALUES (?,?,?,?);";
        PreparedStatement statement = conn.prepareStatement(query);

        int cID = c.getCustID();
        String firstN = c.getFName();
        String lastN = c.getLName();
        String phone = c.getPhone();

         statement.setInt(1, cID);
         statement.setString(2, firstN);
         statement.setString(3, lastN);
         statement.setString(4, phone);
         statement.executeUpdate();
         conn.close();
    }

	// works?
	public static void completeOrder(Order o) throws SQLException, IOException {
		connect_to_db();
		/*
		 * Find the specifed order in the database and mark that order as complete in the database.
		 * 
		 */
		int OrderID = o.getOrderID();
		String query = "UPDATE `order` SET orderStatus = true WHERE Order_ID = ?";
		PreparedStatement stmt = conn.prepareStatement(query);
		stmt.setInt(2, OrderID);
		stmt.executeUpdate();

		conn.close();
	}

	// works
	public static ArrayList<Order> getOrders(boolean openOnly) throws SQLException, IOException {
		connect_to_db();
		/*
		 * Return an arraylist of all of the orders.
		 * 	openOnly == true => only return a list of open (ie orders that have not been marked as completed)
		 *           == false => return a list of all the orders in the database
		 * Remember that in Java, we account for supertypes and subtypes
		 * which means that when we create an arrayList of orders, that really
		 * means we have an arrayList of dineinOrders, deliveryOrders, and pickupOrders.
		 * 
		 * Don't forget to order the data coming from the database appropriately.
		 * 
		 */
		 ArrayList<Order> orders = new ArrayList<>();

		 String query;
		 if (openOnly == true) {
			 query = "SELECT * FROM `order` WHERE OrderStatus = false ORDER BY OrderTimeStamp DESC";
		 } else {
			 query = "SELECT * FROM `order` WHERE OrderStatus = true ORDER BY OrderTimeStamp DESC";
		 }
	 
		 Statement stmt = conn.createStatement();
		 ResultSet rs = stmt.executeQuery(query);
	 
		 while (rs.next()) {
			 int orderID = rs.getInt("Order_ID");
			 int customerID = rs.getInt("OrderCust_ID");
			 String orderType = rs.getString("OrderType");
			 String orderDateTime = rs.getString("OrderTimeStamp");
			 double orderPrice = rs.getDouble("OrderTotalPrice");
			 double orderCost = rs.getDouble("OrderTotalCostBusiness");
			 // needs to be a int not a string
			 String orderStatus = rs.getString("OrderStatus");
			 int orderStat;
			 if(orderStatus == "true"){
				orderStat = 1;
			 }else{
				orderStat = 0;
			 }
			 // create new order object
			 Order order = new Order(orderID, customerID, orderType, orderDateTime, orderPrice, orderCost, orderStat);
			 // add order to ArrayList
			 orders.add(order);
		 }
		 //DO NOT FORGET TO CLOSE YOUR CONNECTION
		 conn.close();
		 return orders;
	}
	
	// works
	public static Order getLastOrder() throws SQLException, IOException{
		/*
		 * Query the database for the LAST order added
		 * then return an Order object for that order.
		 * NOTE...there should ALWAYS be a "last order"!
		 */
		connect_to_db();

		String query = "SELECT * FROM `order` ORDER BY Order_ID DESC LIMIT 1";

		Statement stmt = conn.createStatement();
		ResultSet rset = stmt.executeQuery(query);
	
		if (rset.next()) {
			int orderID = rset.getInt("Order_ID");
			int custID = rset.getInt("OrderCust_ID");
			double busPrice = rset.getDouble("OrderTotalCostBusiness");
			double custPrice = rset.getDouble("OrderTotalPrice");
			String orderType = rset.getString("OrderType");
			String isComplete = rset.getString("OrderStatus");
			int isComplete1;
			if(isComplete == "true"){
				isComplete1 = 1;
			}else{
				isComplete1 = 0;
			}
			String date = rset.getString("OrderTimeStamp");

			Order Order1 = new Order(orderID, custID, orderType, date, custPrice, busPrice, isComplete1);
			conn.close();
			return Order1;
		}
		conn.close();
		 return null;

	}

	// works
	public static ArrayList<Order> getOrdersByDate(String date) throws SQLException, IOException{
		/*
		 * Query the database for ALL the orders placed on a specific date
		 * and return a list of those orders.
		 *  
		 */
		connect_to_db();

		ArrayList<Order> OrderList = new ArrayList<Order>() ;

		String query = "SELECT * FROM `order` WHERE OrderTimeStamp = '" + date + "';";

		Statement stmt = conn.createStatement();
		ResultSet rset = stmt.executeQuery(query);
	
		while (rset.next()) {
			int i = 1;
			int orderID = rset.getInt("Order_ID");
			int custID = rset.getInt("OrderCust_ID");
			i++;
			double busPrice = rset.getDouble("OrderTotalCostBusiness");
			double custPrice = rset.getDouble("OrderTotalPrice");
			String orderType = rset.getString("OrderType");
			String isComplete = rset.getString("OrderStatus");
			int isComplete1;
			if(isComplete == "true"){
				isComplete1 = 1;
			}else{
				isComplete1 = 0;
			}
			String date1 = rset.getString("OrderTimeStamp");

			Order Order1 = new Order(orderID, custID, orderType, date1, custPrice, busPrice, isComplete1);
			
			OrderList.add(Order1);
		}
		 conn.close();
		 return OrderList;
	}
	
	// works
	public static ArrayList<Discount> getDiscountList() throws SQLException, IOException {
		connect_to_db();
		/* 
		 * Query the database for all the available discounts and 
		 * return them in an arrayList of discounts.
		 * 
		*/
		ArrayList<Discount> rDiscount = new ArrayList<Discount>() ;

		String query = "SELECT * FROM discount;";
		
		Statement stmt = conn.createStatement();
		ResultSet rset = stmt.executeQuery(query);

		while(rset.next()){
				//DiscountID, DiscountName, DiscountType, DiscountAmount
				int id = rset.getInt("DiscountID");
				String dName = rset.getString("DiscountName");
				double dType = rset.getDouble("DiscountType");//Percentage Discount
				double dType2 = rset.getDouble("DiscountAmount");//Dollar Discount
				boolean dDollar = rset.getBoolean("DiscountBoolean");//Percent
				
				//If the the discount is not a percentage, it will do this (sorry for the confusing name).
				if(dDollar != true){
					Discount discount = new Discount(id, dName, dType2, dDollar);
					rDiscount.add(discount);
				}
				else{
					Discount discount = new Discount(id, dName, dType, dDollar);
					rDiscount.add(discount);
				}
		}
		conn.close();
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
		return rDiscount;
	}

	// works
	public static Discount findDiscountByName(String name) {
		try {
			connect_to_db();
			/*
			 * Query the database for a discount using its name.
			 * If found, then return a Discount object for the discount.
			 * If it's not found, then return null.
			 */
			PreparedStatement pstmt;
			ResultSet rs;
			String query = "SELECT * FROM discount WHERE DiscountName = ?";
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, name);
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				int id = rs.getInt("DiscountID");
				String discountName = rs.getString("DiscountName");
				boolean isPercent = rs.getBoolean("DiscountBoolean");
				double amount1 = rs.getDouble("DiscountType");
				double amount2 = rs.getDouble("DiscountAmount");
				if(isPercent == true){
					Discount discount = new Discount(id, discountName, amount1, isPercent);
					return discount;

				}else{
					Discount discount = new Discount(id, discountName, amount2, isPercent);
					return discount;
				}
			} else {
				conn.close();
				
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	// works
	public static ArrayList<Customer> getCustomerList() throws SQLException, IOException {
        connect_to_db();
        /*
         * Query the data for all the customers and return an arrayList of all the customers.
         * Don't forget to order the data coming from the database appropriately.
         *
        */
        ArrayList<Customer> rCustomer = new ArrayList<Customer>();
        String query = "SELECT * FROM customer;";

        Statement stmt = conn.createStatement();
        ResultSet rset = stmt.executeQuery(query);

        while(rset.next()){
            int cID = rset.getInt("Customer_ID");
            String cFName = rset.getString("CustomerFirstName");
            String cLName = rset.getString("CustomerLastName");
            String cPhone = rset.getString("CustomerPhoneNum");
            Customer theCus = new Customer(cID, cFName, cLName, cPhone);
            rCustomer.add(theCus);
        }
        conn.close();
        return rCustomer;
    }

	// works
	public static Customer findCustomerByPhone(String phoneNumber) throws SQLException, IOException{
		/*
		 * Query the database for a customer using a phone number.
		 * If found, then return a Customer object for the customer.
		 * If it's not found....then return null
		 *  
		 */
		Connection conn;
		conn = DBConnector.make_connection();

		try{

        String query = "SELECT * FROM customer WHERE  CustomerPhoneNum = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
		stmt.setString(1, phoneNumber);
		ResultSet rset = stmt.executeQuery();
       
         if(rset.next()){
			int CustID = rset.getInt("Customer_ID");
            String FName = rset.getString("CustomerFirstName");
			String LName = rset.getString("CustomerLastName");
			String Phone = rset.getString("CustomerPhoneNum");

            Customer customer = new Customer(CustID, FName, LName, Phone);
			return customer;
		 }else {
			conn.close();
			return null;
		}
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	// works
	public static ArrayList<Topping> getToppingList() throws SQLException, IOException {
        connect_to_db();
        /*
         * Query the database for the aviable toppings and
         * return an arrayList of all the available toppings.
         * Don't forget to order the data coming from the database appropriately.
         *
         */
        ArrayList<Topping> rTopping = new ArrayList<Topping>();
        String query = "SELECT * FROM topping;";

        Statement stmt = conn.createStatement();
        ResultSet rset = stmt.executeQuery(query);

        while(rset.next()){
            //,ToppingAmountTopMedium,ToppingAmountTopLarge,ToppingAmountTopXlarge
           
            int topID = rset.getInt("ToppingID");
            String topName = rset.getString("ToppingName");
            double topPC = rset.getDouble("ToppingPriceCustomer");
            double topPB = rset.getDouble("ToppingPriceBusiness");
            int topCurInv = rset.getInt("ToppingCurrentInventory");
            int topMinInv = rset.getInt("ToppingMinimumInventory");
            double topAmoSml = rset.getDouble("ToppingAmountTopSmall");
            double topAmoMed = rset.getDouble("ToppingAmountTopMedium");
            double topAmoLar = rset.getDouble("ToppingAmountTopLarge");
            double topAmoXlg = rset.getDouble("ToppingAmountTopXlarge");

            Topping nTop = new Topping(topID, topName, topAmoSml, topAmoMed, topAmoLar, topAmoXlg,topPC, topPB, topMinInv, topCurInv);
            rTopping.add(nTop);
        }
        conn.close();
        return rTopping;    
    }

	// works
	public static Topping findToppingByName(String name) throws SQLException, IOException{
		/*
		 * Query the database for the topping using it's name.
		 * If found, then return a Topping object for the topping.
		 * If it's not found....then return null
		 *  
		 */
		Connection conn;
		conn = DBConnector.make_connection();

		try{

        String query = "SELECT * FROM topping WHERE ToppingName = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
		stmt.setString(1, name);
		ResultSet rset = stmt.executeQuery();
       
         if(rset.next()){
			int topID = rset.getInt("ToppingID");
            String topName = rset.getString("ToppingName");
			double custPrice = rset.getDouble("ToppingPriceCustomer");
			double busPrice = rset.getDouble("ToppingPriceBusiness");
			int curINVT = rset.getInt("ToppingCurrentInventory");
			int minINVT = rset.getInt("ToppingMinimumInventory");
			int perAMT = rset.getInt("ToppingAmountTopSmall");
			double medAMT = rset.getDouble("ToppingAmountTopMedium");
			double lgAMT = rset.getDouble("ToppingAmountTopLarge");
			double xlAMT = rset.getDouble("ToppingAmountTopXlarge");
            
            Topping topping = new Topping(topID, topName, perAMT, medAMT, lgAMT, xlAMT, custPrice, busPrice, minINVT, curINVT);
			return topping;
		 }else {
			conn.close();
			return null;
		}
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	// works
	public static void addToInventory(Topping t, double quantity) throws SQLException, IOException {
		connect_to_db();
		/*
		 * Updates the quantity of the topping in the database by the amount specified.
		 * 
		 * */
		String query = "UPDATE topping SET ToppingCurrentInventory = ToppingCurrentInventory + ? WHERE ToppingID = ?";
		PreparedStatement stmt = conn.prepareStatement(query);
		stmt.setDouble(1, quantity);
		stmt.setInt(2, t.getTopID());
		stmt.executeUpdate();
	   
		conn.close();
	}
	
	// works
	public static double getBaseCustPrice(String size, String crust) throws SQLException, IOException {
		connect_to_db();
		/* 
		 * Query the database fro the base customer price for that size and crust pizza.
		 * 
		*/
        String query = "SELECT PizzaSize,PizzaCrustType,PizzaAssoicatedPrice FROM pizza WHERE PizzaSize = " + "\"" + size + "\"" + " AND " + "PizzaCrustType = " + "\"" + crust  + "\"" + ";";
        Statement stmt = conn.createStatement();
        ResultSet rset = stmt.executeQuery(query);
       
        rset.next();
         
        double BasPrice = rset.getDouble("PizzaAssoicatedPrice");
         
		conn.close();
        //DO NOT FORGET TO CLOSE YOUR CONNECTION
        return BasPrice;
	}

	// works
	public static double getBaseBusPrice(String size, String crust) throws SQLException, IOException {
        connect_to_db();
        /*
         * Query the database fro the base business price for that size and crust pizza.
         *
        */
        String query = "SELECT PizzaSize,PizzaCrustType,PizzaCostCompany FROM pizza WHERE PizzaSize = " + "\"" + size + "\"" + " AND " + "PizzaCrustType = " + "\"" + crust  + "\"" + ";";
        Statement stmt = conn.createStatement();
        ResultSet rset = stmt.executeQuery(query);
       
         rset.next();
         
         double BasPricePizza = rset.getDouble("PizzaCostCompany");
         
		 conn.close();
        //DO NOT FORGET TO CLOSE YOUR CONNECTION
        return BasPricePizza;
    }

	// works
	public static void printInventory() throws SQLException, IOException {
        connect_to_db();
        /*
         * Queries the database and prints the current topping list with quantities.
         *  
         * The result should be readable and sorted as indicated in the prompt.
         *
         */

         String name = "";
         int CurrInv;
         int ID;
         String query = "SELECT ToppingID, ToppingName, ToppingCurrentInventory FROM topping ;";
         Statement stmt = conn.createStatement();
         ResultSet rset = stmt.executeQuery(query);
       
         System.out.println("ID Name          CurINVT");
 
         while(rset.next())
         {
             name = rset.getString("ToppingName");
             CurrInv = rset.getInt("ToppingCurrentInventory");
             ID = rset.getInt("ToppingID");
 
             System.out.println(ID + " " + name + "         " + CurrInv);
         }
 
          conn.close();
    }

	// works
	public static void printToppingPopReport() throws SQLException, IOException
    {
        connect_to_db();
        /*
         * Prints the ToppingPopularity view. Remember that this view
         * needs to exist in your DB, so be sure you've run your createViews.sql
         * files on your testing DB if you haven't already.
         *
         * The result should be readable and sorted as indicated in the prompt.
         *
         */
         String query = "SELECT * FROM ToppingPopularity;";
       
         Statement stmt = conn.createStatement();
         ResultSet rset = stmt.executeQuery(query);
       
        System.out.println("Topping         ToppingCount");

         while(rset.next()){
            String topName = rset.getString("Topping");
            int topCount = rset.getInt("ToppingCount");

            System.out.println(topName + "      " + topCount);
         }
         conn.close();
    }

	// works
	public static void printProfitByPizzaReport() throws SQLException, IOException
	{
		connect_to_db();
		/*
		 * Prints the ProfitByPizza view. Remember that this view
		 * needs to exist in your DB, so be sure you've run your createViews.sql
		 * files on your testing DB if you haven't already.
		 * 
		 * The result should be readable and sorted as indicated in the prompt.
		 * 
		 */
		
		 String query = "SELECT * FROM ProfitByPizza;";
		 Statement stmt = conn.createStatement();
		 ResultSet rst = stmt.executeQuery(query);

		 System.out.println("Size    Crust     Profit     Order Month");

		 while(rst.next()){
			String size = rst.getString("Size");
			String crust = rst.getString("Crust");
			String profit = rst.getString("Profit");
			String OrderMonth = rst.getString("Order Month");

			System.out.println(size + "     " + crust + "     " + profit + "     " + OrderMonth);
		 }
		
		 conn.close();
	}
	
	// works
	public static void printProfitByOrderType() throws SQLException, IOException
    {
        connect_to_db();
        /*
         * Prints the ProfitByOrderType view. Remember that this view
         * needs to exist in your DB, so be sure you've run your createViews.sql
         * files on your testing DB if you haven't already.
         *
         * The result should be readable and sorted as indicated in the prompt.
         *
         */
         String query = "SELECT * FROM ProfitByOrderType;";
       
         Statement stmt = conn.createStatement();
         ResultSet rset = stmt.executeQuery(query);

         System.out.println("Order Type      Order Month     TotalOrderPrice     TotalOrderCost     Profit");
       
         while(rset.next()){
           
            String customer = rset.getString("CustomerType");
            Date theDate = rset.getDate("Order Month");
            double tOrderPrice = rset.getDouble("TotalOrderPrice");
            double tOrderCost = rset.getDouble("TotalOrderCost");
            double tOrderProfit = rset.getDouble("Profit");

         System.out.println(customer + "          " + theDate + "       " + tOrderPrice + "                 " + tOrderCost + "           " + tOrderProfit);
         }

         conn.close(); 
    }

	
	
	// works
	public static String getCustomerName(int CustID) throws SQLException, IOException
	{
	/*
		 * This is a helper method to fetch and format the name of a customer
		 * based on a customer ID. This is an example of how to interact with 
		 * your database from Java.  It's used in the model solution for this project...so the code works!
		 * 
		 * OF COURSE....this code would only work in your application if the table & field names match!
		 *
		 */

		 connect_to_db();

		/* 
		 * an example query using a constructed string...
		 * remember, this style of query construction could be subject to sql injection attacks!
		 * 
		 */
		String cname1 = "";
		String query = "Select CustomerFirstName, CustomerLastName From customer WHERE Customer_ID=" + CustID + ";";
		Statement stmt = conn.createStatement();
		ResultSet rset = stmt.executeQuery(query);
		
		while(rset.next())
		{
			cname1 = rset.getString(1) + " " + rset.getString(2); 
		}

		/* 
		* an example of the same query using a prepared statement...
		* 
		*/
		String cname2 = "";
		PreparedStatement os;
		ResultSet rset2;
		String query2;
		query2 = "Select CustomerFirstName, CustomerLastName From customer WHERE Customer_ID=?;";
		os = conn.prepareStatement(query2);
		os.setInt(1, CustID);
		rset2 = os.executeQuery();
		while(rset2.next())
		{
			cname2 = rset2.getString("CustomerFirstName") + " " + rset2.getString("CustomerLastName"); // note the use of field names in the getSting methods
		}

		conn.close();
		return cname1; // OR cname2
	}

	/*
	 * The next 3 private methods help get the individual components of a SQL datetime object. 
	 * You're welcome to keep them or remove them.
	 */
	private static int getYear(String date)// assumes date format 'YYYY-MM-DD HH:mm:ss'
	{
		return Integer.parseInt(date.substring(0,4));
	}
	private static int getMonth(String date)// assumes date format 'YYYY-MM-DD HH:mm:ss'
	{
		return Integer.parseInt(date.substring(5, 7));
	}
	private static int getDay(String date)// assumes date format 'YYYY-MM-DD HH:mm:ss'
	{
		return Integer.parseInt(date.substring(8, 10));
	}

	public static boolean checkDate(int year, int month, int day, String dateOfOrder)
	{
		if(getYear(dateOfOrder) > year)
			return true;
		else if(getYear(dateOfOrder) < year)
			return false;
		else
		{
			if(getMonth(dateOfOrder) > month)
				return true;
			else if(getMonth(dateOfOrder) < month)
				return false;
			else
			{
				if(getDay(dateOfOrder) >= day)
					return true;
				else
					return false;
			}
		}
	}


}