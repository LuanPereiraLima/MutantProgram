package br.ufc.mutant_creator;

import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class VisitorCatch extends VoidVisitorAdapter<Void> {
    @Override
    public void visit(CatchClause n, Void arg) {
    	System.out.println("---------CATCH ENCONTRADO: ");
    	System.out.println(n.getBody());
    	System.out.println("---------");
    	n.setBody(new BlockStmt());
    	MethodChanger.changeClass = true;
    	super.visit(n, arg);
    }
}
