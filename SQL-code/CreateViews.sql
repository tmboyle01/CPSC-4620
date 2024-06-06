USE PIZZERIA;

DROP VIEW IF EXISTS ToppingPopularity;
DROP VIEW IF EXISTS ProfitByPizza;
DROP VIEW IF EXISTS ProfitByOrderType;

CREATE VIEW ToppingPopularity AS
SELECT t.ToppingName AS 'Topping', COUNT(b.BridgeToppingToppingID) + SUM(CASE WHEN b.BridgeToppingExtraTopping = true THEN 1 ELSE 0 END) AS ToppingCount
FROM topping t 
LEFT JOIN bridgetopping b ON b.BridgeToppingToppingID = t.ToppingID
GROUP BY t.ToppingName
ORDER BY ToppingCount DESC;

CREATE VIEW ProfitByPizza AS
SELECT p.PizzaSize AS Size, p.PizzaCrustType AS Crust, SUM(p.PizzaAssoicatedPrice - p.PizzaCostCompany) AS Profit, o.OrderTimeStamp AS "Order Month"
FROM pizza p JOIN `order` o ON o.Order_ID = p.PizzaOrderID
GROUP BY Size,Crust
ORDER BY Profit ASC;




CREATE VIEW ProfitByOrderType AS
SELECT o.OrderType AS 'CustomerType', o.OrderTimeStamp AS 'Order Month',
  ROUND(SUM(CASE WHEN o.OrderType = 'dinein' THEN o.OrderTotalPrice
                WHEN o.OrderType = 'pickup' THEN o.OrderTotalPrice
                WHEN o.OrderType = 'delivery' THEN o.OrderTotalPrice ELSE 0 END),2) AS 'TotalOrderPrice',
SUM(o.OrderTotalCostBusiness) AS 'TotalOrderCost',
SUM( o.OrderTotalPrice - o.OrderTotalCostBusiness) AS 'Profit'
FROM `order` o
GROUP BY CustomerType;




SELECT * FROM ToppingPopularity;
SELECT * FROM ProfitByPizza;
SELECT * FROM ProfitByOrderType;	