import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class SymbolTable {
	public HashMap<String, ClassNode> symbol_table = new HashMap<String, ClassNode>();
	public HashMap<String, String> class_hierarchy = new HashMap<String, String>();
	
	public boolean containsOverloading(String class_name, String method_name) {
		boolean hasParent = class_hierarchy.containsKey(class_name);
		String parent = class_hierarchy.get(class_name);
		while(hasParent) {
			Set<String> methods = symbol_table.get(parent).methods.keySet(); 
			if(methods.contains(method_name)) {
				return true;
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
}
