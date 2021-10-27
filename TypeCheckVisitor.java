import cs132.minijava.visitor.GJVisitor;
//import cs132.minijava.visitor.GJNoArguVisitor;
//import cs132.minijava.visitor.GJDepthFirst;
import cs132.minijava.syntaxtree.*;
import java.util.Map;
import java.util.Iterator;


//public class TypeCheckVisitor implements GJNoArguVisitor<String> {
public class TypeCheckVisitor implements GJVisitor<String, SymbolTable> {
	private static final String INT = "int";
	private static final String BOOLEAN = "boolean";
	private static final String ERROR = "Type Error";
	private static final String INT_ARRAY = "int[]";
	
	private String curr_class;
	private String curr_method;
	private ClassNode curr_classNode;
	private MethodNode curr_methodNode;
	private Boolean isStatic; 

	// f0 -> "new" f1 -> Identifier() f2 -> "(" f3 -> ")"
	public String visit(AllocationExpression n, SymbolTable sym_table) {
		return n.f1.accept(this, sym_table);
	}
	
	// f0 -> PrimaryExpression() f1 -> "&&" f2 -> PrimaryExpression()
	public String visit(AndExpression n, SymbolTable sym_table) {
   		//System.out.println("IN AND EXPRESSION");
   		String f0 = n.f0.accept(this, sym_table);
   		String f2 = n.f2.accept(this, sym_table);
		if(f0.equals(BOOLEAN) && f2.equals(BOOLEAN)){
			return BOOLEAN;
		} else {
			String f0_type = f0;
			String f2_type = f2;
			if(!f0.equals(BOOLEAN)) {
				f0_type = sym_table.completeVarLookUp(f0, curr_methodNode);
			}
			if(!f2.equals(BOOLEAN)) {
				f2_type = sym_table.completeVarLookUp(f2, curr_methodNode);
			}
			if(f0_type.equals(BOOLEAN) && f2_type.equals(BOOLEAN)) {
				return BOOLEAN;
			} else {
				return ERROR;
			}
		}
	}
	
	// f0 -> "new" f1 -> "int" f2 -> "[" f3 -> Expression() f4 -> "]"
	public String visit(ArrayAllocationExpression n, SymbolTable sym_table) {
   		String f3 = n.f3.accept(this, sym_table);
		if(f3.equals(INT)){
			return INT_ARRAY;
		} else {
			String f3_type = sym_table.completeVarLookUp(f3, curr_methodNode);
			if(f3_type.equals(INT)) {
				return INT_ARRAY;
			} else {
				return ERROR;
			}
		}	
	}
	
	// f0 -> Identifier() f1 -> "[" f2 -> Expression() f3 -> "]" f4 -> "=" f5 -> Expression() f6 -> ";"
	public String visit(ArrayAssignmentStatement n, SymbolTable sym_table) {
		String f0 = n.f0.accept(this, sym_table);
		String f0_type = sym_table.completeVarLookUp(f0, curr_methodNode); 
		if(!f0_type.equals(INT_ARRAY)) {
			return ERROR;
		}
   		String f2 = n.f2.accept(this, sym_table);
   		String f5 = n.f5.accept(this, sym_table);
		if(f2.equals(INT) && f5.equals(INT)){
			return "";
		} else {
			String f2_type = f2;
			String f5_type = f5;
			if(!f2.equals(INT)) {
				f2_type = sym_table.completeVarLookUp(f2, curr_methodNode);
			}
			if(!f5.equals(INT)) {
				f5_type = sym_table.completeVarLookUp(f5, curr_methodNode);
			}
			if(f2_type.equals(INT) && f5_type.equals(INT)) {
				return "";
			} else {
				return ERROR;
			}
		}
	}
	
	// f0 -> PrimaryExpression() f1 -> "." f2 -> "length"
	public String visit(ArrayLength n, SymbolTable sym_table) {
   		String f0 = n.f0.accept(this, sym_table);
		if(f0.equals(INT_ARRAY)){
			return INT;
		} else {
			String f0_type = sym_table.completeVarLookUp(f0, curr_methodNode);
			if(f0_type.equals(INT_ARRAY)) {
				return INT;
			} else {
				return ERROR;
			}
		}
	}

	// f0 -> PrimaryExpression() f1 -> "[" f2 -> PrimaryExpression() f3 -> "]"
	public String visit(ArrayLookup n, SymbolTable sym_table) {
   		String f0 = n.f0.accept(this, sym_table);
   		String f2 = n.f2.accept(this, sym_table);
		if(f0.equals(INT_ARRAY) && f2.equals(INT)){
			return INT;
		} else {
			String f0_type = f0;
			String f2_type = f2;
			if(!f0.equals(INT_ARRAY)) {
				f0_type = sym_table.completeVarLookUp(f0, curr_methodNode);
			}
			if(!f2.equals(INT)) {
				f2_type = sym_table.completeVarLookUp(f2, curr_methodNode);
			}
			if(f0_type.equals(INT_ARRAY) && f2_type.equals(INT)) {
				return INT;
			} else {
				return ERROR;
			}
		}	
	}
	
	// f0 -> "int" f1 -> "[" f2 -> "]"
	public String visit(ArrayType n, SymbolTable sym_table) {
		return INT_ARRAY;
	}	
	
	// f0 -> Identifier() f1 -> "=" f2 -> Expression() f3 -> ";"
	public String visit(AssignmentStatement n, SymbolTable sym_table) {
		//System.out.print("Assigment Statement FOR: ");
		
		String lhs = n.f0.accept(this, sym_table);
		//System.out.println(var_id);
		String lhs_type;
		ClassNode lhs_class;
		if((lhs_type = sym_table.varLookUp(lhs, curr_methodNode)) == null) {
			if((lhs_class = sym_table.varLookUpParent(curr_class, lhs)) == null){
				return ERROR;
			}
			lhs_type = lhs_class.fields.get(lhs);
		}
		//System.out.println("LHS type: " + lhs_type);
		
		String rhs = n.f2.accept(this, sym_table);
		String rhs_type;
		ClassNode rhs_class;
		if((rhs_type = sym_table.varLookUp(rhs, curr_methodNode)) == null) {
			if((rhs_class = sym_table.varLookUpParent(curr_class, rhs)) == null){
				rhs_type = rhs; //this means the RHS is "new className()"
			} else {
				rhs_type = rhs_class.fields.get(rhs);
			}
		}	
		
		//System.out.println("RHS TYPE: " + rhs_type);
		if(lhs_type.equals(INT_ARRAY) || lhs_type.equals(INT) || lhs_type.equals(BOOLEAN)) {
			if(lhs_type.equals(rhs_type)) {
				//System.out.println("Assignment Statement - lhs == rhs");
				return "";
			}
		} else if(sym_table.isSubtype(rhs_type, lhs_type)) {
			return "";
		} 
		return ERROR;
	}	
	
	// f0 -> "{" f1 -> ( Statement() )* f2 -> "}"
	public String visit(Block n, SymbolTable sym_table) {
		//System.out.println("IN BLOCK");
		for(Node s : n.f1.nodes) {
			if(s.accept(this, sym_table).equals(ERROR)) {
				return ERROR;
			}
		}
		return "";
		
	}	
	
	// f0 -> "boolean"
	public String visit(BooleanType n, SymbolTable sym_table) {
		return BOOLEAN;
	}
	
	// f0 -> "(" f1 -> Expression() f2 -> ")"
    public String visit(BracketExpression n, SymbolTable sym_table) {
    	//System.out.println("IN BRACKETEXPRESSION");
   		String f1 = n.f1.accept(this, sym_table);
		if(f1.equals(INT_ARRAY) || f1.equals(INT) || f1.equals(BOOLEAN)){
			return f1;
		} else {
			String f1_type = sym_table.completeVarLookUp(f1, curr_methodNode);
			if(f1_type != null) {
				return f1_type;
			} else {
				return ERROR;
			}
		}
    }
    
    // f0 -> "class" f1 -> Identifier() f2 -> "{" f3 -> ( VarDeclaration() )* f4 -> ( MethodDeclaration() )* f5 -> "}"
    public String visit(ClassDeclaration n, SymbolTable sym_table) {
        curr_class = n.f1.accept(this, sym_table);
        curr_classNode = sym_table.symbol_table.get(curr_class);
        
        for(Node md : n.f4.nodes) {
        	if(md.accept(this, sym_table).equals(ERROR)) {
        		return ERROR;
        	}
        }
        
        curr_class = null;
        curr_classNode = null; 
        return "";
  
    }
    // f0 -> "class" f1 -> Identifier() f2 -> "extends" f3 -> Identifier() f4 -> "{" f5 -> ( VarDeclaration() )* f6 -> ( MethodDeclaration() )* f7 -> "}"
    public String visit(ClassExtendsDeclaration n, SymbolTable sym_table) {
        curr_class = n.f1.accept(this, sym_table);
        curr_classNode = sym_table.symbol_table.get(curr_class);
        
        String parent_class = n.f3.accept(this, sym_table);
        sym_table.addParent(curr_class, parent_class);
        
        for(Node md : n.f6.nodes) {
        	if(md.accept(this, sym_table).equals(ERROR)) {
        		return ERROR;
        	}
        }
        
        // check for overloading 
        for(Map.Entry<String, MethodNode> method : curr_classNode.methods.entrySet()) {
        	if(sym_table.containsOverloading(curr_class, method.getValue())) {
        		return ERROR;
        	}
        }
     
        curr_class = null;
        curr_classNode = null; 
        return "";
    }
    
    // f0 -> PrimaryExpression() f1 -> "<" f2 -> PrimaryExpression()
    public String visit(CompareExpression n, SymbolTable sym_table) {
   		//System.out.println("IN COMPARE EXPRESSION");
   		String f0 = n.f0.accept(this, sym_table);
   		String f2 = n.f2.accept(this, sym_table);
		if(f0.equals(INT) && f2.equals(INT)){
			return BOOLEAN;
		} else {
			String f0_type = f0;
			String f2_type = f2;
			if(!f0.equals(INT)) {
				f0_type = sym_table.completeVarLookUp(f0, curr_methodNode);
			}
			if(!f2.equals(INT)) {
				f2_type = sym_table.completeVarLookUp(f2, curr_methodNode);
			}
			if(f0_type.equals(INT) && f2_type.equals(INT)) {
				return BOOLEAN;
			} else {
				return ERROR;
			}
		}
    }
    
    // f0 -> AndExpression() | CompareExpression() | PlusExpression() | MinusExpression() | TimesExpression() | ArrayLookup() | ArrayLength() | MessageSend() | PrimaryExpression()
    public String visit(Expression n, SymbolTable sym_table) {
        return n.f0.accept(this, sym_table);
    }
    
    // f0 -> Expression() f1 -> ( ExpressionRest() )*
    public String visit(ExpressionList n, SymbolTable sym_table) {
    	if(n.f0.accept(this, sym_table).equals(ERROR)) {
    		return ERROR;
    	} else {
    		for(Node e : n.f1.nodes) {
    			if(e.accept(this, sym_table).equals(ERROR)) {
    				return ERROR;
    			}
    		}
		}
    	return "";
    }
    
    // f0 -> "," f1 -> Expression()
    public String visit(ExpressionRest n, SymbolTable sym_table) {
        return n.f1.accept(this, sym_table);
    }
    
    // f0 -> "false"
    public String visit(FalseLiteral n, SymbolTable sym_table) {
    	//System.out.println("IN FALSE LITERAL");
        return BOOLEAN;
    }

	// f0 -> Type() f1 -> Identifier()
    public String visit(FormalParameter n, SymbolTable sym_table) {
        return "";
    }
    
    // f0 -> FormalParameter() f1 -> ( FormalParameterRest() )*
    public String visit(FormalParameterList n, SymbolTable sym_table) {
        return "";
    }
    
    // f0 -> "," f1 -> FormalParameter()
   	public String visit(FormalParameterRest n, SymbolTable sym_table) {
		return "";
	}
	
	// f0 -> MainClass() f1 -> ( TypeDeclaration() )* f2 ->
   	public String visit(Goal n, SymbolTable sym_table) {
   		//System.out.println("IN GOAL");
   		if(n.f0.accept(this, sym_table).equals(ERROR)) {
   			return ERROR;
   		}

   		for(Node td : n.f1.nodes) {
   			if(td.accept(this, sym_table).equals(ERROR)) {
	   				return ERROR;
   			}
   		}
   		/*
   		if(n.f1.accept(this, sym_table).equals(ERROR)) {
   			return ERROR;
   		}
   		*/
		return "";
	}
	
	// f0 ->
    public String visit(Identifier n, SymbolTable sym_table) {
        return n.f0.toString();
    }
    
    // f0 -> "if" f1 -> "(" f2 -> Expression() f3 -> ")" f4 -> Statement() f5 -> "else" f6 -> Statement()
    public String visit(IfStatement n, SymbolTable sym_table) {
    	//System.out.println("IN IF STATEMENT");
		String f2 = n.f2.accept(this, sym_table);
    	String f4 = n.f4.accept(this, sym_table); 
    	String f6 = n.f6.accept(this, sym_table);
		if(!f2.equals(BOOLEAN)){
			f2 = sym_table.completeVarLookUp(f2, curr_methodNode);
		}
        if(f2.equals(BOOLEAN) && (!f4.equals(ERROR)) && (!f6.equals(ERROR))) {
        	return "";
        } else {
        	return ERROR;
        }
    }
    
    // f0 -> 
   	public String visit(IntegerLiteral n, SymbolTable sym_table) {
		return INT;
	}
	
    // f0 -> "int"
    public String visit(IntegerType n, SymbolTable sym_table) {
        return INT;
    }
    
    // f0 -> "class" f1 -> Identifier() f2 -> "{" f3 -> "public" f4 -> "static" f5 -> "void" f6 -> "main" f7 -> "(" f8 -> "String" f9 -> "[" f10 -> "]" f11 -> Identifier() f12 -> ")" f13 -> "{" f14 -> ( VarDeclaration() )* f15 -> ( Statement() )* f16 -> "}" f17 -> "}"
   	public String visit(MainClass n, SymbolTable sym_table) {
   		isStatic = true;
   		curr_class = n.f1.accept(this, sym_table);
   		curr_classNode = sym_table.symbol_table.get(curr_class);
   		curr_method = "main";
		curr_methodNode = sym_table.methodLookup(curr_method, curr_classNode);
		
		for(Node s : n.f15.nodes) {
			if(s.accept(this, sym_table).equals(ERROR)) {
				return ERROR;
			}
		}
		
       	curr_methodNode = null;
       	curr_method = null; 
       	curr_class = null;
       	curr_classNode = null;
   		isStatic = false; 
   		return "";
	}
	
	// f0 -> PrimaryExpression() f1 -> "." f2 -> Identifier() f3 -> "(" f4 -> ( ExpressionList() )? f5 -> ")"
   	public String visit(MessageSend n, SymbolTable sym_table) {
   		//System.out.println("Message Send");
		String prim_exp;
		String id = n.f2.accept(this, sym_table);
		if((prim_exp = n.f0.accept(this, sym_table)).equals(ERROR)) {
			return ERROR;
		} 
		//System.out.println("prim_exp: " + prim_exp);
		String prim_exp_type; 
		ClassNode prim_exp_class; 
		if((prim_exp_type = sym_table.varLookUp(prim_exp, curr_methodNode)) == null) {
			if((prim_exp_class = sym_table.varLookUpParent(curr_class, prim_exp)) == null) { // new object
				//System.out.println("in second if");
				prim_exp_type = prim_exp;
				prim_exp_class = sym_table.classLookup(prim_exp); 
			} else {
				prim_exp_type = prim_exp_class.fields.get(prim_exp);
			}
		} else {
			prim_exp_class = sym_table.classLookup(prim_exp_type);
		}
		//System.out.println("prim_exp_class : " + prim_exp_class.class_name);
		//System.out.println("methodID: " + id);
		MethodNode method;
		if((method = sym_table.methodLookup(id, prim_exp_class)) == null) {
			return ERROR;
		}
		/*
		Iterator<String> iter = method.parameters.values().iterator();
		for(Node e : n.f4.nodes) {
			String e_type = e.accept(this, sym_table);
			if(!iter.hasNext()) {
				return ERROR;
			} else if(!e_type.equals(iter.next())){
				return ERROR;
			}	
		}
		if(iter.hasNext()){
			return ERROR;
		}
		*/
		//System.out.println("DONE WITH Message Send");
		return method.return_type;
	}
	
	// f0 -> "public" f1 -> Type() f2 -> Identifier() f3 -> "(" f4 -> ( FormalParameterList() )? f5 -> ")" f6 -> "{" f7 -> ( VarDeclaration() )* f8 -> ( Statement() )* f9 -> "return" f10 -> Expression() f11 -> ";" f12 -> "}"
    public String visit(MethodDeclaration n, SymbolTable sym_table) {
    	//System.out.println("METHOD DEC");
    	curr_method = n.f2.accept(this, sym_table);
        curr_methodNode = sym_table.methodLookup(curr_method, curr_classNode);
        
        for(Node s : n.f8.nodes) {
        	if(s.accept(this, sym_table).equals(ERROR)) {
        		return ERROR;
        	}
        }
        
        //System.out.println("END OF METHOD DEC1");
        
        // check that the return statement is the correct type
        String f10 = n.f10.accept(this, sym_table);
        if(f10.equals(INT_ARRAY) || f10.equals(INT) || f10.equals(BOOLEAN)) {
		    if(!f10.equals(curr_methodNode.return_type)) {
		    	return ERROR;
		    } 
        } else {
			f10 = sym_table.completeVarLookUp(f10, curr_methodNode);
			if(!f10.equals(curr_methodNode.return_type)) {
		    	return ERROR;
		    }
        }
        
        //System.out.println("END OF METHOD DEC");
        
        curr_method = null;
        curr_methodNode = null;
        return "";
    }
    
    // f0 -> PrimaryExpression() f1 -> "-" f2 -> PrimaryExpression()
    public String visit(MinusExpression n, SymbolTable sym_table) {
   		//System.out.println("IN MINUS EXPRESSION");
   		String f0 = n.f0.accept(this, sym_table);
   		String f2 = n.f2.accept(this, sym_table);
		if(f0.equals(INT) && f2.equals(INT)){
			return INT;
		} else {
			String f0_type = f0;
			String f2_type = f2;
			if(!f0.equals(INT)) {
				f0_type = sym_table.completeVarLookUp(f0, curr_methodNode);
			}
			if(!f2.equals(INT)) {
				f2_type = sym_table.completeVarLookUp(f2, curr_methodNode);
			}
			if(f0_type.equals(INT) && f2_type.equals(INT)) {
				return INT;
			} else {
				return ERROR;
			}
		}
    }
    
   	public String visit(NodeList n, SymbolTable sym_table) {
		return "";
	}
	
   	public String visit(NodeListOptional n, SymbolTable sym_table) {
		return "";
	}
	
   	public String visit(NodeOptional n, SymbolTable sym_table) {
		return "";
	}
	
   	public String visit(NodeSequence n, SymbolTable sym_table) {
		return "";
	}
	
   	public String visit(NodeToken n, SymbolTable sym_table) {
		return "";
	}
	
	// f0 -> "!" f1 -> Expression()
   	public String visit(NotExpression n, SymbolTable sym_table) {
   		String f1 = n.f1.accept(this, sym_table);
		if(f1.equals(BOOLEAN)){
			return BOOLEAN;
		} else {
			String f1_type = sym_table.completeVarLookUp(f1, curr_methodNode);
			if(f1_type.equals(BOOLEAN)) {
				return BOOLEAN;
			} else {
				return ERROR;
			}
		}
	}
	
	// f0 -> PrimaryExpression() f1 -> "+" f2 -> PrimaryExpression()
   	public String visit(PlusExpression n, SymbolTable sym_table) {
   		//System.out.println("IN PLUS EXPRESSION");
   		String f0 = n.f0.accept(this, sym_table);
   		String f2 = n.f2.accept(this, sym_table);
		if(f0.equals(INT) && f2.equals(INT)){
			return INT;
		} else {
			String f0_type = f0;
			String f2_type = f2;
			if(!f0.equals(INT)) {
				f0_type = sym_table.completeVarLookUp(f0, curr_methodNode);
			}
			if(!f2.equals(INT)) {
				f2_type = sym_table.completeVarLookUp(f2, curr_methodNode);
			}
			if(f0_type.equals(INT) && f2_type.equals(INT)) {
				return INT;
			} else {
				return ERROR;
			}
		}
	}
	
	//f0 -> IntegerLiteral() | TrueLiteral() | FalseLiteral() | Identifier() | ThisExpression() | ArrayAllocationExpression() | AllocationExpression() | NotExpression() | BracketExpression()
   	public String visit(PrimaryExpression n, SymbolTable sym_table) {
		return n.f0.accept(this, sym_table);
	}
	
	// f0 -> "//System.out.println" f1 -> "(" f2 -> Expression() f3 -> ")" f4 -> ";"
   	public String visit(PrintStatement n, SymbolTable sym_table) {
   		//System.out.println("IN PRINTSTATEMENT");
   		String f2 = n.f2.accept(this, sym_table);
		if(f2.equals(INT)){
			return INT;
		} else {
			String f2_type = sym_table.completeVarLookUp(f2, curr_methodNode);
			if(f2_type.equals(INT)) {
				return INT;
			} else {
				return ERROR;
			}
		}
	}
	
	// f0 -> Block() | AssignmentStatement() | ArrayAssignmentStatement() | IfStatement() | WhileStatement() | PrintStatement()
   	public String visit(Statement n, SymbolTable sym_table) {
   		//System.out.println("IN STATEMENT");
		return n.f0.accept(this, sym_table);
	}
	
	// f0 -> "this"
   	public String visit(ThisExpression n, SymbolTable sym_table) {
   		/*
   		1. if currentClass is null return ERROR else return currentClass
   		*/
		if(isStatic) {
			return ERROR;
		}
		return curr_class;
	}
	
	// f0 -> PrimaryExpression() f1 -> "*" f2 -> PrimaryExpression()
   	public String visit(TimesExpression n, SymbolTable sym_table) {
   		//System.out.println("IN TIMES EXPRESSION");
   		String f0 = n.f0.accept(this, sym_table);
   		String f2 = n.f2.accept(this, sym_table);
		if(f0.equals(INT) && f2.equals(INT)){
			return INT;
		} else {
			String f0_type = f0;
			String f2_type = f2;
			if(!f0.equals(INT)) {
				f0_type = sym_table.completeVarLookUp(f0, curr_methodNode);
			}
			if(!f2.equals(INT)) {
				f2_type = sym_table.completeVarLookUp(f2, curr_methodNode);
			}
			if(f0_type.equals(INT) && f2_type.equals(INT)) {
				return INT;
			} else {
				return ERROR;
			}
		}
	}
	
	//f0 -> true
   	public String visit(TrueLiteral n, SymbolTable sym_table) {
    	//System.out.println("IN TRUE LITERAL");
        return BOOLEAN;
	}
	
	// f0 -> ArrayType() | BooleanType() | IntegerType() | Identifier()
   	public String visit(Type n, SymbolTable sym_table) {
		return n.f0.accept(this, sym_table);
	}
	
	// f0 -> ClassDeclaration() | ClassExtendsDeclaration()
   	public String visit(TypeDeclaration n, SymbolTable sym_table) {
		return n.f0.accept(this, sym_table);
	}
	
	// f0 -> Type() f1 -> Identifier() f2 -> ";"
   	public String visit(VarDeclaration n, SymbolTable sym_table) {
		return "";
	}
	
	// f0 -> "while" f1 -> "(" f2 -> Expression() f3 -> ")" f4 -> Statement()
   	public String visit(WhileStatement n, SymbolTable sym_table) {
   	    //System.out.println("IN WHILE STATEMENT");
   		String f2 = n.f2.accept(this, sym_table);
   		String f4 = n.f4.accept(this, sym_table);
		if(!f2.equals(BOOLEAN)){
			f2 = sym_table.completeVarLookUp(f2, curr_methodNode);
		}
		if(f2.equals(BOOLEAN) && !f4.equals(ERROR)) {
			return "";
		} else {
			return ERROR;
		}
	}
}
