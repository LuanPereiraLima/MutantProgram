package br.ufc.mutant_creator;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.stmt.TryStmt;

public class VisitorFBD extends MyModifierVisitor {
	
	private Integer actualPosition = 0;
	
	@Override
    public Node visit(TryStmt n, ParameterVisitor parameter) {
		
		if(parameter.getPosition() == actualPosition) {
			
			System.out.println("TESTANDO ESSE: "+ n);
			
			parameter.setBefore(n.toString());
			parameter.setBeginLine(n.getRange().get().begin.line);
			parameter.setEndLine(n.getRange().get().end.line);
			
	    	if(n.getFinallyBlock().isPresent())
	    		n.removeFinallyBlock();
	    	
	    	parameter.setBeginLineModifier(n.getRange().get().begin.line);
			parameter.setEndLineModifier(n.getRange().get().end.line);
			
			parameter.setAfter(n.toString());
		}

		if(n.getFinallyBlock().isPresent())
			actualPosition++;
		
    	return n;
    }

	@Override
	public int countTimes(CompilationUnit cu) {
		return new VisitorCount().countTryBlockWithFinally(cu);
	}
	
	
}
		