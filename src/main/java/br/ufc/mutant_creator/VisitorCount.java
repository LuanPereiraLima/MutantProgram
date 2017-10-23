package br.ufc.mutant_creator;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class VisitorCount {
	
	private int countTryBlock = 0;
	private int countTryBlockWithFinally = 0;
	
	public int countTryBlock(CompilationUnit cu) {
		VisitorC vc = new VisitorC();
		vc.visit(cu, null);
		return countTryBlock;
	}
	
	public int countTryBlockWithFinally(CompilationUnit cu) {
		VisitorC vc = new VisitorC();
		vc.visit(cu, null);
		return countTryBlockWithFinally;
	}
	
	private class VisitorC extends VoidVisitorAdapter<Void>{
		@Override
		public void visit(TryStmt n, Void arg) {
			countTryBlock++;
			if(n.getFinallyBlock().isPresent())
				countTryBlockWithFinally++;
			super.visit(n, arg);
		}
	}
}
		