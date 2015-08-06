package org.lxh.demo17.connectdemo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class ConnectionManager 
{
 
    static 
    {
    	 try 
    	 {
    	    Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
    	 } 
    	 catch (ClassNotFoundException e) 
    	 {
    	    e.printStackTrace();
    	 }
    }
 
    public static Connection getConnection(String path)
    {
    	 Connection con=null;
    	 
    	    String url=new String("jdbc:odbc:driver={Microsoft Access Driver (*.mdb)};"+path);//testïrŸY¡œÅÌ‘¥
    	 
    	    try {
    	 
    	    con= DriverManager.getConnection(url);
    	    } 
    	    catch (SQLException e) 
    	    {
    	    	e.printStackTrace();
    	    }
    	    return con;
    }
}