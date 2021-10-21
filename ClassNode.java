// A ClassNode object is created for every class. 
// It will store information about the class including its name, parent class, fields, and methods.

import java.util.HashMap;

public class ClassNode {
	public String class_name;
	public String parent;
	public HashMap<String, String> fields = new HashMap<String, String>();
	public HashMap<String, MethodNode> methods = new HashMap<String, MethodNode>();
	
	public ClassNode(String class_name, String parent) {
		this.class_name = class_name;
		this.parent = parent; 
	}
	
	/*
	Adds class field to "fields" HashMap. If successfully added: return true,
	otherwise return false. 
	*/
	public boolean addField(String field_name, String field_type) {
		if(!fields.containsKey(field_name)){
			fields.put(field_name, field_type);
			return true;
		} else {
			return false; 
		}
	}
	
	/*
	Adds class method to "method" HashMap. If successfully added: return true,
	otherwise return false. 
	*/
	public boolean addMethod(String method_name, MethodNode method_node) {
		if(!methods.containsKey(method_name)){
			methods.put(method_name, method_node);
			return true;
		} else {
			return false;
		}
	}
}
