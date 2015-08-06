import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Vector;

public class CopyOfImportData {

	private WordLib wordLib = null;
	public WordTree Tree = new WordTree();
	
	public CopyOfImportData(WordLib wordLib)
	{
		this.wordLib = wordLib;
	}
	
	public void set()
	{
		String str, word, wordFull, phonogram, mean;
		int i = 0;
		FileReader input = null;
		BufferedReader buffer = null;
//		InputStream input = null;
//		InputStreamReader buffer = null;
		
		try 
		{
			input = new FileReader("basicDictionaryTranslate.txt");
//			input = new FileReader("weikeDictionaryTranslate.txt");
			buffer = new BufferedReader(input);
//			input = new FileInputStream("basicDictionaryTranslate.txt");
//			buffer = new InputStreamReader(input);
			buffer.readLine();
			word = buffer.readLine();		//读入第一个单词
			while (word != null && !word.isEmpty())
			{
				word = word.trim();
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
//				System.out.println(word);
//				System.out.println(wordFull);
//				System.out.println(phonogram);
//				System.out.println(mean);
				wordLib.addWord(word, word, phonogram, mean);
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
	
//	TODO 在原有词库上添加单词
	public void add()
	{
		String str, word, wordFull, phonogram, mean;
		int i = 0;
		FileReader input = null;
		BufferedReader buffer = null;
//		InputStream input = null;
//		InputStreamReader buffer = null;
		
		try 
		{
//			input = new FileReader("basicDictionaryTranslate.txt");
			input = new FileReader("weikeDictionaryTranslate.txt");
			buffer = new BufferedReader(input);
//			input = new FileInputStream("basicDictionaryTranslate.txt");
//			buffer = new InputStreamReader(input);
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
//				System.out.println(word);
//				System.out.println(wordFull);
//				System.out.println(phonogram);
//				System.out.println(mean);
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
	
	public void out(Vector<String> wordList) throws IOException
	{
		FileWriter fw = new FileWriter("complexDictionaryTranslate2.txt");
		BufferedWriter wr = new BufferedWriter(fw);
		
		for (int i = 0; i < wordList.size(); i++)
		{
			String word = wordList.get(i);
			System.out.print(word+"\n");
			System.out.print(wordLib.getWordFull(Tree.getWordIndex(word)) + "\n");
			System.out.print(wordLib.getPhonogram(Tree.getWordIndex(word)));
			System.out.print(wordLib.getMean(Tree.getWordIndex(word)));
			wr.write("@\n");
			wr.write(wordList.get(i) + "\n");
			wr.write(wordLib.getWordFull(Tree.getWordIndex(word)) + "\n");
			wr.write(wordLib.getPhonogram(Tree.getWordIndex(word)));
			wr.write(wordLib.getMean(Tree.getWordIndex(word)));
		}
		
		wr.close();
		fw.close();
	}
	
	public static void main(String[] args) throws IOException
	{
		WordLib wordLib = new WordLib();
		CopyOfImportData importer = new CopyOfImportData(wordLib);
		importer.set();		//添加基础词库
		importer.add();		//添加进阶词库
		
		for(int i = 0; i < wordLib.length(); i++)
		{
			String word = wordLib.getWord(i);
			if (word == null) break;
			importer.Tree.inputWord(word, i);
		}
		importer.Tree.buildIndex();
				
		Vector<String> wordList = importer.Tree.getWords("");
		importer.out(wordList);
	}
}
