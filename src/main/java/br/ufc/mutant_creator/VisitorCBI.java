package br.ufc.mutant_creator;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.Visitable;

public class VisitorCBI extends MyModifierVisitor {
	
	@Override
    public Node visit(TryStmt n, ParameterVisitor parameter) {
		
//		System.out.println("posição atual: "+actualPosition+
//				" posição do parametro: "+parameter.getPosition()+" n: "+n);
		
		if(parameter.getPosition() == actualPosition) {
			
//			System.out.println("antes: "+ n);
			
			parameter.setBefore(n.toString());
			parameter.setBeginLine(n.getRange().get().begin.line);
			parameter.setEndLine(n.getRange().get().end.line);
			
			VisitorCatch vc = new VisitorCatch();
	    	n.accept(vc, null);
	    	
	    	parameter.setBeginLineModifier(n.getRange().get().begin.line);
			parameter.setEndLineModifier(n.getRange().get().end.line);
			
			parameter.setAfter(n.toString());
			
//			System.out.println("depois: "+ n);

		}

		actualPosition++;
		
    	return n;
    }
	
	private class VisitorCatch extends ModifierVisitor<Void>{
		@Override
		public Visitable visit(CatchClause n, Void arg) {
			System.out.println(n.getParameter().getName());
			return n;
		}
	}

	@Override
	public int countTimes(CompilationUnit cu) {
		return new VisitorCount().countTryBlock(cu);
	}
	
	@Override
	public String pathIdentification() {
		return "CBI";
	}
	
	@Override
	public String toString() {
		return "Catch Block Insertion";
	}
}
		