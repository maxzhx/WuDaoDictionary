import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * 翻译面板类，用户可在这个面板进行句子，段落翻译，中英互译
 * @author foxandhuzh, Zheng Huixiang
 *
 */
public class TranslatePane extends JPanel
{
	private JTextArea inputArea = new JTextArea();
	private JTextArea outputArea = new JTextArea();
	
	private JScrollPane inputScrollPane = new JScrollPane(inputArea);
	private JScrollPane outputScrollPane = new JScrollPane(outputArea);
	
	private JButton clearBtn = new JButton("清除");
	private JButton translateBtn = new JButton("翻译");
	
	private JPanel txtPane = new JPanel(new GridLayout(1, 2));
	private JPanel btnPane = new JPanel(new GridLayout(1, 2));
	private JPanel leftBtnPane = new JPanel(new FlowLayout(FlowLayout.CENTER));
	private JPanel rightBtnPane = new JPanel(new FlowLayout(FlowLayout.CENTER));

	/**
	 * 创建一个翻译面板，需要主窗体的字体参数支持
	 * @param dic 主窗体Dictionary
	 */
	public TranslatePane(Dictionary dic)
	{
		inputArea.setFont(dic.myFont);
		inputArea.setLineWrap(true);
		outputArea.setFont(dic.myFont);
		outputArea.setLineWrap(true);
		
		translateBtn.addActionListener(actionHandle);
		clearBtn.addActionListener(actionHandle);
//		界面布局
		setLayout(new BorderLayout(5, 5));
		
		leftBtnPane.add(translateBtn);
		rightBtnPane.add(clearBtn);
		btnPane.add(leftBtnPane);
		btnPane.add(rightBtnPane);
		
		txtPane.add(inputScrollPane);
		txtPane.add(outputScrollPane);
		
		add(BorderLayout.CENTER, txtPane);
		add(BorderLayout.SOUTH, btnPane);
	}
	
	/**
	 * 设置输入输出文本域的字体
	 * @param f 新的字体
	 */
	public void setTextFont(Font f)
	{
		inputArea.setFont(f);
		outputArea.setFont(f);
	}
	
	private ActionListener actionHandle = new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{
			if (e.getSource() == translateBtn)
			{
				ExecutorService threadeExecutor=Executors.newFixedThreadPool(1);
				threadeExecutor.execute(new Runnable()
				{
					public void run()
					{
						outputArea.setText("联网查询中...");
						MyTranslate translate = new MyTranslate();
						outputArea.setText(translate.Translate(inputArea.getText()));		
					}
					
				});
				threadeExecutor.shutdown();
				
			}
			if (e.getSource() == clearBtn)
			{
				inputArea.setText("");
				outputArea.setText("");
				inputArea.requestFocus();
			}
		}
		
	};
}
