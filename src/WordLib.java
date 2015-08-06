import java.util.ArrayList;

/**
 * 单词图书馆，管理所有的单词，包括了单词本身，单词的完整形式，单词的音标，单词的意思
 * @author foxandhuzh
 *
 */

public class WordLib
{
	final int Tot = 90000; 
	private String word[] = new String[Tot];				//单词
	private String wordFull[] = new String[Tot];			//单词的完整形式
	private String phonogram[] = new String[Tot];			//单词的音标
	private String mean[] = new String[Tot];				//单词的意思
	private int tot = 0;
	
	/**
	 * 创建一个空的单词图书馆
	 */
	public WordLib()
	{
	}
	
	/**
	 * 向单词图书馆的末尾添加一个单词
	 * @param w 单词本身
	 * @param wf 单词的完整形式
	 * @param phono 单词的音标
	 * @param m 单词的意思
	 */
	public void addWord(String w, String wf, String phono, String m)
	{
		word[tot] = w;
		wordFull[tot] = wf;
		phonogram[tot] = phono;
		mean[tot] = m;
		tot++;
	}
	
	/**
	 * 在单词图书馆的指定位置加入一个单词，如果指定位置已经有单词，则替换该单词
	 * @param w 单词本身
	 * @param wf 单词的完整形式
	 * @param phono 单词的音标
	 * @param m 单词的意思
	 * @param index 指定的位置
	 */
	public void addWord(String w, String wf, String phono, String m, int index)
	{
		word[index] = w;
		wordFull[index] = wf;
		phonogram[index] = phono;
		mean[index] = m;
	}
	
	/**
	 * 获取指定位置的单词
	 * @param index 指定的位置
	 * @return 指定位置的单词
	 */
	public String getWord(int index)
	{
		if (index > -1 && index < tot) return word[index];
		return null;
	}
	
	/**
	 * 获取指定位置的单词的完整形式
	 * @param index 指定的位置
	 * @return 指定位置的单词的完整形式
	 */
	public String getWordFull(int index)
	{
		if (index > -1 && index < tot) return wordFull[index];
		return null;
	}
	
	/**
	 * 获取指定位置的单词的音标
	 * @param index 指定的位置
	 * @return 指定位置的单词的音标
	 */
	public String getPhonogram(int index)
	{
		if (index > -1 && index < tot) return phonogram[index];
		return null;
	}
	
	/**
	 * 获取指定位置的单词的意思
	 * @param index 指定的位置
	 * @return 指定位置的单词的意思
	 */
	public String getMean(int index)
	{
		if (index > -1 && index < tot) return mean[index];
		return null;
	}
	
	/**
	 * 获取单词图书馆中的单词的总数
	 * @return 单词图书馆单词的总数
	 */
	public int length()
	{
		return tot;
	}
}