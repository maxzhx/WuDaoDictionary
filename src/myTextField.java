import java.awt.event.KeyEvent;

import javax.swing.JTextField;

/**
 * 这是一个文本域，与单词列表控件相关联，在文本域中输入内容，单词列表也随之更新
 * @author foxandhuzh
 *
 */
public class myTextField extends JTextField{
	private myList wordsList = null;
	private WordTree Tree = null;
	
	/**
	 * 创建一个文本域，与一个单词列表关联，需要dictionary.Tree的查询功能支持
	 * @param dic 主窗体Dictionary
	 * @param wordList 单词列表
	 */
	public myTextField(Dictionary dic, myList wordList)
	{
		this.Tree = dic.Tree;
		this.wordsList = wordList;
	}
	
	protected void processKeyEvent(KeyEvent e)
	{
		/*测试
		System.out.println(e);
		System.out.println("id = " + e.getID());
		System.out.println("modifiers = " + e.getModifiers());
		System.out.println("when = " + e.getWhen());
		System.out.println("keycode = " + e.getKeyCode());
		System.out.println("component = " + e.getComponent());
		*/
		super.processKeyEvent(e);
//		if (!isfunctionKeys(e.getKeyCode()))
		{
			String s = this.getText();
			int index = Tree.getPrefixIndex(s);
			wordsList.setSelectedIndex(index);
			wordsList.ensureIndexIsVisible(index);
		}
	}
	
//	private boolean isfunctionKeys(int KeyCode)
//	{
//		if (KeyCode >= KeyEvent.VK_F1 && KeyCode <= KeyEvent.VK_F24) return true;
//		if (KeyCode == KeyEvent.VK_CONTROL || KeyCode == KeyEvent.VK_SHIFT || KeyCode == KeyEvent.VK_ALT) return true;
//		if (KeyCode == KeyEvent.VK_ESCAPE || KeyCode == KeyEvent.VK_CAPS_LOCK) return true;
//		if (KeyCode == KeyEvent.VK_LEFT || KeyCode == KeyEvent.VK_RIGHT) return true;
//		if (KeyCode == KeyEvent.VK_UP || KeyCode == KeyEvent.VK_DOWN) return true;
//		if (KeyCode == KeyEvent.VK_HOME || KeyCode == KeyEvent.VK_END) return true;
//		if (KeyCode == KeyEvent.VK_PAGE_UP || KeyCode == KeyEvent.VK_PAGE_DOWN) return true;
//		if (KeyCode == 0) return true;
//		return false;
//	}
}
