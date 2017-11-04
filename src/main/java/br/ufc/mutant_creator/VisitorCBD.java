package br.ufc.mutant_creator;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.Visitable;

public class VisitorCBD extends MyModifierVisitor {
	
	@Override
    public Node visit(TryStmt n, ParameterVisitor parameter) {
		
		if(parameter.getPosition() == actualPosition) {

			parameter.setBefore(n.toString());
			parameter.setBeginLine(n.getRange().get().begin.line);
			parameter.setEndLine(n.getRange().get().end.line);
			
			VisitorCatch vc = new VisitorCatch();
	    	n.accept(vc, null);
	    	
	    	if(!n.getFinallyBlock().isPresent())
				n.setFinallyBlock(new BlockStmt());
	    	
	    	parameter.setBeginLineModifier(n.getRange().get().begin.line);
			parameter.setEndLineModifier(n.getRange().get().end.line);
			
			parameter.setAfter(n.toString());
		
		}

		actualPosition++;
		
    	return n;
    }
	
	private class VisitorCatch extends ModifierVisitor<Void>{
		@Override
		public Visitable visit(CatchClause n, Void arg) {
			return null;
		}
	}

	@Override
	public int countTimes(CompilationUnit cu) {
		return new VisitorCount().countTryBlock(cu);
	}
	
	@Override
	public String pathIdentification() {
		return "CBD";
	}
	
	@Override
	public String toString() {
		return "Remove Catch Block";
	}
}
		