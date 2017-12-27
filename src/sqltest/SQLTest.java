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
import java.util.ArrayList;

/**
 *
 * @author Debarquer
 */
public class SQLTest {

    public class DataObject{
        
        public int pk;
        public String data;
        
        DataObject(int pk, String data){
            this.pk = pk;
            this.data = data;
        }
        
        @Override
        public String toString(){
            return data;
        }
    }
    
    /**
     * @param args the command line arguments
     */
    
    static Connection conn = null;
    
    public void SQLTest() {
        // TODO code application logic here
        
    }
    
    public void connect(String address, String port, String database, String username, String password){
        try{
            getConnection(address, port, database, username, password);
        } catch(SQLException e){
            //do whatever
            System.out.println("Unable to connect to database: " + e.getMessage());
        } finally{ 
        }
    }
    
    public void closeConnection(){
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
   
    public void getConnection(String address, String port, String database, String username, String password) throws SQLException{
        if(conn != null){
            conn.close();
        }
        
        conn = DriverManager.getConnection("jdbc:mysql://" + address + ":" + port + "/" + database, username, password);
    
        System.out.println("Connected to database");
    }
    
    public void updateDatabase(String table, ArrayList columns, ArrayList newData, String conditionColumn, String conditionData){
        if(columns.size() != newData.size()){
            System.out.println("Failed to update db: Size of colummns != size of new data");
            return;
        }
        
        String SQLNewData = "";
        for(int i = 0; i < columns.size(); i++){
            if(i != 0)
                SQLNewData += ", " + columns.get(i) + " = ?";
            else
                SQLNewData += columns.get(i) + " = ?";
        }
        String SQL = "UPDATE " + table + " SET " + SQLNewData + " WHERE " + conditionColumn + " = '" + conditionData + "'";
        try (PreparedStatement ps = conn.prepareStatement(SQL)){
            for(int i = 0; i < newData.size(); i++){
                ps.setString(i + 1, (String)newData.get(i));
            }
            int numRowsAffected = ps.executeUpdate();
            System.out.println(numRowsAffected + " row(s) affected");
        } catch (SQLException ex) {
            //Logger.getLogger(SQLTest.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Unable to update database: " + ex.getMessage());
            System.out.println("SQL: " + SQL);
        }
    }
    
    public void deleteFromDatabase(String table, String column, String condition){
        String SQL = "DELETE FROM " + table + " WHERE " + column + " = ?";
        try (PreparedStatement ps = conn.prepareStatement(SQL)){
            //ps.setString(1, table);
            ps.setString(1, condition);
            int numRowsAffected = ps.executeUpdate();
            System.out.println(numRowsAffected + " row(s) affected");
        } catch (SQLException ex) {
            //Logger.getLogger(SQLTest.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Unable to delete from database: " + ex.getMessage());
            System.out.println("SQL: \"DELETE FROM things WHERE a = "+condition+"\"");
        }
    }
    
    public void insertIntoDatabase(String table, String a, String b, String c){
        String SQL = "INSERT INTO " + table + " (a, b, c) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(SQL)){
            ps.setString(1, a);
            ps.setString(2, b);
            ps.setString(3, c);
            int numRowsAffected = ps.executeUpdate();
            System.out.println(numRowsAffected + " row(s) affected");
        } catch (SQLException ex) {
            //Logger.getLogger(SQLTest.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Unable to insert into database: " + ex.getMessage());
        }
    }
    
    public ArrayList<DataObject> printFromDatabase(String table){
        
        ArrayList arr = new ArrayList<DataObject>();
        //String s = "";
        
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

                    String tmp = pk + " " + a + " " + b + " " + c;
                    DataObject tmpdo = new DataObject(Integer.parseInt(pk), tmp + "<br>");
                    arr.add((DataObject)tmpdo);
                    System.out.println(tmp);

                    //System.out.println(rs.getString(4) + " " + rs.getString(6));  
                }
            } catch (SQLException ex) {
                //Logger.getLogger(SQLTest.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Unable to print from database: " + ex.getMessage());
            }
        } catch (SQLException ex) {
            System.out.println("Unable to print from database: " + ex.getMessage());
        }
        
        return arr;
    }
}
