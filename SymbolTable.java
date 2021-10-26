import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import cs132.minijava.syntaxtree.*;

public class SymbolTable {
	public HashMap<String, ClassNode> symbol_table = new HashMap<String, ClassNode>();
	public HashMap<String, String> class_hierarchy = new HashMap<String, String>();
	
	public boolean addClass(String class_name, ClassNode class_node) {
		if(!symbol_table.containsKey(class_name)) {
			symbol_table.put(class_name, class_node);
			return true;
		} 
		return false;
	}
	
	public boolean addParent(String child, String parent) {
		if(!class_hierarchy.containsKey(child)){
			class_hierarchy.put(child, parent);
			return true;
		}
		return false;
	}
	
	public boolean containsOverloading(String class_name, MethodNode currMethod) {
		String currMethod_name = currMethod.method_name;
		String currMethod_retType = currMethod.return_type;
		HashMap<String, String> currMethod_param = currMethod.parameters; 
		
		boolean hasParent = class_hierarchy.containsKey(class_name);
		String parent = class_hierarchy.get(class_name);
		
		while(hasParent) {
			ClassNode currClass = symbol_table.get(parent);
			Set<String> methods = currClass.methods.keySet(); 
			if(methods.contains(currMethod_name)) {
				if(currMethod_retType.equals(currClass.methods.get(currMethod_name).return_type)) {
					for(String param : currMethod_param.keySet()) {
						if(!((currMethod_param.get(param)).equals(currClass.methods.get(currMethod_name).parameters.get(param)))) {
							return true;
						}
					}
				} else {
					return true;
				}	
			}
			hasParent = class_hierarchy.containsKey(parent);
			parent = class_hierarchy.get(parent);
		}
		return false;
	}
	
	public boolean isAcyclic() {
		for(String class_name : class_hierarchy.keySet()){
			Set<String> visited = new HashSet<String>();
			String currClass = class_name;
			while(class_hierarchy.containsKey(currClass)) {
				String parent = class_hierarchy.get(currClass);
				if(visited.contains(parent)) {
					return false;
				}
				visited.add(parent);
				currClass = parent; 	
			}
		}
		return true;
	}
	
	public boolean isSubtype(String child, String parent) {
		String curr = child; 
		if(child.equals(parent)) {
			return true;
		}
		while(class_hierarchy.containsKey(curr)) {
			if(class_hierarchy.get(curr).equals(parent)) {
				return true;
			}
			curr = class_hierarchy.get(curr);
		}
		return false;	
	}
	
	public ClassNode classLookup(String class_name) {
		if(symbol_table.containsKey(class_name)) {
			return symbol_table.get(class_name);
		}
		return null;
	}
	
	public MethodNode methodLookup(String method_name, ClassNode currClass) {
		if(currClass.methods.containsKey(method_name)) { // no overloading allowed therefore we only need to match the the method name
			return currClass.methods.get(method_name);
		} else if(class_hierarchy.containsKey(currClass.class_name)) {
			return methodLookUpParent(currClass.class_name, method_name); 
		} else {
			return null;
		}
	}
	
	/*
	public String varLookUp(String var_id, MethodNode currMethod) {
		if(currMethod.local_vars.containsKey(var_id)){
			return currMethod.local_vars.get(var_id);
		} else if(currMethod.parameters.containsKey(var_id)) {
			return currMethod.parameters.get(var_id);
		} else if(class_hierarchy.containsKey(currMethod.class_name)) {
			return varLookUpParent(currMethod.class_name, var_id);
		} else {
			return null;
		}
	}
	*/
	public String varLookUp(String var_id, MethodNode currMethod) {
		//System.out.println("in varlookup, var_id : " + var_id);
		//System.out.println(currMethod.parameters);
		if(currMethod.local_vars.containsKey(var_id)){
			return currMethod.local_vars.get(var_id);
		} else if(currMethod.parameters.containsKey(var_id)) {
			//System.out.println("IN para ifState for varlookup");
			return currMethod.parameters.get(var_id);
		} else if(symbol_table.get(currMethod.class_name).fields.containsKey(var_id)) {
			return symbol_table.get(currMethod.class_name).fields.get(var_id);
		} else {
			return null;
		}
	}
	
	public ClassNode varLookUpParent(String class_name, String var_id) {
		boolean hasParent = class_hierarchy.containsKey(class_name);
		String parent = class_hierarchy.get(class_name);
		
		while(hasParent) {
			ClassNode currClass = symbol_table.get(parent);
			if(currClass.fields.containsKey(var_id)) {
				return currClass;
			}
			hasParent = class_hierarchy.containsKey(parent);
			parent = class_hierarchy.get(parent);
		}
		return null;
	}	
	
	public String completeVarLookUp(String var_id, MethodNode currMethod) {
		//System.out.println("in varlookup, var_id : " + var_id);
		//System.out.println(currMethod.parameters);
		if(currMethod.local_vars.containsKey(var_id)){
			return currMethod.local_vars.get(var_id);
		} else if(currMethod.parameters.containsKey(var_id)) {
			//System.out.println("IN para ifState for varlookup");
			return currMethod.parameters.get(var_id);
		} else if(symbol_table.get(currMethod.class_name).fields.containsKey(var_id)) {
			return symbol_table.get(currMethod.class_name).fields.get(var_id);
		} else if(class_hierarchy.containsKey(currMethod.class_name)){
			return completeVarLookUpParent(currMethod.class_name, var_id);
		} else {
			return null;
		}
	}	

	
	/*--------------------------------HELPER FUNCTIONS--------------------------------*/
	
	private MethodNode methodLookUpParent(String class_name, String method_name) {
		boolean hasParent = class_hierarchy.containsKey(class_name);
		String parent = class_hierarchy.get(class_name);
		
		while(hasParent) {
			ClassNode currClass = symbol_table.get(parent);
			if(currClass.methods.containsKey(method_name)) {
				return currClass.methods.get(method_name);
			}
			hasParent = class_hierarchy.containsKey(parent);
			parent = class_hierarchy.get(parent);
		}
		return null;	
	}
	
	private String completeVarLookUpParent(String class_name, String var_id) {
		boolean hasParent = class_hierarchy.containsKey(class_name);
		String parent = class_hierarchy.get(class_name);
		
		while(hasParent) {
			ClassNode currClass = symbol_table.get(parent);
			if(currClass.fields.containsKey(var_id)) {
				return currClass.fields.get(var_id);
			}
			hasParent = class_hierarchy.containsKey(parent);
			parent = class_hierarchy.get(parent);
		}
		return null;
	}
	
}
