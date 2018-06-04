import java.util.Enumeration;
import java.util.Hashtable;


class RegisterAllocationTable {

	public String loadto;
	// functions
	private String present_func;
	private Hashtable<String, FuncSymbol> funcs;

	RegisterAllocationTable() {
		funcs = new Hashtable<String, FuncSymbol>();
	}
	
	public void SetFunc(String func) {
		present_func = func;
		if (!funcs.containsKey(func))
			funcs.put(func, new FuncSymbol());
	}

	public void newStmt() {
		FuncSymbol f = funcs.get(present_func);
		f.newStmt();
	}

	public void SetLabel(String string) {
		FuncSymbol f = funcs.get(present_func);
		f.setLabel(string);
	}

	public void SetArguNum(String num) {
		FuncSymbol f = funcs.get(present_func);
		f.argnum = Integer.valueOf(num);
	}

	public void SetCJump(String string) {
		FuncSymbol f = funcs.get(present_func);
		f.addNext(string);
	}

	public void SetJump(String string) {
		FuncSymbol f = funcs.get(present_func);
		f.setNext(string);
	}

	public void remove(String name) {
		FuncSymbol f = funcs.get(present_func);
		f.addRemove(name);
	}

	public void insert(String name) {
		FuncSymbol f = funcs.get(present_func);
		f.addInsert(name);
	}

	public void allocate() {
		for (Enumeration<FuncSymbol> e=funcs.elements();e.hasMoreElements();) {
			FuncSymbol f = e.nextElement();
			f.Iteration();
			f.getlocal();
		}
	}

	public String getArguNum() {
		FuncSymbol f = funcs.get(present_func);
		return String.format("[%d][%d][%d]", f.argnum, f.argnum+f.spilled+f.local.size(), f.maxcall);
	}

	public Enumeration<String> TmpVars() { 
		FuncSymbol f = funcs.get(present_func);
		return f.local.elements();
	}

	public boolean isSpilled(String name) {
		String s=getReg(name);
		return s.length()>=4;
	}

	public String getReg(String name) {
		FuncSymbol f = funcs.get(present_func);
		return f.arg_alloc.get(name);
	}

	public void start() {
		FuncSymbol f = funcs.get(present_func);
		for (int i = 0; i < f.local.size(); ++i) {
			String s = f.local.elementAt(i);
			System.out.printf("\tASTORE SPILLEDARG %d %s\n", i+f.argnum+f.spilled, s);
		}
	}

	public void end() {
		FuncSymbol f = funcs.get(present_func);
		for (int i=f.local.size()-1;i>=0;--i) {
			String s = f.local.elementAt(i);
			System.out.printf("\tALOAD %s SPILLEDARG %d\n", s, i+f.argnum+f.spilled);
		}
	}

	public void callFunc(int num) {
		FuncSymbol f = funcs.get(present_func);
		f.maxcall = f.maxcall>num?f.maxcall:num;
	}
	
}