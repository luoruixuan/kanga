import java.util.*;

class FuncSymbol {
	public int argnum;
	public Vector<String> args;
	public Hashtable<String, String> arg_alloc;
	public int spilled;
	public int maxcall;
	public Vector<String> local;
	FuncSymbol() {
		args = new Vector<String>();
		arg_alloc = new Hashtable<String, String>();
		spilled = 0;
		maxcall = 0;
	}
	public void getlocal() {
		local = new Vector<String>();
		for (Enumeration<String> e=arg_alloc.elements();e.hasMoreElements();) {
			String s = e.nextElement();
			if (s.length()<4)
				local.addElement(s);
		}
	}
}