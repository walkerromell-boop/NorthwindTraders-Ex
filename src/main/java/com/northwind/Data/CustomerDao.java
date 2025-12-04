package com.northwind.Data;

import com.northwind.Model.Customer;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDao {
    private DataSource dataSource;

    public CustomerDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Customer> getAll() {
        List<Customer> customers = new ArrayList<>();

        String query = """
                SELECT CustomerID,CompanyName,ContactName,ContactTitle,
                Address,City,Region,PostalCode,Country,Phone,fax
                FROM Customers;
                """;


        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {


            try (ResultSet resultSet = statement.executeQuery()) {


                while (resultSet.next()) {
                    Customer customer = new Customer(
                            resultSet.getString("CustomerID"),
                            resultSet.getString("CompanyName"),
                            resultSet.getString("ContactName"),
                            resultSet.getString("ContactTitle"),
                            resultSet.getString("Address"),
                            resultSet.getString("City"),
                            resultSet.getString("Region"),
                            resultSet.getString("PostalCode"),
                            resultSet.getString("Country"),
                            resultSet.getString("Phone"),
                            resultSet.getString("Fax"));
                    customers.add(customer);
                }
            }


        } catch (SQLException e) {
            System.out.println("There was an error retrieving the data. Please try again.");
            e.printStackTrace();
        }

        return customers;

    }

    // METHOD 2: FIND ONE CUSTOMER BY ID
    // This searches for a specific customer using their unique ID
    // It's like looking up a specific person in a phone book
    public Customer find(String customerId) {

        // Start with null (nothing) - we'll replace this if we find the customer
        // null is like an empty box - it represents "no value yet"
        Customer customer = null;

        // SQL query with a ? placeholder - this is for security!
        // The ? is like a fill-in-the-blank that we'll safely fill in later
        // WHERE CustomerID = ? means "only get the row where the ID matches what I specify"
        String query = """
                SELECT CustomerID, CompanyName, ContactName, ContactTitle, Address, City, Region, PostalCode, Country, Phone, Fax
                FROM Customers
                WHERE CustomerID = ?;
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Fill in the ? placeholder with the actual customer ID
            // The "1" means "the first placeholder" (in case there were multiple)
            // This is safer than putting the ID directly in the query (prevents SQL injection attacks)
            statement.setString(1, customerId);

            try (ResultSet resultSet = statement.executeQuery()) {

                // "if" instead of "while" because we expect at most ONE result
                // Customer IDs are unique, so there should only be one match
                if (resultSet.next()) {
                    // We found them! Build the Customer object
                    customer = new Customer(
                            resultSet.getString("CustomerID"),
                            resultSet.getString("CompanyName"),
                            resultSet.getString("ContactName"),
                            resultSet.getString("ContactTitle"),
                            resultSet.getString("Address"),
                            resultSet.getString("City"),
                            resultSet.getString("Region"),
                            resultSet.getString("PostalCode"),
                            resultSet.getString("Country"),
                            resultSet.getString("Phone"),
                            resultSet.getString("Fax"));
                }
                // If resultSet.next() is false, customer stays null (customer not found)
            }

        } catch (SQLException e) {
            System.out.println("There was an error retrieving the data. Please try again.");
            e.printStackTrace();
        }

        // Return the customer (or null if not found)
        return customer;
    }

    // METHOD 3: ADD A NEW CUSTOMER
    // This inserts a brand new customer into the database
    // It's like adding a new contact to your phone
    public Customer add(Customer customer) {

        // INSERT INTO means "add a new row to this table"
        // The ? marks are placeholders for all the customer's information
        // We have 11 placeholders because we're inserting 11 pieces of information
        String query = """
                INSERT INTO Customers (CustomerID, CompanyName, ContactName, ContactTitle, Address, City, Region, PostalCode, Country, Phone, Fax)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Fill in each ? placeholder with data from the customer object
            // Think of it like filling out a form, one field at a time
            // The number (1, 2, 3...) corresponds to which ? we're filling in (left to right)
            statement.setString(1, customer.getCustomerID());      // 1st ? gets the ID
            statement.setString(2, customer.getCompanyName());     // 2nd ? gets the company name
            statement.setString(3, customer.getContactName());     // 3rd ? gets the contact name
            statement.setString(4, customer.getContactTitle());    // And so on...
            statement.setString(5, customer.getAddress());
            statement.setString(6, customer.getCity());
            statement.setString(7, customer.getRegion());
            statement.setString(8, customer.getPostalCode());
            statement.setString(9, customer.getCountry());
            statement.setString(10, customer.getPhone());
            statement.setString(11, customer.getFax());            // 11th ? gets the fax

            // executeUpdate() runs the INSERT command
            // Unlike executeQuery() which retrieves data, executeUpdate() changes data
            // It's like pressing the "Save" button
            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("There was an error adding the customer. Please try again.");
            e.printStackTrace();
        }

        // Return the customer object we just added
        return customer;
    }

    // METHOD 4: UPDATE AN EXISTING CUSTOMER
    // This modifies information for a customer that already exists in the database
    // It's like editing a contact in your phone
    public void update(Customer customer) {

        // UPDATE means "modify an existing row"
        // SET specifies which columns to change and to what values
        // WHERE specifies which customer to update (using their ID)
        // Without WHERE, it would update EVERY customer - that would be bad!
        String query = """
                UPDATE Customers
                SET CompanyName = ?, ContactName = ?, ContactTitle = ?, Address = ?, City = ?, Region = ?, PostalCode = ?, Country = ?, Phone = ?, Fax = ?
                WHERE CustomerID = ?;
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Fill in the placeholders with the customer's updated information
            // Notice: CustomerID comes LAST (position 11) because it's in the WHERE clause at the end
            statement.setString(1, customer.getCompanyName());     // Update company name
            statement.setString(2, customer.getContactName());     // Update contact name
            statement.setString(3, customer.getContactTitle());    // Update contact title
            statement.setString(4, customer.getAddress());         // Update address
            statement.setString(5, customer.getCity());            // Update city
            statement.setString(6, customer.getRegion());          // Update region
            statement.setString(7, customer.getPostalCode());      // Update postal code
            statement.setString(8, customer.getCountry());         // Update country
            statement.setString(9, customer.getPhone());           // Update phone
            statement.setString(10, customer.getFax());            // Update fax
            statement.setString(11, customer.getCustomerID());     // Which customer to update (WHERE clause)

            // Execute the update - save the changes to the database
            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("There was an error updating the customer. Please try again.");
            e.printStackTrace();
        }

        // This method returns nothing (void) - it just does the update
    }

    // METHOD 5: DELETE A CUSTOMER
    // This permanently removes a customer from the database
    // It's like deleting a contact from your phone - be careful!
    public void delete(String customerId) {

        // DELETE FROM means "remove a row from this table"
        // WHERE specifies which customer to delete
        // Without WHERE, it would delete ALL customers - disaster!
        String query = """
                DELETE FROM Customers
                WHERE CustomerID = ?;
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Fill in which customer to delete
            statement.setString(1, customerId);

            // Execute the deletion - the customer is now gone from the database
            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("There was an error deleting the customer. Please try again.");
            e.printStackTrace();
        }

        // This method returns nothing (void) - it just performs the deletion
    }


}
// SUMMARY OF THIS CLASS (CRUD OPERATIONS):
// This class provides 5 methods that cover all basic database operations:
//
// C - CREATE: add() creates a new customer
// R - READ:   getAll() reads all customers, find() reads one specific customer
// U - UPDATE: update() modifies an existing customer
// D - DELETE: delete() removes a customer
//
// These are called "CRUD operations" and are fundamental to almost every database application!
// Think of this class as your customer database toolkit - everything you need to manage customers.