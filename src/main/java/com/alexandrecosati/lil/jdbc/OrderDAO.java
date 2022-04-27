package com.alexandrecosati.lil.jdbc;

import com.alexandrecosati.lil.jdbc.util.DataAccessObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderDAO extends DataAccessObject<Order> {

    private static final String GET_ONE = "SELECT c.first_name, c.last_name, c.email, o.order_id, " +
            "o.creation_date, o.total_due, o.status, s.first_name, s.last_name, s.email, ol.quantity, " +
            "p.code, p.name, p.size, p.variety, p.price FROM orders o " +
            "join customer c on o.customer_id = c.customer_id " +
            "join salesperson s on o.salesperson_id = s.salesperson_id " +
            "join order_item ol on ol.order_id = o.order_id " +
            "join product p on ol.product_id = p.product_id " +
            "where o.order_id = ?";

    private static final String GET_FOR_CUST = "SELECT * FROM get_orders_by_customer(?)";

    public OrderDAO(Connection connection)  {
        super(connection);
    }

    @Override
    public Order findById(long id) {
        Order order = new Order();
        try(PreparedStatement statement = this.connection.prepareStatement(GET_ONE);) {
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            long orderId = 0;
            List<OrderLine> orderLines = new ArrayList<>();
            while(rs.next()) {
                if (orderId == 0) {
                    order.setCustomerFirstName(rs.getString(1));
                    order.setCustomerLastName(rs.getString(2));
                    order.setCustomerEmail(rs.getString(3));
                    order.setId(rs.getLong(4));
                    orderId= order.getId();
                    order.setCreationDate(new Date(rs.getDate(5).getTime()));
                    order.setTotalDue(rs.getBigDecimal(6));
                    order.setStatus(rs.getString(7));
                    order.setSalespersonFirstName(rs.getString(8));
                    order.setSalespersonLastName(rs.getString(9));
                    order.setSalespersonEmail(rs.getString(10));
                }
                OrderLine orderLine = new OrderLine();
                orderLine.setQuantity(rs.getInt(11));
                orderLine.setProductCode(rs.getString(12));
                orderLine.setProductName(rs.getString(13));
                orderLine.setProductSize(rs.getInt(14));
                orderLine.setProductVariety(rs.getString(15));
                orderLine.setProductPrice(rs.getBigDecimal(16));
                orderLines.add(orderLine);
            }
            order.setOrderLines(orderLines);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return order;
    }

    @Override
    public List<Order> findAll() {
        return null;
    }

    @Override
    public Order update(Order dto) {
        return null;
    }

    @Override
    public Order create(Order dto) {
        return null;
    }

    @Override
    public void delete(long id) {

    }

    public List<Order> getOrdersForCustomer(long customerId) {
        List<Order> orders = new ArrayList<>();
        try(PreparedStatement statement = this.connection.prepareStatement(GET_FOR_CUST)) {
            statement.setLong(1, customerId);
            ResultSet rs = statement.executeQuery();
            long orderId = 0;
            Order order = null;
            while(rs.next()) {
                long localOrderId = rs.getLong(4);
                if(orderId != localOrderId) {
                    order = new Order();
                    orders.add(order);
                    order.setId(localOrderId);
                    orderId = localOrderId;
                    order.setCustomerFirstName(rs.getString(1));
                    order.setCustomerLastName(rs.getString(2));
                    order.setCustomerEmail(rs.getString(3));
                    order.setCreationDate(new Date(rs.getDate(5).getTime()));
                    order.setTotalDue(rs.getBigDecimal(6));
                    order.setStatus(rs.getString(7));
                    order.setSalespersonFirstName(rs.getString(8));
                    order.setSalespersonLastName(rs.getString(9));
                    order.setSalespersonEmail(rs.getString(10));
                    List<OrderLine> orderLines = new ArrayList<>();
                    order.setOrderLines(orderLines);
                }
                OrderLine orderLine = new OrderLine();
                orderLine.setQuantity(rs.getInt(11));
                orderLine.setProductCode(rs.getString(12));
                orderLine.setProductName(rs.getString(13));
                orderLine.setProductSize(rs.getInt(14));
                orderLine.setProductVariety(rs.getString(15));
                orderLine.setProductPrice(rs.getBigDecimal(16));
                order.getOrderLines().add(orderLine);
            }
        } catch(SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return orders;
    }
}
