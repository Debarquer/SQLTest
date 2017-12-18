/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sqltest;

import java.sql.*;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 *
 * @author Debarquer
 */
public class SQLTest {

    /**
     * @param args the command line arguments
     */
    
    static Connection conn = null;
    
    public static void SQLTest() {
        // TODO code application logic here
        
    }
    
    public static void connect(String address, String port, String database, String username, String password){
        try{
            conn = getConnection(address, port, database, username, password);
        } catch(SQLException e){
            //do whatever
            System.out.println("Unable to connect to database: " + e.getMessage());
        } finally{ 
        }
    }
    
    public static void closeConnection(){
        if(conn != null){
                //.out.println("Finally");
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(SQLTest.class.getName()).log(Level.SEVERE, null, ex);
                } finally{
                    System.out.println("Database closed");
                }
            }
    }
    
    public static void printCommands(){
        Scanner reader = new Scanner(System.in);  // Reading from System.in
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Boolean done = false;
        while(!done){
            System.out.println("Enter a command: ");
            String s = reader.next(); // Scans the next token of the input as an int.
            System.out.println("You entered: '" + s + "'");
            switch(s){
                case "exit":
                    System.out.println("Exiting...");
                    done = true;
                    break;
                case "print":
                    System.out.println("Enter table: ");
                    String tableToPrint = reader.next();
                    SQLTest.printFromDatabase(tableToPrint);
                    break;
                case "insert":
                    System.out.println("Enter table: ");
                    String tableToInsert = reader.next();
                    System.out.println("Enter a: ");
                    String a = reader.next(); // Scans the next token of the input as an int.
                    System.out.println("Enter b: ");
                    String b = reader.next(); // Scans the next token of the input as an int.
                    System.out.println("Enter c: ");
                    String c = reader.next(); // Scans the next token of the input as an int.
                    SQLTest.insertIntoDatabase(tableToInsert, a, b, c);
                    break;
                case "delete":
                    System.out.println("Enter table: ");
                    String tableToDelete = reader.next();
                    //System.out.println("Enter column: ");
                    //String column = reader.next();
                    System.out.println("Enter condition: ");
                    String condition = reader.next(); // Scans the next token of the input as an int.

                    SQLTest.deleteFromDatabase(tableToDelete, condition);
                    break;
                case "help":
                    System.out.println("Available commands: exit, print, insert, delete, help");
                    break;
                default:
                    System.out.println("Invalid command, enter help for available commands");
                    break;
            }
        }

        //once finished
        reader.close();
    }
    
    public static Connection getConnection(String address, String port, String database, String username, String password) throws SQLException{
        Connection conn = null;
        //Properties connectionProps = new Properties();
        //connectionProps.put("user", "root2");
        //connectionProps.put("password", "root2");

        if(conn != null){
            conn.close();
        }
        
        conn = DriverManager.getConnection("jdbc:mysql://" + address + ":" + port + "/" + database, username, password);
    
        System.out.println("Connected to database");
        
        return conn;
    }
    
    public static void deleteFromDatabase(String table, String condition){
        String SQL = "DELETE FROM ? WHERE a LIKE ?";
        try (PreparedStatement ps = conn.prepareStatement(SQL)){
            ps.setString(1, table);
            ps.setString(2, condition);
            int numRowsAffected = ps.executeUpdate();
            System.out.println(numRowsAffected + " row(s) affected");
        } catch (SQLException ex) {
            //Logger.getLogger(SQLTest.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Unable to delete from database: " + ex.getMessage());
            System.out.println("SQL: \"DELETE FROM "+table+" WHERE "+condition+"\"");
        }
    }
    
    public static void insertIntoDatabase(String table, String a, String b, String c){
        String SQL = "INSERT INTO ? (a, b, c) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(SQL)){
            ps.setString(1, table);
            ps.setString(2, a);
            ps.setString(3, b);
            ps.setString(4, c);
            int numRowsAffected = ps.executeUpdate();
            System.out.println(numRowsAffected + " row(s) affected");
        } catch (SQLException ex) {
            //Logger.getLogger(SQLTest.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Unable to insert into database: " + ex.getMessage());
        }
    }
    
    public static void printFromDatabase(String table){
        Statement stmt = null;  
                
        ResultSet rs = null;  

        String SQL = "SELECT * FROM " + table;
        try {
            stmt = conn.createStatement();
            
            try{
                rs = stmt.executeQuery(SQL);
                
                Boolean cont = true;
                while (cont) {
                    try{
                        if(rs != null)
                            cont = rs.next();
                        else
                            System.out.println("RS null at line 57");
                    }
                    catch(Exception e){
                        System.out.println(e + "\n");
                        cont = false;
                        continue;
                    } finally{
                        if(cont != true){
                            cont = false;
                            continue;
                        }   
                    }
                    String pk = "null";
                    String a = "null";
                    String b = "null";
                    String c = "null";

                    try{
                        pk = rs.getString(1);
                    }
                    catch(Exception e){
                        System.out.println(e + "\n");
                    }
                    try{
                        a = rs.getString(2);
                    }
                    catch(Exception e){
                        System.out.println(e + "\n");
                    }
                    try{
                        b = rs.getString(3);
                    }
                    catch(Exception e){
                        System.out.println(e + "\n");
                    }
                    try{
                        c = rs.getString(4);
                    }
                    catch(Exception e){
                        System.out.println(e + "\n");
                    }

                    System.out.println(pk + " " + a + " " + b + " " + c);

                    //System.out.println(rs.getString(4) + " " + rs.getString(6));  
                }
            } catch (SQLException ex) {
                //Logger.getLogger(SQLTest.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Unable to print from database: " + ex.getMessage());
            }
        } catch (SQLException ex) {
            System.out.println("Unable to print from database: " + ex.getMessage());
        }  
    }
}
