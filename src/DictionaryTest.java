import java.awt.Dimension;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.UIManager;

import org.jvnet.substance.SubstanceLookAndFeel;
import org.jvnet.substance.border.*;
import org.jvnet.substance.button.StandardButtonShaper;
import org.jvnet.substance.skin.OfficeBlue2007Skin;
import org.jvnet.substance.skin.SubstanceGreenMagicLookAndFeel;
import org.jvnet.substance.theme.*;
import org.jvnet.substance.watermark.SubstanceBinaryWatermark;
import org.jvnet.substance.watermark.SubstanceBubblesWatermark;

//import com.sun.awt.AWTUtilities;

//http://blog.csdn.net/ja5on/article/details/5871348

/**
 * 词典入口类，进行组件的注册和界面初始化
 * @author Zheng Sujin, Zheng Huixiang, Zheng qirong, foxandhuzh
 *
 */
public class DictionaryTest {

	
	public static void main(String[] args) {
		
		try { 
//			组件注册
			String classDir = DictionaryTest.class.getClass().getClass().getResource("/").getPath();
			classDir=classDir.substring(1, classDir.length());
			Runtime.getRuntime().exec("regsvr32 /s \""+classDir+"XdictGrb.dll"+"\"");
			Runtime.getRuntime().exec("C:\\Windows\\Microsoft.NET\\Framework\\v4.0.30319\\regasm "+classDir+"WordGrabber.dll /codebase");
			Runtime.getRuntime().exec("C:\\Windows\\Microsoft.NET\\Framework\\v4.0.30319\\regasm "+classDir+"SoundRecognition.dll /codebase");
			
//			界面
			UIManager.setLookAndFeel(new SubstanceGreenMagicLookAndFeel());
			UIManager.put("swing.boldMetal", false); 
			if (System.getProperty("substancelaf.useDecorations") == null)
			{ 
				JFrame.setDefaultLookAndFeelDecorated(true); 
				JDialog.setDefaultLookAndFeelDecorated(true); 
			} 
//			System.setProperty("sun.awt.noerasebackground", "true"); 
//			SubstanceLookAndFeel.setCurrentTheme(new SubstanceBrownTheme());
//			SubstanceLookAndFeel.setCurrentTheme(new SubstanceJadeForestTheme());
//			SubstanceLookAndFeel.setCurrentTheme(new SubstanceDarkVioletTheme());//紫黑
//			SubstanceLookAndFeel.setCurrentTheme(new SubstanceCharcoalTheme());//酷hong黑
//			SubstanceLookAndFeel.setCurrentTheme(new SubstanceBrownTheme());//棕色
//			SubstanceLookAndFeel.setCurrentTheme(new SubstanceCremeTheme());//原始灰色
			SubstanceLookAndFeel.setCurrentTheme(new SubstanceLimeGreenTheme());//浅绿色
//			SubstanceLookAndFeel.setCurrentTheme(new SubstanceBarbyPinkTheme());//粉红
//			SubstanceLookAndFeel.setCurrentTheme(new SubstanceLightAquaTheme());//蓝色
//			SubstanceLookAndFeel.setCurrentTheme(new SubstanceTerracottaTheme());//浅橙色
			
			SubstanceLookAndFeel.setCurrentButtonShaper(new StandardButtonShaper());
//          SubstanceLookAndFeel.setCurrentWatermark(new SubstanceBubblesWatermark());//泡泡
//			SubstanceLookAndFeel.setCurrentWatermark(new SubstanceBinaryWatermark());//二进制
//			SubstanceLookAndFeel.setCurrentBorderPainter(new StandardBorderPainter());
//			SubstanceLookAndFeel.setCurrentBorderPainter(new GlassBorderPainter());
			//设置当前的主题风格，同样我 们还可以设置当前的按钮形状，水印风格等等 
		
		} 
		catch (Exception e) { 
			System.err.println("Oops! Something went wrong!"); 
			} 
		
		Dictionary frame=new Dictionary();
			
//		frame.setDefaultCloseOperation();
		
//		JFrame.setDefaultLookAndFeelDecorated(true);  
//		AWTUtilities.setWindowShape(frame,  
//	            new RoundRectangle2D.Double(0.0D, 0.0D, frame.getWidth(),  
//	            		frame.getHeight(), 6.0D, 6.0D)); 
//		frame.setUndecorated(true);
//    
//        /** 设置圆角 */  
//        AWTUtilities.setWindowShape(frame, new RoundRectangle2D.Double(  
//            0.0D, 0.0D, frame.getWidth(), frame.getHeight(), 26.0D,  
//            26.0D));  
        frame.setVisible(true);  
	}
}
