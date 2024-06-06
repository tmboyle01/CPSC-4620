DROP SCHEMA IF EXISTS PIZZERIA;
CREATE SCHEMA PIZZERIA;
USE PIZZERIA;

-- (MAKE SURE WHEN RUNNING ANY CODE THAT YOU DON"T HAVE ANYTHING HIGHLIGHTED, will cause ERRORS, Trust Me).

CREATE TABLE `discount` (
  `DiscountID` INTEGER,
  `DiscountName` VARCHAR(25),
  `DiscountType` DECIMAL(5,2),
  `DiscountAmount` DECIMAL(5,2),
  `DiscountBoolean` boolean,
  PRIMARY KEY (`DiscountID`)
);

CREATE TABLE `customer` (
  `Customer_ID` INTEGER AUTO_INCREMENT,
  `CustomerFirstName` VARCHAR(25),
  `CustomerLastName` VARCHAR(25),
  `CustomerPhoneNum` VARCHAR(15),
  `CustomerHouseNum` INTEGER,
  `CustomerStreet` VARCHAR(50),
  `CustomerCity` VARCHAR(50),
  `CustomerState` VARCHAR(5),
  `CustomerZipCode` INTEGER,
  PRIMARY KEY (`Customer_ID`)
);

CREATE TABLE `topping` (
  `ToppingID` INTEGER,
  `ToppingName` VARCHAR(25),
  `ToppingPriceCustomer` DECIMAL(9,2),
  `ToppingPriceBusiness` DECIMAL(9,2),
   `ToppingCurrentInventory` INTEGER,
  `ToppingMinimumInventory` INTEGER,
  `ToppingAmountTopSmall` DECIMAL(4,2),
  `ToppingAmountTopMedium` DECIMAL(4,2),
  `ToppingAmountTopLarge` DECIMAL(4,2),
  `ToppingAmountTopXlarge` DECIMAL(4,2),
  PRIMARY KEY (`ToppingID`)
);

CREATE TABLE `baseprice` (
  `BaseP_ID` INTEGER,
  `BasePricePizzaSize` VARCHAR(25),
  `BasePriceCrustType` VARCHAR(25),
  `BasePriceCustomer` DECIMAL(5,2),
  `BasePriceBusiness` DECIMAL(5,2),
  PRIMARY KEY(`BaseP_ID`)
);

-- Switch Customer Table with Order
CREATE TABLE `order` (
  `Order_ID` INTEGER AUTO_INCREMENT,
  `OrderTotalCostBusiness` DECIMAL(9,2),
  `OrderTimeStamp` DATE,
  `OrderTotalPrice` DECIMAL(5,2),
  `OrderType` VARCHAR(25),
  `OrderStatus` VARCHAR(25),
  `OrderCustomer` VARCHAR(25),
  `OrderCust_ID` INTEGER,
  PRIMARY KEY (`Order_ID`),
  FOREIGN KEY (`OrderCust_ID`) REFERENCES `customer`(`Customer_ID`)
);
-- `OrderTimeStamp` VARCHAR(25),
CREATE TABLE `orderdiscount` (
  `OrderDiscountDiscountID` INTEGER,
  `OrderDiscountOrderID` INTEGER,
  PRIMARY KEY (`OrderDiscountDiscountID`,`OrderDiscountOrderID`),
  FOREIGN KEY (`OrderDiscountDiscountID`) REFERENCES `discount`(`DiscountID`),
  FOREIGN KEY (`OrderDiscountOrderID`) REFERENCES `order`(`Order_ID`)
--   KEY `FK1` (`OrderDiscountDiscountID`),
--   KEY `PK2` (`OrderDiscountOrderID`),
--   KEY `FK2` (`OrderDiscountOrderID`)
);
CREATE TABLE `pizza` (
  `PizzaID` INTEGER,
  `PizzaOrderID` INTEGER,
  `PizzaBasePriceID` INTEGER,
  `PizzaSize` VARCHAR(25),
  `PizzaCostCompany` DECIMAL(4,2),
  `PizzaAssoicatedPrice` DECIMAL(4,2),
  `PizzaCrustType` VARCHAR(25),
  `PizzaStatus` VARCHAR(25),
  PRIMARY KEY (`PizzaID`),
  FOREIGN KEY (`PizzaBasePriceID`) REFERENCES `baseprice`(`BaseP_ID`),
  FOREIGN KEY (`PizzaOrderID`) REFERENCES `order`(`Order_ID`)
--   KEY `FK1` (`PizzaOrderID`),
--   KEY `FK2` (`PizzaBasePriceID`)
);

CREATE TABLE `pizzadiscount` (
  `PizzaDiscountDiscountID` INTEGER,
  `PizzaDiscountPizzaID` INTEGER,
  PRIMARY KEY (`PizzaDiscountDiscountID`,`PizzaDiscountPizzaID`),
  FOREIGN KEY (`PizzaDiscountDiscountID`) REFERENCES `discount`(`DiscountID`),
  FOREIGN KEY (`PizzaDiscountPizzaID`) REFERENCES `pizza`(`PizzaID`)
 --  KEY `FK1` (`PizzaDiscountDiscountID`),
--   KEY `PK2 FK2` (`PizzaDiscountPizzaID`)
);

CREATE TABLE `bridgetopping` (
  `BridgeToppingPizzaID` INTEGER,
  `BridgeToppingToppingID` INTEGER,
  `BridgeToppingExtraTopping` BOOL,
  PRIMARY KEY (`BridgeToppingPizzaID`,`BridgeToppingToppingID`),
  FOREIGN KEY (`BridgeToppingPizzaID`) REFERENCES `pizza`(`PizzaID`),
  FOREIGN KEY (`BridgeToppingToppingID`) REFERENCES `topping`(`ToppingID`)
--   KEY `FK1` (`OrderDiscountDiscountID`),
--   KEY `PK2` (`OrderDiscountOrderID`),
--   KEY `FK2` (`OrderDiscountOrderID`)
);
CREATE TABLE `delivery` (
  `DeliveryOrderID` INTEGER,
  `DeliveryCustomerID` INTEGER,
  PRIMARY KEY (DeliveryOrderID),
  FOREIGN KEY(DeliveryOrderID) REFERENCES `order`(`Order_ID`),
  FOREIGN KEY(DeliveryCustomerID) REFERENCES `customer`(`Customer_ID`)
  -- KEY `PK FK` (`DeliveryOrderID`)
);

CREATE TABLE `dinein` (
  `DinInOrder_ID` INTEGER,
  `DineInTableNum` INTEGER,
   PRIMARY KEY (DinInOrder_ID),
   FOREIGN KEY(DinInOrder_ID) REFERENCES `order`(`Order_ID`)
 --  KEY `PK FK` (`DinInOrder_ID`)
);

CREATE TABLE `pickup` (
  `PickUpOrderID` INTEGER,
  `PickupCustomer_ID` INTEGER,
   PRIMARY KEY (PickUpOrderID),
   FOREIGN KEY(PickUpOrderID) REFERENCES `order`(`Order_ID`),
   FOREIGN KEY(PickupCustomer_ID) REFERENCES `customer`(`Customer_ID`)
 --  KEY `PK FK` (`PickUpOrderID`)
);

-- drop user ejcoffe;
-- create user 'ejcoffe' identified by 'Cheese546$';

-- grant all on PIZZERIA.* to 'ejcoffe';
