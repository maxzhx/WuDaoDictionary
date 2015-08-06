import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jvnet.substance.SubstanceLookAndFeel;
import org.jvnet.substance.button.StandardButtonShaper;
import org.jvnet.substance.skin.BusinessBlueSteelSkin;
import org.jvnet.substance.skin.ChallengerDeepSkin;
import org.jvnet.substance.skin.EbonyHighContrastSkin;
import org.jvnet.substance.skin.GreenMagicSkin;
import org.jvnet.substance.skin.OfficeBlue2007Skin;
import org.jvnet.substance.theme.SubstanceBarbyPinkTheme;
import org.jvnet.substance.theme.SubstanceCharcoalTheme;
import org.jvnet.substance.theme.SubstanceCremeTheme;
import org.jvnet.substance.theme.SubstanceDarkVioletTheme;
import org.jvnet.substance.theme.SubstanceLightAquaTheme;
import org.jvnet.substance.theme.SubstanceLimeGreenTheme;
import org.jvnet.substance.watermark.SubstanceBinaryWatermark;

/**
 * 查词面板，用户可以在这个面板进行单词的查询，添加生词，发音
 * @author foxandhuzh
 *
 */
public class WordPane extends JPanel
{
	private Dictionary dictionary = null;
	private WordLib wordLib = null;
	private JSplitPane jsp = null;
	
	private JPanel UpPane = new JPanel(new BorderLayout());
	private JPanel DownPane = new JPanel(new BorderLayout());
	private JPanel ButtonPane = new JPanel(new GridLayout(1, 2));
	
	myTextField inputTextField = null;
	private JTextArea outputTextArea = new JTextArea();
	private JButton addNewWord = new JButton("加为生词");
	
	private JButton enterButton=new JButton("查询");
	private JButton voiceButton=new JButton("发音");
	
	private myList wordsList = new myList();
	private JScrollPane wordsJScrollPane=new JScrollPane(wordsList);
	private JScrollPane outputJScrollPane=new JScrollPane(outputTextArea);
	
	/**
	 * 创建一个查词面板，需要主窗体dictionary.Tree，dictionary.wordLib的功能支持和字体参数支持
	 * @param dic 主窗体Dictionary
	 */
	public WordPane(Dictionary dic)
	{
		//界面设计
		dictionary = dic;
		wordLib = dic.wordLib;
		inputTextField = new myTextField(dic, wordsList);
		setLayout(new BorderLayout());
		
		outputTextArea.setLineWrap(true);
		outputTextArea.setEditable(false);
		outputTextArea.setFont(dic.myFont);
		
		ButtonPane.add(enterButton);
		ButtonPane.add(voiceButton);
		UpPane.add(BorderLayout.CENTER, inputTextField);
		UpPane.add(BorderLayout.EAST, ButtonPane);
		
		wordsJScrollPane.setViewportView(wordsList);			//??
//		wordsJScrollPane.setPreferredSize(new Dimension(120,200));
//		outputJScrollPane.setPreferredSize(new Dimension(240,250));
		
		JPanel Output = new JPanel(new BorderLayout());
		JPanel ButtonPane = new JPanel(new BorderLayout());
		ButtonPane.add(BorderLayout.EAST, addNewWord);
		
		Output.add(outputJScrollPane,BorderLayout.CENTER);
		Output.add(ButtonPane,BorderLayout.NORTH);
		jsp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, wordsJScrollPane, Output);
		jsp.setDividerLocation(150);
		
		DownPane.add(jsp);
		
		add(UpPane,BorderLayout.NORTH);
		add(DownPane,BorderLayout.CENTER);
		
		wordsList.combines(inputTextField);
		wordsList.setListData(dictionary.Tree.getWords(""));
		
		//事件处理
		ActionHandler actionHandler = new ActionHandler();			//按钮单击事件
		KeyHandler keyHandler = new KeyHandler();			//单词输入的键盘事件
		MouseHandler mouseHandler = new MouseHandler();
		
		enterButton.addActionListener(actionHandler);
		addNewWord.addActionListener(actionHandler);
		voiceButton.addActionListener(actionHandler);
		dictionary.addKeyListener(keyHandler);
		wordsList.addKeyListener(keyHandler);
		inputTextField.addKeyListener(keyHandler);
		enterButton.addKeyListener(keyHandler);
		voiceButton.addKeyListener(keyHandler);
		outputTextArea.addKeyListener(keyHandler);
		wordsList.addMouseListener(mouseHandler);
		wordsList.addListSelectionListener(new ListSelectionListener()		//List选项发生改变时，获取单词释义
		{
			public void valueChanged(ListSelectionEvent e)
			{
				int wordIndex = dictionary.Tree.getWordIndex(wordsList.getSelectedValue());
				outputTextArea.setText(wordLib.getWordFull(wordIndex) + "\n" + wordLib.getPhonogram(wordIndex) 
						+ wordLib.getMean(wordIndex));
			}
		});
	}
	
	/**
	 * 设置查询区域的字体
	 * @param f 新的字体
	 */
	public void setTextFont(Font f)
	{
		outputTextArea.setFont(f);
	}
	
	//单击查询按钮或者按下Enter键，执行的查询操作
	private void do_quire()
	{
		int wordIndex = dictionary.Tree.getFirstIndex(inputTextField.getText());
		if (wordIndex > -1)
		{
			inputTextField.setText(wordsList.getSelectedValue());
			wordIndex = dictionary.Tree.getWordIndex(wordsList.getSelectedValue());
			outputTextArea.setText(wordLib.getWordFull(wordIndex) + "\n" + wordLib.getPhonogram(wordIndex) 
					+ wordLib.getMean(wordIndex));
			if (dictionary.autoSpeak) dictionary.speaker.Speak(inputTextField.getText());
			return;
		}
		wordIndex = dictionary.Tree.getWordIndex(inputTextField.getText());
		if (wordIndex == -1)
		{
			outputTextArea.setText("木有找到这个单词！");
		}
		else
		{
			outputTextArea.setText(wordLib.getWordFull(wordIndex) + "\n" + wordLib.getPhonogram(wordIndex) 
					+ wordLib.getMean(wordIndex));
			if (dictionary.autoSpeak) dictionary.speaker.Speak(inputTextField.getText());
		}
	}
	
	private class ActionHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e) {
			if(e.getSource()==enterButton)
			{
				do_quire();
				inputTextField.selectAll();
			} 
			else if(e.getSource()==voiceButton)
			{
				dictionary.speaker.Speak(inputTextField.getText());
			}
			else if(e.getSource()==addNewWord)
			{
				if(!(wordsList.getSelectedValue()==null))
					NewWordNotePane.AddNewWord(wordsList.getSelectedValue());
			}
		}
		
	}
	
	private class KeyHandler extends KeyAdapter
	{	
		public void keyPressed(KeyEvent e) {
			/*KEY_TYPED的事件与KEY_PRESSED和KEY_RELEASED的不太一样，无法仿造出来
			KeyEvent q = new KeyEvent(inputTextField , KeyEvent.KEY_TYPED, e.getWhen(), e.getModifiers(), e.getKeyCode());
			System.out.println(q);
			*/
			if (e.getSource() != wordsList && isMoves(e.getKeyCode()))
			{
				e.setSource(wordsList);
				wordsList.processKeyEvent(e);
				return;
			}
			if (e.getKeyCode() == KeyEvent.VK_ENTER)
			{
				do_quire();
				inputTextField.selectAll();
				return;
			}
			if (e.getSource() == inputTextField) return;
			
			
			if (isdeleteKeys(e.getKeyCode()))
			{
				inputTextField.setText("");
				inputTextField.requestFocus();
			}
			else if (e.isControlDown())
			{
				return;
			}
			else if (!isfunctionKeys(e.getKeyCode()))
			{
				System.out.println(e.getKeyChar());
				inputTextField.setText("" + e.getKeyChar());
				inputTextField.requestFocus();
			}
		}
		
		//判断输入的键是否是功能键
		public boolean isfunctionKeys(int KeyCode)
		{
			if (KeyCode >= KeyEvent.VK_F1 && KeyCode <= KeyEvent.VK_F24) return true;
			if (KeyCode == KeyEvent.VK_CONTROL || KeyCode == KeyEvent.VK_SHIFT || KeyCode == KeyEvent.VK_ALT) return true;
			if (KeyCode == KeyEvent.VK_ESCAPE || KeyCode == KeyEvent.VK_CAPS_LOCK) return true;
			if (KeyCode == KeyEvent.VK_LEFT || KeyCode == KeyEvent.VK_RIGHT) return true;
			if (KeyCode == KeyEvent.VK_UP || KeyCode == KeyEvent.VK_DOWN) return true;
			if (KeyCode == KeyEvent.VK_HOME || KeyCode == KeyEvent.VK_END) return true;
			if (KeyCode == KeyEvent.VK_PAGE_UP || KeyCode == KeyEvent.VK_PAGE_DOWN) return true;
			if (KeyCode == 0) return true;
			return false;
		}
		
		//判读输入的键是否是删除键
		public boolean isdeleteKeys(int KeyCode)
		{
			if (KeyCode == KeyEvent.VK_DELETE || KeyCode == KeyEvent.VK_BACK_SPACE) return true;
			return false;
		}
		
		//判断输入的键是否是控制myList移动的键
		public boolean isMoves(int KeyCode)
		{
			if (KeyCode == KeyEvent.VK_UP || KeyCode == KeyEvent.VK_DOWN) return true;
//			if (KeyCode == KeyEvent.VK_HOME || KeyCode == KeyEvent.VK_END) return true;		与TextField的Home和End冲突
			if (KeyCode == KeyEvent.VK_PAGE_UP || KeyCode == KeyEvent.VK_PAGE_DOWN) return true;
			return false;
		}
	}

	private class MouseHandler extends MouseAdapter
	{
		//myList的单击，双击响应
		public void mouseClicked(MouseEvent e)
		{
			inputTextField.setText(wordsList.getSelectedValue());
		}
	}
}
