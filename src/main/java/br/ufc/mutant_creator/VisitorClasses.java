package br.ufc.mutant_creator;

import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.AnnotationMemberDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.TypeParameter;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.model.declarations.ClassDeclaration;
import com.github.javaparser.symbolsolver.model.typesystem.Type;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

import br.ufc.teste.ClassParameter;

public class VisitorClasses extends VoidVisitorAdapter<ClassParameter>{
	
	@Override
	public void visit(ClassOrInterfaceDeclaration n, ClassParameter arg) {
		for(ClassOrInterfaceType b : n.getExtendedTypes()) {
			arg.getListExtends().add(b);
		}
		arg.setName(n.getName().asString());
		super.visit(n, arg);
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
