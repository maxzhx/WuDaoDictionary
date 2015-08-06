import java.util.HashMap;
import java.util.Locale;
import java.util.Vector;


/**
 * 单词树，实现了存放单词列表，建立单词与意思索引之间的联系，查找单词的意思索引，列出单词列表等功能。
 * @author foxandhuzh
 */
public class WordTree
{
	private final int INDEX = 60;
	
	private WordNode root = new WordNode();
	private String word;
	private int len;
	private Vector<String> words = new Vector<String>();
	private static int tot = 0;
	private HashMap<Character, Integer> reflect = new HashMap<Character, Integer>();
	
	/**
	 * 建立一个单词树，采用默认的字符映射方式
	 */
	public WordTree()
	{
		char basic = 'a';
		for (int i = 0; i < 26; i++) reflect.put((char)(basic+i), i*2);
		basic = 'A';
		for (int i = 0; i < 26; i++) reflect.put((char)(basic+i), i*2+1);
		//Z---51
		reflect.put(' ', 52);
		reflect.put('-', 53);
		reflect.put('.', 54);
		reflect.put(',', 55);
		reflect.put('/', 56);
		reflect.put('\\', 57);
		reflect.put('(', 58);
		reflect.put(')', 59);
		/*
		if (ch == '-') return 0;
		if (ch == ' ') return 1;
		if (ch == ',') return 2;
		if (ch == '.') return 3;
		if (ch == '(') return 4;
		if (ch == ')') return 5;
		if (ch == '/') return 6;
		if (ch == '\'') return 7;
		*/
	}
	
	/**
	 * 输入一个单词，将这个单词和单词的意思索引加入到单词树中
	 * @param word 添加的单词
	 * @param index 单词的意思索引
	 */
	public void inputWord(String word, int index)
	{
//		this.word = word.toLowerCase(Locale.ENGLISH);
		this.word = word;
		this.len = word.length();
		buildWord(0, root, index);
	}
	
	//获取字母的索引值
	private int getIndex(char ch)
	{
		Integer index = reflect.get(ch);
		if (index == null)
		{
			return -1;
		}
		else
		{
			return index;
		}
	}
	
	private boolean buildWord(int k, WordNode node, int index)
	{
		char ch = word.charAt(k);
		int childnum = getIndex(ch);
		if (childnum == -1) return false;
		if (k == len-1)
		{
			if (node.child[childnum]  == null)
			{
				node.child[childnum] = new WordNode(ch, index);
			}
			else
			{
				node.child[childnum].isWord = index;
			}
			return true;
		}
		else
		{
			if (node.child[childnum] == null)
			{
				node.child[childnum] = new WordNode(ch);
				return buildWord(k+1, node.child[childnum], index);
			}
			else
			{
				return buildWord(k+1, node.child[childnum], index);
			}
		}
	}
	
	/**
	 * 获取以prefix为前缀的所有单词
	 * @param prefix 单词的前缀
	 * @return 返回Vector<String>对象，包含了所有以prefix为前缀的单词
	 */
	public Vector<String> getWords(String prefix)
	{
		int index;
		WordNode now = root;
		words.clear();
		word = prefix.toLowerCase(Locale.ENGLISH);
		for (int i = 0; i < prefix.length(); i++)
		{
			index = getIndex(word.charAt(i));
			if (index == -1) return words;
			if (now.child[index] == null)
			{
				now = null;
				break;
			}
			now = now.child[index];
		}
		if (now != null) travel(now, prefix);
		return words;
	}
	
	/**
	 * 为单词树建立索引，只需在建树后调用一次
	 * @return 是否成功建立索引，成功返回true
	 */
	public boolean buildIndex()
	{
		tot = 0;
		if (travel_buildindex(root)) return true;
		else return false;
	}

	/**
	 * 获取前缀为prefix的单词的顺序索引，若没有单词以此为前缀，则返回向前的最接近这个字符串的单词的顺序索引
	 * @param prefix 单词的前缀
	 * @return 前缀为prefix的单词的顺序索引，或者向前的最接近这个字符串的单词的顺序索引
	 */
	public int getPrefixIndex(String prefix)
	{
		int childnum, i, j = 0;
		WordNode now = root;
		word = prefix;
		for (i = 0; i < prefix.length(); i++)
		{
			childnum = getIndex(word.charAt(i));
			if (childnum == -1) break;
			if (now.child[childnum] == null)
			{
				for (j = childnum-1; j >= 0; j--)				
					if (now.child[j] != null)
					{
						now = now.child[j];
						break;
					}
				break;
			}
			now = now.child[childnum];
		}
		if (i != prefix.length() && j >= 0) return travel_lastindex(now);
		else return travel_firstindex(now);
	}
	
	/**
	 * 获取单词str的意思索引；如果没有找到，则尝试还原成单词的原型继续搜索；如果还是没有，则返回-1 
	 * @param str 单词字符串
	 * @return 单词的意思索引或者单词原型的意思索引，若没有找到这个单词，返回-1
	 */
	public int getWordIndex(String str)
	{
		int index = -1;
		str = str.trim();
		index = getIntegralWordIndex(str);
		if (index > -1)
		{
			return index;
		}
		else
		{
			str = str.toLowerCase(Locale.ENGLISH);
			index = getIntegralWordIndex(str);			//小写
			if (index > -1)
			{
				return index;
			}
			if (str.endsWith("s"))
			{
				String newstr = str.substring(0, str.length()-1);
				index = getIntegralWordIndex(newstr);
				if (index > -1) return index;
			}
			if (str.endsWith("xes") || str.endsWith("ses"))
			{
				String newstr = str.substring(0, str.length()-2);
				index = getIntegralWordIndex(newstr);
				if (index > -1) return index;
			}
			if (str.endsWith("ies"))
			{
				String newstr = str.substring(0, str.length()-3)+"y";
				index = getIntegralWordIndex(newstr);
				if (index > -1) return index;
			}
			if (str.endsWith("ed"))
			{
				String newstr = str.substring(0, str.length()-1);
				index = getIntegralWordIndex(newstr);
				if (index > -1) return index;
				newstr = str.substring(0, str.length()-2);
				index = getIntegralWordIndex(newstr);
				if (index > -1) return index;
				if (newstr.charAt(newstr.length()-1) == newstr.charAt(newstr.length()-2))		//判双写尾字母
				{
					newstr = str.substring(0, str.length()-3);
					index = getIntegralWordIndex(newstr);
					if (index > -1) return index;
				}
				
			}
			if (str.endsWith("ied"))
			{
				String newstr = str.substring(0, str.length()-3)+"y";
				index = getIntegralWordIndex(newstr);
				if (index > -1) return index;
			}
			if (str.endsWith("ing"))
			{
				String newstr = str.substring(0, str.length()-3)+"e";
				index = getIntegralWordIndex(newstr);
				if (index > -1) return index;
				newstr = str.substring(0, str.length()-3);
				index = getIntegralWordIndex(newstr);
				if (index > -1) return index;
				if (newstr.charAt(newstr.length()-1) == newstr.charAt(newstr.length()-2))		//判双写尾字母
				{
					newstr = str.substring(0, str.length()-4);
					index = getIntegralWordIndex(newstr);
					if (index > -1) return index;
				}
			}
//		====================================================================
//			特判请况
			if (str.equals("tomatoes") || str.equals("negroes"))
			{
				String newstr = str.substring(0, str.length()-2);
				index = getIntegralWordIndex(newstr);
				if (index > -1) return index;
			}
		}
		return -1;
	}
	
	//获取integral单词的索引，若没有此单词，则返回-1
	private int getIntegralWordIndex(String integral)
	{
		int index, i;
		WordNode now = root;
		word = integral;
		for (i = 0; i < integral.length(); i++)
		{
			index = getIndex(word.charAt(i));
			if (index == -1) break;
			if (now.child[index] == null) break;
			now = now.child[index];
		}
		if (i != integral.length()) return -1;
		else if (now.isWord > -1) return now.isWord;
		else return -1;
	}
	
	/*
	public void displayAllword()
	{
		travel(root, "");
	}
	*/
	
	private void travel(WordNode node, String currentStr)
	{
//		if (node.isWord)
//		{
//			System.out.println(currentStr + " " + node.index);
//		}
		if (node.isWord > -1) words.add(currentStr);
		for (int i = 0; i < INDEX; i++)
		{
			if (node.child[i] != null)
			{
				travel(node.child[i], currentStr+node.child[i].character);
			}
		}
	}
	
	/**
	 * 获取以prefix为前缀的向后的最近单词的顺序索引；若没有以prefix为前缀的单词，返回-1
	 * @param prefix 单词的前缀
	 * @return 返回prefix为前缀的向后的最近单词的顺序索引；若没有，返回-1
	 */
	public int getFirstIndex(String prefix)
	{
		int childnum, i;
		WordNode now = root;
		word = prefix;
		for (i = 0; i < prefix.length(); i++)
		{
			childnum = getIndex(word.charAt(i));
			if (childnum == -1) break;
			now = now.child[childnum];
			if (now == null) break;
		}
		if (i == prefix.length()) return travel_firstindex(now);
		else return -1;
	}
	
	//返回以当前节点为根节点的子树，第一个单词节点的索引
	private int travel_firstindex(WordNode node)
	{
		if (node == null)
		{
			return -1;
		}
		if (node.isWord > -1)
		{
			return node.index;
		}
		for (int i = 0; i < INDEX; i++)
		{
			if (node.child[i] != null) return travel_firstindex(node.child[i]);
		}
		return -1;
	}
	
	//返回以当前节点为根节点的子树，最后一个单词节点的索引
	private int travel_lastindex(WordNode node)
	{
		if (node == null)
		{
			return -1;
		}
		for (int i = INDEX-1; i >= 0; i--)
		{
			if (node.child[i] != null) return travel_lastindex(node.child[i]);
		}
		if (node.isWord > -1)
		{
			return node.index;
		}
		return -1;
	}
	
	private boolean travel_buildindex(WordNode node)
	{
		if (node == null) return false;
		if (node.isWord > -1)
		{
			node.index = tot++;
		}
		for (int i = 0; i < INDEX; i++)
		{
			if (node.child[i] != null)
				if (!travel_buildindex(node.child[i])) return false;
		}
		return true;
	}
	
	/**
	 * 单词树的节点，存放一个字母，当前节点是否为单词，当前节点的意思索引
	 * @author James
	 *
	 */
	private class WordNode
	{
		private char character;
		private int isWord;
		private int index;
		private WordNode[] child = new WordNode[INDEX];
		
		/**
		 * 创建一个单词树节点，默认采用默认的参数
		 */
		public WordNode()
		{
		}
	
		/**
		 * 创建一个单词树节点，字母为ch，其他参数默认
		 * @param ch 当前节点的字母
		 */
		public WordNode(char ch)
		{
			character = ch;
			isWord = -1;
			index = -1;
		}
		
		/**
		 * 创建一个单词树节点，字母为ch，当前节点的意思索引或-1，index默认为-1
		 * @param ch 当前节点的字母
		 * @param isword 当前节点的意思索引或-1
		 */
		public WordNode(char ch, int isword)
		{
			character = ch;
			this.isWord = isword;
			index = -1;
		}
	}
}
