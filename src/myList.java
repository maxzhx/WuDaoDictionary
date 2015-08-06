import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.JTextField;

/**
 * 这是一个列表类，用于显示词库中的所有单词，能够绑定一个文本域
*/
public class myList extends JList<String>{
	myTextField inputTextField = null;
	
	/**
	 * 创建一个列表控件，默认没有关联TextField
	 */
	public myList()
	{
	}
	
	protected void processKeyEvent(KeyEvent e)
	{
		super.processKeyEvent(e);
		if (isMoves(e.getKeyCode()))			//当输入的键是移动键的话，将输入栏的内容更新为新选中的词
		{
			inputTextField.setText(this.getSelectedValue());
		}
	}
	
	/**
	 * 绑定一个TextField，使得在使用移动键变化List项时，TextField能够同步更新
	 * @param temp
	 */
	public void combines(myTextField temp)
	{
		this.inputTextField = temp;
	}
	
//	判断输入键是否为移动键
	private boolean isMoves(int KeyCode)
	{
		if (KeyCode == KeyEvent.VK_UP || KeyCode == KeyEvent.VK_DOWN) return true;
		if (KeyCode == KeyEvent.VK_HOME || KeyCode == KeyEvent.VK_END) return true;
		if (KeyCode == KeyEvent.VK_PAGE_UP || KeyCode == KeyEvent.VK_PAGE_DOWN) return true;
		return false;
	}
}