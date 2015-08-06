import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class TakeWordSearch
{
    ActiveXComponent dotnetCom;
	Dictionary dictionary = null;
	WordLib wordLib = null;
	boolean runningFlag = true;
	Thread thread;
	
	public TakeWordSearch(Dictionary dictionary) {
		dotnetCom = new ActiveXComponent("WordGrabber.WordGrabber");
		this.dictionary = dictionary;
		this.wordLib = dictionary.wordLib;
		Dispatch.call(dotnetCom,"beginGrab");
//		startSearch();
	}
	
    Runnable runnable = new Runnable() {
		public void run() {
			while (true)
			{
				String string = Dispatch.call(dotnetCom, "getString").toString();
				if (!runningFlag)
				{
					break;
				}
				new SmallWindow(string, dictionary);
			}
		}
	};
	
	void startSearch()
	{
		Dispatch.call(dotnetCom, "setFlag", new Variant(0));
		runningFlag = true;
		ExecutorService threadeExecutor=Executors.newFixedThreadPool(1);
		threadeExecutor.execute(runnable);
		threadeExecutor.shutdown();
	}
	
	void stopSearch()
	{
		runningFlag=false;
		Dispatch.call(dotnetCom, "setFlag",new Variant(1));
	}
	
}
