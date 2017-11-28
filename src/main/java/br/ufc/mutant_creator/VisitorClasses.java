package br.ufc.mutant_creator;

import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import br.ufc.teste.ClassParameter;

public class VisitorClasses extends VoidVisitorAdapter<ClassParameter>{
	
	@Override
	public void visit(ClassOrInterfaceDeclaration n, ClassParameter arg) {
		if(!n.isInterface() && !n.isInnerClass()) {
			//System.out.println("Classe verificada; "+n.getName());
			for(ClassOrInterfaceType b : n.getExtendedTypes()) {
				//System.out.println("Extendes: "+b);
				arg.getListExtends().add(b);
			}
			//System.out.println("---------");
			arg.setName(n.getName().asString());
			super.visit(n, arg);
		}
	}
	
	@Override
	public void visit(ImportDeclaration n, ClassParameter arg) {
		arg.getListImports().add(n);
		super.visit(n, arg);
	}
	
	@Override
	public void visit(PackageDeclaration n, ClassParameter arg) {
		arg.setPkg(n.getName().asString());
		super.visit(n, arg);
	}
}
