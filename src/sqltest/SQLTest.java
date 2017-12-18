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

/**
 *
 * @author Debarquer
 */
public class SQLTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        Connection conn = null;
        try{
            conn = getConnection();
        } catch(SQLException e){
            //do whatever
            System.out.println("Unable to connect to database: " + e.getMessage());
        } finally{
            if(conn != null){
                //.out.println("Finally");
                
                Scanner reader = new Scanner(System.in);  // Reading from System.in
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
                            SQLTest.printFromDatabase(conn);
                            break;
                        case "insert":
                            System.out.println("Enter a: ");
                            String a = reader.next(); // Scans the next token of the input as an int.
                            System.out.println("Enter b: ");
                            String b = reader.next(); // Scans the next token of the input as an int.
                            System.out.println("Enter c: ");
                            String c = reader.next(); // Scans the next token of the input as an int.
                            SQLTest.insertIntoDatabase(conn, a, b, c);
                            break;
                        default:
                            System.out.println("Invalid command");
                            break;
                    }
//                    if(s.equals("exit")){
//                        System.out.println("Exiting...");
//                        done = true;
//                    }
//                    else if(s.equals("print")){
//                        SQLTest.printFromDatabase(conn);
//                    }
//                    else{
//                        System.out.println("Invalid command");
//                    }
                }

                //once finished
                reader.close();

                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(SQLTest.class.getName()).log(Level.SEVERE, null, ex);
                } finally{
                    System.out.println("Database closed");
                }
            }
        }
    }
    
    public static Connection getConnection() throws SQLException{
        Connection conn = null;
        Properties connectionProps = new Properties();
        connectionProps.put("user", "root2");
        connectionProps.put("password", "root2");

        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/javabase", "root", "");
    
        System.out.println("Connected to database");
        
        return conn;
    }
    
    public static void insertIntoDatabase(Connection conn, String a, String b, String c){
        String SQL = "INSERT INTO things(a, b, c) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(SQL)){
            ps.setString(1, a);
            ps.setString(2, b);
            ps.setString(3, c);
            int numRowsAffected = ps.executeUpdate();
            System.out.println(numRowsAffected + " row(s) affected");
        } catch (SQLException ex) {
            Logger.getLogger(SQLTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void printFromDatabase(Connection conn){
        Statement stmt = null;  
                
        ResultSet rs = null;  

        String SQL = "SELECT * FROM things";
        try {
            stmt = conn.createStatement();
            
            try{
                rs = stmt.executeQuery(SQL);
            } catch (SQLException ex) {
                Logger.getLogger(SQLTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SQLTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        

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
    }
}
