// imports tell the compiler where these libraries are
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Scanner;

/*
 * Dean Garcia
 * CECS 444
 * This program ciphers through a .txt file and .c file and capitalizes all reserved words outside of comments
 * in a  C program. 
 */
public class parser 
{	
	public static void main(String[] args) throws IOException 
	{
		intializeAlphaMap();
		Scanner scan = new Scanner(System.in);
		String file = scan.nextLine(); // /Users/deangarcia/Desktop/code
		toUpperCase(file + ".c", file + "out.c");
		toUpperCase(file + ".txt", file + "out.txt");
		printCount();
		scan.close();
	}
	
	// Populates hashtable with key pair value of upper case and lower case
	// alphabet characters
	public static void intializeAlphaMap()
	{
		String low = "abcdefghijklmnopqrstuvwxyz";
		String up = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		for(int i = 0; i < low.length(); i++)
		{
			alphaMap.put(low.charAt(i), up.charAt(i));
		}
	}
	
	// Own toUpperCase function argument word is a key word in 
	// C and for loop iterates through each char of the word
	// to use as key to replace lower case letter with upper
	public static String to_upper_case(String word)
	{
		String upperCaseWord = "";
		for(int i = 0; i < word.length(); i++)
		{
			upperCaseWord += alphaMap.get(word.charAt(i));
		}
		return upperCaseWord;
	}
	
	public static void printCount() throws IOException
	{
		BufferedWriter bufferedCountWriter = null;
		String CountOutFile = "/Users/deangarcia/Desktop/countOut.txt";
		try 
		{
			bufferedCountWriter = new BufferedWriter(new FileWriter(CountOutFile));
			for(int i = 0; i < count.length; i++)
			{
				if(count[i] >= 1)
				{
					bufferedCountWriter.write(reservedWords[i] + " = " + Integer.toString(count[i]) + "\n");
				}
			}
	        bufferedCountWriter.newLine();
	        bufferedCountWriter.flush();
		} 
		catch (FileNotFoundException e) 
		{
		    e.printStackTrace();
		} 
		catch (IOException e) 
		{
		    e.printStackTrace();
		} 
		finally 
		{
		     if(bufferedCountWriter!=null)
		        bufferedCountWriter.close();
		}	
			
	}
	
	public static void toUpperCase(String ReadFile, String OutFile) throws IOException
	{
		BufferedReader bufferedReader = null;
		BufferedWriter bufferedWriter = null;
		try {
			// variable used for each line of the file being read in will
			// later be split by white spaces
		    String s = "";
	    	bufferedReader = new BufferedReader(new FileReader(ReadFile));
	    	bufferedWriter = new BufferedWriter(new FileWriter(OutFile));
	    	
	    	// Flag for whenever a multiple line comment (/*) is started it is put 
	    	// outside the while loop because it needs to be consistent throughout 
	    	// each line until a */ is found
	    	boolean mulCom = false;
	    	
	    	/*
	    	 * while loop reads a line each line of a file until it reaches the end
	    	 */
		   	while ((s = bufferedReader.readLine()) != null) 
		   	{
		   		// Each line is split by white spaces into an array of strings
		   		String indi[] = s.split("\\s+");
		   		// Flag that represents a condition when a single line (//)
		   		// comment is found within the array indi
		   		boolean com = false;
		   		
		   		/*
		   		 * For loop to go through the whole array of words in the indi 
		   		 * array the array is iterated through to check for reserved word
		   		 * and comment indicators
		   		 */
		   		for(int i = 0; i < indi.length; i++)
		   		{
		   			/* 
		   			 * Series of comments to check for comment indicators
		   			 */
		   			if(indi[i].contains("//"))
		   				com = true;
		   			if(indi[i].contains("/*"))
		   				mulCom = true;
		   			if(indi[i].contains("*/"))
		   				mulCom = false;
		   			
		   			/*
		   			 * For loop to iterate through the entire list of reserved words
		   			 * to check the current cell of the indi array with the list of 
		   			 * reserved words 
		   			 */
				    for(int j = 0; j < reservedWords.length; j++)
				    {
				    	/*
				    	 * if there is a match that means the word in the indi array
				    	 * is a reserved word so we have to capitalize unless there
				    	 * was a comment indicator in that case one of the flags will
				    	 * make the if condition false and the word will be lift as is
				    	 */
				    	if(indi[i].equals(reservedWords[j]) && (!com && !mulCom))
				    	{
				    		indi[i] = indi[i].replaceAll(reservedWords[j], to_upper_case(reservedWords[j]));
				    		count[j]++;
				    	}
				    	else if(indi[i].contains("(" + reservedWords[j]) && (!com && !mulCom))
				    	{
				    		// Issue occurred where whenever the string did not complete match
				    		// the reserved word it would not be replaced such as in the case where 
				    		// we have a method declaration and one of arguments does not have a space 
				    		// between the ( and the reserved word ex. void method(int a) since method(int
				    		// is not a keyword the if statement was not being satisfied first solution was
				    		// to take out the .equals condition solved the problem but also caused another
				    		// which was any sequence of a reserved word combination was being capitalized 
				    		// ex. printf was being turned into prINTf so next solution was to hard code a 
				    		// condition where if a string contains a ( and a keyword immediately after then 
				    		// my parser assumes it is a reserved word. 
				    		indi[i] = indi[i].replaceAll(reservedWords[j], to_upper_case(reservedWords[j]));
				    		count[j]++;
				    	}
				    }
				    
				    // Write current word plus a space to the new output file
				    bufferedWriter.write(indi[i] + " ");
		   		}
		        bufferedWriter.newLine();
		        bufferedWriter.flush();
		    }
		} 
		catch (FileNotFoundException e) 
		{
		    e.printStackTrace();
		} 
		catch (IOException e) 
		{
		    e.printStackTrace();
		} 
		finally 
		{
			if(bufferedReader!=null)
	            bufferedReader.close();
	         if(bufferedWriter!=null)
	            bufferedWriter.close();
		}	
	}
	
	public static Hashtable<Character, Character> alphaMap = new Hashtable<Character, Character>();
	/*
	 * C requires all of these reserved words to be in lower case. This is why the program only checks lower
	 * for lower case reserved words. These words are the reserved words used to build the basic instruction
	 * set of C. No reserved words from libraries are included.
	 */
	public static String reservedWords[] = new String[] 
	{
		"auto", "if", "break", "int", "case", "char", "continue", "return",
		"default", "short", "do", "sizeof", "double", "else", "struct",
		"entry", "switch", "extern", "typedef", "float", "for", "unsigned",
		"goto", "while", "enum", "void", "const", "signed", "volatile"  
	};
	// count to keep track of how many times reserved words are used
	public static int count[] = new int[29];
}
