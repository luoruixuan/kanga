import syntaxtree.*;
import visitor.GJDepthFirst;
import visitor.Visitor;

import java.io.FileInputStream;
import java.util.*;

public class Main{
	public static void main(String args[]) {
		//AutoTest tester = new AutoTest();
		//tester.run(15);
		//Scanner s = new Scanner(System.in);
	
		try {
			FileInputStream in = new FileInputStream(args[0]);
			//String input_file = s.next();
			//FileInputStream in = new FileInputStream(input_file);
			Node root = new SpigletParser(in).Goal();
			RegisterAllocationTable RAT = new RegisterAllocationTable();
			LAVisitor V = new LAVisitor();
			root.accept(V, RAT);
			RAT.allocate();
			TranslateVisitor TV = new TranslateVisitor();
			root.accept(TV, RAT);
		}catch (ParseException e){
			e.printStackTrace();
		}catch (TokenMgrError e){
			e.printStackTrace();
		}catch (Exception e){
			e.printStackTrace();
		}
		
	}
}