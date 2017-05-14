/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameModel;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ethan
 */
public class Score {
    
    String name;
    int moves;
    
    public Score(String name, int moves){
        this.name = name;
        this.moves = moves;
    }
    
    public boolean update(){
        String dbName = "KiwiIsland";
        String dbUsername = "kiwi";
        String dbPassword = "kiwi";

        String dbURL = "jdbc:derby://localhost:1527/" + dbName + ";"
                    + "create=true;"
                    + "user=" + dbUsername + ";"
                    + "password=" + dbPassword;
        
        String driver = "org.apache.derby.jdbc.ClientDriver";
        try{
            Class.forName(driver).newInstance();
            Connection con = DriverManager.getConnection(dbURL);
            
            DatabaseMetaData md = con.getMetaData();
            ResultSet rs = md.getTables(null, null, "SCORES", null);
            if(!rs.next()){
                PreparedStatement ps = con.prepareStatement("CREATE TABLE scores "+
                        "(name VARCHAR(30)," +
                        "score int," +
                        "id int GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)," +
                        "PRIMARY KEY(id))");
                int execute = ps.executeUpdate();
            }
            
            PreparedStatement st = con.prepareStatement("INSERT INTO scores(name, score) values(?,?)");
            st.setString(1, this.name);
            st.setInt(2, this.moves);
            
            int result = st.executeUpdate();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Score.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (InstantiationException ex) {
            Logger.getLogger(Score.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Score.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (SQLException ex) {
            Logger.getLogger(Score.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
    
    public ArrayList<String> view(){
        String dbName = "KiwiIsland";
        String dbUsername = "kiwi";
        String dbPassword = "kiwi";

        String dbURL = "jdbc:derby://localhost:1527/" + dbName + ";"
                    + "create=true;"
                    + "user=" + dbUsername + ";"
                    + "password=" + dbPassword;
        
        String driver = "org.apache.derby.jdbc.ClientDriver";
        try{
            Class.forName(driver).newInstance();
            Connection con = DriverManager.getConnection(dbURL);
            
            DatabaseMetaData md = con.getMetaData();
            ResultSet rs = md.getTables(null, null, "SCORES", null);
            if(!rs.next()){
                PreparedStatement ps = con.prepareStatement("CREATE TABLE scores "+
                        "(name VARCHAR(30)," +
                        "score int," +
                        "id int GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)," +
                        "PRIMARY KEY(id))");
                int execute = ps.executeUpdate();
            }
            
            PreparedStatement st = con.prepareStatement("SELECT name, score FROM scores ORDER BY score ASC");
            rs = st.executeQuery();
            
            ArrayList<String> results = new ArrayList<String>();
            while(rs.next()){
                String toAdd = "Name: " + rs.getString("name") +", Score: " + rs.getString("score");
                results.add(toAdd);
            }
            
            return results;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Score.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(Score.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Score.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Score.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
