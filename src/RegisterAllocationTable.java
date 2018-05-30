import java.util.Enumeration;
import java.util.Hashtable;


class RegisterAllocationTable {

	public String loadto;
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
		// TODO Auto-generated method stub
		
	}

	public void SetLabel(String string) {
		// TODO Auto-generated method stub
		
	}

	public void SetArguNum(String num) {
		FuncSymbol f = funcs.get(present_func);
		f.argnum = Integer.valueOf(num);
		for (int i=4;i<f.argnum;++i) {
				f.arg_alloc.put(String.valueOf(i), String.format("SPILLEDARG %d", i-4));
				f.spilled += 1;
			
		}
		for (int i=0;i<4&&i<f.argnum;++i) {
			f.arg_alloc.put(String.valueOf(i), String.format("SPILLEDARG %d", f.spilled++));
		}
	}

	public void SetCJump(String string) {
		// TODO Auto-generated method stub
		
	}

	public void SetJump(String string) {
		// TODO Auto-generated method stub
		
	}

	public void remove(String name) {
		FuncSymbol f = funcs.get(present_func);
		if (!f.arg_alloc.containsKey(name))
			f.arg_alloc.put(name, String.format("SPILLEDARG %d", f.spilled++));
	}

	public void insert(String name) {
		FuncSymbol f = funcs.get(present_func);
		if (!f.arg_alloc.containsKey(name))
			f.arg_alloc.put(name, String.format("SPILLEDARG %d", f.spilled++));
	}

	public void allocate() {
		for (Enumeration<FuncSymbol> e=funcs.elements();e.hasMoreElements();) {
			FuncSymbol f = e.nextElement();
			f.getlocal();
		}
	}

	public String getArguNum() {
		FuncSymbol f = funcs.get(present_func);
		return String.format("[%d][%d][%d]", f.argnum, f.spilled+f.local.size(), f.maxcall);
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
		for (int i=0;i<f.local.size();++i) {
			String s = f.local.elementAt(i);
			System.out.printf("\tASTORE SPILLEDARG %d %s\n", i+f.spilled, s);
		}
		for (int i=0;i<4&&i<f.argnum;++i)
			System.out.printf("\tASTORE %s a%d\n", getReg(String.valueOf(i)), i);
	}

	public void end() {
		FuncSymbol f = funcs.get(present_func);
		for (int i=f.local.size()-1;i>=0;--i) {
			String s = f.local.elementAt(i);
			System.out.printf("\tALOAD %s SPILLEDARG %d\n", s, i+f.spilled);
		}
	}

	public void callFunc(int num) {
		FuncSymbol f = funcs.get(present_func);
		f.maxcall = f.maxcall>num?f.maxcall:num;
	}
	
}