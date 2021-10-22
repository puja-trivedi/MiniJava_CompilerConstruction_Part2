import cs132.minijava.visitor.GJVisitor;
//import cs132.minijava.visitor.GJNoArguVisitor;
//import cs132.minijava.visitor.GJDepthFirst;
import cs132.minijava.syntaxtree.*;


//public class TypeCheckVisitor implements GJNoArguVisitor<String> {
public class TypeCheckVisitor implements GJVisitor<String, SymbolTable> {
	private static final String INT = "int";
	private static final String BOOLEAN = "boolean";
	private static final String ERROR = "Type Error";
	private static final String INT_ARRAY = "int[]";
	
	public String curr_class;
	public String curr_method;

	// f0 -> "new" f1 -> Identifier() f2 -> "(" f3 -> ")"
	public String visit(AllocationExpression n, SymbolTable sym_table) {
		return n.f1.accept(this, sym_table);
	}
	
	// f0 -> PrimaryExpression() f1 -> "&&" f2 -> PrimaryExpression()
	public String visit(AndExpression n, SymbolTable sym_table) {
   		System.out.println("IN AND EXPRESSION");
		if(n.f0.accept(this, sym_table).equals(BOOLEAN) && n.f2.accept(this, sym_table).equals(BOOLEAN)){
			return BOOLEAN;
		} else {
			return ERROR;
		}
	}
	
	// f0 -> "new" f1 -> "int" f2 -> "[" f3 -> Expression() f4 -> "]"
	public String visit(ArrayAllocationExpression n, SymbolTable sym_table) {
		if(n.f3.accept(this, sym_table).equals(INT)) {
			return INT_ARRAY;
		} else {
			return ERROR;
		}
	}
	
	// f0 -> Identifier() f1 -> "[" f2 -> Expression() f3 -> "]" f4 -> "=" f5 -> Expression() f6 -> ";"
	public String visit(ArrayAssignmentStatement n, SymbolTable sym_table) {
		return "";
	}
	
	// f0 -> PrimaryExpression() f1 -> "." f2 -> "length"
	public String visit(ArrayLength n, SymbolTable sym_table) {
		if(n.f0.accept(this, sym_table).equals(INT_ARRAY)) {
			return INT;
		} else {	
			return ERROR;
		}
	}

	// f0 -> PrimaryExpression() f1 -> "[" f2 -> PrimaryExpression() f3 -> "]"
	public String visit(ArrayLookup n, SymbolTable sym_table) {
		if(n.f0.accept(this, sym_table).equals(INT_ARRAY) && n.f2.accept(this, sym_table).equals(INT)) {
			return INT;
		} else {
			return ERROR;
		} 
	}
	
	// f0 -> "int" f1 -> "[" f2 -> "]"
	public String visit(ArrayType n, SymbolTable sym_table) {
		return INT_ARRAY;
	}	
	
	// f0 -> Identifier() f1 -> "=" f2 -> Expression() f3 -> ";"
	public String visit(AssignmentStatement n, SymbolTable sym_table) {
		return "";
	}	
	
	// f0 -> "{" f1 -> ( Statement() )* f2 -> "}"
	public String visit(Block n, SymbolTable sym_table) {
		System.out.println("IN BLOCK");
		return n.f1.accept(this, sym_table);
	}	
	
	// f0 -> "boolean"
	public String visit(BooleanType n, SymbolTable sym_table) {
		return BOOLEAN;
	}
	
	// f0 -> "(" f1 -> Expression() f2 -> ")"
    public String visit(BracketExpression n, SymbolTable sym_table) {
    	System.out.println("IN BRACKETEXPRESSION");
		return n.f1.accept(this, sym_table);
    }
    
    // f0 -> "class" f1 -> Identifier() f2 -> "{" f3 -> ( VarDeclaration() )* f4 -> ( MethodDeclaration() )* f5 -> "}"
    public String visit(ClassDeclaration n, SymbolTable sym_table) {
        return "";
    }
    // f0 -> "class" f1 -> Identifier() f2 -> "extends" f3 -> Identifier() f4 -> "{" f5 -> ( VarDeclaration() )* f6 -> ( MethodDeclaration() )* f7 -> "}"
    public String visit(ClassExtendsDeclaration n, SymbolTable sym_table) {
        return "";
    }
    
    // f0 -> PrimaryExpression() f1 -> "<" f2 -> PrimaryExpression()
    public String visit(CompareExpression n, SymbolTable sym_table) {
   		System.out.println("IN COMPARE EXPRESSION");
		if(n.f0.accept(this, sym_table).equals(INT) && n.f2.accept(this, sym_table).equals(INT)){
			return BOOLEAN;
		} else {
			return ERROR;
		}
    }
    
    // f0 -> AndExpression() | CompareExpression() | PlusExpression() | MinusExpression() | TimesExpression() | ArrayLookup() | ArrayLength() | MessageSend() | PrimaryExpression()
    public String visit(Expression n, SymbolTable sym_table) {
        return n.f0.accept(this, sym_table);
    }
    
    // f0 -> Expression() f1 -> ( ExpressionRest() )*
    public String visit(ExpressionList n, SymbolTable sym_table) {
        return "";
    }
    
    // f0 -> "," f1 -> Expression()
    public String visit(ExpressionRest n, SymbolTable sym_table) {
        return n.f1.accept(this, sym_table);
    }
    
    // f0 -> "false"
    public String visit(FalseLiteral n, SymbolTable sym_table) {
    	System.out.println("IN FALSE LITERAL");
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
   		System.out.println("IN GOAL");
   		n.f0.accept(this, sym_table);
		return "";
	}
	
	// f0 ->
    public String visit(Identifier n, SymbolTable sym_table) {
        return "";
    }
    
    // f0 -> "if" f1 -> "(" f2 -> Expression() f3 -> ")" f4 -> Statement() f5 -> "else" f6 -> Statement()
    public String visit(IfStatement n, SymbolTable sym_table) {
        if(n.f2.accept(this, sym_table).equals(BOOLEAN) && !n.f4.accept(this, sym_table).equals(ERROR) && !n.f6.accept(this, sym_table).equals(ERROR)) {
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
   		System.out.println("IN MAINCLASS");
   		n.f14.accept(this, sym_table);
   		n.f15.accept(this, sym_table);
		return "";
	}
	
	// f0 -> PrimaryExpression() f1 -> "." f2 -> Identifier() f3 -> "(" f4 -> ( ExpressionList() )? f5 -> ")"
   	public String visit(MessageSend n, SymbolTable sym_table) {
		return "";
	}
	
	// f0 -> "public" f1 -> Type() f2 -> Identifier() f3 -> "(" f4 -> ( FormalParameterList() )? f5 -> ")" f6 -> "{" f7 -> ( VarDeclaration() )* f8 -> ( Statement() )* f9 -> "return" f10 -> Expression() f11 -> ";" f12 -> "}"
    public String visit(MethodDeclaration n, SymbolTable sym_table) {
        return "";
    }
    
    // f0 -> PrimaryExpression() f1 -> "-" f2 -> PrimaryExpression()
    public String visit(MinusExpression n, SymbolTable sym_table) {
   		System.out.println("IN MINUS EXPRESSION");
		if(n.f0.accept(this, sym_table).equals(INT) && n.f2.accept(this, sym_table).equals(INT)){
			return INT;
		} else {
			return ERROR;
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
   		System.out.println("IN NOT EXPRESSION");
		if(n.f1.accept(this, sym_table).equals(BOOLEAN)){
			return BOOLEAN;
		} else {
			return ERROR;
		}
	}
	
	// f0 -> PrimaryExpression() f1 -> "+" f2 -> PrimaryExpression()
   	public String visit(PlusExpression n, SymbolTable sym_table) {
   		System.out.println("IN PLUS EXPRESSION");
		if(n.f0.accept(this, sym_table).equals(INT) && n.f2.accept(this, sym_table).equals(INT)){
			return INT;
		} else {
			return ERROR;
		}
	}
	
	//f0 -> IntegerLiteral() | TrueLiteral() | FalseLiteral() | Identifier() | ThisExpression() | ArrayAllocationExpression() | AllocationExpression() | NotExpression() | BracketExpression()
   	public String visit(PrimaryExpression n, SymbolTable sym_table) {
		return n.f0.accept(this, sym_table);
	}
	
	// f0 -> "System.out.println" f1 -> "(" f2 -> Expression() f3 -> ")" f4 -> ";"
   	public String visit(PrintStatement n, SymbolTable sym_table) {
   		System.out.println("IN PRINTSTATEMENT");
		if(n.f2.accept(this, sym_table).equals(INT)){
			return "";
		} else {
			return ERROR;
		}
	}
	
	// f0 -> Block() | AssignmentStatement() | ArrayAssignmentStatement() | IfStatement() | WhileStatement() | PrintStatement()
   	public String visit(Statement n, SymbolTable sym_table) {
   		System.out.println("IN STATEMENT");
		return n.f0.accept(this, sym_table);
	}
	
	// f0 -> "this"
   	public String visit(ThisExpression n, SymbolTable sym_table) {
		return "";
	}
	
	// f0 -> PrimaryExpression() f1 -> "*" f2 -> PrimaryExpression()
   	public String visit(TimesExpression n, SymbolTable sym_table) {
   		System.out.println("IN TIMES EXPRESSION");
		if(n.f0.accept(this, sym_table).equals(INT) && n.f2.accept(this, sym_table).equals(INT)){
			return INT;
		} else {
			return ERROR;
		}
	}
	
	//f0 -> true
   	public String visit(TrueLiteral n, SymbolTable sym_table) {
    	System.out.println("IN TRUE LITERAL");
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
		if(n.f3.accept(this, sym_table).equals(BOOLEAN) && !n.f4.accept(this, sym_table).equals(ERROR)) {
			return "";
		} else {
			return ERROR;
		}
	}
}
