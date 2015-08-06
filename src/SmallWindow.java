import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolTip;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;

/**
 * 弹出式小窗体，在使用屏幕取词和剪贴板取词时弹出
 * @author Zheng Huixiang, foxandhuzh
 *
 */
public class SmallWindow extends JDialog
{
	private WordTree Tree = null;
	private WordLib wordLib = null;
	private Speaker speaker = null;
	
	private Font UpFont = new Font(Font.SERIF, Font.PLAIN, 12);
	private Font DownFont = new Font(Font.SERIF, Font.PLAIN, 14);
	
	private JPanel titlePane = new JPanel(new BorderLayout());
	private JTextField inputTextField = new JTextField(10); 
	private JTextArea textArea = new JTextArea("");
	private JButton queryBtn = new JButton("查询");
	private JButton speakBtn = new JButton("发音");
	private JButton addWordBtn = new JButton("加生词");
	private JPanel btnPane = new JPanel(new FlowLayout());
	
	private Point originalLocation = MouseInfo.getPointerInfo().getLocation();
	private int index = -1;
	private String originalString = null;
	private String meanString = null;
	
	private ExecutorService threadExecutor;
	
	private boolean escape = false;
	
	/**
	 * 创建小窗体，对传入的单词进行查询，需要主窗体的功能支持
	 * @param os 传入的单词
	 * @param dic 主窗体引用
	 */
	public SmallWindow(String os, Dictionary dic)
	{
		
		wordLib = dic.wordLib;
		Tree = dic.Tree;
		speaker = dic.speaker;
		originalString = os;
		
//		TODO 界面设计
//		JFrame.setDefaultLookAndFeelDecorated(false);
		JDialog.setDefaultLookAndFeelDecorated(false);
		inputTextField.setFont(UpFont);
		speakBtn.setFont(UpFont);
		addWordBtn.setFont(UpFont);
		textArea.setFont(DownFont);
		textArea.setEditable(false);
		textArea.setBackground(Color.white);
		titlePane.setBackground(Color.green);
		titlePane.setBorder(new EtchedBorder());
//		com.sun.awt.AWTUtilities.setWindowOpacity(frame, 0.6f);
		setLocation(originalLocation.x - 1, originalLocation.y - 1);

		btnPane.add(queryBtn);
		btnPane.add(speakBtn);
		btnPane.add(addWordBtn);
		
		titlePane.add(BorderLayout.CENTER, inputTextField);
		titlePane.add(BorderLayout.EAST, btnPane);
		
		add(BorderLayout.NORTH, titlePane);
		add(BorderLayout.CENTER, textArea);
		setUndecorated(true);
		
//		TODO 事件处理
//		this.addMouseListener(mouseAdapter);
//		inputTextField.addMouseListener(mouseAdapter);
//		textArea.addMouseListener(mouseAdapter);
//		titlePane.addMouseListener(mouseAdapter);
//		btnPane.addMouseListener(mouseAdapter);
//		queryBtn.addMouseListener(mouseAdapter);
//		speakBtn.addMouseListener(mouseAdapter);
//		addWordBtn.addMouseListener(mouseAdapter);
		
		inputTextField.addKeyListener(keyAdapter);
		
		queryBtn.addActionListener(actionListener);
		speakBtn.addActionListener(actionListener);
		addWordBtn.addActionListener(actionListener);
		
		threadExecutor=Executors.newFixedThreadPool(1);
		threadExecutor.execute(mouseEscape);
		threadExecutor.shutdown();
		
//		TODO 执行查询
		inputTextField.setText(originalString);
		do_query();
		
		setAlwaysOnTop(true);
		setVisible(true);
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				SmallWindow.this.pack();
			}
			
		});

		textArea.requestFocus();
	}
	
//	执行查询操作，若本地词库没有找到，就进行网络查询
	private void do_query()
	{
		index = Tree.getWordIndex(originalString);
		if (index != -1)
		{
			meanString = wordLib.getWordFull(index) + "\n" + wordLib.getPhonogram(index) + wordLib.getMean(index);
		}
		else
		{
			meanString="正在联网查询";
		}
		textArea.setText(meanString);
		if (meanString=="正在联网查询")
		{
			ExecutorService threadExecutor=Executors.newFixedThreadPool(1);
			threadExecutor.execute(new Runnable()
			{
				public void run()
				{
					MyTranslate translate=new MyTranslate();
					meanString=translate.Translate(originalString);
					textArea.setText(originalString+'\n'+ meanString);
					pack();
				}
				
			});
			threadExecutor.shutdown();
		}
	}
	
	ActionListener actionListener = new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{
			if (e.getSource() == queryBtn)
			{
				originalString = inputTextField.getText();
				do_query();
				SmallWindow.this.pack();
			}
			if (e.getSource() == speakBtn)
			{
				speaker.Speak(originalString);
			}
			if (e.getSource() == addWordBtn)
			{
				if (index != -1)
				{
					NewWordNotePane.AddNewWord(wordLib.getWord(index));
				}
				else
				{
					JToolTip tip = new JToolTip();
					tip.setTipText("无法添加网络单词！");
					tip.setVisible(true);
					tip.setLocation(500, 500);
					tip.show(true);
				}
			}
		}
		
	};
	
	MouseAdapter mouseAdapter=new MouseAdapter() 
	{
		public void mouseExited(MouseEvent e) {
//			System.out.println("Exit" + e.getSource().getClass());
			escape = true;
			ExecutorService threadExecutor=Executors.newFixedThreadPool(1);
			threadExecutor.execute(new Runnable()
			{
				public void run()
				{
					try{
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (escape) SmallWindow.this.dispose();
				}
				
			});
			threadExecutor.shutdown();
		}
		
		public void mouseEntered(MouseEvent e)
		{
			escape = false;
		}
		
	};
	
	KeyAdapter keyAdapter = new KeyAdapter()
	{
		public void keyPressed(KeyEvent e)
		{
			if (e.getKeyCode() == KeyEvent.VK_ENTER)
			{
				originalString = inputTextField.getText();
				do_query();
				SmallWindow.this.pack();
			}
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
			{
				SmallWindow.this.dispose();
			}
		}
	};
	
//	判断鼠标是否移出小窗体，移出自动关闭
	Runnable mouseEscape = new Runnable()
	{
		public void run()
		{
			int confine = 5;
			Point ptMouse, ptWindow;
			while (!escape)
			{
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				ptMouse = MouseInfo.getPointerInfo().getLocation();
				ptWindow = SmallWindow.this.getLocation();
				if (ptMouse.x < ptWindow.x - confine || ptMouse.x > ptWindow.x + SmallWindow.this.getWidth() + confine)
				{
					escape = true;
					System.out.println("Dispose!");
					SmallWindow.this.dispose();
				}
				else if (ptMouse.y < ptWindow.y - confine || ptMouse.y > ptWindow.y + SmallWindow.this.getHeight() + confine)
				{
					escape = true;
					System.out.println("Dispose!");
					SmallWindow.this.dispose();
				}
			}
		}
	};
}
