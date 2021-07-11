import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Evaluator {
	
	static Map<String, Integer> variableList = new HashMap<>();
	static String nodeData = "";
	static Scanner scan = new Scanner(System.in);
	
	static void eval(Node node) {
		switch(node.data) {
			case "assignment":
				String leftSide = "";
				for(Node n: node.subnodes) {
					if(leftSide.equals(""))
						leftSide = n.data;
					
					if(n.data.equals("expression")) {
						for(Node n2: n.subnodes) {
							nodeData += n2.data + " ";
						}
					}
				}
				variableList.put(leftSide, operations(nodeData));
				nodeData = "";
				break;
				
			case "while":
				for(Node n: node.subnodes.get(0).subnodes) {
					nodeData += n.data + " ";
				}
				while(operations(nodeData) != 0) {
					for(Node n: node.subnodes.get(1).subnodes) {
						nodeData = "";
						eval(n);
					}
					for(Node n: node.subnodes.get(0).subnodes) {
						nodeData += n.data + " ";
					}
				}
				nodeData = "";
				break;
			case "if":
				for(Node n: node.subnodes.get(0).subnodes) {
					nodeData += n.data + " ";
				}
				if(operations(nodeData) != 0) {
					for(Node n: node.subnodes.get(1).subnodes) {
						eval(n);
					}
				}
				else {
					if(node.subnodes.size()>2) {
						for(Node n: node.subnodes.get(2).subnodes) {
							nodeData = "";
							eval(n);
						}
					}			
				}
				nodeData = "";
				break;
				
			case "output":
				nodeData = "";
				for(Node n: node.subnodes.get(0).subnodes) {
					nodeData += n.data + " ";
				}
				System.out.println(operations(nodeData));
				nodeData = "";			
				break;
				
			case "input":			
				variableList.put(node.subnodes.get(0).data, scan.nextInt());
				break;
		}
		for(Node n: node.subnodes) {
			if(!n.data.equals("body") && !n.data.equals("else"))
				eval(n);
		}
	}
	
	static int operations(String nodeData) {
		String[] pieces = nodeData.split("\\s+");
		int result = 1;

		if(pieces.length == 1) {
			if(isNumber(pieces[0])) {
				return Integer.parseInt(pieces[0]);
			}
			else {
				return variableList.get(pieces[0]);
			}
		}
		else {
			ArrayList<Integer> temp = new ArrayList<Integer>();
			String ope = "";
			for(String p: pieces) {
				if(isNumber(p)) {
					temp.add(Integer.parseInt(p));
				}
				else if(Character.isLetter(p.charAt(0))) {
					temp.add(variableList.get(p));
				}
				else {
					ope = p;
				}
				if(temp.size() > 1) {
					switch(ope) {
						case "+":
							result = temp.get(0) + temp.get(1);
							break;
						case "-":
							result = temp.get(0) - temp.get(1);
							break;
						case "*":
							result = temp.get(0) * temp.get(1);
							break;
						case "/":
							result = temp.get(0) / temp.get(1);
							break;
						case "^":
							for(int i=0; i<temp.get(1); i++)
								result *= temp.get(0);
							break;
						case "%":
							result = temp.get(0) % temp.get(1);
							break;
					}
					temp.clear();
					temp.add(result);
				}
			}
			temp.clear();
		}
		return result;
	}
	
	static boolean isNumber(String token) {
		try {
			int num = Integer.parseInt(token);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;	
	}
}
