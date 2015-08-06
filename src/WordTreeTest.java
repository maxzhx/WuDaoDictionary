
import java.util.Scanner;
import java.util.Vector;

public class WordTreeTest {
	static WordTree Tree = new WordTree();
	
	static void foundWord(String prefix)
	{
		Vector<String> arr = Tree.getWords(prefix);
		System.out.println("Word with the prefix <" + prefix + "> is");
		for (String word : arr)
		{
			System.out.print(word + " ");
		}
		System.out.println();
	}
	
	static void foundIndex(String prefix)
	{
		foundWord(prefix);
		System.out.println(Tree.getWordIndex(prefix));
	}
	
	public static void main(String[] args)
	{
		Scanner input = new Scanner(System.in);
		//���쵥�ʱ�
		Tree.inputWord("dog", 0);
		Tree.inputWord("cat", 1);
		Tree.inputWord("FOX", 2);
		Tree.inputWord("Pig", 3);
		Tree.inputWord("Duck", 4);
		Tree.inputWord("CHICK", 5);
		Tree.inputWord("cow", 6);
		Tree.inputWord("mouse", 7);
		Tree.inputWord("micky mouse", 8);
		Tree.inputWord("cat-dog", 9);
		Tree.inputWord("cat-mouse", 10);
		Tree.inputWord("dog-mouse", 11);
		Tree.inputWord("dog*", 12);
		Tree.inputWord("ct@#!a", 13);
		System.out.println("*************************************");
		Tree.buildIndex();
//		Tree.displayAllword();
		foundWord("");
		while (input.hasNext())
		{
			System.out.print("Please Enter the prefix:");
			foundIndex(input.next());
		}
	}
	
	
}