import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;

/**
 * 这个类实现了词典数据库的装载
 * @author foxandhuzh
 *
 */
public class ImportData {

	private WordLib wordLib = null;
	
	/**
	 * 创建词典数据库装载器，连接词典图书馆对象
	 * @param wordLib 词典图书馆对象
	 */
	public ImportData(WordLib wordLib)
	{
		this.wordLib = wordLib;
	}
	
	/**
	 * 将词库装载入词典图书馆对象
	 */
	public void set()
	{
		String str, word, wordFull, phonogram, mean;
		FileReader input = null;
		BufferedReader buffer = null;
		
		try 
		{
			input = new FileReader("complexDictionaryTranslate.dic");
			buffer = new BufferedReader(input);
			buffer.readLine();
			word = buffer.readLine();		//读入第一个单词
			while (word != null && !word.isEmpty())
			{
				word = word.trim();
				wordFull = buffer.readLine();
				wordFull = wordFull.trim();
				str = buffer.readLine();
				if (str.startsWith("[") && str.endsWith("]"))
				{
					phonogram = str + '\n';
					str = buffer.readLine();
				}
				else
				{
					phonogram = "";
				}
				mean = new String();
				while (!str.equals("@"))
				{
					mean = mean + str + '\n';
					str = buffer.readLine();
				}
				wordLib.addWord(word, wordFull, phonogram, mean);
				word = buffer.readLine();
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				buffer.close();
				input.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			
		}
	}
}
