import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import sun.applet.Main;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

/**
 * Text to Speach，通过这个类可以读出单词或句子
 * 注意：使用这个类需要添加额外的jar文件
 * @author foxandhuzh
 *
 */
public class Speaker
{
	VoiceManager voiceManager = VoiceManager.getInstance();					//获得一个声音管理器
	
	/**
	 * 创建一个发声器，读出传入的单词
	 * @param str
	 */
	public void Speak(final String str)
	{
		ExecutorService threadeExecutor=Executors.newFixedThreadPool(1);
		threadeExecutor.execute(new Runnable()
		{
			public void run()
			{
				Voice voice = voiceManager.getVoice("kevin16");
				if (voice != null)
				{
					voice.allocate();
					voice.speak(str);										//对字符串发音
					voice.deallocate();
				}
			}
			
		});
		threadeExecutor.shutdown();
	}
	
	
//	public static void main(String[] args)
//	{
//		new Speaker().Speak("Hello");
//		System.out.println("Hello");
//	}
}
