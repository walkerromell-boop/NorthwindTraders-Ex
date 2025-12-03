package com.northwind.Data;

import com.northwind.Model.Customer;
import com.northwind.Model.Product;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
                            resultSet.getInt("UnitsonOrder"),
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
}
