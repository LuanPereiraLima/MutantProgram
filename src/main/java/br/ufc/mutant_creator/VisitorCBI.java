package br.ufc.mutant_creator;

import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.Visitable;

import br.ufc.teste.ClassUtil;

public class VisitorCBI extends MyModifierVisitor {
	
	List<AuxItem> listaDeItens;
	
	public VisitorCBI() {
		listaDeItens = new ArrayList<AuxItem>();
	}
	
	private void resetListaItens() {
		listaDeItens.clear();
	}
	
	@Override
    public Node visit(TryStmt n, ParameterVisitor parameter) {
		
		//System.out.println(n);
		
		
//		System.out.println("posição atual: "+actualPosition+
//				" posição do parametro: "+parameter.getPosition()+" n: "+n);
		
		if(parameter.getPosition() == actualPosition) {
			resetListaItens();
			parameter.setNeedModification(false);
//			System.out.println("antes: "+ n);
			
			parameter.setBefore(n.toString());
			parameter.setBeginLine(n.getRange().get().begin.line);
			parameter.setEndLine(n.getRange().get().end.line);
			
			VisitorCatch vc = new VisitorCatch();
			n.accept(vc, parameter);
	    	
	    	for(AuxItem item : listaDeItens) {
	    		System.out.println("ADICIONEI UM NOVO");
		    	for(String subtypes: item.getSubtypes()) {
			    	Parameter p = new Parameter(new ClassOrInterfaceType(subtypes), subtypes);
			    	p.setName(item.getNamePameter());
			    	n.getCatchClauses().add(new CatchClause(p, item.getBlock()));
			    	parameter.setNeedModification(true);
	    		}
	    	}
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
			//System.out.println("-----------------!-----------------");
			//System.out.println("Name CLass: "+n.getParameter().getType().asString());
			String name = ClassUtil.returnNamedWithPackageIfNecessary(n.getParameter().getType().asString(), parameter.getClassParameter());
		//	System.out.println("Name after CLass returnNamedWithPackageIfNecessary: "+name);
			//List<String> lista = ClassUtil.getSubTypes(name, ConstantsPath.PROJECT_PATH_JAVA);
			final List<String> lista = new ArrayList();
			ClassUtil.getTreeAllSubTypes(lista, name);
		//	System.out.println("lista: "+lista);
			if(lista.size() > 0) {
				AuxItem aux = new AuxItem();
				aux.setSubtypes(lista);
				aux.setBlock(n.getBody());
				aux.setNamePameter(n.getParameter().getName());
				listaDeItens.add(aux);
			}
		//	System.out.println("-----------------!-----------------");
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
	

	
	private class AuxItem{
		private BlockStmt block;
		private SimpleName namePameter;
		private List<String> subtypes;
		public BlockStmt getBlock() {
			return block;
		}
		public void setBlock(BlockStmt block) {
			this.block = block;
		}
		public SimpleName getNamePameter() {
			return namePameter;
		}
		public void setNamePameter(SimpleName namePameter) {
			this.namePameter = namePameter;
		}
		public List<String> getSubtypes() {
			return subtypes;
		}
		public void setSubtypes(List<String> subtypes) {
			this.subtypes = subtypes;
		}
	}
}
		