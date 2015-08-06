package org.lxh.demo17.connectdemo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class AddNewWord {
	Connection connection = ConnectionManager.getConnection("DBQ=D:\\2.mdb");
	Statement start = null;
	ResultSet resultSet = null;
	public void AddWord(String word,String mean) throws SQLException
	{
		start=connection.createStatement();
		String sql1 = "SELECT * FROM newword WHERE word=('"+word+"')";
		resultSet=start.executeQuery(sql1);
		if(!resultSet.next())
		{
			String sql = "INSERT INTO newword(word,mean)"+"VALUES('"+word+"','"+mean+"')";
			start.executeUpdate(sql);
			JOptionPane jOptionPane = new JOptionPane();
			jOptionPane.showMessageDialog(null ,word+"以添加到生词本中",null,JOptionPane.CANCEL_OPTION);
		}
		else
		{
			JOptionPane jOptionPane = new JOptionPane();
			jOptionPane.showMessageDialog(null ,word+"已存在",null,JOptionPane.CANCEL_OPTION);
		}
	}

}
