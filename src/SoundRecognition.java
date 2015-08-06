
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

public class SoundRecognition extends JDialog
{
	JProgressBar progressBar=new JProgressBar();
	JLabel label=new JLabel("Please read :");
    ActiveXComponent dotnetCom1;
    boolean runningFlag=false;
    String str="";
    
	SoundRecognition()
	{
		dotnetCom1 = new ActiveXComponent("SoundRecognition.SoundRecognition");
		setLayout(new BorderLayout());
		add(progressBar,BorderLayout.NORTH);
		progressBar.setValue(0);
		add(label,BorderLayout.CENTER);
		addWindowListener(windowAdapter);
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		pack();
		Dispatch.call(dotnetCom1,"beginRec");
	}
	
	WindowAdapter windowAdapter=new WindowAdapter()
	{
		public void windowClosing(java.awt.event.WindowEvent e) {
//			System.out.println("sad");
			Dispatch.call(dotnetCom1, "setStrFlag", new Variant(1));
		};
	};
	
//	Runnable runnable1=new Runnable() {
//		
//		@Override
//		public void run() {
//			// TODO Auto-generated method stub
//			String string = Dispatch.call(dotnetCom, "getStr").toString();
////			new SmallWindow(string,translate(string));
////			textArea.setText(string);
////			textArea.insert(string+"\n", textArea.getText().length());
////			System.out.println(string);
//			runningFlag=false;
//			
//		}
//	};
	
	Runnable runnable2=new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (true)
			{
				int va = Dispatch.call(dotnetCom1, "getVoice").getInt();
				if (!runningFlag)
				{
					break;
				}
				progressBar.setValue(va);
//				System.out.println(va);
//				System.out.println(string);
//				System.out.println(translate(string));
			}
			Dispatch.call(dotnetCom1, "stopRec");
//			SoundRecognition.this.dispose();
		}
	};
	
	String startRec()
	{
		setVisible(true);
		Dispatch.call(dotnetCom1, "startRec");
		Dispatch.call(dotnetCom1, "setStrFlag", new Variant(0));
		Dispatch.call(dotnetCom1, "setVoiceFlag", new Variant(0));
		runningFlag=true;
		ExecutorService threadeExecutor=Executors.newFixedThreadPool(1);
		threadeExecutor.execute(runnable2);
		threadeExecutor.shutdown();
//		threadeExecutor.execute(runnable1);
		
		str = Dispatch.call(dotnetCom1, "getStr").toString();
//		textArea.insert(string+"\n", textArea.getText().length());
		runningFlag=false;
		setVisible(false);
		return str;
//		thread1=new Thread(runnable1);
//		thread1.run();
	}
	/**
	 * @param args
	 */
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		SoundRecognition sr=new SoundRecognition();
//		System.out.println(sr.startRec());
////		sr.startRec();
//	}

}
