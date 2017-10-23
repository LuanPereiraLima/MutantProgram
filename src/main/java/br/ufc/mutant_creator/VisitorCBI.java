package br.ufc.mutant_creator;

import org.reflections.Reflections;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.Visitable;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.model.typesystem.Type;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

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
			System.out.println(n.getParameter());
			n.getParameter().getType();
			
			Reflections reflections = new Reflections(CreateMutants.PROJECT_PATH_JAVA);
			Type typeOfTheNode = JavaParserFacade.get(new ReflectionTypeSolver()).getType(n.getParameter());
			
			System.out.println(n.getParameter().getType());
					try {
						Class sub = Class.forName(typeOfTheNode.describe());
						java.util.Set<Class<? extends Exception>> subTypes = reflections.getSubTypesOf((sub));
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			
			//java.util.Set<Class<? extends Exception>> subTypes = reflections.getSubTypesOf((typeOfTheNode.describe()));
		//	System.out.println(subTypes);
			//n.getParameter().getClass();
			return n;
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
		