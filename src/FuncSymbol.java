import java.util.*;

class FuncSymbol {
	public int argnum;
	public Vector<String> args;
	public Hashtable<String, String> arg_alloc;
	public int spilled;
	public int maxcall;
	public Vector<String> local;
	
	private int stmt_idx;
	private Vector<LivenessSet> Stmts;
	
	private Hashtable<String, Integer> LabeltoStmtidx;
	
	FuncSymbol() {
		spilled = 0;
		maxcall = 0;
		
		stmt_idx = 0;
		Stmts.addElement(new LivenessSet());
	
		args = new Vector<String>();
		arg_alloc = new Hashtable<String, String>();

		local = new Vector<String>();
	}
	
	public void newStmt() {
		LivenessSet s = Stmts.elementAt(stmt_idx);
		stmt_idx += 1;
		Stmts.addElement(new LivenessSet());
		s.setNext(stmt_idx);
	}
	
	public void setLabel(String s) {
		LabeltoStmtidx.put(s, stmt_idx);
	}
	
	public void setNext(String str) {
		LivenessSet s = Stmts.elementAt(stmt_idx);
		s.setNext(LabeltoStmtidx.get(str));
	}
	
	public void addNext(String str) {
		LivenessSet s = Stmts.elementAt(stmt_idx);
		s.addNext(LabeltoStmtidx.get(str));
	}
	
	public void addRemove(String str) {
		LivenessSet s = Stmts.elementAt(stmt_idx);
		s.addRemove(str);
	}

	public void addInsert(String str) {
		LivenessSet s = Stmts.elementAt(stmt_idx);
		s.addInsert(str);
	}
	
	public void Iteration() {
		boolean cont_flag = true;
		while(cont_flag) {
			int len = Stmts.size(); 
			for (int i = len-1; i >= 0; --i) {
				LivenessSet s = Stmts.elementAt(i);
				LivenessSet s1 = null, s2 = null;
				if (s.next_idx != -1)
					s1 = Stmts.elementAt(s.next_idx);
				if (s.next_idx2 != -1)
					s2 = Stmts.elementAt(s.next_idx2);
				cont_flag |= s.update(s1, s2);
			}
		}
	}
	
	public void getlocal() {
		Hashtable<String, Integer> L, R;
		L = new Hashtable<String, Integer>();
		R = new Hashtable<String, Integer>();
		Vector<String> arrlist = new Vector<String>();
		
		int len = Stmts.size();
		for(int i = 0; i < len; ++i) {
			LivenessSet s = Stmts.elementAt(i);
			Enumeration<String> j = s.main_set.keys();
			while(j.hasMoreElements()) {
				String var = j.nextElement();
				if (!L.containsKey(var)) {
					L.put(var, i);
					arrlist.addElement(var);
				}
			}
		}
		for(int i = len-1; i >= 0; --i) {
			LivenessSet s = Stmts.elementAt(i);
			Enumeration<String> j = s.main_set.keys();
			while(j.hasMoreElements()) {
				String var = j.nextElement();
				if (!R.containsKey(var)) R.put(var, i);
			}
		}
		
		Hashtable<String, String> alo_arg = new Hashtable<String, String>();
		for (int i = 0; i < 8; ++i)
			alo_arg.put("s"+String.valueOf(i), "-1");
		
		int maxj = -1, tmpj = -1;
		Enumeration<String> i = arrlist.elements(); 
		while(i.hasMoreElements()) {
			String var = i.nextElement();
			int l = L.get(var);
			for (int j = 0; j < 8; ++j) {
				String alo_var = alo_arg.get("s"+String.valueOf(j));
				if(alo_var.equals("-1")) {
					maxj = j;
					tmpj = -2;
					alo_arg.put("s"+String.valueOf(tmpj), var);
					arg_alloc.put(var, "s"+String.valueOf(j));
					break;
				}
				else {
					int alo_r = R.get(alo_var);
					if (l > alo_r) { 
						if (tmpj == -1) tmpj = j;
						alo_arg.put("s"+String.valueOf(tmpj), "-1");
					}
				}
			}
			if (tmpj == -2) continue;
			if (tmpj != -1) {
				alo_arg.put("s"+String.valueOf(tmpj), var);
				arg_alloc.put(var, "s"+String.valueOf(tmpj));
			}
			else {
				arg_alloc.put(var, "SPILLEDARG "+String.valueOf(argnum+spilled));
				spilled++;
			}
		}
		
		for (int j = 0; j < maxj; ++j)
			local.addElement("s"+String.valueOf(j));
	}
}