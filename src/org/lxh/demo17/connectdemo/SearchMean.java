package org.lxh.demo17.connectdemo;

import java.sql.*;
public class SearchMean {
	public static String meanString =null;
	Statement start = null;
	Connection connection = null;
	ResultSet resultSet = null;
	public String getMean(String word)
	{
        //String sql1 = "SELECT * FROM WORDLISTS WHERE WORD LIKE '%"+word+"%' limit 0,1";
		String sql1 = "SELECT * FROM WORDLISTS WHERE word=('"+word+"')";
		try 
		{
			connection = ConnectionManager.getConnection("DBQ=D:\\weikeDic.mdb");
			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		try 
		{
			start=connection.createStatement();
			resultSet = start.executeQuery(sql1);
			resultSet = start.executeQuery(sql1);
			int i=0;
			while(resultSet.next())
		    {
				meanString=resultSet.getString("mean");
		    }
			start.close();
			//resultSet.close();
			connection.close();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return meanString;
	}
}