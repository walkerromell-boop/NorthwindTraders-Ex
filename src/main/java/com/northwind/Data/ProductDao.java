package com.northwind.Data;

import com.northwind.Model.Customer;
import com.northwind.Model.Product;
import com.northwind.Model.Shipper;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDao {
    private DataSource dataSource;

    public ProductDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Product> getAll() {
        List<Product> products = new ArrayList<>();

        String query = """
                SELECT ProductID,ProductName,SupplierID,CategoryID,QuantityPerUnit,UnitPrice,UnitsInStock,UnitsOnOrder,ReorderLevel,Discontinued\s
                FROM northwind.Products;
                """;


        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {


            try (ResultSet resultSet = statement.executeQuery()) {


                while (resultSet.next()) {
                    Product product = new Product(
                            resultSet.getInt("ProductID"),
                            resultSet.getString("ProductName"),
                            resultSet.getInt("SupplierID"),
                            resultSet.getInt("CategoryID"),
                            resultSet.getString("QuantityPerUnit"),
                            resultSet.getDouble("UnitPrice"),
                            resultSet.getInt("UnitsInStock"),
                            resultSet.getInt("UnitsOnOrder"),
                            resultSet.getInt("ReorderLevel"),
                            resultSet.getInt("Discontinued"));
                    products.add(product);
                }
            }


        } catch (SQLException e) {
            System.out.println("There was an error retrieving the data. Please try again.");
            e.printStackTrace();
        }

        return products;
    }
        public Product find(int productID){
            Product product = null;

            String findQuery = """
                    SELECT ProductID,ProductName,SupplierID,CategoryID,QuantityPerUnit,UnitPrice,UnitsInStock,UnitsOnOrder,ReorderLevel,Discontinued
                    FROM Products
                    WHERE ProductID = ?;
                    """;

            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement(findQuery)) {

                statement.setInt(1, productID);

                try (ResultSet resultSet = statement.executeQuery()) {

                    if (resultSet.next()) {
                        product = new Product(
                                resultSet.getInt("ProductID"),
                                resultSet.getString("ProductName"),
                                resultSet.getInt("SupplierID"),
                                resultSet.getInt("CategoryID"),
                                resultSet.getString("QuantityPerUnit"),
                                resultSet.getDouble("UnitPrice"),
                                resultSet.getInt("UnitsInStock"),
                                resultSet.getInt("UnitsOnOrder"),
                                resultSet.getInt("ReorderLevel"),
                                resultSet.getInt("Discontinued"));
                    }

                }
            } catch (SQLException e) {
                System.out.println("There was an error retrieving the data. Please try again.");
                e.printStackTrace();
            }

            return product;
        }

    public Product add(Product product){

        String query = """
                INSERT INTO Shippers (CompanyName, Phone)
                VALUES (?, ?);
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, shipper.getCompanyName());  // 1st ? gets the company name
            statement.setString(2, shipper.getPhone());        // 2nd ? gets the phone number
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {

                // Check if we got a generated key back
                if (generatedKeys.next()) {
                    // Get the generated ID from the first column (index 1)
                    // This is the ShipperID the database automatically created
                    int generatedId = generatedKeys.getInt(1);

                    // Set this ID on our shipper object so it knows its own ID now
                    // Before this, the shipper object had no ID (or ID = 0)
                    // Now it knows its actual database ID!
                    shipper.setShipperId(generatedId);
                }
            }

        } catch (SQLException e) {
            System.out.println("There was an error adding the shipper. Please try again.");
            e.printStackTrace();
        }

        // Return the shipper object - it now has its ID set!
        return shipper;

        }
    }

    

