import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class myTextArea extends JTextArea
{
	public myTextArea()
	{
//		setViewportView(outpuTextArea);
//		outpuTextArea.setEditable(true);
	}
	
	public myTextArea(String str)
	{
		super(str);
	}
	
	public myTextArea(String str, int x, int y)
	{
		super(str, x, y);
	}
}
