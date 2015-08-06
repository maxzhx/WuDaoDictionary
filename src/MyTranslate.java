import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.memetix.mst.language.Language;  
import com.memetix.mst.translate.Translate;  
 

/**
 * 用BING Translate API制作的翻译类，有中文译英文，和英文译中文两种功能
 * @author 郑启荣
 *
 */
public class MyTranslate {
	
	/**
	 * 构造函数，自动生成一个翻译类。无参数。。
	 */
	public MyTranslate()
	{

		Translate.setClientId("maxzhx");
	    Translate.setClientSecret("Rtbroz9KJRWnnjGUB/iBBJV9bfRhhBWPk++tY5dhT24=");
	}
	
    @SuppressWarnings("finally")
    
    /**
     * 翻译函数，把要翻译的中文或英文传到函数中，函数将会把中文译为英文，或把英文译为中文。参数String是要翻译的句子
     * @param string
     * @return
     */
	public String Translate(String string)
    {
    	if(string.length()>1600)
    	{
    		string=string.substring(0,1600);
    	}
    	String translatedText = null;
    	int flag=0;
    	Pattern p = Pattern.compile("\\b[a-zA-Z]+\\b");
    	Matcher m = p.matcher(string);
    	while(m.find()) 
    	{
    		flag=1;
    	}
		try 
		{

			if(flag==1)
			{
				translatedText = new String(Translate.execute(string,Language.ENGLISH, Language.CHINESE_SIMPLIFIED));
			}
			else 
			{
				translatedText = Translate.execute(string,Language.CHINESE_SIMPLIFIED,Language.ENGLISH);
			}
//			System.out.print(translatedText);
		} 
		catch (Exception e) 
		{  
			translatedText="无网络链接!";
			return "";
		} 
		finally{			
			return translatedText;
		}
    }
}  

