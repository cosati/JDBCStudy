package com.alexandrecosati.lil.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class JDBCExecutor {

    public static void main(String[] args) {
        DatabaseConnectionManager dcm = new DatabaseConnectionManager("localhost", "hplussport",
                                                                    "postgres", "password");
        try {
            Connection connection = dcm.getConnection();
            CustomerDAO customerDAO = new CustomerDAO(connection);
            customerDAO.findAllSorted(20).forEach(System.out::println);
            System.out.println("Paged");
            for (int i = 0; i < 3; i++) {
                System.out.println("Page number: " + i);
                customerDAO.findAllPaged(10, i+1).forEach(System.out::println);
            }
//            CustomerDAO customerDao = new CustomerDAO(connection);
//            OrderDAO orderDAO = new OrderDAO(connection);
//            List<Order> orders = orderDAO.getOrdersForCustomer(789);
//            orders.forEach(System.out::println);
//            Order order = orderDAO.findById(1000);
//            System.out.println(order);
//            Customer customer = new Customer();
//            customer.setFirstName("Mary");
//            customer.setLastName("Jane");
//            customer.setEmail("mary.jane@gml.com");
//            customer.setPhone("555 666 666");
//            customer.setAddress("321 Main St.");
//            customer.setCity("Warsaw");
//            customer.setState("Poland");
//            customer.setZipCode("22 555");
//
//            Customer customerDB = customerDao.create(customer);
//            System.out.println(customerDB);
//            customerDB = customerDao.findById(customerDB.getId());
//            System.out.println(customerDB);
//            customerDB.setEmail("maryjane.new@bol.com.br");
//            customerDB = customerDao.update(customerDB);
//            System.out.println(customerDB);
//            customerDao.delete(customerDB.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
