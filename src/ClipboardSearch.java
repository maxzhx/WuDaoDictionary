import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * 剪贴板取词的功能类，实现了剪贴板取词功能
 * @author Zheng Huixiang
 *
 */
public class ClipboardSearch
{
	private Clipboard clipboard1;
	private boolean flag=false;
	private boolean errFlag=true;
	private String str="";
	private Dictionary dictionary = null;
	
	public ClipboardSearch(Dictionary dictionary)
	{
		this.dictionary = dictionary;
	}	

	ClipboardOwner co = new ClipboardOwner()
	{
		public void lostOwnership(Clipboard clipboard, Transferable contents)
		{
			if (flag)
			{
				do
				{
					try
					{
						errFlag=true;
						java.awt.datatransfer.Transferable transferable = clipboard.getContents(this);
						java.awt.datatransfer.DataFlavor flavor = java.awt.datatransfer.DataFlavor.stringFlavor;
//						java.awt.datatransfer.DataFlavor flavor=java.awt.datatransfer.DataFlavor.javaFileListFlavor;
						clipboard.setContents(transferable, co);
						if(transferable.isDataFlavorSupported(flavor))
						{
							str = (String)transferable.getTransferData(flavor);
							new SmallWindow(str, dictionary);
						}
//						System.out.println("ss");
						errFlag=false;
					}
					catch (Exception e)
					{
//				    	e.printStackTrace();
//						System.err.println(e);
						errFlag=true;
					}				
				} while (errFlag);
			}
		}
	};
	
	public boolean Copy()
	{
		flag=true;
		clipboard1=java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard1.setContents(java.awt.Toolkit.getDefaultToolkit().getSystemClipboard().getContents(this), co);
		return true;
	}
	
	public boolean Stop()
	{
		flag=false;
		clipboard1.setContents(clipboard1.getContents(this), null);	
		return true;
	}
}
