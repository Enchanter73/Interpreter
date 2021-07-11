import java.util.ArrayList;

public class ParseTree {
	Node root;
	
	ParseTree() {
		root = new Node("root");
	}
	
	static void printTree(Node node) {
		System.out.println(node.data + " ");
		for(Node n: node.subnodes) {
			printTree(n);
		}
	}
}

class Node {
	ArrayList<Node> subnodes;
	String data;
	
	Node(String data) {
		subnodes = new ArrayList<Node>();
		this.data = data;
	}
}

