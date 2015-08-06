import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.MenuItem;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;


/**
 * 学习模块面板，用于打开文章，可以进行文章的管理，可以读文章添加注释等
 * 注意：默认的收藏夹路径是 C:\File\
 * @author 郑启荣
 *
 */
public class LearnEnglishPane extends JPanel {
	
	private Dictionary dictionary = null;
	private WordLib wordLib = null;
	
	private Font hintFont = new Font(Font.SERIF, Font.PLAIN, 14);
	
	//界面构造用到的swing组件
	private JTextArea article = new JTextArea("");
	private JScrollPane articleScrollpane = new JScrollPane(article);
	private JScrollBar sBar = articleScrollpane.getVerticalScrollBar();
	private JPanel LeftPane = new JPanel(new BorderLayout());
	
	private JPanel remarkPanel = new JPanel();
	private JScrollPane remarkScrollpane = new JScrollPane(remarkPanel);
	
	private JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
	private JButton upbutton = new JButton("up");
	private JButton downbutton = new JButton("down");
	
	private JLabel remark = new JLabel("单词注释");
	
	//文件管理树
    private JScrollPane fileManageScrollPane =new JScrollPane();//用于文件管理的面板
    private DefaultMutableTreeNode root=new DefaultMutableTreeNode("File"); //文件管理树的跟结点
    private JTree tree=new JTree(root);//文件管理树
    private JButton Refresh = new JButton("刷新");
	
	//分割窗口
	private JSplitPane jsp;
	
	//文章域的弹出式菜单
	private JPopupMenu articlePopMenu = new JPopupMenu();
	private JMenuItem open = new JMenuItem("打开               ");
	private JMenuItem collect = new JMenuItem("收藏                 ");
	private JMenuItem listen = new JMenuItem("听录音             ");
	private JMenuItem record = new JMenuItem("录音               ");
	private JMenuItem practice = new JMenuItem("练习               ");
	private JTextField status = new JTextField("");
	
	private String articlrName = "";
	private boolean isOpen=false;
	
	//文件管理域的弹出式菜单
	private JPopupMenu FileManagepopmenu = new JPopupMenu();
	private JMenuItem CopyTo = new JMenuItem("复制到               ");
	private JMenuItem CutTo = new JMenuItem("剪切到               ");
	private JMenuItem Delete = new JMenuItem("删除               ");
	private JMenuItem SetDir = new JMenuItem("新建文件夹         ");
	
	
	//各种事件生成
	private JTabbedPane tabbedPane=new JTabbedPane();		//多桌面控件
	private reamouseHandler reamouseHandler = new reamouseHandler();
	private ActionHandler actionHandler = new ActionHandler();		//动作事件
	private ArticleMouseHandler articleMouseHandler = new ArticleMouseHandler();		//鼠标事件
	private ArticleMouseMotionHandler articleMouseMotionHandler = new ArticleMouseMotionHandler();		//鼠标事件
	private TreemouseHandler treemouseHandler = new TreemouseHandler();			//文件树的鼠标事件
	 
	//各种数据结构
	private int remaryNum=0;
	private int splitPosition = 250;
	private LinkedList<Remark> Word = new LinkedList<Remark>();
	private JTextArea[] remarks = new JTextArea[1000];
	private boolean IsExistText = false;
	public String RemarkFilePath=null;
	private static ArrayList filelist = new ArrayList(); 
	//private NewWordNotePanel newWordNotePanel;
	
	
	/**
	 * 构造函数，构造一个学习模块的面板，需要主窗体dictionary.Tree和dictionary.wordLib的功能支持
	 * @param dictionary 主窗体Dictionary
	 */
	public LearnEnglishPane(Dictionary dictionary)
	{
		super();
		this.dictionary = dictionary; 
		this.wordLib = dictionary.wordLib;
		
		article.setFont(dictionary.myFont);
		status.setFont(dictionary.myFont);
		
//		TODO 界面设计
		setLayout(new BorderLayout());
		
		open.addActionListener(actionHandler);
		articlePopMenu.add(open);
		articlePopMenu.addSeparator();
		articlePopMenu.add(collect);
		articlePopMenu.addSeparator();
		articlePopMenu.add(listen);
		articlePopMenu.addSeparator();
		articlePopMenu.add(record);
		articlePopMenu.addSeparator();
		articlePopMenu.add(practice);
		
//		文本域
		article.setComponentPopupMenu(articlePopMenu);//设置文本域的弹出式菜单
		article.setEditable(false);
		article.setLineWrap(true);
		article.addMouseListener(articleMouseHandler);
		article.addMouseMotionListener(articleMouseMotionHandler);
		collect.addActionListener(actionHandler);
		
//		注释域
		tabbedPane.addTab("Remark",null,remarkScrollpane,"Remark");
		remarkScrollpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		remarkPanel.setLayout(new GridLayout(0,1));
		remarkPanel.add(remark);
		remarkPanel.setBorder(new TitledBorder("单词解释"));
		
		//文件管理域
        refreshFileList("C:\\File\\",root);
        JPanel panel = new JPanel();
        JPanel refreshpanel = new JPanel(new GridLayout(0,3));
        refreshpanel.add(new JLabel());
        refreshpanel.add(Refresh);
        refreshpanel.add(new JLabel());
        panel.setLayout(new BorderLayout());
        panel.add(fileManageScrollPane,BorderLayout.CENTER);
        panel.add(refreshpanel,BorderLayout.SOUTH);
		tabbedPane.addTab("File",null,panel,"File");
		tree.addMouseListener(treemouseHandler);
	    fileManageScrollPane.setViewportView(tree);
	    CutTo.addActionListener(actionHandler);
	    FileManagepopmenu.add(CutTo);
	    CopyTo.addActionListener(actionHandler);
	    FileManagepopmenu.add(CopyTo);
	    FileManagepopmenu.add(Delete);
	    FileManagepopmenu.add(SetDir);
	    expandAllNode(tree,new TreePath(root),true);
	   // tree.setComponentPopupMenu(FileManagepopmenu);
	    Refresh.addActionListener(actionHandler);
	    Delete.addActionListener(actionHandler);
	    SetDir.addActionListener(actionHandler);
		
		//按钮域
	    JPanel panel2 = new JPanel(new BorderLayout());
		upbutton.addActionListener(actionHandler);
		buttonPanel.add(upbutton);
		buttonPanel.add(downbutton);
		downbutton.addActionListener(actionHandler);
		status.setEditable(false);
		panel2.add(status,BorderLayout.SOUTH);
		
		LeftPane.add(BorderLayout.CENTER, articleScrollpane);
		LeftPane.add(BorderLayout.SOUTH, buttonPanel);
		jsp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, LeftPane, tabbedPane);
		jsp.setDividerLocation(jsp.getWidth() - splitPosition);
//		设置右边侧边栏的宽度不随窗体的变化而变化
		dictionary.addComponentListener(new ComponentAdapter()
	      {
	    	  public void componentResized(ComponentEvent e)
	    	  {
	    		  jsp.setDividerLocation(jsp.getWidth() - splitPosition);
	    	  } 
	      }
		);
		dictionary.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent arg0)
			{
				if (RemarkFilePath != null)
					saveremark(new File(RemarkFilePath));
			}
		});
		
		add(jsp, BorderLayout.CENTER);
		add(panel2,BorderLayout.SOUTH);
	}
	
//	 打开文件夹及其内容的函数（递归调用）
	 private static void refreshFileList(String strPath, DefaultMutableTreeNode node) 
	 { 
	        File dir = new File(strPath); 
	        File[] files = dir.listFiles(); 
	        if (files == null) 
	            return; 
	        for (int i = 0; i < files.length; i++)
	        { 
	            if (files[i].isDirectory())
	            { 
	            	DefaultMutableTreeNode node1=new DefaultMutableTreeNode(files[i].getName());
	                node.add(node1);
	                refreshFileList(files[i].getAbsolutePath(),node1); 
	            } 
	            else 
	            { 
	                if(files[i].getName().endsWith(".txt"))
	                {
	                	DefaultMutableTreeNode node2=new DefaultMutableTreeNode(files[i].getName());
	                	node.add(node2);
	                }
	                filelist.add(files[i].getAbsolutePath());
	            } 
	        } 
	 }

//		TODO 	//用于打开的函数
	private void Open()
	{
		IsExistText=true;
		JFileChooser fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("文本文档(*.txt)", "txt");
		fileChooser.setFileFilter(filter);
		int returnValue;
		returnValue = fileChooser.showOpenDialog(this);
		if(returnValue == fileChooser.APPROVE_OPTION)
		{
			OpenAs(fileChooser.getSelectedFile().getAbsolutePath());
		}
	}
	
//	TODO   用于打开文件内容，加载注释
	private void OpenAs(String Path) 
	{
		String filePath=Path;
		File file = new File(filePath); 		// 构造文件对象
		articlrName=file.getName();
	    try 
		{
//	    	判断编码类型UTF-8或者其他
			InputStream ios = new FileInputStream(file);
			byte[] b = new byte[3];
			ios.read(b);
			ios.close();
			InputStream fr = new FileInputStream(file);		// 输入流对象
			InputStreamReader in = null;					// 文件输入流
			char[] t = new char[fr.available()];
			if(b[0]==-17 && b[1]==-69 && b[2]==-65)
			{
         		System.out.println(file.getName() + "编码为UTF-8");
         		in = new InputStreamReader(fr, "UTF-8");
         	}
			else
			{
				System.out.println(file.getName() + "可能是GBK");
				in = new InputStreamReader(fr, "GBK");
			}
		    in.read(t);
		    in.close();
		    fr.close();
		    
		    String text = new String(t);
		    article.setText(text);
//		    ???为啥直接写不行，绘图机制
		    SwingUtilities.invokeLater(new Runnable()
		    {
				public void run()
				{
					 sBar.setValue(0);	
				}
		    	
		    });
		    in.close();// 关闭输入流对象
		    fr.close();
		} 
		catch (Exception e1) 
		{
			 e1.getStackTrace();
		}
	    String filePathInf = filePath.replaceAll("txt","inf" );
		File fileinf = new File(filePathInf);
	    try 
		{
	    	remarkPanel.removeAll();
	    	while(true)
			{
			   if(Word.isEmpty())
			   {
			      System.out.print("break");
			      break;
			   }
			   Word.remove();
			}
	    	RemarkFilePath=filePathInf;
		    FileReader fr = new FileReader(fileinf);// 输入流对象
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
		   
		    String remarkword[] = text.split("@");
		   
		    for(int i = 1; i < remarkword.length-1; i += 3)
		    {
		    	Word.add(new Remark(remarkword[i],remarkword[i+1],Integer.parseInt(remarkword[i+2])));
		    	System.out.println(remarkword[i+1]);
		    }
		    remarkPanel.removeAll();
		    for(int i = 0; i < Word.size(); i++)
			{
				remarks[i] = new JTextArea((i+1)+":"+Word.get(i).mean);
				remarks[i].setLineWrap(true);
				remarks[i].setEditable(false);
				remarks[i].setFont(hintFont);
				remarks[i].addMouseListener(reamouseHandler);
				remarkPanel.add(remarks[i]);
			}
			dictionary.show();
		} 
		catch (Exception e1) 
		{
			 e1.getStackTrace();
		}
	    isOpen=true;				//标记已打开
	}

	
 	//把新添加的注释添加到注释文件中
	private void saveremark(File file)
	{
		String text ="";
		for(int i=0;i<Word.size();i++)
		{
			text+="@"+Word.get(i).word+"@"+Word.get(i).mean+"@"+Word.get(i).index;
		}
		text+="@";
		try 
		{
			    FileWriter fw = new FileWriter(file);// 创建输出流对象
				BufferedWriter out = new BufferedWriter(fw);
				out.write(text);// 将文本区内容写到指定文件中
				out.flush();// 关闭输出流
				fw.close();
		} 
		catch (Exception eo) 
		{
				eo.printStackTrace();
		}
	}
	
//	TODO 	//展开文件树
    private void expandAllNode(JTree tree, TreePath parent, boolean expand) {
        // Traverse children
        TreeNode node = (TreeNode) parent.getLastPathComponent();
        if (node.getChildCount() >= 0) {
            for (Enumeration<?> e = node.children(); e.hasMoreElements();) {
                TreeNode n = (TreeNode) e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                //expandAllNode(tree, path, expand);//递归调用展开文件树
            }
        }

        if (expand) {
            tree.expandPath(parent);
        } else {
            tree.collapsePath(parent);
        }
    }
	
	
//	TODO 	//刷新文件树
	private void Refresh()
	{
		root.removeAllChildren();
		tree = new JTree(root);
		tree.addMouseListener(treemouseHandler);
		//tree.setComponentPopupMenu(FileManagepopmenu);
		refreshFileList("C:\\File\\",root);
		fileManageScrollPane.setViewportView(null);
		fileManageScrollPane.setViewportView(tree);
		expandAllNode(tree,new TreePath(root),true);
		dictionary.show();
	}
	
//	TODO 	//用于创建文件夹的函数
	private void SetDir(int n)
	{
		Object[] Path =  tree.getSelectionPath().getPath();
		String FilePath="C:\\";
		for(int i=0;i<Path.length-n;i++)
		{
			FilePath=FilePath+Path[i]+"\\";
		}
		String fileName = JOptionPane.showInputDialog(this, "输入文件夹名称");
		FilePath = FilePath+fileName;
		File file = new File(FilePath); 
		int yesorno=0;
		if(file.exists())
		{
			yesorno = JOptionPane.showConfirmDialog(null, fileName+"以存在是否要覆盖");
		}
		if(yesorno==0 && !file.getName().equals("null"))
		{
			file.mkdir();
		}
		Refresh();
	}
	
//	TODO 	//动作事件
	private class ActionHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if(e.getSource()==open)
			{
				if(IsExistText==true)
				{
					for(int i=0;i<Word.size();i++)
					{
						System.out.println(Word.get(i).GetWord());
						NewWordNotePane.AddNewWord(Word.get(i).GetWord());
					}
					File file = new File(RemarkFilePath);
					file.delete();
					saveremark(new File(RemarkFilePath));
				}
				Open();
				dictionary.show();
			}
			else if(e.getSource()==collect)
			{
				if(isOpen==true)
				{
					FileManageFrame fileManageFrame = new FileManageFrame(2);
					fileManageFrame.setLocationRelativeTo(LearnEnglishPane.this);
				}
			}
			if(e.getSource()==upbutton)
			{
				int rowvalue = 22;//(sBar.getMaximum()-sBar.getMinimum())/article.getLineCount();
				int value = sBar.getValue();
				System.out.print(rowvalue);
				sBar.setValue(value-rowvalue*(int)(articleScrollpane.getSize().getHeight()/(article.getFont().getSize()*1.5)));
			}
			else if(e.getSource()==downbutton)
			{
				int rowvalue = 22;//(sBar.getMaximum()-sBar.getMinimum())/article.getLineCount();
				int value = sBar.getValue();
				System.out.print(1);
				sBar.setValue(value+rowvalue*(int)(articleScrollpane.getSize().getHeight()/(article.getFont().getSize()*1.5)));
			}
			if(e.getSource()==Refresh)
			{
				Refresh();
			}
			if(e.getSource()==Delete)
			{
				Object[] Path =  tree.getSelectionPath().getPath();
				String FilePath="C:\\";
				int i;
				for(i=0;i<Path.length-1;i++)
				{
					FilePath=FilePath+Path[i]+"\\";
				}
				int k=i;
				FilePath=FilePath+Path[i];
				File fileSource  = new File(FilePath);
				int yesorno = JOptionPane.showConfirmDialog(null, "是否要删除"+fileSource.getName());
				if(yesorno==0)
				{
					if(fileSource.getName().toString().endsWith(".txt"))
					{
						File fileSourceinf  = new File(FilePath.replace("txt", "inf"));
						fileSource.delete();
						fileSourceinf.delete();
						Refresh();
					}
					else
					{
						deleteFile(fileSource);
						Refresh();
					}
				}
			}
			else if(e.getSource()==SetDir)
			{
				Object[] Path =  tree.getSelectionPath().getPath();
				if(Path[Path.length-1].toString().endsWith(".txt"))
				{
					SetDir(1);
				}
				else
				{
					SetDir(0);
				}
			}
			
			if(e.getSource()==CutTo)
			{
				FileManageFrame fileManageFrame = new FileManageFrame(0);
				fileManageFrame.setLocationRelativeTo(LearnEnglishPane.this);
			}
			
			if(e.getSource()==CopyTo)
			{
				FileManageFrame fileManageFrame = new FileManageFrame(1);
				fileManageFrame.setLocationRelativeTo(LearnEnglishPane.this);
			}
			
		}
		
	}
	
//	TODO 	//用于删除文件的函数
	private void deleteFile(File file)
	{ 
		if(file.exists())//判断文件是否存在
		{                    
			if(file.isFile())//判断是否是文件
		    {                    
				file.delete();                       //delete()方法 你应该知道 是删除的意思;
		    }
		    else if(file.isDirectory()) //否则如果它是一个目录
		    {             
		    	File files[] = file.listFiles();               //声明目录下所有的文件 files[];
		    	for(int i=0;i<files.length;i++)//遍历目录下所有的文件
		        {            
		    	    this.deleteFile(files[i]);             //把每个文件 用这个方法进行迭代
		        } 
		    } 
			file.delete(); 
		 }
	}
	
//	TODO 	//文件管理树鼠标事件
	private class TreemouseHandler extends MouseAdapter
	{
		public void mouseClicked(MouseEvent e)
		{
			if( e.getButton() == e.BUTTON1 && e.getClickCount() == 2 ) 
			{
				Object[] Path =  tree.getSelectionPath().getPath();
				String FilePath="C:\\";
				int i;
				for(i=0;i<Path.length-1;i++)
				{
					FilePath=FilePath+Path[i]+"\\";
				}
				FilePath=FilePath+Path[i];
				if(FilePath.endsWith(".txt"))
				{
					if(IsExistText==true)
					{
//						关闭已打开的文件，保存注释。
						File file = new File(RemarkFilePath);
						file.delete();
						saveremark(new File(RemarkFilePath));
					}
					IsExistText=true;
					OpenAs(FilePath);
//TODO				dictionary.show();
				}
			}
			if(e.getButton()==e.BUTTON3)
			{
				int   selRow   =   tree.getRowForLocation(e.getX(),e.getY()); 
				System.out.println(selRow);
                // TreePath   selPath   =   tree.getPathForLocation(e.getX(),   e.getY()); 
                tree.setSelectionRow(selRow);
                FileManagepopmenu.show(tree, e.getX(), e.getY());
			}
		}
		
	}
	
	
	private class ArticleMouseMotionHandler extends MouseMotionAdapter
	{
		public void mouseMoved(MouseEvent e)
		{
			if(article.getText().length()>1)
			{
			int p = article.viewToModel(e.getPoint());
			int up=0;
			int down=0;
			String txtString;
			boolean exist=false;
			try 
			{
				while(true)
				{
					txtString = article.getText(p-1,1);
					p--;
					if(!txtString.matches("\\w"))
						break;
					else
					{
						up++;
					}
				}
				while(true)
				{
					txtString = article.getText(p+1,1);
					p++;
					if(!txtString.matches("\\w"))
						break;
					else
					{
						down++;
					}
				}
				txtString=article.getText(p-down,down);
				//点击位置处无单词时
				if(up==0)
					status.setText("本处无单词");
				
				//单击位置的单词在词典查询不到时
				if(dictionary.Tree.getWordIndex(txtString)==-1)
					status.setText("本词典查无此词");
				else 
				{
					int wordIndex = dictionary.Tree.getWordIndex(txtString);
					String mean = wordLib.getWord(wordIndex) + " " + wordLib.getMean(wordIndex).replace('\n', ' ');
					status.setText(mean);
				}
			}
			catch (BadLocationException e1) 
			 {
				 e1.printStackTrace();
			 }
			}
		}
	}
	
//	TODO 文本域鼠标事件
	private class ArticleMouseHandler extends MouseAdapter
	{
		//article的单击响应
		public void mouseClicked(MouseEvent e)
		{
			//File file = new File("C:\\1.txt");//创建一个注释文件
			int buttonKey = e.getButton();
			//但是鼠标左键的时候区单间位置的一个单词
			if( buttonKey == e.BUTTON1 && e.getClickCount() == 2 ) 
			{
				int p = article.getCaretPosition();
				int up = 0;
				int down = 0;
				String txtString;
				boolean exist=false;
				try 
				{
					while(true)
					{
						
						txtString = article.getText(p-1,1);
						System.out.println(txtString);
						p--;
						if(!txtString.matches("\\w"))
							break;
						else
						{
							up++;
						}
					}
					while(true)
					{
						txtString = article.getText(p+1,1);
						System.out.println(txtString);
						p++;
						if(!txtString.matches("\\w"))
							break;
						else
						{
							down++;
						}
					}
					txtString = article.getText(p-down,down);
					//点击位置处无单词时
					if(up==0)
						System.out.println("null");
					
					//单击位置的单词在词典查询不到时
					if(dictionary.Tree.getWordIndex(txtString)==-1)
						System.out.print(-1);
					else 
					{
						int wordIndex = dictionary.Tree.getWordIndex(txtString);
//						获得单词意思，使用单词的完整形式
						String mean = wordLib.getWordFull(wordIndex) + "\n" + wordLib.getPhonogram(wordIndex) 
								+ wordLib.getMean(wordIndex);
						
						int index=0;			//单词第一次出现的位置
						while(true)
						{
							String textString;
							if(index==0)
							{
								textString = article.getText();
								index = textString.indexOf(txtString);
							}
							else 
							{
								textString = article.getText(index + txtString.length(), article.getText().length() - index - txtString.length());
								index = textString.indexOf(txtString)+index+txtString.length();
							}
							String nextChar = article.getText(index+txtString.length(), 1);
							if(nextChar.matches("\\W"))
							{
								for(int i = 0; i < Word.size();i++)
								{
									if(Word.get(i).GetWord().equals(txtString))
									{
										exist = true;
										break;
									}
								}
								if(exist == false)
								{
									remarks[Word.size()] = new JTextArea(++remaryNum + mean);
									remarkPanel.add(remarks[Word.size()]);
//									添加到生词本
									NewWordNotePane.AddNewWord(txtString);
									Word.add(new Remark(txtString,mean,index));
									for(int i = 0; i < Word.size(); i++)
										for(int j = 0; j < Word.size()-1-i; j++)
											if(Word.get(j).index > Word.get(j+1).index)
											{
												Remark a = new Remark(Word.get(j));
												Word.set(j, Word.get(j+1));
												Word.set(j+1, a);
											}
									remarkPanel.removeAll();
									for(int i = 0; i < Word.size(); i++)
									{
										remarks[i].setEditable(false);
										remarks[i]=new JTextArea((i+1)+":"+Word.get(i).mean);
										remarks[i].setLineWrap(true);
										remarks[i].setFont(hintFont);
										remarks[i].addMouseListener(reamouseHandler);
										remarkPanel.add(remarks[i]);
									}
									dictionary.show();
								}
								break;
							}
						}
					} 
				 } 
				 catch (BadLocationException e1) 
				 {
					 e1.printStackTrace();
				 }
			 }
		}
	}

	//注释域的鼠标事件
	private class reamouseHandler extends MouseAdapter
	{
		public void mouseClicked(MouseEvent e)
		{
			if( e.getButton() == e.BUTTON1 && e.getClickCount() == 2 ) 
			{
				int num = 0;
				//弹出一个消息对话框用于判断是否要删除本注释，是返回0，否返回1，取消返回2
				int yesorno = JOptionPane.showConfirmDialog(null, "是否要删除本注释");
				
				//当选择删除时删除本注释
				if(yesorno == 0)
				{
					for(int i=0;i<Word.size();i++)
					{
						if(e.getSource()==remarks[i])
						{
							num=i;
							break;
						}
					}
					Word.remove(num);
					remarkPanel.removeAll();
					for(int i=0;i<Word.size();i++)
					{
						remarks[i]=new JTextArea((i+1)+":"+Word.get(i).mean);
						remarks[i].setFont(hintFont);
						remarks[i].setEditable(false);
						remarks[i].addMouseListener(reamouseHandler);
						remarkPanel.add(remarks[i]);
					}
					dictionary.show();
				}
			}
		}
	}
	
//	TODO 	//记录注释用的类
	private class Remark
	{
		private String word;
		private String mean;
		private int index;
		public Remark()
		{
			
		}
		public Remark(Remark a)
		{
			word=a.word;
			mean=a.mean;
			index=a.index;
		}
		public Remark(String word,String mean,int index)
		{
			this.word=word;
			this.mean=mean;
			this.index=index;
		}
		public String GetWord()
		{
			return word;
		}
	}
	
	
//	TODO 文件管理窗体
	private class FileManageFrame extends JFrame
	{
		 private JScrollPane fileManageScrollPane =new JScrollPane();	//用于文件管理的面板
		 private DefaultMutableTreeNode root1=new DefaultMutableTreeNode("File");		//文件管理树的跟结点
		 private JTree tree1=new JTree(root1);							//文件管理树
		 private JButton sureBtn = new JButton("确定");
		 private JButton cancelBtn = new JButton("取消");
		 private JPanel buttonPanel = new JPanel();
		 
		 public  FileManageFrame( final int n)
		 {
			 buttonPanel.setLayout(new GridLayout(0,3));
			 buttonPanel.add(sureBtn);
			 buttonPanel.add(new JLabel(""));
			 buttonPanel.add(cancelBtn);
			 
			 add(fileManageScrollPane,BorderLayout.CENTER);
			 add(buttonPanel,BorderLayout.SOUTH);
			 
			 refreshFileList("C:\\File\\",root1);
			 fileManageScrollPane.setViewportView(tree1);
			 setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			 setSize(200, 300);
			 setVisible(true);
			 sureBtn.addActionListener(new ActionListener()
			 {
				public void actionPerformed(ActionEvent arg0)
				{
					if(n==2)
					{
						if(!tree1.getSelectionPath().toString().endsWith(".txt]"))
						{
							Object[] Path1 =  tree1.getSelectionPath().getPath();
							String FilePath="C:\\";
							int i;
							for(i = 0; i < Path1.length; i++)
							{
								FilePath=FilePath+Path1[i]+"\\";
							}
							FilePath=FilePath+articlrName;
							File file = new File(FilePath);
							int yesorno=0;
							if(file.exists()==true)
								yesorno = JOptionPane.showConfirmDialog(null, articlrName+"已存在是否要覆盖");
							if(yesorno==0)
							{
								try 
								{
									    FileWriter fw = new FileWriter(file);		// 创建输出流对象
										BufferedWriter out = new BufferedWriter(fw);
										out.write(article.getText());				// 将文本区内容写到指定文件中
										out.flush();								// 关闭输出流
										fw.close();
										saveremark(new File(FilePath.replace("txt", "inf")));
								} 
								catch (Exception eo) 
								{
										eo.printStackTrace();
								}
								Refresh();
								Close();
							}
						}
						else 
						{
							JOptionPane.showMessageDialog(null, "请选择一个文件夹为目的位置");
						}
					}
					
					else 
					{
					if(!tree1.getSelectionPath().toString().endsWith(".txt]") && tree.getSelectionPath().toString().endsWith(".txt]"))
					{
						System.out.println( tree.getSelectionPath().toString());
						Copy(tree.getSelectionPath(), tree1.getSelectionPath(),n);
					}
					else if(!tree1.getSelectionPath().toString().endsWith(".txt]") && !tree.getSelectionPath().toString().endsWith(".txt]"))
					{
						Object[] Path =  tree.getSelectionPath().getPath();
						String FilePath="C:\\";
						int i;
						for(i=0;i<Path.length-1;i++)
						{
							FilePath=FilePath+Path[i]+"\\";
						}
						int k=i;
						FilePath=FilePath+Path[i];
						File fileSource  = new File(FilePath);
						Object[] Path1 = tree1.getSelectionPath().getPath();
						FilePath="C:\\";
						for(i=0;i<Path1.length;i++)
						{
							FilePath=FilePath+Path1[i]+"\\";
						}
						FilePath=FilePath+Path[k];
						File fileto = new File(FilePath);
						int yesorno=0;
						if(fileto.exists()==true)
							yesorno = JOptionPane.showConfirmDialog(null, Path[k]+"已存在是否要覆盖");
						if(fileto.exists()==false)
							fileto.mkdir();
						if(yesorno==0)
							DirectoryCopy(fileSource.getAbsolutePath(),fileto.getAbsolutePath(),n);
						if(n==0 && fileSource.isDirectory()==true)
						{
							File[] files = fileSource.listFiles(); 
							if(files.length==0)
								deleteFile(fileSource);
						}
						if(n==0 && fileSource.isDirectory()==false)
						{
							fileSource.delete();
						}
						Refresh();
						Close();
						
					}
					else 
					{
						JOptionPane.showMessageDialog(null, "请选择一个文件夹为目的位置");
					}
					}
				}
			 });
			 cancelBtn.addActionListener(new ActionListener()
			 {
				public void actionPerformed(ActionEvent arg0) 
				{
					Close();
				}
			 });
			 
		 }
		 
//			TODO 文件夹复制
		 private void DirectoryCopy(String strPath,String ToPath,int n)
		 {
			 File dir = new File(strPath); 
		        File[] files = dir.listFiles(); 
		        if (files == null) 
		            return; 
		        for (int i = 0; i < files.length; i++)
		        { 
		            if (files[i].isDirectory())
		            { 
		                
						File fileSource  = new File(files[i].getAbsolutePath());
						File fileto = new File(ToPath+"\\"+files[i].getName());
						int yesorno=0;
						if(fileto.exists()==true)
							yesorno = JOptionPane.showConfirmDialog(null, ToPath+"已存在是否要覆盖");
						if(fileto.exists()==false)
							fileto.mkdir();
						if(yesorno==0)
							DirectoryCopy(files[i].getAbsolutePath(),ToPath+"\\"+files[i].getName(), n);
		            } 
		            else 
		            { 
		                String strFileName = files[i].getAbsolutePath();
		                File fileSource  = new File(strFileName);
		                File fileto = new File(ToPath+"\\"+files[i].getName());
		                int yesorno=0;
						if(fileto.exists()==true)
							yesorno = JOptionPane.showConfirmDialog(null, strFileName+"已存在是否要覆盖");
						if(yesorno==0)
						{
							CopyFile(fileSource, fileto);
							if(n==0)
							{
								fileSource.delete();
							}
						}
		                filelist.add(files[i].getAbsolutePath());              
		            } 
		        } 
		        Refresh();
		 }
		 
//			TODO 关闭小窗体
		 private void Close()
		 {
			 this.dispose();
		 }
		 
//			TODO 复制文件
		 private void Copy(TreePath Source,TreePath to,int n)
		 {
			    Object[] Path =  Source.getPath();
				String FilePath="C:\\";
				int i;
				for(i=0;i<Path.length-1;i++)
				{
					FilePath=FilePath+Path[i]+"\\";
				}
				int k=i;
				FilePath=FilePath+Path[i];
				File fileSource  = new File(FilePath);
				File fileSourceinf  = new File(FilePath.replace("txt", "inf"));
				System.out.println(FilePath);
				Object[] Path1 =  to.getPath();
				FilePath="C:\\";
				for(i=0;i<Path1.length;i++)
				{
					FilePath=FilePath+Path1[i]+"\\";
				}
				FilePath=FilePath+Path[k];
				System.out.println(FilePath);
				File fileto = new File(FilePath);
				File filetoinf  = new File(FilePath.replace("txt", "inf"));
				int yesorno=0;
				if(fileto.exists()==true)
					yesorno = JOptionPane.showConfirmDialog(null, Path[k] + "已存在是否要覆盖");
				if(yesorno==0)
				{
					CopyFile(fileSource, fileto);
					CopyFile(fileSourceinf, filetoinf);
					if(n==0)
					{
						fileSource.delete();
						fileSourceinf.delete();
						
					}
					Refresh();
					Close();
				}
				Close();
		 }
		 
//			TODO 复制文件
		 private void CopyFile(File fileSource,File fileto)
		 {
			 String text="";
			 try 
				{
					 FileReader fr = new FileReader(fileSource);// 输入流对象
					 BufferedReader in = new BufferedReader(fr);
					 String line = in.readLine();// 读入一行数据
					    
					 while (line != null) 
					 {
					      text = text + line + "\n";// 将数据附加到字符串变量text后
						  line = in.readLine();// 读入一行新数据
					 }
					 in.close();// 关闭输入流对象
					 fr.close();
				} 
				catch (Exception e1) 
				{
						e1.getStackTrace();
				}
				try 
				{
						FileWriter fw = new FileWriter(fileto);// 创建输出流对象
						BufferedWriter out = new BufferedWriter(fw);
						out.write(text);// 将文本区内容写到指定文件中
						out.flush();// 关闭输出流
						fw.close();
				} 
				catch (Exception eo) 
				{
						eo.printStackTrace();
				}
		 }
	}
}                  