import java.awt.AWTException;  
import java.awt.Image;  
import java.awt.MenuItem;  
import java.awt.PopupMenu;  
import java.awt.SystemTray;  
import java.awt.Toolkit;  
import java.awt.TrayIcon;  
import java.awt.event.ActionEvent;  
import java.awt.event.ActionListener;  
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;  
import java.awt.event.MouseListener;  
import javax.swing.*;

/**
 * 窗体右下角的小图标，用户可以呼出隐藏的词典窗体
 * @author foxandhuzh
 *
 */
public class SystemTrayTest  
{    
	Dictionary dictionary; 
    public TrayIcon trayIcon;
    
    private PopupMenu popup = new PopupMenu();
    private MenuItem showItem = new MenuItem("Show");
    private MenuItem exitItem = new MenuItem("Exit");
    
    /**
     * 创建窗体右下角的小图标
     * @param dictionary 词典主窗体
     */
    public SystemTrayTest(final Dictionary dictionary)  
    {  
       
    	this.dictionary = dictionary;
         
        if (SystemTray.isSupported()) {  
  
            SystemTray tray = SystemTray.getSystemTray();  
            Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("images\\3.png"));  
  
            MouseAdapter mouseAdapter = new MouseAdapter()
            {  
//                  
                public void mouseClicked(MouseEvent e)
                {  
                    dictionary.setVisible(true);                   
                }  
            };  
            popup.add(showItem);
            popup.add(exitItem);
            
            showItem.addActionListener(actionListener);
            exitItem.addActionListener(actionListener);
            
            trayIcon = new TrayIcon(image,"无道词典:\n 暂停鼠标取词", popup);  
            trayIcon.setImageAutoSize(true);  
            trayIcon.addActionListener(actionListener);  
            trayIcon.addMouseListener(mouseAdapter);  
  
            //    Depending on which Mustang build you have, you may need to uncomment   
            //    out the following code to check for an AWTException when you add    
            //    an image to the system tray.   
  
            //    try {   
                      try {  
                    	  
                    	tray.add(trayIcon);  
                    } catch (AWTException e1) {  
                        // TODO Auto-generated catch block   
                        e1.printStackTrace();  
                    }  
            //    } catch (AWTException e) {   
            //        System.err.println("TrayIcon could not be added.");   
            //    }   
  
        } else {  
            System.err.println("System tray is currently not supported.");  
        }  
    }
    
    private ActionListener actionListener = new ActionListener() {  
        public void actionPerformed(ActionEvent e)
        {
        	if (e.getSource() == showItem)
        	{
        		System.out.println("show...");  
        		dictionary.setVisible(true);
        	}
        	if (e.getSource() == exitItem)
        	{
        		System.out.println("Exiting...");
        		System.exit(0);
        	}
            if (e.getSource() == trayIcon)
            {
        		System.out.println("show...");  
            	dictionary.setVisible(true); 
            }
        }  
    };  
//    public static void main(String[] args)  
//    {  
//        SystemTrayTest main = new SystemTrayTest();  
//    }  
      
}  
