import cs132.minijava.visitor.GJDepthFirst;
import cs132.minijava.syntaxtree.*;


public class SymbolTableVisitor extends GJDepthFirst<Boolean, SymbolTable> {
	private static final String INT = "int";
	private static final String BOOLEAN = "boolean";
	private static final String ERROR = "Type Error";
	private static final String INT_ARRAY = "int[]";
	
	private String curr_class;
	private String curr_method;
	private ClassNode curr_classNode;
	private MethodNode curr_methodNode;
	
	//ArrayType() | BooleanType() | IntegerType() | Identifier()
	public String getType(Type t) {
		if(t.f0.choice instanceof ArrayType) {
			return INT_ARRAY;
		} else if(t.f0.choice instanceof BooleanType) {
			return BOOLEAN;
		} else if(t.f0.choice instanceof IntegerType) {
			return INT;
		} else if(t.f0.choice instanceof Identifier) {
			return ((Identifier)t.f0.choice).f0.toString();
		} else {
			return "TYPE ERROR";
		}
	}

	// f0 -> MainClass() f1 -> ( TypeDeclaration() )* f2 ->
	@Override
   	public Boolean visit(Goal n, SymbolTable sym_table) {
	   	if(n.f0.accept(this, sym_table)){
	   		for(Node td : n.f1.nodes) {
	   			if(!td.accept(this, sym_table)) {
	   				return false;
	   			}
	   		}
	   		return sym_table.isAcyclic();
	   	}
		return false;
	}
	
	// f0 -> ClassDeclaration() | ClassExtendsDeclaration()
	@Override
   	public Boolean visit(TypeDeclaration n, SymbolTable sym_table) {
		return n.f0.accept(this, sym_table);
	}
	
    // f0 -> "class" f1 -> Identifier() f2 -> "{" f3 -> ( VarDeclaration() )* f4 -> ( MethodDeclaration() )* f5 -> "}"
    @Override
    public Boolean visit(ClassDeclaration n, SymbolTable sym_table) {
       	String className = n.f1.f0.toString();
       	curr_class = className;
       	
       	if(!sym_table.addClass(className, new ClassNode(className, null))) {
       		return false;
       	}
       	
       	for(Node vd : n.f3.nodes) {
       		if(!vd.accept(this, sym_table)) {
       			return false; 
       		}
       	}
       	
       	for(Node md : n.f4.nodes) {
       		if(!md.accept(this, sym_table)) {
       			return false; 
       		}
       	}
       	curr_class = null;
       	return true;
    }
    
    // f0 -> "class" f1 -> Identifier() f2 -> "extends" f3 -> Identifier() f4 -> "{" f5 -> ( VarDeclaration() )* f6 -> ( MethodDeclaration() )* f7 -> "}"
    @Override
    public Boolean visit(ClassExtendsDeclaration n, SymbolTable sym_table) {
       	String className = n.f1.f0.toString();
       	String parentName = n.f3.f0.toString();
       	curr_class = className;
       	
       	if(!sym_table.addClass(className, new ClassNode(className, parentName))) {
       		return false;
       	}
       	if(!sym_table.addParent(className, parentName)) {
       		return false;
       	}
       	
       	for(Node vd : n.f5.nodes) {
       		if(!vd.accept(this, sym_table)) {
       			return false; 
       		}
       	}
       	
       	for(Node md : n.f6.nodes) {
       		if(!md.accept(this, sym_table)) {
       			return false; 
       		}
       	}
       	curr_class = null;
       	return true;
    }
    
    // f0 -> "class" f1 -> Identifier() f2 -> "{" f3 -> "public" f4 -> "static" f5 -> "void" f6 -> "main" f7 -> "(" f8 -> "Boolean" f9 -> "[" f10 -> "]" f11 -> Identifier() f12 -> ")" f13 -> "{" f14 -> ( VarDeclaration() )* f15 -> ( Statement() )* f16 -> "}" f17 -> "}"
    @Override
   	public Boolean visit(MainClass n, SymbolTable sym_table) {
       	//String className = n.f1.f0.toString();
       	curr_class = n.f1.f0.toString();
       	curr_method = "main";
       	
       	curr_classNode = new ClassNode(curr_class, null);
       	if(!sym_table.addClass(curr_class, curr_classNode)) {
       		return false;
       	}
       	
       	curr_methodNode = new MethodNode(curr_method, curr_class, "void");
       	if(!curr_classNode.addMethod(curr_method, curr_methodNode)) {
       		return false;
       	}

       	if(!curr_methodNode.addParameter(n.f11.f0.toString(), "String []")) {
       		return false;
       	}
       	
       	for(Node vd : n.f14.nodes) {
       		if(!vd.accept(this, sym_table)) {
       			return false; 
       		}
       	}
       	
       	/* do we need this here? or in TypeCheck Visitor 
       	for(Node s : n.f15.nodes) {
       		if(!s.accept(this, sym_table)) {
       			return false; 
       		}
       	}
       	*/
       	curr_methodNode = null;
       	curr_method = null; 
       	curr_class = null;
       	curr_classNode = null;
       	return true;
       	 	
	}
	
	
    // f0 -> "public" f1 -> Type() f2 -> Identifier() f3 -> "(" f4 -> ( FormalParameterList() )? f5 -> ")" f6 -> "{" f7 -> ( VarDeclaration() )* f8 -> ( Statement() )* f9 -> "return" f10 -> Expression() f11 -> ";" f12 -> "}"
    @Override
    public Boolean visit(MethodDeclaration n, SymbolTable sym_table) {
       	curr_method = n.f2.f0.toString();

       	curr_methodNode = new MethodNode(curr_method, curr_class, getType(n.f1)); 
       	//curr_methodNode = new MethodNode(curr_method, curr_class, "void"); // NEED TO CHANGE VOID TO CORRECT TYPE
       	if(!curr_classNode.addMethod(curr_method, curr_methodNode)) {
       		return false;
       	}
       	
       	// add formal parameters
   		if(!n.f4.accept(this, sym_table)) {
       			return false; 
   		}
     
       	
       	// add variable declarations 
       	for(Node vd : n.f7.nodes) {
       		if(!vd.accept(this, sym_table)) {
       			return false; 
       		}
       	}
       	
       	/* do we need this here? or in TypeCheck Visitor 
       	for(Node s : n.f8.nodes) {
       		if(!s.accept(this, sym_table)) {
       			return false; 
       		}
       	*/
       	curr_methodNode = null;
       	return true;
    }
    
    // f0 -> Type() f1 -> Identifier()
    @Override
    public Boolean visit(FormalParameter n, SymbolTable sym_table) {
        return curr_methodNode.addParameter(n.f1.f0.toString(), getType(n.f0));
    }
    
    // f0 -> FormalParameter() f1 -> ( FormalParameterRest() )*
    @Override
    public Boolean visit(FormalParameterList n, SymbolTable sym_table) {
    	if(!n.f0.accept(this, sym_table)) {
    		return false; 
    	}
    	for(Node fp : n.f1.nodes) {
    		if(!fp.accept(this, sym_table)) {
    			return false; 
    		}
    	}
        return true;
    }
    
    // f0 -> "," f1 -> FormalParameter()
    @Override
   	public Boolean visit(FormalParameterRest n, SymbolTable sym_table) {
		return n.f1.accept(this, sym_table);
	}
	
	// f0 -> Type() f1 -> Identifier() f2 -> ";"
	@Override
   	public Boolean visit(VarDeclaration n, SymbolTable sym_table) {
		return curr_methodNode.addLocalVar(n.f1.f0.toString(), getType(n.f0));
	}
}
