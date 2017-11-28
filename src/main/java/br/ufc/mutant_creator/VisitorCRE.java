package br.ufc.mutant_creator;

import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.ThrowStmt;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.Visitable;

import br.ufc.teste.ClassUtil;

public class VisitorCRE extends MyModifierVisitor {
	
	public VisitorCRE() {
	}
	
	@Override
    public Node visit(TryStmt n, ParameterVisitor parameter) {
		//System.out.println(n);
		
		
//		System.out.println("posição atual: "+actualPosition+
//				" posição do parametro: "+parameter.getPosition()+" n: "+n);
		
		if(parameter.getPosition() == actualPosition) {
			parameter.setNeedModification(false);
//			System.out.println("antes: "+ n);
			
			parameter.setBefore(n.toString());
			parameter.setBeginLine(n.getRange().get().begin.line);
			parameter.setEndLine(n.getRange().get().end.line);
			
			VisitorCatch vc = new VisitorCatch();
			n.accept(vc, parameter);
	    	
	    	/*for(AuxItem item : listaDeItens) {
	    		System.out.println("ADICIONEI UM NOVO");
		    	for(String subtypes: item.getSubtypes()) {
			    	Parameter p = new Parameter(new ClassOrInterfaceType(subtypes), subtypes);
			    	p.setName(item.getNamePameter());
			    	n.getCatchClauses().add(new CatchClause(p, item.getBlock()));
			    	parameter.setNeedModification(true);
	    		}
	    	}*/
	    	parameter.setBeginLineModifier(n.getRange().get().begin.line);
			parameter.setEndLineModifier(n.getRange().get().end.line);
			
			parameter.setAfter(n.toString());
			
		//	System.out.println(parameter);
			
//			System.out.println("depois: "+ n);

		}

		actualPosition++;
		
    	return n;
    }
	
	private class VisitorCatch extends ModifierVisitor<ParameterVisitor>{
		@Override
		public Visitable visit(CatchClause n, ParameterVisitor parameter) {
			parameter.setNeedModification(true);
			NodeList<Statement> listaStatements = new NodeList<Statement>();
			Statement positionReturn = null;
			for(int i=0; i < n.getBody().getStatements().size(); i++) {
				if(n.getBody().getStatements().get(i) instanceof ReturnStmt) {
					positionReturn = n.getBody().getStatements().get(i);
				}else {
					listaStatements.add(n.getBody().getStatements().get(i));
				}
			}
			
			String name = ClassUtil.returnNamedWithPackageIfNecessary(n.getParameter().getType().asString(), parameter.getClassParameter());
			final List<String> lista = new ArrayList();
			ClassUtil.getTreeAllSubTypes(lista, name);
			
			n.getBody().setStatements(listaStatements);

			ThrowStmt ts = new ThrowStmt();
			ts.setExpression(name);
			n.getBody().getStatements().add(ts);
			parameter.setNeedModification(true);
			
			if(lista.size() > 0) {
				for(String s: lista) {
					ts = new ThrowStmt();
					ts.setExpression(s);
					
					n.getBody().getStatements().add(ts);
				}
			}
			if(positionReturn!=null)
				n.getBody().getStatements().add(positionReturn);
			return n;
		}
	}

	@Override
	public int countTimes(CompilationUnit cu) {
		return new VisitorCount().countTryBlock(cu);
	}
	
	@Override
	public String pathIdentification() {
		return "CRE";
	}
	
	@Override
	public String toString() {
		return "Catch and rethrow exception";
	}
}
		