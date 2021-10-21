import cs132.minijava.syntaxtree.*;
import cs132.minijava.syntaxtree.Goal;
import cs132.minijava.MiniJavaParser;


public class Typecheck {
	public static void main(String[] args) {
		try {
			Goal root = new MiniJavaParser(System.in).Goal();
			//TypeCheckVisitor typecheckvisitor = new TypeCheckVisitor();
			//String returnStat = root.accept(typecheckvisitor);
			if(("Type Error").equals(root.accept(new TypeCheckVisitor()))) {
			//if(("Type Error").equals(returnStat)) {
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
