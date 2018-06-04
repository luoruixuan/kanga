import java.util.*;

class LivenessSet {
	
	public int next_idx, next_idx2;
	public Hashtable<String, Integer> main_set; 
	private Vector<String> var_remove_vec, var_insert_vec;
	
	LivenessSet () {
		next_idx = -1;
		next_idx2 = -1;
		
		var_remove_vec = new Vector<String> ();
		var_insert_vec = new Vector<String> ();
		main_set = new Hashtable<String, Integer>();
	}
	
	void setNext(int idx) { next_idx = idx; }
	void addNext(int idx) { next_idx2 = idx; }
	
	void addRemove(String str) {
		var_remove_vec.addElement(str);
	}
	void addInsert(String str) {
		var_insert_vec.addElement(str);
	}
	
	boolean update(LivenessSet s1, LivenessSet s2) {
		Enumeration<String> i;
		Hashtable<String, Integer> temp_set = new Hashtable<String, Integer>();
		
		if (s1 != null) {
			i = s1.main_set.keys();
			while(i.hasMoreElements()) {
				String var = i.nextElement();
				if (!temp_set.containsKey(var))
					temp_set.put(var, 1);
			}
		}
		if (s2 != null) {
			i = s2.main_set.keys();
			while(i.hasMoreElements()) {
				String var = i.nextElement();
				if (!temp_set.containsKey(var))
					temp_set.put(var, 1);
			}
		}
		
		i = var_remove_vec.elements();
		while(i.hasMoreElements()) {
			String var = i.nextElement();
			if (temp_set.containsKey(var))
				temp_set.remove(var);
		}
		i = var_insert_vec.elements();
		while(i.hasMoreElements()) {
			String var = i.nextElement();
			if (!temp_set.containsKey(var))
				temp_set.put(var, 1);
		}
		
		if (main_set.equals(temp_set))
			return false;
		else {
			main_set = temp_set;
			return true;
		}
	}
	
}