// A MethodNode object is created for every class. 
// It will store information about the method including its name, return type, parameters,
// local variables, and class it belongs to.

import java.util.HashMap;

public class MethodNode {
	public String method_name;
	public String class_name; 
	public String return_type;
	public HashMap<String, String> parameters = new HashMap<String,String>();
	public HashMap<String, String> local_vars = new HashMap<String, String>();
	
	public MethodNode(String method_name, String class_name, String return_type) {
		this.method_name = method_name;
		this.class_name = class_name;
		this.return_type = return_type;
	}
	
	/*
	Adds method parameter to "parameters" HashMap. If successfully added: return true,
	otherwise return false. 
	*/
	public boolean addParameter(String para_name, String para_type) {
		if(!parameters.containsKey(para_name)) {
			parameters.put(para_name, para_type);
			return true;
		} else {
			return false;
		}
	}
	
	/*
	Adds method local variable to "local_vars" HashMap. If successfully added: return true,
	otherwise return false. 
	*/
	public boolean addLocalVar(String var_name, String var_type) {
		if(!local_vars.containsKey(var_name)) {
			local_vars.put(var_name, var_type);
			return true;
		} else {
			return false;
		}
	}
}
