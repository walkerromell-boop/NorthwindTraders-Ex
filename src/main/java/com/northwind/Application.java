package com.northwind;

import com.northwind.Data.CustomerDao;
import com.northwind.Data.ProductDao;
import com.northwind.Data.ShipperDao;
import com.northwind.Model.Customer;
import com.northwind.Model.Product;
import com.northwind.Model.Shipper;
import org.apache.commons.dbcp2.BasicDataSource;

import java.util.List;


public class Application {
    public static void main(String[] args) {
        String username = args[0];
        String password = args[1];
        String url = "jdbc:mysql://localhost:3306/northwind";

        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        ShipperDao shipperDao= new ShipperDao(dataSource);
        ProductDao productDao =new ProductDao(dataSource);

        CustomerDao customerDao = new CustomerDao(dataSource);
        List<Customer> customers =  customerDao.getAll();
        System.out.println("Total: "+customers.size());
        if (!customers.isEmpty()) {
            System.out.println("First customer: " + customers.get(0));
        }
        Customer foundCustomer = customerDao.find("ALFKI");
        if (foundCustomer != null) {
            System.out.println("Found: " + foundCustomer);
        } else {
            System.out.println("Customer ALFKI not found.");
        }
//        System.out.println("\n--- Test 3: Add New Customer ---");
//        Customer newCustomer = new Customer(
//                "TSTID",              // Customer ID - we choose this (it's the primary key)
//                "Test Company",       // Company name
//                "John Doe",           // Contact person's name
//                "Manager",            // Their job title
//                "123 Test St",        // Street address
//                "Test City",          // City
//                "TC",                 // Region/State abbreviation
//                "12345",              // Postal/ZIP code
//                "USA",                // Country
//                "555-1234",           // Phone number
//                "555-5678"            // Fax number
//        );
//        Customer addedCustomer = customerDao.add(newCustomer);
//        System.out.println("Added: " + addedCustomer);
//        System.out.println("\n--- Test 4: Verify Customer Was Added ---");
//        Customer verifyCustomer = customerDao.find("TSTID");
//        if (verifyCustomer != null) {
//            System.out.println("Verified: " + verifyCustomer);
//        } else {  System.out.println("Customer TSTID not found after adding.");
//        }

        System.out.println("\n--- Test 2: Find Shipper by ID ---");
        Shipper foundShipper = shipperDao.find(1);
        if (foundShipper != null) {
            System.out.println("Found: " + foundShipper);
        } else {
            System.out.println("Shipper with ID 1 not found.");
        }
//        ProductDao productDao = new ProductDao(dataSource);
//        List<Product> products = productDao.getAll();
//        System.out.println(products);

        Product foundProduct = productDao.find(1);
        if (foundProduct != null) {
            System.out.println("Found it: "+ foundProduct);
        }else{
            System.out.println("product with ID 1 not found");
        }
    }
}
