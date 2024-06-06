USE PIZZERIA;

INSERT INTO discount (DiscountID, DiscountName, DiscountType, DiscountAmount,DiscountBoolean)
VALUES (1, 'Employee', 0.15, NULL,true),
(2, 'Lunch Special Medium', NULL, 1.00,false),
(3, 'Lunch Special Large', NULL, 2.00,false),
(4, 'Specialty Pizza', NULL, 1.50,false),
(5, 'Happy Hour', 0.10, NULL,true),
(6, 'Gameday Special', 0.20, NULL,true);


INSERT INTO baseprice (BaseP_ID, BasePricePizzaSize, BasePriceCrustType, BasePriceCustomer, BasePriceBusiness)
Values (1, 'Small', 'Thin', 3, 0.5),
(2, 'Small', 'Original', 3, 0.75),
(3, 'Small', 'Pan', 3.5, 1),
(4, 'Small', 'Original', 3, 0.75),
(5, 'Small', 'Gluten-Free', 4, 2),
(6, 'Medium', 'Thin', 5, 1),
(7, 'Medium', 'Original', 5, 1.5),
(8, 'Medium', 'Thin', 5, 1),
(9, 'Medium', 'Pan', 6, 2.25),
(10, 'Medium', 'Gluten-Free', 6.25, 3),
(11, 'Large', 'Thin', 8, 1.25),
(12, 'Large', 'Original', 8, 2),
(13, 'Large', 'Pan', 9, 3),
(14, 'Large', 'Gluten-Free', 9.5, 4),
(15, 'XLarge', 'Thin', 10, 2),
(16, 'XLarge', 'Original', 10, 3),
(17, 'XLarge', 'pan', 11.5, 4.5),
(18, 'XLarge', 'Gluten-Free', 12.5, 6);

INSERT INTO topping (ToppingID,ToppingName,ToppingPriceCustomer,
ToppingPriceBusiness,ToppingCurrentInventory,ToppingMinimumInventory,ToppingAmountTopSmall,ToppingAmountTopMedium,ToppingAmountTopLarge,ToppingAmountTopXlarge)
VALUES (1, 'Pepperoni' , 1.25 , 0.2 , 100 , 50 , 2 , 2.75 , 3.5 , 4.5),
  (2, 'Sausage', 1.25 , 0.15, 100 , 50, 2.5, 3 , 3.5 , 4.25),
       (3, 'Ham', 1.5, 0.15, 78, 25, 2, 2.5, 3.25, 4),
       (4, 'Chicken', 1.75, 0.25, 56, 25,1.5,2,2.25,3),
       (5, 'Green Pepper', 0.5,0.02,79,25,1,1.5,2,2.5),
       (6, 'Onion', 0.5, 0.02, 85, 25,1,1.5,2,2.75),
       (7, 'Roma Tomato', 0.75, 0.03,86,10,2,3,3.5,4.5),
       (8, 'Mushrooms', 0.75, 0.1, 52, 50, 1.5, 2, 2.5,3),
       (9,'Black Olives', 0.6, 0.1, 39, 25, 0.75, 1, 1.5,2),
       (10, 'Pineapple', 1, 0.25, 15, 0, 1, 1.25, 1.75, 2),
       (11, 'Jalapenos', 0.5, 0.05, 64, 0, 0.5, 0.75, 1.25, 1.75),
       (12, 'Banana Peppers', 0.5, 0.05, 36, 0, 0.6, 1, 1.3, 1.75),
       (13, 'Regular Cheese', 0.5, 0.12, 250, 50, 2, 3.5, 5, 7),
       (14, 'Four Cheese Blend', 1, 0.15, 150, 25, 2, 3.5, 5, 7),
       (15, 'Feta Cheese', 1.5, 0.18, 75, 0, 1.75, 3, 4, 5.5),
       (16, 'Goat Cheese', 1.5, 0.2, 54, 0, 1.6, 2.75, 4, 5.5),
       (17, 'Bacon', 1.5, 0.25, 89, 0, 1, 1.5, 2, 3);
       
       
       
       
-- On March 5th at 12:03 pm there was a dine-in order (at table 21) for a large thin crust pizza with Regular
-- Cheese (extra), Pepperoni, and Sausage (Price: $20.75, Cost: $3.68). They used the “Lunch Special Large”
-- discount for the pizza.
INSERT INTO customer (CustomerFirstName,CustomerLastName)
VALUES('INSTORE', 'customer');

SET @CUS_ID = LAST_INSERT_ID();

INSERT INTO `order`(OrderTotalCostBusiness, OrderTimeStamp, OrderTotalPrice, OrderType,OrderStatus,OrderCustomer,OrderCust_ID)
VALUES (3.68, '2024-3-5', 18.75, 'dinein','true','INSTORE customer',@CUS_ID);

INSERT INTO pizza (PizzaID,PizzaOrderID,PizzaBasePriceID, PizzaSize, PizzaCostCompany, PizzaAssoicatedPrice, PizzaCrustType, PizzaStatus)
VALUES (1, 1, 11 , 'Large', 3.68, 18.75, 'Thin', 'completed');

INSERT INTO dinein (DinInOrder_ID, DineInTableNum)
VALUES (1, 21);

INSERT INTO bridgetopping (BridgeToppingPizzaID, BridgeToppingToppingID, BridgeToppingExtraTopping)
VALUES (1, 13, true),
(1, 1, false),
(1, 2, false);

INSERT INTO pizzadiscount (PizzaDiscountDiscountID, PizzaDiscountPizzaID)
VALUES (3, 1);


-- On April 3rd at 12:05 pm there was a dine-in order (at table 4). They ordered a medium pan pizza with
-- Feta Cheese, Black Olives, Roma Tomatoes, Mushrooms and Banana Peppers (Price: $12.85, Cost: $3.23).
-- They used the “Lunch Special Medium” and the “Specialty Pizza” discounts for the pizza. They also ordered
-- a small original crust pizza with Regular Cheese, Chicken and Banana Peppers (Price: $6.93, Cost: $1.40).

INSERT INTO customer (CustomerFirstName,CustomerLastName)
VALUES('INSTORE', 'customer');
SET @CUS_ID = LAST_INSERT_ID();
INSERT INTO `order`(OrderTotalCostBusiness, OrderTimeStamp, OrderTotalPrice, OrderType,OrderStatus,OrderCustomer,OrderCust_ID)
VALUES (4.63, '2024-4-3', 17.28, 'dinein','true','INSTORE customer',@CUS_ID);


INSERT INTO pizza (PizzaID, PizzaOrderID,PizzaBasePriceID ,PizzaSize, PizzaCostCompany, PizzaAssoicatedPrice, PizzaCrustType, PizzaStatus)
VALUES (2, 2,9,'Medium', 3.23, 10.35, 'Pan', 'completed'),
(3, 2,2,'Small', 1.40, 6.93, 'Original', 'completed');

INSERT INTO dinein (DinInOrder_ID, DineInTableNum)
VALUES (2, 4);

INSERT INTO bridgetopping (BridgeToppingPizzaID, BridgeToppingToppingID, BridgeToppingExtraTopping)
VALUES (2, 15, false),
(2, 9, false),
(2, 7, false),
(2, 8, false),
(2, 12, false),
(3, 13, false),
(3, 4, false),
(3, 12, false);

INSERT INTO pizzadiscount (PizzaDiscountDiscountID, PizzaDiscountPizzaID)
VALUES (2, 2),
(4,2);

-- On March 3rd at 9:30 pm Andrew Wilkes-Krier placed an order for pickup of 6 large original crust pizzas
-- with Regular Cheese and Pepperoni (Price: $14.88, Cost: $3.30 each). Andrew’s phone number is 864-254-
-- 5861.
INSERT INTO customer (CustomerFirstName,CustomerLastName, CustomerPhoneNum)
VALUES('Andrew', 'Wilkes-Krier', '8642545861');

SET @CUS_ID = LAST_INSERT_ID();

INSERT INTO `order`(OrderTotalCostBusiness, OrderTimeStamp, OrderTotalPrice, OrderType,OrderStatus,OrderCustomer,OrderCust_ID)
VALUES (19.80, '2024-3-3', 89.28, 'pickup','true','Andrew Wilkes-Krier',@CUS_ID);

INSERT INTO pizza (PizzaID, PizzaOrderID,PizzaBasePriceID,PizzaSize, PizzaCostCompany, PizzaAssoicatedPrice, PizzaCrustType, PizzaStatus)
VALUES (4, 3,12,'Large', 3.30, 14.88, 'Original', 'completed'),
(5, 3, 12,'Large', 3.30, 14.88, 'Original', 'completed'),
(6, 3, 12,'Large', 3.30, 14.88, 'Original', 'completed'),
(7, 3, 12,'Large', 3.30, 14.88, 'Original', 'completed'),
(8, 3, 12,'Large', 3.30, 14.88, 'Original', 'completed'),
(9, 3, 12,'Large', 3.30, 14.88, 'Original', 'completed');


INSERT INTO pickup (PickUpOrderID, PickupCustomer_ID)
VALUES (3, 1);

INSERT INTO bridgetopping (BridgeToppingPizzaID, BridgeToppingToppingID, BridgeToppingExtraTopping)
VALUES (4, 13, false),
(4, 1, false),
(5, 13, false),
(5, 1, false),
(6, 13, false),
(6, 1, false),
(7, 13, false),
(7, 1, false),
(8, 13, false),
(8, 1, false),
(9, 13, false),
(9, 1, false);

-- On April 20th at 7:11 pm there was a delivery order made by Andrew Wilkes-Krier for 1 xlarge pepperoni
-- and Sausage pizza (Price: $27.94, Cost: $9.19), one xlarge pizza with Ham (extra) and Pineapple (extra)
-- pizza (Price: $31.50, Cost: $6.25), and one xlarge Chicken and Bacon pizza (Price: $26.75, Cost: $8.18). All
-- the pizzas have the Four Cheese Blend on it and are original crust. The order has the “Gameday Special”
-- discount applied to it, and the ham and pineapple pizza has the “Specialty Pizza” discount applied to it. The
-- pizzas were delivered to 115 Party Blvd, Anderson SC 29621. His phone number is the same as before.

INSERT INTO customer (CustomerFirstName,CustomerLastName, CustomerPhoneNum, CustomerHouseNum, CustomerStreet, CustomerCity, CustomerState, CustomerZipCode)
VALUES('Andrew', 'Wilkes-Krier', '8642545861', 115, 'Party Blvd', 'Anderson', 'SC', 29621);

SET @CUS_ID = LAST_INSERT_ID();

INSERT INTO `order`(OrderTotalCostBusiness, OrderTimeStamp, OrderTotalPrice, OrderType,OrderStatus,OrderCustomer,OrderCust_ID)
VALUES (23.62, '2024-4-20', 67.75, 'delivery','true','Andrew Wilkes-Krier',@CUS_ID);

INSERT INTO pizza (PizzaID,PizzaOrderID,PizzaBasePriceID, PizzaSize, PizzaCostCompany, PizzaAssoicatedPrice, PizzaCrustType, PizzaStatus)
VALUES (10, 4, 16 , 'XLarge', 9.19, 22.35, 'Original', 'completed'),
(11, 4, 16, 'XLarge', 6.25, 24.00, 'Original', 'completed'),
(12, 4, 16, 'XLarge', 8.18, 21.40, 'Original', 'completed');


INSERT INTO delivery (DeliveryOrderID, DeliveryCustomerID) 
VALUES (4, 2);

INSERT INTO bridgetopping (BridgeToppingPizzaID, BridgeToppingToppingID, BridgeToppingExtraTopping)
VALUES (10, 14, false),
(10, 1, false),
(10, 2, false),
(11, 14, false),
(11, 3, true),
(11, 10, true),
(12, 14, false),
(12, 4, false),
(12, 17, false);

INSERT INTO pizzadiscount (PizzaDiscountDiscountID, PizzaDiscountPizzaID)
VALUES (4, 11);

INSERT INTO orderdiscount (OrderDiscountDiscountID, OrderDiscountOrderID)
VALUES (6, 4);
       
       
-- On March 2nd at 5:30 pm Matt Engers placed an order for pickup for an xlarge pizza with Green Pepper,
-- Onion, Roma Tomatoes, Mushrooms, and Black Olives on it. He wants the Goat Cheese on it, and a Gluten
-- Free Crust (Price: $27.45, Cost: $7.88). The “Specialty Pizza” discount is applied to the pizza. Matt’s phone
-- number is 864-474-9953.
INSERT INTO customer (CustomerFirstName,CustomerLastName, CustomerPhoneNum)
VALUES('Matt', 'Engers', '8644749953');	

SET @CUS_ID = LAST_INSERT_ID();

INSERT INTO `order`(OrderTotalCostBusiness, OrderTimeStamp, OrderTotalPrice, OrderType,OrderStatus,OrderCustomer,OrderCust_ID)
VALUES (7.88, '2024-3-2', 25.95, 'pickup','true','Matt Engers',@CUS_ID);

INSERT INTO pizza (PizzaID,PizzaOrderID,PizzaBasePriceID, PizzaSize, PizzaCostCompany, PizzaAssoicatedPrice, PizzaCrustType, PizzaStatus)
VALUES (13, 5, 18 , 'XLarge', 7.88, 25.95, 'Gluten-Free', 'completed');


INSERT INTO pickup (PickUpOrderID, PickupCustomer_ID)
VALUES (5, 3);

INSERT INTO bridgetopping (BridgeToppingPizzaID, BridgeToppingToppingID, BridgeToppingExtraTopping)
VALUES (13, 5, false),
(13, 6, false),
(13, 7, false),
(13, 8, false),
(13, 9, false),
(13, 16, false);

INSERT INTO pizzadiscount (PizzaDiscountDiscountID, PizzaDiscountPizzaID)
VALUES (4, 13);

-- On March 2nd at 6:17 pm Frank Turner places an order for delivery of one large pizza with Chicken, Green
-- Peppers, Onions, and Mushrooms. He wants the Four Cheese Blend (extra) and thin crust (Price: $20.81,
-- Cost: $3.19). The pizza was delivered to 6745 Wessex St Anderson SC 29621. Frank’s phone number is 864-
-- 232-8944.
 
INSERT INTO customer (CustomerFirstName,CustomerLastName, CustomerPhoneNum, CustomerHouseNum, CustomerStreet, CustomerCity, CustomerState, CustomerZipCode)
VALUES('Frank', 'Turner', '8642328944', 6745, 'Wessex St', 'Anderson', 'SC', 29621);
     
SET @CUS_ID = LAST_INSERT_ID();
     
INSERT INTO `order`(OrderTotalCostBusiness, OrderTimeStamp, OrderTotalPrice, OrderType,OrderStatus,OrderCustomer,OrderCust_ID)
VALUES (3.19, '2024-3-2', 20.81, 'delivery','true','Frank Turner',@CUS_ID);

INSERT INTO pizza (PizzaID,PizzaOrderID,PizzaBasePriceID, PizzaSize, PizzaCostCompany, PizzaAssoicatedPrice, PizzaCrustType, PizzaStatus)
VALUES (14, 6, 11 , 'Large', 3.19, 20.81, 'Thin', 'completed');


INSERT INTO delivery (DeliveryOrderID, DeliveryCustomerID) 
VALUES (6, 4);

INSERT INTO bridgetopping (BridgeToppingPizzaID, BridgeToppingToppingID, BridgeToppingExtraTopping)
VALUES (14, 4, false),
(14, 5, false),
(14, 6, false),
(14, 8, false),
(14, 14, true);


-- On April 13th at 8:32 pm Milo Auckerman ordered two large thin crust pizzas. One had the Four Cheese
-- Blend on it (extra) (Price: $13.00, Cost: $2.00), the other was Regular Cheese and Pepperoni (extra) (Price:
-- $19.25, Cost: $3.25). He used the “Employee” discount on his order. He had them delivered to 8879
-- Suburban Home, Anderson, SC 29621. His phone number is 864-878-5679.
INSERT INTO customer (CustomerFirstName,CustomerLastName, CustomerPhoneNum, CustomerHouseNum, CustomerStreet, CustomerCity, CustomerState, CustomerZipCode)
VALUES('Milo', 'Auckerman', '8648785679', 8879, 'Suburban Home', 'Anderson', 'SC', 29621);

SET @CUS_ID = LAST_INSERT_ID();

INSERT INTO `order`(OrderTotalCostBusiness, OrderTimeStamp, OrderTotalPrice, OrderType,OrderStatus,OrderCustomer,OrderCust_ID)
VALUES (5.25, '2024-4-13', 27.41, 'delivery','false','Milo Auckerman',@CUS_ID);

INSERT INTO pizza (PizzaID,PizzaOrderID,PizzaBasePriceID, PizzaSize, PizzaCostCompany, PizzaAssoicatedPrice, PizzaCrustType, PizzaStatus)
VALUES (15, 7, 11 , 'Large', 2.00, 11.05, 'Thin', 'completed'),
(16, 7, 11 , 'Large', 3.25, 16.36, 'Thin', 'completed');


INSERT INTO delivery (DeliveryOrderID, DeliveryCustomerID) 
VALUES (7, 5);

INSERT INTO bridgetopping (BridgeToppingPizzaID, BridgeToppingToppingID, BridgeToppingExtraTopping)
VALUES (15, 14, true),
(16, 13, false),
(16, 1, true);

INSERT INTO orderdiscount (OrderDiscountDiscountID, OrderDiscountOrderID)
VALUES (1, 7);
       
       
-- 	create user 'dbtester1' identified by 'CPSC46201';
--     
--     grant all on PIZZERIA.* to 'dbtester1';

