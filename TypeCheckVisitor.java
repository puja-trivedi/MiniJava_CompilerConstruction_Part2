//import cs132.minijava.visitor.GJVisitor;
import cs132.minijava.visitor.GJNoArguVisitor;
import cs132.minijava.syntaxtree.*;


public class TypeCheckVisitor implements GJNoArguVisitor<String> {
	private static final String INT = "int";
	private static final String BOOLEAN = "boolean";
	private static final String ERROR = "Type Error";
	
	public String curr_class;
	public String curr_method;

	// f0 -> "new" f1 -> Identifier() f2 -> "(" f3 -> ")"
	public String visit(AllocationExpression n) {
		return "";
	}
	
	// f0 -> PrimaryExpression() f1 -> "&&" f2 -> PrimaryExpression()
	public String visit(AndExpression n) {
   		System.out.println("IN AND EXPRESSION");
		if(n.f0.accept(this).equals(BOOLEAN) && n.f2.accept(this).equals(BOOLEAN)){
			return BOOLEAN;
		} else {
			return ERROR;
		}
	}
	
	// f0 -> "new" f1 -> "int" f2 -> "[" f3 -> Expression() f4 -> "]"
	public String visit(ArrayAllocationExpression n) {
		return "";
	}
	
	// f0 -> Identifier() f1 -> "[" f2 -> Expression() f3 -> "]" f4 -> "=" f5 -> Expression() f6 -> ";"
	public String visit(ArrayAssignmentStatement n) {
		return "";
	}
	
	// f0 -> PrimaryExpression() f1 -> "." f2 -> "length"
	public String visit(ArrayLength n) {
		return "";
	}

	// f0 -> PrimaryExpression() f1 -> "[" f2 -> PrimaryExpression() f3 -> "]"
	public String visit(ArrayLookup n) {
		return "";
	}
	
	// f0 -> "int" f1 -> "[" f2 -> "]"
	public String visit(ArrayType n) {
		return "";
	}	
	
	// f0 -> Identifier() f1 -> "=" f2 -> Expression() f3 -> ";"
	public String visit(AssignmentStatement n) {
		return "";
	}	
	
	// f0 -> "{" f1 -> ( Statement() )* f2 -> "}"
	public String visit(Block n) {
		System.out.println("IN BLOCK");
		return n.f1.accept(this);
	}	
	
	// f0 -> "boolean"
	public String visit(BooleanType n) {
		return BOOLEAN;
	}
	
	// f0 -> "(" f1 -> Expression() f2 -> ")"
    public String visit(BracketExpression n) {
    	System.out.println("IN BRACKETEXPRESSION");
		return n.f1.accept(this);
    }
    
    // f0 -> "class" f1 -> Identifier() f2 -> "{" f3 -> ( VarDeclaration() )* f4 -> ( MethodDeclaration() )* f5 -> "}"
    public String visit(ClassDeclaration n) {
        return "";
    }
    // f0 -> "class" f1 -> Identifier() f2 -> "extends" f3 -> Identifier() f4 -> "{" f5 -> ( VarDeclaration() )* f6 -> ( MethodDeclaration() )* f7 -> "}"
    public String visit(ClassExtendsDeclaration n) {
        return "";
    }
    
    // f0 -> PrimaryExpression() f1 -> "<" f2 -> PrimaryExpression()
    public String visit(CompareExpression n) {
   		System.out.println("IN COMPARE EXPRESSION");
		if(n.f0.accept(this).equals(INT) && n.f2.accept(this).equals(INT)){
			return BOOLEAN;
		} else {
			return ERROR;
		}
    }
    
    // f0 -> AndExpression() | CompareExpression() | PlusExpression() | MinusExpression() | TimesExpression() | ArrayLookup() | ArrayLength() | MessageSend() | PrimaryExpression()
    public String visit(Expression n) {
        return n.f0.accept(this);
    }
    
    // f0 -> Expression() f1 -> ( ExpressionRest() )*
    public String visit(ExpressionList n) {
        return "";
    }
    
    // f0 -> "," f1 -> Expression()
    public String visit(ExpressionRest n) {
        return "";
    }
    
    // f0 -> "false"
    public String visit(FalseLiteral n) {
    	System.out.println("IN FALSE LITERAL");
        return BOOLEAN;
    }

	// f0 -> Type() f1 -> Identifier()
    public String visit(FormalParameter n) {
        return "";
    }
    
    // f0 -> FormalParameter() f1 -> ( FormalParameterRest() )*
    public String visit(FormalParameterList n) {
        return "";
    }
    
    // f0 -> "," f1 -> FormalParameter()
   	public String visit(FormalParameterRest n) {
		return "";
	}
	
	// f0 -> MainClass() f1 -> ( TypeDeclaration() )* f2 ->
   	public String visit(Goal n) {
   		System.out.println("IN GOAL");
   		n.f0.accept(this);
		return "";
	}
	
	// f0 ->
    public String visit(Identifier n) {
        return "";
    }
    
    // f0 -> "if" f1 -> "(" f2 -> Expression() f3 -> ")" f4 -> Statement() f5 -> "else" f6 -> Statement()
    public String visit(IfStatement n) {
        return "";
    }
    
    // f0 -> 
   	public String visit(IntegerLiteral n) {
		return INT;
	}
	
    // f0 -> "int"
    public String visit(IntegerType n) {
        return INT;
    }
    
    // f0 -> "class" f1 -> Identifier() f2 -> "{" f3 -> "public" f4 -> "static" f5 -> "void" f6 -> "main" f7 -> "(" f8 -> "String" f9 -> "[" f10 -> "]" f11 -> Identifier() f12 -> ")" f13 -> "{" f14 -> ( VarDeclaration() )* f15 -> ( Statement() )* f16 -> "}" f17 -> "}"
   	public String visit(MainClass n) {
   		System.out.println("IN MAINCLASS");
   		n.f14.accept(this);
   		n.f15.accept(this);
		return "";
	}
	
	// f0 -> PrimaryExpression() f1 -> "." f2 -> Identifier() f3 -> "(" f4 -> ( ExpressionList() )? f5 -> ")"
   	public String visit(MessageSend n) {
		return "";
	}
	
	// f0 -> "public" f1 -> Type() f2 -> Identifier() f3 -> "(" f4 -> ( FormalParameterList() )? f5 -> ")" f6 -> "{" f7 -> ( VarDeclaration() )* f8 -> ( Statement() )* f9 -> "return" f10 -> Expression() f11 -> ";" f12 -> "}"
    public String visit(MethodDeclaration n) {
        return "";
    }
    
    // f0 -> PrimaryExpression() f1 -> "-" f2 -> PrimaryExpression()
    public String visit(MinusExpression n) {
   		System.out.println("IN MINUS EXPRESSION");
		if(n.f0.accept(this).equals(INT) && n.f2.accept(this).equals(INT)){
			return INT;
		} else {
			return ERROR;
		}
    }
    
   	public String visit(NodeList n) {
		return "";
	}
	
   	public String visit(NodeListOptional n) {
		return "";
	}
	
   	public String visit(NodeOptional n) {
		return "";
	}
	
   	public String visit(NodeSequence n) {
		return "";
	}
	
   	public String visit(NodeToken n) {
		return "";
	}
	
	// f0 -> "!" f1 -> Expression()
   	public String visit(NotExpression n) {
   		System.out.println("IN NOT EXPRESSION");
		if(n.f1.accept(this).equals(BOOLEAN)){
			return BOOLEAN;
		} else {
			return ERROR;
		}
	}
	
	// f0 -> PrimaryExpression() f1 -> "+" f2 -> PrimaryExpression()
   	public String visit(PlusExpression n) {
   		System.out.println("IN PLUS EXPRESSION");
		if(n.f0.accept(this).equals(INT) && n.f2.accept(this).equals(INT)){
			return INT;
		} else {
			return ERROR;
		}
	}
	
	//f0 -> IntegerLiteral() | TrueLiteral() | FalseLiteral() | Identifier() | ThisExpression() | ArrayAllocationExpression() | AllocationExpression() | NotExpression() | BracketExpression()
   	public String visit(PrimaryExpression n) {
		return n.f0.accept(this);
	}
	
	// f0 -> "System.out.println" f1 -> "(" f2 -> Expression() f3 -> ")" f4 -> ";"
   	public String visit(PrintStatement n) {
   		System.out.println("IN PRINTSTATEMENT");
		if(n.f2.accept(this).equals(INT)){
			return null;
		} else {
			return ERROR;
		}
	}
	
	// f0 -> Block() | AssignmentStatement() | ArrayAssignmentStatement() | IfStatement() | WhileStatement() | PrintStatement()
   	public String visit(Statement n) {
   		System.out.println("IN STATEMENT");
		return n.f0.accept(this);
	}
	
	// f0 -> "this"
   	public String visit(ThisExpression n) {
		return "";
	}
	
	// f0 -> PrimaryExpression() f1 -> "*" f2 -> PrimaryExpression()
   	public String visit(TimesExpression n) {
   		System.out.println("IN TIMES EXPRESSION");
		if(n.f0.accept(this).equals(INT) && n.f2.accept(this).equals(INT)){
			return INT;
		} else {
			return ERROR;
		}
	}
	
	//f0 -> true
   	public String visit(TrueLiteral n) {
    	System.out.println("IN TRUE LITERAL");
        return BOOLEAN;
	}
	
	// f0 -> ArrayType() | BooleanType() | IntegerType() | Identifier()
   	public String visit(Type n) {
		return n.f0.accept(this);
	}
	
	// f0 -> ClassDeclaration() | ClassExtendsDeclaration()
   	public String visit(TypeDeclaration n) {
		return n.f0.accept(this);
	}
	
	// f0 -> Type() f1 -> Identifier() f2 -> ";"
   	public String visit(VarDeclaration n) {
		return "";
	}
	
	// f0 -> "while" f1 -> "(" f2 -> Expression() f3 -> ")" f4 -> Statement()
   	public String visit(WhileStatement n) {
		return "";
	}
}
