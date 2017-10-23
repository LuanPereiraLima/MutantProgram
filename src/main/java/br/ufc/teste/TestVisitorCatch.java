package br.ufc.teste;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.Visitable;

public class TestVisitorCatch extends ModifierVisitor<Void> {
	
	@Override
    public Node visit(TryStmt n, Void arg) {
    	System.out.println("---------CATCH ENCONTRADO: ");
    	System.out.println(n.getCatchClauses().toString());
    	
    	/*n.accept(new ModifierVisitor<Void>() {
    		@Override
    		public Visitable visit(CatchClause n, Void arg) {
    			return null;
    		}
    	}, null);*/
    	
    	System.out.println(n.getTryBlock().getChildNodes().get(0).toString());
    	
    	return n;
    }
}
