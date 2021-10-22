import cs132.minijava.syntaxtree.*;
import cs132.minijava.syntaxtree.Goal;
import cs132.minijava.MiniJavaParser;


public class Typecheck {
	public static void main(String[] args) {
		try {
			Goal root = new MiniJavaParser(System.in).Goal();
			SymbolTable sym_table = new SymbolTable();
			if(!root.accept(new SymbolTableVisitor(), sym_table) || ("Type Error").equals(root.accept(new TypeCheckVisitor(), sym_table))) {
			//if(("Type Error").equals(root.accept(new TypeCheckVisitor(), sym_table))) {
				System.out.println("Type Error");
			} else {
				System.out.println("Program type checked successfully");
			}
		} catch (Exception e) {
			System.out.println("Catch Statement: Type Error");
			//exit(0);
		}
	}
}
