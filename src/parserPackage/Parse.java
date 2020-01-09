package parserPackage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class Parse 
{

	static final int MAX_LEXEME_LENGTH = 100;
	static final int MAX_TOKEN_LENGTH = 100;
	static int charClass;
	static char[] lexeme = new char[MAX_LEXEME_LENGTH];
	static char nextChar;
	static int lexLen;
	static int token;
	static int nextToken;
	static String nextLine;
	static FileReader file;
	static FileReader file2;
	static BufferedReader reader;
	static BufferedReader r2;
	
	/*Character classes*/
	static final int LETTER = 0;
	static final int DIGIT = 1;
	static final int UNKNOWN = 99;
	
	/*Token codes*/
	static String[] token_dict = new String[MAX_TOKEN_LENGTH];
	static final int INT_LIT = 10;
	static final int IDENT = 11;
	static final int ASSIGN_OP = 20;
	static final int ADD_OP = 21;
	static final int SUB_OP = 22;
	static final int MULT_OP = 23;
	static final int DIV_OP = 24;
	static final int LEFT_PAREN = 25;
	static final int RIGHT_PAREN = 26;
	static final int END_OF_FILE = 98;
	
/********************************************************************/
	
	
	static LinkedList<String> tokenList = new LinkedList<>(); // Creates a list of tokens from Lexical Analyzer
	static LinkedList<String> lexList = new LinkedList<>(); // Creates a list of lexemes from Lexical Analyzer
	static String nextTok=""; // String to store a next Token
	static String nextLex=""; // String to store a next Lexeme

	
	/*Open the input data file and process its contents*/
	public static void main(String[] args) throws FileNotFoundException {

		token_dict[INT_LIT] = "INT_LIT\t";
		token_dict[IDENT] = "IDENT\t";
		token_dict[ASSIGN_OP] = "ASSIGN_OP";
		token_dict[ADD_OP] = "ADD_OP\t";
		token_dict[SUB_OP] = "SUB_OP\t";
		token_dict[MULT_OP] = "MULT_OP\t";
		token_dict[DIV_OP] = "DIV_OP\t";
		token_dict[LEFT_PAREN] = "LEFT_PAREN";
		token_dict[RIGHT_PAREN] = "RIGHT_PAREN";
		token_dict[END_OF_FILE] = "END_OF_FILE";
		file = new FileReader("src\\parserPackage\\statements.txt");
		file2 = new FileReader("src\\parserPackage\\statements.txt");
		reader = new BufferedReader(file);
		r2 = new BufferedReader(file2);
		if (file == null)
		{
			System.out.println("ERROR - cannot open front.in \n");
		}
		else
		{
			try 
			{
				while(reader.ready())
				{
					nextLine = r2.readLine();
					System.out.println("Input: " + nextLine + "\n");
					getChar();
					do
					{
						lex();
					}
					while(nextToken != END_OF_FILE);
					assign(); // Initiates a series of recursive methods
					System.out.println("");
					System.out.println("******************************************************");
					System.out.println("");
					tokenList.clear();
					lexList.clear();
				}
			} 
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
/**************************************************************************************************************************/
	static void assign() // Starts parse with following rule <assign> -> id = <expr>
	{
		String EODToken = tokenList.removeLast(); // Stores EOD Token in a separate String
		String EODLex = lexList.removeLast(); // Stores EOD Lexeme in a separate String
		System.out.printf("Next token is: %s\t\t Next lexeme is %s\n", tokenList.removeFirst(), lexList.removeFirst());
		System.out.println("Enter <assign>"); 
		System.out.printf("Next token is: %s\t\t Next lexeme is %s\n", tokenList.removeFirst(), lexList.removeFirst());
		grabToken(); 
		expr(); 
		System.out.println("Exit <assign>");
		System.out.printf("Next token is: %s\t\t Next lexeme is %s\n", EODToken, EODLex);
	}
	static void expr() // follows the rule <expr> -> <term> {(+|-) <term>}
	{
		System.out.println("Enter <expr>");
		term(); 
		while (nextTok == "ADD_OP	" || nextTok == "SUB_OP	") // Checks if next token  is + or - to proceed with statement
		{
			grabToken();
			term(); 
		}
		System.out.println("Exit <expr>");
	}
	static void term() // follows the rule <term> -> <factor> {(*|/) <factor>}
	{
		System.out.println("Enter <term>");
		factor(); 
		while (nextTok == "MULT_OP	" || nextTok == "DIV_OP	") // Checks if next token  is * or / to proceed with statement
		{
			grabToken(); 
			factor(); 
		}
		System.out.println("Exit <term>");
	}
	static void factor() // follows the rule <factor> -> id | int_constant | ( <expr> )
	{
		System.out.println("Enter <factor>");
		if (nextTok.equals("IDENT	") || nextTok == "INT_LIT	") // Checks if next token is id or integer to proceed with statement
		{
			grabToken();
		}
		else
		{
			if (nextTok == "LEFT_PAREN") // Checks if next token is ( to proceed with statement
			{
				grabToken(); 
				expr();
				if ((nextTok == "RIGHT_PAREN")) // Checks if next token is ( to proceed with statement
				{
					grabToken();
				}
				else
					System.out.println("************ERROR***********"); // This could be resolved in a method with specifics on how to handle the error
			 }
			else
				System.out.println("***********ERROR***********"); // This could be resolved in a method with specifics on how to handle the error
		}
		System.out.println("Exit <factor>");
	}
	static void grabToken() // Grabs next Token and Lexeme
	{
		if (!tokenList.isEmpty()) // Checks if the list is not empty
		{
		nextTok= tokenList.removeFirst(); // Removes first token from the list
		nextLex= lexList.removeFirst(); // Removes first lexeme from the list
		System.out.printf("Next token is: %s\t\t Next lexeme is %s\n", nextTok, nextLex); // Prints the statement
		}
	}
	
/**************************************************************************************************************************/
	/*lookup operators and parentheses and return their token value*/
	static int lookup(char ch){
		
		switch (ch) {
		case '(':
			addChar();
			nextToken = LEFT_PAREN;
			break;
			
		case ')':
			addChar();
			nextToken = RIGHT_PAREN;
			break;
			
		case '+':
			addChar();
			nextToken = ADD_OP;
			break;
			
		case '-':
			addChar();
			nextToken = SUB_OP;
			break;
			
		case '*':
			addChar();
			nextToken = MULT_OP;
			break;
			
		case '/':
			addChar();
			nextToken = DIV_OP;
			break;
			
		case '=':
			addChar();
			nextToken = ASSIGN_OP;
			break;
			
		default:
			lexeme[0] = 'E';
			lexeme[1] = 'O';
			lexeme[2] = 'F';
			lexeme[3] = 0;
			lexLen = 4;
			nextToken = END_OF_FILE;
			break;
		}
		return nextToken;
	}
	
/********************************************************************/
	
	/*adds next char to lexeme*/
	static void addChar(){
		
		if(lexLen <= (MAX_LEXEME_LENGTH-2)){
			lexeme[lexLen++] = nextChar;
			lexeme[lexLen] = 0;
		}else{
			System.out.println("ERROR - lexeme is too long \n");
		}
	}
	
/********************************************************************/
	
	/*get the next char of an input and determine its character class*/
	static void getChar(){
		
		char c = 0;
		try {
			c = (char)reader.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		nextChar = c;
		if((int)nextChar != 0){
			if(isalpha(nextChar)){
				charClass = LETTER;
			}else if(isdigit(nextChar)){
				charClass = DIGIT;
			}else{ charClass = UNKNOWN;}
		}else{
			charClass = END_OF_FILE;
		}
	}
	
/********************************************************************/
	
	/*call getChar function until it returns a non-whitespace character*/
	static void getNonBlank(){
		
		while(isspace(nextChar)){
			getChar();
		}
	}
	
/********************************************************************/
	
	/*a simple lexical analyzer for arithmetic expressions*/
	static int lex(){
		
		lexLen = 0;
		getNonBlank();
		switch (charClass){
		
		/*Parse identifiers */
		case LETTER:
			addChar();
			getChar();
			while(charClass == LETTER || charClass == DIGIT){
				addChar();
				getChar();
			}
			nextToken = IDENT;
			break;
			
		/*Parse integer literals */
		case DIGIT:
			addChar();
			getChar();
			while(charClass == DIGIT){
				addChar();
				getChar();
			}
			nextToken = INT_LIT;
			break;
			
		/*Parentheses and operators*/
		case UNKNOWN:
			lookup(nextChar);
			getChar();
			break;
			
		/*End of the file*/
		case END_OF_FILE:
			nextToken = END_OF_FILE;;
			lexeme[0] = 'E';
			lexeme[1] = 'O';
			lexeme[2] = 'F';
			lexeme[3] = 0;
			lexLen = 4;
			break;
		}/*End of switch */
		String s = new String(lexeme);
		s = s.substring(0,lexLen);
		tokenList.add(token_dict[nextToken]);
		lexList.add(s);
		return nextToken;
	}/*End of function lex() */
	
/********************************************************************/
	
	/*returns true if char is a letter*/
	static boolean isalpha(char c){
		
		int ascii = (int) c;
		if((ascii > 64 && ascii < 91) || (ascii > 96 && ascii < 123)){
			return true;
		}else {return false;}
	}
	
/********************************************************************/
	
	/*returns true if char is a digit*/
	static boolean isdigit(char c){
		
		int ascii = (int) c;
		if(ascii > 47 && ascii < 58){
			return true;
		}else {return false;}
	}
	
/********************************************************************/
	
	/*returns true if char is a space*/
	static boolean isspace(char c){
		
		int ascii = (int) c;
		if(ascii == 32){
			return true;
		}else {return false;}
	}
	
/********************************************************************/
}
