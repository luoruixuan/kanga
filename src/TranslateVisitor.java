//
// Generated by JTB 1.3.2
//

import syntaxtree.*;
import visitor.GJDepthFirst;

import java.util.*;

/**
 * Provides default methods which visit each node in the tree in depth-first
 * order.  Your visitors may extend this class.
 */
public class TranslateVisitor extends GJDepthFirst<String, RegisterAllocationTable> {
	boolean isReg(String s) {
		if (s.length()!=2)
			return false;
		char c = s.charAt(0);
		return c=='a' || c=='t' || c=='s' || c=='v';
	}
   //
   // Auto class visitors--probably don't need to be overridden.
   //
   public String visit(NodeList n, RegisterAllocationTable argu) {
      String _ret=null;
      int _count=0;
      for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
         e.nextElement().accept(this,argu);
         _count++;
      }
      return _ret;
   }

   public String visit(NodeListOptional n, RegisterAllocationTable argu) {
      if ( n.present() ) {
         String _ret=null;
         int _count=0;
         for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
        	Node nd = e.nextElement();
            // nd.getClass() can be class syntaxtree.Procedure(Main)/Temp(Call)/NodeSequence(Stmt)
        	if (nd.getClass().toString().equals("class syntaxtree.Temp")) {
        		argu.loadto="t1";
        		String reg = nd.accept(this,argu);
        		if (_count<4)
        			System.out.printf("\tMOVE a%d %s\n", _count, reg);
        		else
        			System.out.printf("\tPASSARG %d %s\n", _count-3, reg);
        	}
        	else
        		nd.accept(this,argu);
            _count++;
         }
         return _ret;
      }
      else
         return null;
   }

   public String visit(NodeOptional n, RegisterAllocationTable argu) {
      if ( n.present() ) {
    	  Label nd = (Label) n.node;
    	  System.out.print(nd.f0.toString());
    	  return null;
      }
      else
         return null;
   }

   public String visit(NodeSequence n, RegisterAllocationTable argu) {
      String _ret=null;
      int _count=0;
      for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
         Node nd = e.nextElement();
         nd.accept(this,argu);
         _count++;
      }
      return _ret;
   }

   public String visit(NodeToken n, RegisterAllocationTable argu) { return null; }

   //
   // User-generated visitor methods below
   //

   /**
    * f0 -> "MAIN"
    * f1 -> StmtList()
    * f2 -> "END"
    * f3 -> ( Procedure() )*
    * f4 -> <EOF>
    */
   public String visit(Goal n, RegisterAllocationTable argu) {
      String _ret=null;
      argu.SetFunc("MAIN");
      System.out.println("MAIN "+argu.getArguNum());
      n.f1.accept(this, argu);
      System.out.println("END");
      n.f3.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> ( ( Label() )? Stmt() )*
    */
   public String visit(StmtList n, RegisterAllocationTable argu) {
      String _ret=null;
      n.f0.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> Label()
    * f1 -> "["
    * f2 -> IntegerLiteral()
    * f3 -> "]"
    * f4 -> StmtExp()
    */
   public String visit(Procedure n, RegisterAllocationTable argu) {
      String name=n.f0.f0.toString();
      argu.SetFunc(name);
      System.out.println(name+" "+argu.getArguNum());
      argu.start();
      n.f4.accept(this, argu);
      argu.end();
      System.out.println("END");
      return null;
   }

   /**
    * f0 -> NoOpStmt()
    *       | ErrorStmt()
    *       | CJumpStmt()
    *       | JumpStmt()
    *       | HStoreStmt()
    *       | HLoadStmt()
    *       | MoveStmt()
    *       | PrintStmt()
    */
   public String visit(Stmt n, RegisterAllocationTable argu) {
      String _ret=null;
      n.f0.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> "NOOP"
    */
   public String visit(NoOpStmt n, RegisterAllocationTable argu) {
      String _ret=null;
      System.out.println("\tNOOP");
      return _ret;
   }

   /**
    * f0 -> "ERROR"
    */
   public String visit(ErrorStmt n, RegisterAllocationTable argu) {
      String _ret=null;
      System.out.println("\tERROR");
      return _ret;
   }

   /**
    * f0 -> "CJUMP"
    * f1 -> Temp()
    * f2 -> Label()
    */
   public String visit(CJumpStmt n, RegisterAllocationTable argu) {
      String _ret=null;
      argu.loadto = "t0";
      String reg = n.f1.accept(this, argu);
      System.out.printf("\tCJUMP %s %s\n", reg, n.f2.f0.toString());
      return _ret;
   }

   /**
    * f0 -> "JUMP"
    * f1 -> Label()
    */
   public String visit(JumpStmt n, RegisterAllocationTable argu) {
      String _ret=null;
      System.out.printf("\tJUMP %s\n", n.f1.f0.toString());
      return _ret;
   }

   /**
    * f0 -> "HSTORE"
    * f1 -> Temp()
    * f2 -> IntegerLiteral()
    * f3 -> Temp()
    */
   public String visit(HStoreStmt n, RegisterAllocationTable argu) {
      String _ret=null;
      argu.loadto="t0";
      String reg1=n.f1.accept(this, argu);
      argu.loadto="t1";
      String reg3=n.f3.accept(this, argu);
      System.out.printf("\tHSTORE %s %s %s\n", reg1, n.f2.f0.toString(), reg3);
      return _ret;
   }

   /**
    * f0 -> "HLOAD"
    * f1 -> Temp()
    * f2 -> Temp()
    * f3 -> IntegerLiteral()
    */
   public String visit(HLoadStmt n, RegisterAllocationTable argu) {
      String _ret=null;
      argu.loadto="t0";
      String reg2=n.f2.accept(this, argu);
      String tmp1 = n.f1.f1.f0.toString();
      if (argu.isSpilled(tmp1)) {
    	  System.out.printf("\tHLOAD %s %s %s\n", "t0", reg2, n.f3.f0.toString());
    	  System.out.printf("\tASTORE %s t0\n", argu.getReg(tmp1));
      }
      else
    	  System.out.printf("\tHLOAD %s %s %s\n", argu.getReg(tmp1), reg2, n.f3.f0.toString());
      return _ret;
   }

   /**
    * f0 -> "MOVE"
    * f1 -> Temp()
    * f2 -> Exp()
    */
   public String visit(MoveStmt n, RegisterAllocationTable argu) {
      String _ret=null;
      argu.loadto="t0";
      String reg2=n.f2.accept(this, argu);
      String tmp1 = n.f1.f1.f0.toString();
      if (argu.isSpilled(tmp1)) {
    	  if (!isReg(reg2)) {
    		  System.out.printf("\tMOVE t0 %s\n", reg2);
    		  reg2="t0";
    	  }
    	  System.out.printf("\tASTORE %s %s\n", argu.getReg(tmp1), reg2);
      }
      else
    	  System.out.printf("\tMOVE %s %s\n", argu.getReg(tmp1), reg2);
      return _ret;
   }

   /**
    * f0 -> "PRINT"
    * f1 -> SimpleExp()
    */
   public String visit(PrintStmt n, RegisterAllocationTable argu) {
      String _ret=null;
      argu.loadto="t0";
      String reg1=n.f1.accept(this, argu);
      System.out.printf("\tPRINT %s\n",reg1);
      return _ret;
   }

   /**
    * f0 -> Call()
    *       | HAllocate()
    *       | BinOp()
    *       | SimpleExp()
    */
   public String visit(Exp n, RegisterAllocationTable argu) {
      return n.f0.accept(this, argu);
   }

   /**
    * f0 -> "BEGIN"
    * f1 -> StmtList()
    * f2 -> "RETURN"
    * f3 -> SimpleExp()
    * f4 -> "END"
    */
   public String visit(StmtExp n, RegisterAllocationTable argu) {
      String _ret=null;
      n.f1.accept(this, argu);
      argu.loadto="t0";
      String ret=n.f3.accept(this, argu);
      System.out.printf("\tMOVE v0 %s\n", ret);
      return _ret;
   }

   /**
    * f0 -> "CALL"
    * f1 -> SimpleExp()
    * f2 -> "("
    * f3 -> ( Temp() )*
    * f4 -> ")"
    */
   public String visit(Call n, RegisterAllocationTable argu) {
      argu.loadto="t0";
      String reg1=n.f1.accept(this, argu);
      n.f3.accept(this, argu);
      System.out.printf("\tCALL %s\n", reg1);
      return "v0";
   }

   /**
    * f0 -> "HALLOCATE"
    * f1 -> SimpleExp()
    */
   public String visit(HAllocate n, RegisterAllocationTable argu) {
      argu.loadto = "t0";
      String reg=n.f1.accept(this, argu);
      return String.format("HALLOCATE %s", reg);
   }

   /**
    * f0 -> Operator()
    * f1 -> Temp()
    * f2 -> SimpleExp()
    */
   public String visit(BinOp n, RegisterAllocationTable argu) {
      argu.loadto="t0";
      String reg1=n.f1.accept(this, argu);
      argu.loadto="t1";
      String reg2=n.f2.accept(this, argu);
      return String.format("%s %s %s", n.f0.f0.choice.toString(), reg1, reg2);
   }

   /**
    * f0 -> "LT"
    *       | "PLUS"
    *       | "MINUS"
    *       | "TIMES"
    */
   public String visit(Operator n, RegisterAllocationTable argu) {
      String _ret=null;
      n.f0.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> Temp()
    *       | IntegerLiteral()
    *       | Label()
    */
   public String visit(SimpleExp n, RegisterAllocationTable argu) {
      return n.f0.accept(this, argu);
   }

   /**
    * f0 -> "TEMP"
    * f1 -> IntegerLiteral()
    */
   public String visit(Temp n, RegisterAllocationTable argu) {
	  String name=n.f1.f0.toString();
      if (argu.isSpilled(name)) {
    	  System.out.printf("\tALOAD %s %s\n",argu.loadto, argu.getReg(name));
    	  return argu.loadto;
      }
      return argu.getReg(name);
   }

   /**
    * f0 -> <INTEGER_LITERAL>
    */
   public String visit(IntegerLiteral n, RegisterAllocationTable argu) {
      return n.f0.toString();
   }

   /**
    * f0 -> <IDENTIFIER>
    */
   public String visit(Label n, RegisterAllocationTable argu) {
      return n.f0.toString();
   }

}