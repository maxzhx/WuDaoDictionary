import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *  单词学习模块的面板，可以添加删除生词，可以进行单词的读写练习，用于记忆单词
 * @author 郑启荣
 *
 */
public class NewWordNotePane extends JPanel{
	
	private FileReader input = null;
	private BufferedReader buffer = null;
	private static Dictionary dictionary; 
	private static WordLib wordLib = null;
	
	//用于操作生词的Swing组件
	private static LinkedList<String> NewWord= new LinkedList<String>();
	private static JList NewwordsList = new JList();
	private static JScrollPane NewWordListPanel = new JScrollPane();
	private JButton btnSpellWord = new JButton("拼读单词练习");
	private JButton btnPractice = new JButton("记忆单词练习");
	private JButton deleteWord = new JButton("删除");
	private JPanel btnPracticePane = new JPanel(new BorderLayout());
	private JPanel ButtonPane = new JPanel();
	
	/**
	 * 构造函数，构造一个生词学习模块的面板，采用默认的字符映射方式
	 * @param dictionary
	 */
	
	//构造函数
	public NewWordNotePane(Dictionary dictionary)
	{
		super();
		this.dictionary = dictionary;
		this.wordLib = dictionary.wordLib;
		this.setLayout(new BorderLayout());
		ReadNewWord();
		AddWordToList();
		this.add(NewWordListPanel,BorderLayout.CENTER);
		ButtonPane.add(btnSpellWord);
		ButtonPane.add(btnPractice);
		btnPracticePane.add(BorderLayout.EAST, ButtonPane);
		btnPracticePane.add(BorderLayout.WEST,deleteWord);
		//NewwordsList.setSelectedIndices(arg0)	
		this.add(btnPracticePane,BorderLayout.NORTH);
		btnPractice.addActionListener(new ActionHandler());
		btnSpellWord.addActionListener(new ActionHandler());
		deleteWord.addActionListener(new ActionHandler());
	}
	
	private class ActionHandler implements ActionListener
	{

		public void actionPerformed(ActionEvent e) 
		{
			if(e.getSource()==btnPractice)
			{
				if(NewWord.size()==0)
				{
					JOptionPane.showMessageDialog(null, "单词本现在无生词，请加入生词后再做练习");
				}
				else 
				{
					dictionary.setVisible(false);
					PracticeFrame practiceFrame = new PracticeFrame(1);
					practiceFrame.setLocationRelativeTo(NewWordNotePane.this);
					btnSpellWord.setEnabled(false);
					btnPractice.setEnabled(false);
				}
			}
			else if(e.getSource()==btnSpellWord)
			{
				if(NewWord.size()==0)
				{
					JOptionPane.showMessageDialog(null, "单词本现在无生词，请加入生词后再做练习");
				}
				else 
				{
					dictionary.setVisible(false);
					PracticeFrame practiceFrame = new PracticeFrame(2);
					practiceFrame.setLocationRelativeTo(NewWordNotePane.this);
					btnSpellWord.setEnabled(false);
					btnPractice.setEnabled(false);
				}
			}
			else if(e.getSource() == deleteWord)
			{
				DeleteNewWord();
			}
		}
		
	}
	
	//用于从列表中删除生词的函数
	private void DeleteNewWord()
	{
		int yesorno = 0;
		yesorno = JOptionPane.showConfirmDialog(null, "是否要删除这些词");
		if(yesorno==JOptionPane.YES_OPTION)
		{
			int[] Word = NewwordsList.getSelectedIndices();
			if(Word.length>0)
			{
				for(int i=Word.length-1;i>=0;i--)
				{
					String word = NewWord.get(Word[i]);
					NewWord.remove(NewWord.get(Word[i]));
					AddWordToList();
					try 
					{
						    FileReader fr = new FileReader("NewWord.txt");// 输入流对象
						    BufferedReader in = new BufferedReader(fr);
						    String line = in.readLine();// 读入一行数据
						    String text = "";
						    while (line != null) 
						    {
						    	 text = text + line + "\n";// 将数据附加到字符串变量text后
							     line = in.readLine();// 读入一行新数据
						    }
						    in.close();// 关闭输入流对象
						    fr.close();
						    text = text.replace(word+"@", "");
						    File file = new File("NewWord.txt");
						    file.delete();
						    File file1 = new File("NewWord.txt");
						    FileWriter fw = new FileWriter("NewWord.txt");// 创建输出流对象
							BufferedWriter out = new BufferedWriter(fw);
							out.write(text);// 将文本区内容写到指定文件中
							out.flush();// 关闭输出流
							fw.close();
							NewSpeakWord();
					}
					catch (Exception e1) 
					{
						 e1.getStackTrace();
					}
				}
		    }
		}
	}
	//用于把所有生词添加到List中的函数
	private static void AddWordToList()
	{
		NewwordsList.setFont(dictionary.myFont);
		String[] newword = new String[NewWord.size()];
		for(int i=0;i<NewWord.size();i++)
		{
			String blank="";
			for(int j=0;j<20-NewWord.get(i).length();j++)
			{
				blank=blank+" ";
			}
			newword[i] = NewWord.get(i) + blank +wordLib.getMean(dictionary.Tree.getWordIndex(NewWord.get(i)));
		}
		NewwordsList.setListData(newword);
		NewWordListPanel.setViewportView(null);
		NewWordListPanel.setViewportView(NewwordsList);
	}
	
	//在启动时读出所有生词的函数
	private  void ReadNewWord()
	{
		try 
		{
			    FileReader fr = new FileReader("NewWord.txt");// 输入流对象
			    BufferedReader in = new BufferedReader(fr);
			    String line = in.readLine();// 读入一行数据
			    String text = "";
			    while (line != null) 
			    {
			    	 text = text + line;// 将数据附加到字符串变量text后
				     line = in.readLine();// 读入一行新数据
			    }
			    in.close();// 关闭输入流对象
			    fr.close();
			    String NewWords[] = text.split("@");
			    for(int i=0;i<NewWords.length-1;i++)
			    {
			    	NewWord.add(NewWords[i]);
			    }
			    Collections.sort(NewWord);
				System.out.println(NewWord);
		}
		catch (Exception e1) 
		{
			 e1.getStackTrace();
		}
	}
	
//	TODO 复制文件		//生成发音词库
	private static void NewSpeakWord()
	{
		String text = "";
		for(int i = 0;i<NewWord.size();i++)
		{
			text=text+NewWord.get(i)+"\r\n";
		}
		File file = new File("example.txt");
		file.delete();
		File file1 = new File("example.txt");
		FileWriter fw;
		try
		{
			fw = new FileWriter("example.txt");// 创建输出流对象
			BufferedWriter out = new BufferedWriter(fw);
		    out.write(text);// 将文本区内容写到指定文件中
		    out.flush();// 关闭输出流
		    fw.close(); 
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * 用于添加一个生词到生词库中，需要一个String型的参数，这个参数为一个需要添加的单词。
	 * @param Word
	 */
	//用于添加新词的函数
	public static void AddNewWord(String Word) 
	{
		if(NewWord.indexOf(Word)==-1)
		{
			try 
			{
				    FileWriter fw = new FileWriter("NewWord.txt",true);// 创建输出流对象
					fw.write(Word+"@");// 将文本区内容写到指定文件中
					fw.close();
					NewWord.add(Word);
					Collections.sort(NewWord);
					System.out.println(NewWord);
					AddWordToList();
					NewSpeakWord();
			} 
			catch (Exception eo) 
			{
					eo.printStackTrace();
			}
		}
		
	} 
	
	/**
	 * 用于显示单练习模块的窗口，在单词练习时用于自动生成题目
	 * @author 郑启荣
	 *
	 */
//	TODO 复制文件	//显示问题的窗体
	private class PracticeFrame extends JFrame
	{
		//用于布局的SWING组件第一个CARD中的
		private JButton delete = new JButton("删除");
		private JButton next = new JButton("下一个");
		private JLabel Subject = new JLabel();//显示题目的一个label
		private ButtonGroup buttongroup = new ButtonGroup();//用于存放选项的一个BOX
		private JRadioButton[] Answer = new JRadioButton[4];//4个答案
		private JPanel SubjectPane = new JPanel(new BorderLayout());
		
		//用于布局的Pane
		private JPanel AnswerPane = new JPanel(new GridLayout(4,1,10,10));
		private JPanel ButtonPane = new JPanel(new GridLayout(0,2,10,10));
		private JPanel  p1 = new JPanel();
		private JPanel  p2 = new JPanel();
		private JPanel  p3 = new JPanel();
		
		//两个CARD中的组件
		private JTextArea RightAnswer = new JTextArea("",20,20);//记入显示信息的
		private JScrollPane RightAnswerpane = new JScrollPane(RightAnswer);
		private JButton NextQuestion = new JButton("下一题");
		private boolean[] mark = new boolean[NewWord.size()];
		private ActionHandler actionHandler = new ActionHandler();
		private CardLayout card = new CardLayout();
		private JPanel pane = new JPanel();
		
		//cardP3中的组件
		private JTextArea Questionmean = new JTextArea("sdsfddfsdf",10,10);  
		private JButton Sure = new JButton("确认");
		private JLabel Spell = new JLabel(" 拼读:");
		private JTextField WordTextFile = new JTextField("",10);
		private JButton Speak  = new JButton("语言识别");
		private int x;
		//用于保存产生的随机数的标量
		int index = 0;
		int index1 = 0;
		int index2 = 0;
		int type = 0;
		
		/**
		 * 构造函数，构造一个题目窗体，要以int型的参数用于确定题目类型。参数为1时代表生词选择题，参数为2时用于
		 * 生词拼写题
		 * @param x 题目类型
		 */
		//构造函数
		public PracticeFrame(final int x)
		{
			super();
			this.x=x;
			this.setVisible(true);
			this.setBounds(200, 200, 400, 300);
			this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			this.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent arg0) 
				{
					btnPractice.setEnabled(true);
					btnSpellWord.setEnabled(true);
				}
				
			});
			
			delete.addActionListener(new DeleteAction());
			delete.addKeyListener(new keyEventHandler());
			next.addKeyListener(new keyEventHandler());
			next.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					if(x==1)
					{
						NewPractice();
					}
					else if(x==2)
					{
						NextSpellQuestion();
					}
				}
			});
			ButtonPane.add(delete);
			ButtonPane.add(next);
			SubjectPane.add(Subject,BorderLayout.CENTER);
			SubjectPane.add(new JLabel(),BorderLayout.EAST);
			//初始化4个答案
			for(int i=0;i<4;i++)
			{
				Answer[i] = new JRadioButton(""+i);
				buttongroup.add(Answer[i]);
				AnswerPane.add(Answer[i]);
				Answer[i].addActionListener(actionHandler);
				Answer[i].addKeyListener(new keyEventHandler());
				
			}
			Answer[0].setMnemonic(KeyEvent.VK_1);
			Answer[1].setMnemonic(KeyEvent.VK_2);
			Answer[2].setMnemonic(KeyEvent.VK_3);
			Answer[3].setMnemonic(KeyEvent.VK_4);
			
			//card的布局
			//对p1的操作
			pane.setLayout(card);
			p1.setLayout(new BorderLayout());
			p1.add(SubjectPane,BorderLayout.NORTH);
			p1.add(AnswerPane,BorderLayout.CENTER);
			pane.add(p1,"p1");
			
			//对p2的操作
			p2.setLayout(new BorderLayout());
			RightAnswer.setFont(new Font("宋体", Font.BOLD, 20));
			RightAnswer.setLineWrap(true);
			RightAnswer.setEditable(false);
			p2.add(RightAnswerpane,BorderLayout.CENTER);
			JPanel buJPanel = new JPanel(new GridLayout(0,5));
			pane.add(p2,"p2");
			this.getContentPane().add(ButtonPane,BorderLayout.NORTH);
			this.getContentPane().add(pane,BorderLayout.CENTER);
			this.addKeyListener(new keyEventHandler());
			
			//对p3的操作
			p3.setLayout(new BorderLayout());
			JPanel panel = new JPanel(new GridLayout(0,4,10,10));
			Spell.setFont(new Font("宋体",Font.BOLD,20));
			Questionmean.setFont(new Font("宋体",Font.BOLD,18));
			Questionmean.setEditable(false);
			Questionmean.setLineWrap(true);
			panel.add(Spell);
			panel.add(WordTextFile);
			panel.add(Sure);
			panel.add(Speak);
			p3.add(Questionmean,BorderLayout.CENTER);
			p3.add(panel,BorderLayout.SOUTH);
			pane.add(p3,"p3");
			Sure.addActionListener(actionHandler);
			WordTextFile.addKeyListener(new keyEventHandler());
			Speak.addActionListener(actionHandler);
			Speak.addKeyListener(new keyEventHandler());
			
			//初始化标记数组
			for(int i=0;i<mark.length;i++)
			{
				mark[i]=false;
			}
			if(x==1)
			{
				NewPractice();
			}
			else if(x==2)
			{
				NextSpellQuestion();
				NewSpeakWord();
			}
		}
		
//		TODO 用于删除生词的函数
		private void DeleteWord(int x)
		{
			String word="";
			if(x==1)
			{
				if(type==0)
				{
					NewWord.remove(Subject.getText());
					word=Subject.getText();
				}
				else
				{
					word=NewWord.get(index);
					NewWord.remove(NewWord.get(index));
				}
			}
			else if(x==2)
			{
				word=NewWord.get(index);
				NewWord.remove(NewWord.get(index));
			}
			AddWordToList();
			mark = new boolean[NewWord.size()];
			//初始化标记数组
			for(int i=0;i<mark.length;i++)
			{
				mark[i]=false;
			}
			try 
			{
				    FileReader fr = new FileReader("NewWord.txt");// 输入流对象
				    BufferedReader in = new BufferedReader(fr);
				    String line = in.readLine();// 读入一行数据
				    String text = "";
				    while (line != null) 
				    {
				    	 text = text + line + "\n";// 将数据附加到字符串变量text后
					     line = in.readLine();// 读入一行新数据
				    }
				    in.close();// 关闭输入流对象
				    fr.close();
				    text = text.replace(word+"@", "");
				    File file = new File("NewWord.txt");
				    file.delete();
				    File file1 = new File("NewWord.txt");
				    FileWriter fw = new FileWriter("NewWord.txt");// 创建输出流对象
					BufferedWriter out = new BufferedWriter(fw);
					out.write(text);// 将文本区内容写到指定文件中
					out.flush();// 关闭输出流
					fw.close();
			}
			catch (Exception e1) 
			{
				 e1.getStackTrace();
			}
			finally
			{
				NewSpeakWord();
				if(x==1)
				{
				    NewPractice();
				}
				else if(x==2)
				{
					NextSpellQuestion();
				}
			}
		}
		
//		TODO 复制文件		//用于删除新词的动作事件
		private class DeleteAction implements ActionListener
		{
			public void actionPerformed(ActionEvent e)
			{
				int yesorno = 0;
				yesorno = JOptionPane.showConfirmDialog(PracticeFrame.this, "是否要删除本词");
				if(yesorno==JOptionPane.YES_OPTION)
				{
					if(x==1)
					{
						DeleteWord(1);
					}
					else
					{
						DeleteWord(2);
					}
					
				}
			}
		}
		
//		TODO 用于判断题目答案是否正确的函数	
		private void Charge(int i)
		{
			if(type==0)
			{
				if(Answer[i].getText().equals(wordLib.getMean(dictionary.Tree.getWordIndex(NewWord.get(index)))))
				{
					RightAnswer.setText("恭喜回答正确");
					ChangeCard("p2");
				}
				else
				{
					RightAnswer.setText("可惜了回答错误，继续努力。正确答案为"+wordLib.getMean(dictionary.Tree.getWordIndex(NewWord.get(index))));
					ChangeCard("p2");
				}
			}
			else
			{
				if(Answer[i].getText().equals(NewWord.get(index)))
				{
					RightAnswer.setText("恭喜回答正确");
					ChangeCard("p2");
				}
				else
				{
					RightAnswer.setText("可惜了回答错误，继续努力。正确答案为"+NewWord.get(index));
					ChangeCard("p2");
				}
			}
		}
		
		SoundRecognition sound = new SoundRecognition();
//		TODO 动作事件用于在选择答案时跳转到第二个card
		private class ActionHandler implements ActionListener
		{
			public void actionPerformed(ActionEvent e)
			{
				for(int i=0;i<4;i++)
				{
					if(e.getSource()==Answer[i])
					{
						//Answer[i].setSelected(false);
						Charge(i);
					}
				}
				if(e.getSource()==Sure)
				{
					ChargeSpell();
				}
				else if(e.getSource() == Speak)
				{
					ExecutorService executorService = Executors.newFixedThreadPool(1);
					executorService.execute(new Runnable() 
					{
						public void run() 
						{
							try 
							{
								Speak.setEnabled(false);
								WordTextFile.setText(sound.startRec());
								Speak.setEnabled(true);
								System.out.println("done");
							} 
							catch (Exception e2) 
							{
								
							}
						}
					});
//					Dictionary.takeWordSearch.startSearch();
					executorService.shutdown();
				}
			}
		}
		
		private class keyEventHandler extends KeyAdapter
		{

			public void keyPressed(KeyEvent e) 
			{
				if(e.getKeyCode() == KeyEvent.VK_1)
				{
					Charge(0);
				}
				else if(e.getKeyCode() == KeyEvent.VK_2)
				{
					Charge(1);
				}
				else if(e.getKeyCode() == KeyEvent.VK_3)
				{
					Charge(2);
				}
				else if(e.getKeyCode() == KeyEvent.VK_4)
				{
					Charge(3);
				}
				else if(e.getKeyCode() == KeyEvent.VK_D)
				{
					if(x==1)
					{
					int yesorno = 0;
					yesorno = JOptionPane.showConfirmDialog(PracticeFrame.this, "是否要删除本词");
					if(yesorno==JOptionPane.YES_OPTION)
						DeleteWord(1);
					}
				}
				else if(e.getKeyCode() == KeyEvent.VK_RIGHT)
				{
					if(x==1)
					{
						NewPractice();
					}
					else if(x==2)
					{
						NextSpellQuestion();
					}
				}
				else if(e.getKeyCode()==KeyEvent.VK_ENTER)
				{
					ChargeSpell();
				}
			}
			
		}
		
//		TODO 复制文件		//用于跳转card的函数
		private void ChangeCard(String p)
		{
			card.show(pane, p);
		}
		
//		TODO 复制文件		//用于产生下一个题目的函数
		private void NewPractice()
		{
			ChangeCard("p1");
			boolean flag=false;
			for(int i=0;i<NewWord.size();i++)
			{
				if(!mark[i])
					flag=true;
			}
			if(!flag)
			{
				for(int i=0;i<NewWord.size();i++)
				{
					mark[i]=false;
				}
			}
			Random random = new Random();
			while(true)
			{
				index = random.nextInt(NewWord.size());
				if(!mark[index])
				{
					mark[index]=true;
					break;
				}
			}
			type = random.nextInt(2);
			if(type==0)
			{
				Subject.setText(NewWord.get(index));
				boolean[] num = new boolean[4];
				for(int i=0;i<4;i++)
				{
					num[i]=false;
				}
				int index1 = random.nextInt(4);
				Answer[index1].setText(wordLib.getMean(dictionary.Tree.getWordIndex(NewWord.get(index))));
				num[index1]=true;
				for(int i=0;i<3;i++)
				{
					while(true)
					{
						index2 = random.nextInt(4);
						if(!num[index2])
							break;
					}
					if(NewWord.size()<10)
					{
						
						Answer[index2].setText(wordLib.getMean(dictionary.Tree.getWordIndex(wordLib.getWord(random.nextInt(15000)))));
					}
					else
					{
						Answer[index2].setText(wordLib.getMean(dictionary.Tree.getWordIndex(wordLib.getWord(random.nextInt(NewWord.size())))));
					}
					num[index2]=true;
				}
			}
			else
			{
				Subject.setText(wordLib.getMean(dictionary.Tree.getWordIndex(NewWord.get(index))));
				boolean[] num = new boolean[4];
				for(int i=0;i<4;i++)
				{
					num[i]=false;
				}
				index1 = random.nextInt(4);
				Answer[index1].setText(NewWord.get(index));
				num[index1]=true;
				for(int i=0;i<3;i++)
				{
					int index2 = 0;
					while(true)
					{
						index2 = random.nextInt(4);
						if(!num[index2])
							break;
					}
					if(NewWord.size()<10)
					{
						Answer[index2].setText(wordLib.getWord(random.nextInt(15000)));
					}
					else 
					{
						Answer[index2].setText(NewWord.get(random.nextInt(NewWord.size())));
					}
					num[index2]=true;
				}
			}
		}
		
		
//		TODO 复制文件		//用于生成下一个spell题目的函数
		private void NextSpellQuestion()
		{
			ChangeCard("p3");
			boolean flag=false;
			for(int i=0;i<NewWord.size();i++)
			{
				if(!mark[i])
					flag=true;
			}
			if(!flag)
			{
				for(int i=0;i<NewWord.size();i++)
				{
					mark[i]=false;
				}
			}
			Random random = new Random();
			while(true)
			{
				index = random.nextInt(NewWord.size());
				if(!mark[index])
				{
					mark[index]=true;
					break;
				}
			}
			Questionmean.setText(wordLib.getMean(dictionary.Tree.getWordIndex(NewWord.get(index))));
			WordTextFile.setText("");
			WordTextFile.requestFocus();
		}
		
//		TODO 复制文件		//用于判断拼写正确与否的函数
		private void ChargeSpell()
		{
			String word = WordTextFile.getText();
			if(word.equals(NewWord.get(index)))
			{
				RightAnswer.setText("恭喜回答正确");
				ChangeCard("p2");
			}
			else 
			{
				RightAnswer.setText("可惜了回答错误，继续努力。正确答案为"+NewWord.get(index));
				ChangeCard("p2");
			}
		}
		
	}
}
