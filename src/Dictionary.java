import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.dnd.Autoscroll;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.basic.BasicOptionPaneUI.ButtonActionListener;

import org.jvnet.substance.SubstanceLookAndFeel;
import org.jvnet.substance.button.StandardButtonShaper;
import org.jvnet.substance.skin.CremeSkin;
import org.jvnet.substance.skin.GreenMagicSkin;
import org.jvnet.substance.skin.OfficeBlue2007Skin;
import org.jvnet.substance.skin.SubstanceGreenMagicLookAndFeel;
import org.jvnet.substance.skin.SubstanceSkin;
import org.jvnet.substance.theme.SubstanceBarbyPinkTheme;
import org.jvnet.substance.theme.SubstanceCharcoalTheme;
import org.jvnet.substance.theme.SubstanceCremeTheme;
import org.jvnet.substance.theme.SubstanceDarkVioletTheme;
import org.jvnet.substance.theme.SubstanceLightAquaTheme;
import org.jvnet.substance.theme.SubstanceLimeGreenTheme;
import org.jvnet.substance.watermark.SubstanceBinaryWatermark;
import org.jvnet.substance.watermark.SubstanceBubblesWatermark;

import javax.swing.UIManager;


import org.jvnet.substance.SubstanceLookAndFeel;
import org.jvnet.substance.button.StandardButtonShaper;

import org.jvnet.substance.skin.*;

import org.jvnet.substance.theme.*;
import org.jvnet.substance.watermark.*;

import com.jacob.com.ComThread;

/**
 * 词典主窗体类，进行一系列的词典初始化操作，提供通用功能类实例，用户可以在这里进行一些设置，开关取词等
 * @author foxandhuzh
 *
 */
public class Dictionary extends JFrame
{	
	int i = 0;
	public int  TAKEWORD =0;	//判断是否屏幕取词
	public int STROKEWORD =0;	//判断是否划线取词
	public int CLIPBOARD =0;	//判断是否剪贴板取词
//	界面元素*****************************************************************************
	WordPane wordPane = null;
	LearnEnglishPane learnEnglishPane = null;
	NewWordNotePane newWordNotePane = null;
	TranslatePane translatePane = null;
	
	TakeWordSearch takeWordSearch = null;							//屏幕取词
	ClipboardSearch clipboardSearch = null;							//剪贴板取词
	
	JPanel buttonPanel1=new JPanel(new GridLayout(1,5));
	JPanel checkbuttonPanel1=new JPanel(new GridLayout(1,3));
	JTabbedPane tabbedPane=new JTabbedPane();
	
	JButton setButton=new JButton("设置");
	
	Container  setPane=new Container();
	
	JCheckBox takeword=new JCheckBox("取词");
	JCheckBox strokeword=new JCheckBox("划词");
	JCheckBox clipboard=new JCheckBox("剪贴板");
	JCheckBox autostart=new JCheckBox("开机启动");
	
	WordLib wordLib = new WordLib();				//单词数据库
	
	Font myFont = new Font(Font.SERIF, Font.PLAIN, 16);
	Font largeFont = new Font(Font.SERIF, Font.PLAIN, 20);
	Font smallFont = new Font(Font.SERIF, Font.PLAIN, 12);
//	Font myFont = new Font(Font.SANS_SERIF,Font.LAYOUT_LEFT_TO_RIGHT,18);
	
	Speaker speaker = new Speaker();
	WordTree Tree = new WordTree();
	
	boolean autoSpeak = true;
	int index = 0;
	
	SystemTrayTest systemTray = new SystemTrayTest(this);
	String trayString = "无道词典\n";
	
	
//	JCheckBoxMenuItem autostartItem=new JCheckBoxMenuItem("开机启动");
	
	
	/*移除说明：重写了myTextField的processKeyEvent方法，将inputListener所做的操作移至其中
	 * 这样做的好处是wordsList选中的单词不会一直处于最下方。
	 * 而且当使用上下键，或者鼠标选择wordsList中的单词是时，inputTextField是相应发生变化的。
	 * 产生这些变化并不会触发processKeyEvent，所以避免了多余的操作。
	 * ensureIndexIsVisible如果所选的项当前可见，则不进行任何操作。
	DocumentListener inputListener=new DocumentListener()
	{
		
		public void removeUpdate(DocumentEvent e)
		{
			String s=inputTextField.getText();
			index = Tree.getWordIndex(s);
			wordsList.setSelectedIndex(index);
			//wordsList.ensureIndexIsVisible(true);
			wordsList.ensureIndexIsVisible(index);
			//wordsList.setWordsStrings(Tree.getWords(s));
		}
		
		public void insertUpdate(DocumentEvent e)
		{
			String s=inputTextFieald.getText();
			wordsList.setSelectedIndex(Tree.getWordIndex(s));
			index=Tree.getWordIndex(s);
			wordsList.ensureIndexIsVisible(index);
		}
		
		public void changedUpdate(DocumentEvent e)
		{
			wordsList.ensureIndexIsVisible(index);
		}
	};
	*/
	
	/**
	 * 词典窗体初始化，读入词库和生词本
	 */
	public Dictionary() {
//		****************************************************************************
		super("无道词典");
//		System.out.println(Charset.defaultCharset().toString());		//默认编码方式
//		****************************************************************************
//		TODO 词典读入******************************************************************
		
		ImportData importer = new ImportData(wordLib);
		importer.set();
		for(int i = 0; i < wordLib.length(); i++)
		{
			String word = wordLib.getWord(i);
			if (word == null) break;
			Tree.inputWord(word, i);
		}
		Tree.buildIndex();
//		checkbox处理
		clipboardSearch = new ClipboardSearch(this);
		takeWordSearch = new TakeWordSearch(this);
//		SoundRecognition soundRecognition=new SoundRecognition();
//		takeWordSearch = new TakeWordSearch(this);
		
		clipboard.setSelected(false);
		takeword.setSelected(false);
		strokeword.setSelected(false);
		ActionListener checkboxListener = new ActionListener()
		{	
			public void actionPerformed(ActionEvent e) {
				trayString="无道词典：\n";
				if (e.getSource() == clipboard) {
					if (clipboard.isSelected())
					{
						CLIPBOARD = 1;	
						clipboardSearch.Copy();
					}
					else {
						CLIPBOARD = 0;
						clipboardSearch.Stop();
					}
				}
				else if (e.getSource()==strokeword) {
					STROKEWORD=1;
				}
				else if (e.getSource()==takeword) {
					if (takeword.isSelected()) {
						TAKEWORD=1;
						trayString+=" 鼠标取词\n";
						takeWordSearch.startSearch();
					}
					else {
						TAKEWORD=0;
						trayString+=" 暂停鼠标取词\n";
						takeWordSearch.stopSearch();
					}
				}
//				开机启动设置
				else if(e.getSource()==autostart)
				{
				}
					systemTray.trayIcon.setToolTip(trayString);			
				}
		};
		
		clipboard.addActionListener(checkboxListener);
		takeword.addActionListener(checkboxListener);
		strokeword.addActionListener(checkboxListener);
		
//		********************************************************************************************		
//		TODO 事件处理**********************************************************************************
		

		ActionHandler actionHandler = new ActionHandler();			//按钮单击事件
		setButton.addActionListener(actionHandler);
		
//		inputTextField.getDocument().addDocumentListener(inputListener);
		
//		****************************************************************************
//		TODO 界面初始化*****************************************************************
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		buttonPanel1.add(setButton);
	
		checkbuttonPanel1.add(takeword);
//		checkbuttonPanel1.add(strokeword);
		checkbuttonPanel1.add(clipboard);
//		checkbuttonPanel1.add(autostart);

		setPane.setLayout(new BorderLayout());
	    setPane.add(buttonPanel1, BorderLayout.WEST);
	    setPane.add(checkbuttonPanel1, BorderLayout.EAST);
		
		wordPane = new WordPane(this);
		translatePane = new TranslatePane(this);
		learnEnglishPane = new LearnEnglishPane(this);
		newWordNotePane = new NewWordNotePane(this); 
		tabbedPane.addTab("查词", null, wordPane, "WordPane");
		tabbedPane.addTab("翻译", null, translatePane, "TranslatePane");
		tabbedPane.addTab("阅读", null, learnEnglishPane, "LearnEnghlisPane");
		tabbedPane.addTab("单词本", null, newWordNotePane, "NewWordNotePane");
		
		add(tabbedPane,BorderLayout.CENTER);
		add(setPane,BorderLayout.SOUTH);
		
//		设置标题和标题图标
//		ImageIcon img = new ImageIcon(getClass().getResource("3.png"));
//	    Image image=((ImageIcon) img).getImage();
//	    setIconImage(image);
	    java.awt.Toolkit tk = java.awt.Toolkit.getDefaultToolkit(); 
	    Dimension screenSize = tk.getScreenSize();
	    
	    int screenWidth = screenSize.width;
	    int screenHeight = screenSize.height;
	    int Width = 800;
	    int Height = 500;
	    
	    setSize(Width, Height);
	    setLocation((screenWidth-Width)/2, (screenHeight-Height)/2);
	    
	    Image img = tk.getImage(getClass().getResource("images\\3.png"));
	    setIconImage(img);
		
		setVisible(true);
		
		SmallWindow sw = new SmallWindow("", this);
		sw.setVisible(false);
		sw.dispose();
		
		wordPane.inputTextField.requestFocus(true);				//输入栏申请焦点
	}
	
	private class ActionHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e) {
			if(e.getSource()==setButton)
			{
				JPopupMenu menu=new JPopupMenu();
				JMenuItem fontItem=new JMenu("字号");
				//JMenuItem autostartItem=new JMenu("开机启动");
				JMenuItem hideItem=new JMenuItem("隐藏窗体");
				JMenuItem aboutItem=new JMenuItem("关于...");
			    
			    JMenuItem backgroundItem = new JMenu("风格");
				menu.add(fontItem);
				menu.add(backgroundItem);
				menu.add(hideItem);
				menu.add(aboutItem);
				//menu.add(autostartItem);
				menu.show(setPane, 30,-65);
				
				JMenuItem large=new JMenuItem("大");
				JMenuItem medium=new JMenuItem("中");
				JMenuItem small=new JMenuItem("小");
				JMenuItem blueItem=new JMenuItem("BLUE");
				JMenuItem greenItem=new JMenuItem("GREEN");
				JMenuItem pinkItem=new JMenuItem("PINK");
				JMenuItem reddarkItem=new JMenuItem("RED-DARK");
				JMenuItem purpledarkItem=new JMenuItem("RED-PURPLR");
				JMenuItem grayItem = new JMenuItem("GRAY");
				
				
				fontItem.add(large);
				fontItem.add(medium);
				fontItem.add(small);
				backgroundItem.add(blueItem);
				backgroundItem.add(greenItem);
				backgroundItem.add(pinkItem);
				backgroundItem.add(reddarkItem);
				backgroundItem.add(purpledarkItem);
				backgroundItem.add(grayItem);

				try {
					
				blueItem.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e) {
						SubstanceLookAndFeel.setSkin( new GreenMagicSkin());//条纹
						SubstanceLookAndFeel.setCurrentTheme(new SubstanceLightAquaTheme());//蓝色
						SubstanceLookAndFeel.setCurrentWatermark(new SubstanceBubblesWatermark());//泡泡
						SubstanceLookAndFeel.setCurrentButtonShaper(new StandardButtonShaper());
						repaint();
					}
				});
				greenItem.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e) {
						SubstanceLookAndFeel.setSkin(new GreenMagicSkin());//横线条纹
						SubstanceLookAndFeel.setCurrentTheme(new SubstanceLimeGreenTheme());
						//SubstanceLookAndFeel.setCurrentWatermark(new SubstanceBubblesWatermark());//泡泡
						SubstanceLookAndFeel.setCurrentWatermark(new SubstanceBinaryWatermark());//二进制
						SubstanceLookAndFeel.setCurrentButtonShaper(new StandardButtonShaper());
						repaint();
					}
				});
				pinkItem.addActionListener(new ActionListener()
				{				
					public void actionPerformed(ActionEvent e) {
						SubstanceLookAndFeel.setSkin(new GreenMagicSkin());
						SubstanceLookAndFeel.setCurrentTheme(new SubstanceBarbyPinkTheme());
						SubstanceLookAndFeel.setCurrentButtonShaper(new StandardButtonShaper());
						SubstanceLookAndFeel.setCurrentWatermark(new SubstanceBubblesWatermark());//泡泡
						//SubstanceLookAndFeel.setCurrentWatermark(new SubstanceBinaryWatermark());//二进制
						repaint();
					}
				});
				reddarkItem.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						SubstanceLookAndFeel.setSkin(new GreenMagicSkin());
						SubstanceLookAndFeel.setCurrentTheme(new SubstanceCharcoalTheme());
						SubstanceLookAndFeel.setCurrentButtonShaper(new StandardButtonShaper());
						repaint();
					}
				});
				purpledarkItem.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						SubstanceLookAndFeel.setSkin(new GreenMagicSkin());
						SubstanceLookAndFeel.setCurrentTheme(new SubstanceDarkVioletTheme());
						SubstanceLookAndFeel.setCurrentButtonShaper(new StandardButtonShaper());
						repaint();
					}
				});
				grayItem.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						SubstanceLookAndFeel.setSkin(new GreenMagicSkin());
						SubstanceLookAndFeel.setCurrentTheme(new SubstanceCremeTheme());
						SubstanceLookAndFeel.setCurrentWatermark(new SubstanceBinaryWatermark());//二进制
						SubstanceLookAndFeel.setCurrentButtonShaper(new StandardButtonShaper());
						repaint();
					}
				});
				aboutItem.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent arg0)
					{
						JOptionPane.showMessageDialog(Dictionary.this, "无道词典 v1.0\n制作人：郑飞腾，郑慧翔，郑启荣，郑素津");
					}
					
				});
				} 
				catch (Exception e2)
				{
				}
				//设置隐藏窗体事件
				hideItem.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						setVisible(false);
					}
				});

//				TODO 字体变更
				large.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e)
					{
						wordPane.setTextFont(largeFont);
						translatePane.setTextFont(largeFont);
					}
				});
				medium.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e)
					{
						wordPane.setTextFont(myFont);
						translatePane.setTextFont(myFont);
					}
				});
				small.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						wordPane.setTextFont(smallFont);
						translatePane.setTextFont(smallFont);
					}
				});
				
			}
		}
		
	}
}
