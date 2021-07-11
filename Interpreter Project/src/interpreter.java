import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class interpreter {
	public static void main(String[] args) {
		Parser p = new Parser(new Scanner("programTest.txt"));
		//p.Parse();	
		p.actualParse();
		Evaluator.eval(p.getparseTree());
	}
}

class Scanner {
	private String progText;
	private int curPos = 0;
	Scanner(String fileName) {
		try {
			byte[] allBytes = Files.readAllBytes(Paths.get(fileName));
			progText = new String(allBytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	Token nextToken() {	
		if(curPos == progText.length()) 
			return new EOFToken(null);	
			
		while(Character.isWhitespace(progText.charAt(curPos))) {
			curPos++;
		}
		
		char curChar = progText.charAt(curPos);	
		curPos++;
		
		if(Character.isDigit(curChar)) 
			return new DigitToken(String.valueOf(curChar));				
		
		else if(Character.isAlphabetic(curChar))
			return new IdentifierToken(String.valueOf(curChar));
		
		else if(curChar == '{')
			return new WhileBegToken(String.valueOf(curChar));
		
		else if(curChar == '}')
			return new WhileEndToken(String.valueOf(curChar));
		
		else if(curChar == '[')
			return new IfBegToken(String.valueOf(curChar));
		
		else if(curChar == ']')
			return new IfEndToken(String.valueOf(curChar));
		
		else if(curChar == ':')
			return new ElseToken(String.valueOf(curChar));
		
		else if(curChar == '?')
			return new ConditionToken(String.valueOf(curChar));
		
		else if(curChar == '<')
			return new OutputToken(String.valueOf(curChar));
		
		else if(curChar == '>')
			return new InputToken(String.valueOf(curChar));
		
		else if(curChar == '=')
			return new AssignToken(String.valueOf(curChar));
		
		else if(curChar == '+')
			return new AdditionToken(String.valueOf(curChar));
		
		else if(curChar == '-')
			return new SubstractionToken(String.valueOf(curChar));
		
		else if(curChar == '*')
			return new MultiplicationToken(String.valueOf(curChar));
		
		else if(curChar == '/')
			return new DivisionToken(String.valueOf(curChar));
		
		else if(curChar == ';')
			return new EndLineToken(String.valueOf(curChar));
		
		else if(curChar == '(')
			return new GroupBegToken(String.valueOf(curChar));
		
		else if(curChar == ')')
			return new GroupEndToken(String.valueOf(curChar));
		
		else if(curChar == '%')
			return new ModToken(String.valueOf(curChar));
		
		else if(curChar == '^')
			return new ExponentToken(String.valueOf(curChar));
		
		return new ErrorToken("NotRecognizedToken");		
	}
}

class Token {
	private String text;
	protected TokenType tokenType;
	
	Token(String text) {
		this.text = text;
	}
	
	TokenType getType() {
		return tokenType;
	}
	
	String getText() {
		return text;
	}
}

class EOFToken extends Token {	
	EOFToken(String text) {
		super("EOF");
		this.tokenType = TokenType.END_OF_FILE;
	}
}

class DigitToken extends Token {	
	DigitToken(String text) {
		super(text);
		this.tokenType = TokenType.DIG;
	}
}

class IdentifierToken extends Token {
	IdentifierToken(String text) {
		super(text);
		this.tokenType = TokenType.IDENTIFIER;
	}
}

class ErrorToken extends Token {
	ErrorToken(String text) {
		super(text);
		this.tokenType = TokenType.NRT_ERROR;
	}
}

class WhileBegToken extends Token {
	WhileBegToken(String text) {
		super(text);
		this.tokenType = TokenType.WHILE_BEG;
	}
}

class WhileEndToken extends Token {
	WhileEndToken(String text) {
		super(text);
		this.tokenType = TokenType.WHILE_END;
	}
}

class IfBegToken extends Token {
	IfBegToken(String text) {
		super(text);
		this.tokenType = TokenType.IF_BEG;
	}
}

class IfEndToken extends Token {
	IfEndToken(String text) {
		super(text);
		this.tokenType = TokenType.IF_END;
	}
}

class ElseToken extends Token {
	ElseToken(String text) {
		super(text);
		this.tokenType = TokenType.ELSE;
	}
}

class ConditionToken extends Token {
	ConditionToken(String text) {
		super(text);
		this.tokenType = TokenType.CONDITION;
	}
}

class OutputToken extends Token {
	OutputToken(String text) {
		super(text);
		this.tokenType = TokenType.OUTPUT;
	}
}

class InputToken extends Token {
	InputToken(String text) {
		super(text);
		this.tokenType = TokenType.INPUT;
	}
}

class AssignToken extends Token {
	AssignToken(String text) {
		super(text);
		this.tokenType = TokenType.ASSIGN;
	}
}

class AdditionToken extends Token {
	AdditionToken(String text) {
		super(text);
		this.tokenType = TokenType.ADD;
	}
}

class SubstractionToken extends Token {
	SubstractionToken(String text) {
		super(text);
		this.tokenType = TokenType.SUB;
	}
}

class MultiplicationToken extends Token {
	MultiplicationToken(String text) {
		super(text);
		this.tokenType = TokenType.MUL;
	}
}

class DivisionToken extends Token {
	DivisionToken(String text) {
		super(text);
		this.tokenType = TokenType.DIV;
	}
}

class EndLineToken extends Token {
	EndLineToken(String text) {
		super(text);
		this.tokenType = TokenType.END_LINE;
	}
}

class GroupBegToken extends Token {
	GroupBegToken(String text) {
		super(text);
		this.tokenType = TokenType.GROUP_BEG;
	}
}

class GroupEndToken extends Token {
	GroupEndToken(String text) {
		super(text);
		this.tokenType = TokenType.GROUP_END;
	}
}

class ModToken extends Token {
	ModToken(String text) {
		super(text);
		this.tokenType = TokenType.MOD;
	}
}

class ExponentToken extends Token {
	ExponentToken(String text) {
		super(text);
		this.tokenType = TokenType.EXPONENT;
	}
}

class Parser {
	private Scanner scanner;
	private Token currentToken;
	private ParseTree parseTree;
	
	boolean inCondition = false;
	boolean inLoop = false;
	
	Parser(Scanner scanner) {
		this.scanner = scanner;
	}
	
	void Parse() {
		Token token = scanner.nextToken();
		
		while(!token.getType().equals(TokenType.END_OF_FILE)) {
			System.out.println("Token text: " + token.getText() + " Token type: " + token.getType());
			token = scanner.nextToken();					
		}
		System.out.println("Token text: " + token.getText() + " Token type: " + token.getType());		
	}
	
	//checking if the program is syntactically right
	void actualParse() {
		currentToken = scanner.nextToken();
		if(currentToken.getText().equals(".")) {
			System.exit(0);
		}
		else {
			parseTree = new ParseTree();
			S(parseTree.root);
		}
		if(currentToken.getType().equals(TokenType.END_OF_FILE)) {
			System.out.println("The program is syntatically correct");
			//System.out.println("Parse Tree:");
			//ParseTree.printTree(parseTree.root);
		}		
	}
	
	void S(Node tempNode) {
			if(currentToken.getType().equals(TokenType.IF_BEG)) {
				inCondition = true;
				tempNode.subnodes.add(new Node("if"));
				C(tempNode.subnodes.get(tempNode.subnodes.size()-1));

				if(currentToken.getType().equals(TokenType.IF_END)) {
					currentToken = scanner.nextToken();
					S(tempNode);
				}
				else {
					System.out.println("Error: ']' is expected!");
					System.exit(0);
				}
				return;
			}
			else if(currentToken.getType().equals(TokenType.WHILE_BEG)) {
				inLoop = true;
				tempNode.subnodes.add(new Node("while"));
				W(tempNode.subnodes.get(tempNode.subnodes.size()-1));

				if(currentToken.getType().equals(TokenType.WHILE_END)) {
					currentToken = scanner.nextToken();
					S(tempNode);
				}
				else {
					System.out.println("Error: '}' is expected!");
					System.exit(0);
				}
				return;
			}
			else if(currentToken.getType().equals(TokenType.IDENTIFIER)) {
				tempNode.subnodes.add(new Node("assignment"));
				A(tempNode.subnodes.get(tempNode.subnodes.size()-1));
				if(!currentToken.getType().equals(TokenType.END_OF_FILE)) {
					S(tempNode);
				}
				return;
			}
			else if(currentToken.getType().equals(TokenType.OUTPUT)) {
				tempNode.subnodes.add(new Node("output"));
				O(tempNode.subnodes.get(tempNode.subnodes.size()-1));
				
				if(!currentToken.getType().equals(TokenType.END_OF_FILE)) {
					S(tempNode);
				}
				return;
			}
			else if(currentToken.getType().equals(TokenType.INPUT)) {
				tempNode.subnodes.add(new Node("input"));
				I(tempNode.subnodes.get(tempNode.subnodes.size()-1));
				
				if(!currentToken.getType().equals(TokenType.END_OF_FILE)) {
					S(tempNode);
				}
				return;
			}
			else {
				if(!inCondition && !inLoop && !currentToken.getType().equals(TokenType.END_OF_FILE)) {
					System.out.println("Syntax Error");
					System.exit(0);
				}				
			}
	}
	void C(Node tempNode) {		
		currentToken = scanner.nextToken();
		
		tempNode.subnodes.add(new Node("expression"));
		E(tempNode.subnodes.get(tempNode.subnodes.size()-1));
		
		if(currentToken.getType().equals(TokenType.CONDITION)) {
			currentToken = scanner.nextToken();
			
			tempNode.subnodes.add(new Node("body"));
			S(tempNode.subnodes.get(tempNode.subnodes.size()-1));
			
			while(!currentToken.getType().equals(TokenType.ELSE) 
					&& !currentToken.getType().equals(TokenType.IF_END) 
						&& !currentToken.getType().equals(TokenType.END_OF_FILE)) {
				S(tempNode.subnodes.get(tempNode.subnodes.size()-1));
			}
			if(currentToken.getType().equals(TokenType.ELSE)) {
				currentToken = scanner.nextToken();
				tempNode.subnodes.add(new Node("else"));
				S(tempNode.subnodes.get(tempNode.subnodes.size()-1));
				
				while(!currentToken.getType().equals(TokenType.IF_END) 
						&& !currentToken.getType().equals(TokenType.END_OF_FILE)) {
					S(tempNode.subnodes.get(tempNode.subnodes.size()-1));
				}		
			}
			inCondition = false;
		}
		else {
			System.out.println("ERROR! '?' is expected after expression");
			System.exit(0);
		}
	}
	void W(Node tempNode) {
		currentToken = scanner.nextToken();
		
		tempNode.subnodes.add(new Node("expression"));
		E(tempNode.subnodes.get(tempNode.subnodes.size()-1));
		
		if(currentToken.getType().equals(TokenType.CONDITION)) {
			currentToken = scanner.nextToken();

			tempNode.subnodes.add(new Node("body"));
			S(tempNode.subnodes.get(tempNode.subnodes.size()-1));
			
			while(!currentToken.getType().equals(TokenType.WHILE_END) && 
					!currentToken.getType().equals(TokenType.END_OF_FILE)) {
				S(tempNode.subnodes.get(tempNode.subnodes.size()-1));
			}		
			inLoop = false;
		}
		else {
			System.out.println("ERROR! '?' is expected after expression");
			System.exit(0);
		}
	}
	void A(Node tempNode) {
		L(tempNode);
		if(currentToken.getType().equals(TokenType.ASSIGN)) {
			tempNode.subnodes.add(new Node(currentToken.getText()));
			
			currentToken = scanner.nextToken();
			
			tempNode.subnodes.add(new Node("expression"));
			E(tempNode.subnodes.get(tempNode.subnodes.size()-1));
			
			if(currentToken.getType().equals(TokenType.END_LINE)) {
				currentToken = scanner.nextToken();
			}
			else {
				System.out.println("ERROR: ';' is expected instead of '" + currentToken.getText() + "'");
				System.exit(0);
			}
		}
		else {
			System.out.println("ERROR: '=' is expected!");
			System.exit(0);
		}
		
	}
	void O(Node tempNode) {
		tempNode.subnodes.add(new Node("expression"));
		currentToken = scanner.nextToken();
		E(tempNode.subnodes.get(tempNode.subnodes.size()-1));	
		if(currentToken.getType().equals(TokenType.END_LINE)) {
			currentToken = scanner.nextToken();
		}
		else {
			System.out.println("ERROR: ';' is expected!");
			System.exit(0);
		}
	}
	void I(Node tempNode) {
		currentToken = scanner.nextToken();
		L(tempNode);
		if(currentToken.getType().equals(TokenType.END_LINE)) {
			currentToken = scanner.nextToken();
		}
		else {
			System.out.println("ERROR: ';' is expected!");
			System.exit(0);
		}
	}
	void E(Node tempNode) {
		T(tempNode);
		while(currentToken.getType().equals(TokenType.ADD) ||
				currentToken.getType().equals(TokenType.SUB)) {
			
			if(currentToken.getType().equals(TokenType.ADD)) {
				tempNode.subnodes.add(new Node(currentToken.getText()));
				currentToken = scanner.nextToken();
				T(tempNode);
			}
			else if(currentToken.getType().equals(TokenType.SUB)) {
				tempNode.subnodes.add(new Node(currentToken.getText()));
				currentToken = scanner.nextToken();
				T(tempNode);
			}
		}		
	}
	void T(Node tempNode) {
		U(tempNode);
		while(currentToken.getType().equals(TokenType.MUL) ||
				currentToken.getType().equals(TokenType.DIV) ||
				currentToken.getType().equals(TokenType.MOD)) {
			
			if(currentToken.getType().equals(TokenType.MUL)) {
				tempNode.subnodes.add(new Node(currentToken.getText()));
				currentToken = scanner.nextToken();
				U(tempNode);
			}
			else if(currentToken.getType().equals(TokenType.DIV)) {
				tempNode.subnodes.add(new Node(currentToken.getText()));
				currentToken = scanner.nextToken();
				U(tempNode);
			}
			else if(currentToken.getType().equals(TokenType.MOD)) {
				tempNode.subnodes.add(new Node(currentToken.getText()));
				currentToken = scanner.nextToken();
				U(tempNode);
			}
		}
	}
	void U(Node tempNode) {
		F(tempNode);
		if(currentToken.getType().equals(TokenType.EXPONENT)) {
			tempNode.subnodes.add(new Node(currentToken.getText()));
			currentToken = scanner.nextToken();
			U(tempNode);
		}	
	}
	void F(Node tempNode) {
		if(currentToken.getType().equals(TokenType.GROUP_BEG)) {
			currentToken = scanner.nextToken();
			E(tempNode);
			currentToken = scanner.nextToken();
			if(currentToken.getType().equals(TokenType.GROUP_END)) {
				currentToken = scanner.nextToken();
			}
			else {
				System.out.println(" ERROR: ')' is expected!");
				System.exit(0);
			}
		}
		else if(currentToken.getType().equals(TokenType.IDENTIFIER)) {
			L(tempNode);
		}
		else if(currentToken.getType().equals(TokenType.DIG)) {
			D(tempNode);		
		}
		else {
			System.out.println("Expression expected!");
			System.exit(0);
		}
	}
	void D(Node tempNode) {
		tempNode.subnodes.add(new Node(currentToken.getText()));
		currentToken = scanner.nextToken();
	}
	void L(Node tempNode) {
		tempNode.subnodes.add(new Node(currentToken.getText()));
		currentToken = scanner.nextToken();
	}
	Node getparseTree() {
		return parseTree.root;
	}
}

enum TokenType {
	IF_BEG("["), IF_END("]"), ELSE(":"), CONDITION("?"), WHILE_BEG("{"), WHILE_END("}"), 
	GROUP_BEG("("), GROUP_END(")"),
	ASSIGN("="), ADD("+"), SUB("-"), MUL("*"), DIV("/"), MOD("%"), EXPONENT("^"),
	DIG, IDENTIFIER,
	OUTPUT("<"), INPUT(">"),
	END_LINE(";"),
	NRT_ERROR,
	END_OF_FILE;
	
	String text;
	TokenType() {
		this.text = this.toString();
	}
	
	TokenType(String text) {
		this.text = text;
	}
}