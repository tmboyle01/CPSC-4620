package cpsc4620;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import cpsc4620.DBNinja;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;


/*
 * This file is where the front end magic happens.
 * 
 * You will have to write the methods for each of the menu options.
 * 
 * This file should not need to access your DB at all, it should make calls to the DBNinja that will do all the connections.
 * 
 * You can add and remove methods as you see necessary. But you MUST have all of the menu methods (including exit!)
 * 
 * Simply removing menu methods because you don't know how to implement it will result in a major error penalty (akin to your program crashing)
 * 
 * Speaking of crashing. Your program shouldn't do it. Use exceptions, or if statements, or whatever it is you need to do to keep your program from breaking.
 * 
 */

public class Menu {

	public static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

	public static void main(String[] args) throws SQLException, IOException {

		System.out.println("Welcome to Pizzas-R-Us!");
		
		int menu_option = 0;

		// present a menu of options and take their selection

		DBNinja dbNinja = new DBNinja();
		
		PrintMenu();
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String option = reader.readLine();
		menu_option = Integer.parseInt(option);

		while (menu_option != 9) {
			switch (menu_option) {
			case 1:// enter order
				EnterOrder();
				break;
			case 2:// view customers
				viewCustomers();
				break;
			case 3:// enter customer
				EnterCustomer();
				break;
			case 4:// view order
				// open/closed/date
				ViewOrders();
				break;
			case 5:// mark order as complete
				MarkOrderAsComplete();
				break;
			case 6:// view inventory levels
				ViewInventoryLevels();
				break;
			case 7:// add to inventory
				AddInventory();
				break;
			case 8:// view reports
				PrintReports();
				break;
			}
			PrintMenu();
			option = reader.readLine();
			menu_option = Integer.parseInt(option);
		}

	}

	// allow for a new order to be placed
	public static void EnterOrder() throws SQLException, IOException
    {
        /*
         * EnterOrder should do the following:
         *
         * Ask if the order is delivery, pickup, or dinein
         *   if dine in....ask for table number
         *   if pickup...
         *   if delivery...
         *
         * Then, build the pizza(s) for the order (there's a method for this)
         *  until there are no more pizzas for the order
         *  add the pizzas to the order
         *
         * Apply order discounts as needed (including to the DB)
         *
         * return to menu
         *
         * make sure you use the prompts below in the correct order!
         */

		// get last order
		String query = "SELECT * FROM `order` ORDER BY Order_ID DESC LIMIT 1";
		Connection conn = DBConnector.make_connection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(query);

		int orderID = 0;
		if (rs.next()) {
			orderID = rs.getInt(1);
		}
		orderID++;

		rs.close();

		String dateString = "2023-06-08";
		Order order = new Order(orderID, 1, "", dateString, 0.0, 0.0, 0);

         // User Input Prompts...
        System.out.println("Is this order for: \n1.) Dine-in\n2.) Pick-up\n3.) Delivery\nEnter the number of your choice:");
		String oType = reader.readLine();
		switch (oType) {
			case "1":
				order.setOrderType("Dine-in");
				System.out.println("What is the table number for this order?");
				String TableNum = reader.readLine();
				break;
			case "2":
				order.setOrderType("Pick-up");
				break;
			case "3":
				order.setOrderType("Delivery");
				break;
			default:
				System.out.println("Error");
				break;
		}
	
		int custID1= 0;
		if(!oType.equals("1")){

        System.out.println("Is this order for an existing customer? Answer y/n: ");
		String existCust = reader.readLine();
		switch (existCust) {
			case "y":
				System.out.println("Here's a list of the current customers: ");
				viewCustomers();
        		System.out.println("Which customer is this order for? Enter ID Number:");
				String custIDNum = reader.readLine();
				int customerID = Integer.parseInt(custIDNum);
				order.setCustID(customerID);
				break;
			case "n":
				EnterCustomer();

				String query1 = "SELECT * FROM customer ORDER BY Customer_ID DESC LIMIT 1";
				Connection conn1 = DBConnector.make_connection();
				Statement stmt1 = conn1.createStatement();
				ResultSet rs1 = stmt1.executeQuery(query1);
		
				if (rs1.next()) {
					custID1 = rs1.getInt(1);
				}
				rs1.close();

				order.setCustID(custID1);

				break;
			default:
				System.out.println("Error");
				break;
		}
	}

		if(oType.equals("3")){
			System.out.println("What is the House/Apt Number for this order? (e.g., 111)");
			String houseNum = reader.readLine();
			System.out.println("What is the Street for this order? (e.g., Smile Street)");
			String street = reader.readLine();
			System.out.println("What is the City for this order? (e.g., Greenville)");
			String city = reader.readLine();
			System.out.println("What is the State for this order? (e.g., SC)");
			String state = reader.readLine();
			System.out.println("What is the Zip Code for this order? (e.g., 20605)");
			String zip = reader.readLine();

			String query1 = "UPDATE customer SET CustomerHouseNum = ?, CustomerStreet = ?, CustomerCity = ?, CustomerState = ?, CustomerZipCode = ? WHERE Customer_ID = ?";
			Connection conn1 = DBConnector.make_connection();
			PreparedStatement stmt1 = conn1.prepareStatement(query1);
			stmt1.setString(1, houseNum);
			stmt1.setString(2, street);
			stmt1.setString(3, city);
			stmt1.setString(4, state);
			stmt1.setString(5, zip);
			stmt1.setInt(6, custID1);

			stmt1.executeUpdate();
			conn1.close();
		}

		DBNinja.addOrder(order);
        // System.out.println("ERROR: I don't understand your input for: Is this order an existing customer?");
        System.out.println("Let's build a pizza!");
		Pizza pizza = buildPizza(orderID);
		



        System.out.println("Enter -1 to stop adding pizzas...Enter anything else to continue adding pizzas to the order.");
        System.out.println("Do you want to add discounts to this order? Enter y/n?");
        System.out.println("Which Order Discount do you want to add? Enter the DiscountID. Enter -1 to stop adding Discounts: ");

       
       
        System.out.println("Finished adding order...Returning to menu...");
    }

	
	
	public static void viewCustomers() throws SQLException, IOException 
	{
		Connection conn; //connect_to_db(), look at DBNinja for example
        conn = DBConnector.make_connection(); // Is this okay, should I call the function?

        
        String query = "SELECT Customer_ID,CustomerFirstName,CustomerLastName,CustomerPhoneNum FROM customer;";
        Statement stmt = conn.createStatement();
        ResultSet rset = stmt.executeQuery(query);
        
        while(rset.next()){
            int id = rset.getInt("Customer_ID"); //int Thing about duplicate entry...
            String first_Name = rset.getString ("CustomerFirstName"); //String
            String last_Name = rset.getString ("CustomerLastName");//String
            String phone_Num = rset.getString ("CustomerPhoneNum");//String
            //String the_Address = rset2.getString ("Address"); String (What to do if it doesn't exist)

            System.out.println("CustID=" + id + " | Name= " + first_Name + " " + last_Name + ", Phone= " + phone_Num);
          }
        
	}
	

	// Enter a new customer in the database
	public static void EnterCustomer() throws SQLException, IOException
    {
        /*
         * Ask for the name of the customer:
         *   First Name <space> Last Name
         *
         * Ask for the  phone number.
         *   (##########) (No dash/space)
         *
         * Once you get the name and phone number, add it to the DB
         */
       
        Connection conn;
        conn = DBConnector.make_connection();

        // User Input Prompts...
         System.out.println("What is this customer's name (first <space> last)");

         String cus_FullName = reader.readLine();
         String[] nameParts = cus_FullName.split(" ");// Splits the First Name
       
         // First part is the first name and the second part is the last name
         String firstName = nameParts[0];
         String lastName = nameParts[1];

         System.out.println("What is this customer's phone number (##########) (No dash/space)");
        //Error Check? for more than ten
         String phone_num = reader.readLine();

         String query = "INSERT INTO customer (CustomerFirstName,CustomerLastName,CustomerPhoneNum) VALUES (?,?,?);";
         PreparedStatement statement = conn.prepareStatement(query);

         statement.setString(1, firstName);
         statement.setString(2, lastName);
         statement.setString(3, phone_num);
         statement.executeUpdate();

         conn.close();
    }



	// View any orders that are not marked as completed
	public static void ViewOrders() throws SQLException, IOException 
	{
		/*  
		* This method allows the user to select between three different views of the Order history:
		* The program must display:
		* a.	all open orders
		* b.	all completed orders 
		* c.	all the orders (open and completed) 
		  d. since a specific date (inclusive)
		* 
		* After displaying the list of orders (in a condensed format) must allow the user to select a specific order for viewing its details.  
		* The details include the full order type information, the pizza information (including pizza discounts), and the order discounts.
		* 
		*/ 
		
		boolean loopOption = false;

		//Controls if the user wants to see more about displaced order
		boolean moreDetail = false;		

		Connection conn; //connect_to_db(), look at DBNinja for example
		conn = DBConnector.make_connection(); // Is this okay, should I call the function?
		
		// User Input Prompts...
		System.out.println("Would you like to:\n(a) display all orders [open or closed]\n(b) display all open orders\n(c) display all completed [closed] orders\n(d) display orders since a specific date");
		   
		  while(loopOption != true){
			String sOption = reader.readLine();
			String query = "";
		  	
			//Make the cases swithc the query!
		   switch (sOption) {
			//* a.	all orders
			 case "a":
			 query = "SELECT Order_ID,OrderCustomer,OrderType,OrderStatus FROM `order`;";//Order Status?
			 break;
			 //* b.	all uncompleted orders (false)
			 case "b":
			 query = "SELECT Order_ID,OrderCustomer,OrderType,OrderStatus FROM `order` WHERE OrderStatus = 'false';";//Order Status?
			 break;
			 // c.	all the orders (open and completed) 
			 case "c":
			 query = "SELECT Order_ID,OrderCustomer,OrderType,OrderStatus FROM `order` WHERE OrderStatus = 'true';";//Order Status?
			 break;
			 //since a specific date (inclusive)
			case "d":
			  
			  System.out.println("What is the date you want to restrict by? (FORMAT= YYYY-MM-DD)");
			  String storeDate = reader.readLine();
			  System.out.println("You Entered:" + storeDate);

			  query = "SELECT Order_ID,OrderCustomer,OrderType,OrderStatus FROM `order` WHERE OrderTimeStamp >= '" + storeDate + "';";

				//iF passed 2025, how to deal with error? (the No Order to Display)
		        //   System.out.println("Incorrect entry, returning to menu.");
		        //   System.out.println("No orders to display, returning to menu.");
			break;
			
			default:
			 System.out.println("I don't understand that input, returning to menu");
			 loopOption = true;
			 moreDetail = true;
		 }//End of Switch

	      if (loopOption != true) {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(query);

				while(rset.next()){
				  int id = rset.getInt("Order_ID");
				  String cus_Name = rset.getString("OrderCustomer");
				  String type = rset.getString("OrderType");
				  String status = rset.getString("OrderStatus");

					System.out.println("OrderID=" + id + " | Customer name= " + cus_Name + ", OrderType= " + type + ", IsComplete =" + status);
				}
		  }
		  //See if Person Wants more Detail on one of the orders.
		  while(moreDetail != true){
			System.out.println("Which order would you like to see in detail? Enter the number (-1 to exit): ");//Has to be apart of every Choice.
			String detailChoiceID = reader.readLine();

			 if(detailChoiceID.equals("-1")){
				// System.out.println("We are Exiting");
				loopOption = true;
				moreDetail = true;
			 }

			 else{
				//This Gets the Detailed Order
				String query2 = "SELECT Order_ID,OrderTotalCostBusiness,OrderTimeStamp,OrderTotalPrice,OrderType,OrderStatus,OrderCustomer FROM `order` WHERE Order_ID = " + detailChoiceID + ";";
				Statement stmt2 = conn.createStatement();
			    ResultSet rset2 = stmt2.executeQuery(query2);
				Date placed = new Date();
				  while(rset2.next()){
				    int id = rset2.getInt("Order_ID");
				    String cus_Name = rset2.getString("OrderCustomer");
				    placed = rset2.getDate("OrderTimeStamp");
				    String type = rset2.getString("OrderType");
				    double cusPrice = rset2.getDouble("OrderTotalPrice");
				    double busPrice = rset2.getDouble("OrderTotalCostBusiness");//Check if Order had discount
				    String status = rset2.getString("OrderStatus");
				
				
				   System.out.println("OrderID="+id + " | For customer: " + cus_Name + " | OrderType= " + type + ", Placed on:" + placed + " | CustPrice= " + cusPrice + ", BusPrice= " + busPrice + " | ");//What about table number.

				  }

				  //Checks to See if order Has Discount
				   String subquery1 = "SELECT OrderDiscountDiscountID,OrderDiscountOrderID FROM orderdiscount WHERE OrderDiscountOrderID = " + detailChoiceID + ";";
				   Statement stmtsq1 = conn.createStatement();
			       ResultSet rsetsq1 = stmtsq1.executeQuery(subquery1);

				   if(!rsetsq1.next()){
					System.out.println("NO ORDER DISCOUNTS");
				   }
				   else{
					// System.out.println("THERE IS ORDER DISCOUNTS");
					// ArrayList<Integer> dNumbers = new ArrayList<Integer>();
					// while(rsetsq1.next()){

					//  int oDiscountID = rsetsq1.getInt("OrderDiscountDiscountID");

					// }

					// String subquery2 = "SELECT  FROM discount WHERE OrderDiscountOrderID = " + detailChoiceID + ";";
				    // Statement stmtsq2 = conn.createStatement();
			        // ResultSet rsetsq2 = stmtsq1.executeQuery(subquery2);
				   }
				  	
				  //PizzaID,CrustType,Size,OrderID,PizzaStatus,TimeStamp, Customer Price,Business price, Pizza Discount name.
				String query3 = "SELECT PizzaID,PizzaCrustType,PizzaSize,PizzaOrderID,PizzaStatus,PizzaAssoicatedPrice,PizzaCostCompany FROM pizza WHERE PizzaOrderID = " + detailChoiceID + ";";
				Statement stmt3 = conn.createStatement();
			    ResultSet rset3 = stmt3.executeQuery(query3);

				while(rset3.next()){
					int pID = rset3.getInt("PizzaID");
					String pCrustT = rset3.getString("PizzaCrustType");
					String pSize = rset3.getString("PizzaSize");
					int pOrderID = rset3.getInt("PizzaOrderID");
					String pStatus = rset3.getString("PizzaStatus");
					Double pCustP = rset3.getDouble("PizzaAssoicatedPrice");
					Double pBusP = rset3.getDouble("PizzaCostCompany");

					System.out.println("PizzaID=" + pID + " | CrustType= " + pCrustT + ", Size=" + pSize + " For order " + pOrderID + " | Pizza Status: " + pStatus + ", as of " + placed + " | Customer Price= " + pCustP + " | Business Price= " + pBusP);
				}
				loopOption = true;
				moreDetail = true;
			 }
		    }//End of More Detail While
		  }//End of While loop
		  conn.close();
	}



	
	// When an order is completed, we need to make sure it is marked as complete
	// selection 5
	public static void MarkOrderAsComplete() throws SQLException, IOException
    {
        /*
         * All orders that are created through java (part 3, not the orders from part 2) should start as incomplete
         *
         * When this method is called, you should print all of the "opoen" orders marked
         * and allow the user to choose which of the incomplete orders they wish to mark as complete
         *
         */

         Connection conn; //connect_to_db(), look at DBNinja for example
         conn = DBConnector.make_connection(); // Is this okay, should I call the function?

         boolean loop = false;
         while(loop != true){
         System.out.println("Which order would you like mark as complete? Enter the OrderID: ");
         String detailChoiceID = reader.readLine();

         String query = "SELECT OrderStatus FROM `order` WHERE Order_ID = " + detailChoiceID + ";";
         Statement stmt3 = conn.createStatement();
         ResultSet rset3 = stmt3.executeQuery(query);

          if(!rset3.next()){
            System.out.println("Incorrect entry, not an option");
            loop = true;
           }
           
           else{
            String query2 = "UPDATE `order` SET orderStatus = \"true\" WHERE Order_ID = " + detailChoiceID + ";";
            PreparedStatement stmt = conn.prepareStatement(query2);
            stmt.executeUpdate();
           
            loop = true;
           }
        }//End of While
        conn.close();
    }

	// number 6 "view inventory levels"
	public static void ViewInventoryLevels() throws SQLException, IOException 
	{
		/*
		 * Print the inventory. Display the topping ID, name, and current inventory
		*/
		
		Connection conn;
		conn = DBConnector.make_connection();

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

			System.out.println(ID + " " + name + "           " + CurrInv);
		}

		conn.close();
	}

	// number 7 "Add inventory"
	public static void AddInventory() throws SQLException, IOException 
	{
		/*
		 * This should print the current inventory and then ask the user which topping (by ID) they want to add more to and how much to add
		 */
		// prints inventory levels
		ViewInventoryLevels();
		
		// User Input Prompts...
		System.out.println("Which topping do you want to add inventory to? Enter the number: ");
		// take in string
		String toppingtNum = reader.readLine();
		// convert to integer
		int topnum = Integer.parseInt(toppingtNum);
		// if input is not a valid choice, ask again
		while(topnum < 0 || topnum > 17){
			System.out.println("Incorrect entry, not an option");
			System.out.println("Which topping do you want to add inventory to? Enter the number: ");
			// take in string
			toppingtNum = reader.readLine();
			// convert to integer
			topnum = Integer.parseInt(toppingtNum);
		}
		// convert to string
		toppingtNum = Integer.toString(topnum);

		System.out.println("How many units would you like to add? ");
		String unitsNum = reader.readLine();
		Double unitsDouble = Double.parseDouble(unitsNum);

		// creates topping instance
		Topping top = new Topping(topnum, null, 0, 0, 0, 0, 0, 0, 0, 0);
		// calls addToInventory function
		DBNinja.addToInventory(top, unitsDouble);
	}



	// A method that builds a pizza. Used in our add new order method
	public static Pizza buildPizza(int orderID) throws SQLException, IOException {
		Pizza ret = null;
		String pizzaCont;

		String query1 = "SELECT * FROM pizza ORDER BY PizzaID DESC LIMIT 1";
		Connection conn1 = DBConnector.make_connection();
		Statement stmt1 = conn1.createStatement();
		ResultSet rs1 = stmt1.executeQuery(query1);

		int pizzaID1 = 0;
		if (rs1.next()) {
			pizzaID1 = rs1.getInt(1);
		}
		rs1.close();

		pizzaID1++;
	
		do {
			ret = new Pizza(pizzaID1, "", "", orderID, "notcomplete", "", 0, 0);
	
			// Take in pizza size
			System.out.println("What size is the pizza?");
			System.out.println("1." + DBNinja.size_s);
			System.out.println("2." + DBNinja.size_m);
			System.out.println("3." + DBNinja.size_l);
			System.out.println("4." + DBNinja.size_xl);
			System.out.println("Enter the corresponding number: ");
			String pizzaSize = reader.readLine();
			switch (pizzaSize) {
				case "1":
					ret.setSize(DBNinja.size_s);
					break;
				case "2":
					ret.setSize(DBNinja.size_m);
					break;
				case "3":
					ret.setSize(DBNinja.size_l);
					break;
				case "4":
					ret.setSize(DBNinja.size_xl);
					break;
				default:
					System.out.println("Error");
					break;
			}
	
			// Take in crust type
			System.out.println("What crust for this pizza?");
			System.out.println("1." + DBNinja.crust_thin);
			System.out.println("2." + DBNinja.crust_orig);
			System.out.println("3." + DBNinja.crust_pan);
			System.out.println("4." + DBNinja.crust_gf);
			System.out.println("Enter the corresponding number: ");
			String crustType = reader.readLine();
			switch (crustType) {
				case "1":
					ret.setCrustType(DBNinja.crust_thin);
					break;
				case "2":
					ret.setCrustType(DBNinja.crust_orig);
					break;
				case "3":
					ret.setCrustType(DBNinja.crust_pan);
					break;
				case "4":
					ret.setCrustType(DBNinja.crust_gf);
					break;
				default:
					System.out.println("Error");
					break;
			}
	
			System.out.println("Available Toppings:");
			DBNinja.printInventory();
			System.out.println("Which topping do you want to add? Enter the TopID. Enter -1 to stop adding toppings: ");
			String toppingType = reader.readLine();
			ArrayList<Topping> toppings = new ArrayList<>();
			while (!toppingType.equals("-1")) {
				// currently taking in num as string but needs to be name
				// converts number to topping name
				String TopType = getToppingString(toppingType);

				Topping topping = DBNinja.findToppingByName(TopType);
				if (topping != null) {
					toppings.add(topping);
					System.out.println("Do you want to add extra topping? Enter y/n");
					String extra = reader.readLine();
					if (extra.equalsIgnoreCase("y")) {
						if (topping.getCurINVT() < topping.getPerAMT() * 2) {
							System.out.println("We don't have enough of that topping to add extra...");
						} else {
							DBNinja.useTopping(ret, topping, true);
						}
					} else {
						DBNinja.useTopping(ret, topping, false);
					}
				} else {
					System.out.println("Invalid topping ID. Please try again.");
				}
				System.out.println("Which topping do you want to add? Enter the TopID. Enter -1 to stop adding toppings: ");
				toppingType = reader.readLine();
			}
	
			ret.setToppings(toppings);
	
			System.out.println("Do you want to add discounts to this Pizza? Enter y/n?");
			String discountTypePizza = reader.readLine();
			if (discountTypePizza.equalsIgnoreCase("y")) {
				ArrayList<Discount> discounts = DBNinja.getDiscountList();
				for (Discount discount : discounts) {
					System.out.println(discount.getDiscountID() + ". " + discount.getDiscountName());
				}
				System.out.println("Which Pizza Discount do you want to add? Enter the DiscountID. Enter -1 to stop adding Discounts: ");
				String pizzaDiscount = reader.readLine();
				while (!pizzaDiscount.equals("-1")) {

					String str3 = getSizeString3(pizzaDiscount);

					Discount discount = DBNinja.findDiscountByName(str3);
					if (discount != null) {
						// DBNinja.usePizzaDiscount(ret, discount);
					} else {
						System.out.println("Invalid discount ID. Please try again.");
					}
					System.out.println("Do you want to add more discounts to this Pizza? Enter y/n?");
					String addMore = reader.readLine();
					if (!addMore.equalsIgnoreCase("y")) {
						break;
					}
					System.out.println("Which Pizza Discount do you want to add? Enter the DiscountID. Enter -1 to stop adding Discounts: ");
					pizzaDiscount = reader.readLine();
				}
			}
	
			DBNinja.addPizza(ret);

			String str1 = getSizeString(pizzaSize);
			String str2 = getSizeString2(crustType);

			Connection conn2 = DBConnector.make_connection();
			String queryBP = "SELECT BasePriceCustomer,BasePriceBusiness FROM baseprice WHERE BasePricePizzaSize = \"" + str1 + "\" AND BasePriceCrustType = \"" + str2 + "\";" ;
			Statement stmt2 = conn2.createStatement();
			ResultSet rs2 = stmt2.executeQuery(queryBP);

			double cusPriceSave = 0.0;
			double busPriceSave = 0.0;
			if(rs2.next()){
			cusPriceSave = rs2.getDouble("BasePriceCustomer");
			busPriceSave = rs2.getDouble("BasePriceBusiness");
			}
			conn2.close();
		
			Connection conn3 = DBConnector.make_connection();
			String queryAddToPizza = "UPDATE pizza SET PizzaAssoicatedPrice = ?, PizzaCostCompany = ? WHERE PizzaID = ?";
			PreparedStatement stmt34 = conn3.prepareStatement(queryAddToPizza);
		
			stmt34.setDouble(1, cusPriceSave);
			stmt34.setDouble(2,busPriceSave);
			stmt34.setInt(3, pizzaID1);
			stmt34.executeUpdate();

			conn3.close();
	
			System.out.println("Enter -1 to stop adding pizzas...Enter anything else to continue adding pizzas to the order.");
			pizzaCont = reader.readLine();
		} while (!pizzaCont.equals("-1"));
	
		return ret;
	}
	
	
	public static void PrintReports() throws SQLException, NumberFormatException, IOException
	{
		/*
		 * This method asks the use which report they want to see and calls the DBNinja method to print the appropriate report.
		 * 
		 */

		// User Input Prompts...
		System.out.println("Which report do you wish to print? Enter\n(a) ToppingPopularity\n(b) ProfitByPizza\n(c) ProfitByOrderType:");
		String report = reader.readLine();
		if(report.equals("a")){
			DBNinja.printToppingPopReport();
		}
		else if(report.equals("b")){
			DBNinja.printProfitByPizzaReport();
		}
		else if(report.equals("c")){
			DBNinja.printProfitByOrderType();
		}
		else{
			System.out.println("I don't understand that input... returning to menu...");
		}
	}

	//Prompt - NO CODE SHOULD TAKE PLACE BELOW THIS LINE
	// DO NOT EDIT ANYTHING BELOW HERE, THIS IS NEEDED TESTING.
	// IF YOU EDIT SOMETHING BELOW, IT BREAKS THE AUTOGRADER WHICH MEANS YOUR GRADE WILL BE A 0 (zero)!!

	public static void PrintMenu() {
		System.out.println("\n\nPlease enter a menu option:");
		System.out.println("1. Enter a new order");
		System.out.println("2. View Customers "); // works
		System.out.println("3. Enter a new Customer "); // works
		System.out.println("4. View orders"); // works
		System.out.println("5. Mark an order as completed");// ??
		System.out.println("6. View Inventory Levels"); // works
		System.out.println("7. Add Inventory"); // works
		System.out.println("8. View Reports");// works
		System.out.println("9. Exit\n\n");
		System.out.println("Enter your option: ");
	}

	/*
	 * autograder controls....do not modiify!
	 */

	public final static String autograder_seed = "6f1b7ea9aac470402d48f7916ea6a010";

	
	private static void autograder_compilation_check() {

		try {
			Order o = null;
			Pizza p = null;
			Topping t = null;
			Discount d = null;
			Customer c = null;
			ArrayList<Order> alo = null;
			ArrayList<Discount> ald = null;
			ArrayList<Customer> alc = null;
			ArrayList<Topping> alt = null;
			double v = 0.0;
			String s = "";

			DBNinja.addOrder(o);
			DBNinja.addPizza(p);
			DBNinja.useTopping(p, t, false);
			DBNinja.usePizzaDiscount(p, d);
			DBNinja.useOrderDiscount(o, d);
			DBNinja.addCustomer(c);
			DBNinja.completeOrder(o);
			alo = DBNinja.getOrders(false);
			o = DBNinja.getLastOrder();
			alo = DBNinja.getOrdersByDate("01/01/1999");
			ald = DBNinja.getDiscountList();
			d = DBNinja.findDiscountByName("Discount");
			alc = DBNinja.getCustomerList();
			c = DBNinja.findCustomerByPhone("0000000000");
			alt = DBNinja.getToppingList();
			t = DBNinja.findToppingByName("Topping");
			DBNinja.addToInventory(t, 1000.0);
			v = DBNinja.getBaseCustPrice("size", "crust");
			v = DBNinja.getBaseBusPrice("size", "crust");
			DBNinja.printInventory();
			DBNinja.printToppingPopReport();
			DBNinja.printProfitByPizzaReport();
			DBNinja.printProfitByOrderType();
			s = DBNinja.getCustomerName(0);
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
	}

//-------------
public static String getToppingString(String toppingNumberString) {
    int toppingNumber;
    try {
        toppingNumber = Integer.parseInt(toppingNumberString);
    } catch (NumberFormatException e) {
        return "Invalid Topping Number";
    }

    switch (toppingNumber) {
        case 1:
            return "Pepperoni";
        case 2:
            return "Sausage";
        case 3:
            return "Ham";
        case 4:
            return "Chicken";
        case 5:
            return "Green Pepper";
        case 6:
            return "Onion";
        case 7:
            return "Roma Tomato";
        case 8:
            return "Mushrooms";
        case 9:
            return "Black Olives";
        case 10:
            return "Pineapple";
        case 11:
            return "Jalapenos";
        case 12:
            return "Banana Peppers";
        case 13:
            return "Regular Cheese";
        case 14:
            return "Four Cheese Blend";
        case 15:
            return "Feta Cheese";
        case 16:
            return "Goat Cheese";
        case 17:
            return "Bacon";
        default:
            return "Unknown Topping";
    }
}


public static String getSizeString(String sizeNumber) {
    switch (sizeNumber) {
        case "1":
            return "Small";
        case "2":
            return "Medium";
        case "3":
            return "Large";
        case "4":
            return "XLarge";
        default:
            return "unknown";
    }
}

public static String getSizeString2(String sizeNumber) {
    switch (sizeNumber) {
        case "1":
            return "Thin";
        case "2":
            return "Original";
        case "3":
            return "Pan";
        case "4":
            return "Gluten-Free";
        default:
            return "unknown";
    }
}


public static String getSizeString3(String sizeNumber) {
    switch (sizeNumber) {
        case "1":
            return "Employee";
        case "2":
            return "Lunch Special Medium"; 
        case "3":
            return "Lunch Special Large";
        case "4":
            return "Specialty Pizza";
		case "5":
            return "Happy Hour";
		case "6":
            return "Gameday Special";
        default:
            return "unknown";
    }
}




}


