package br.ufc.teste;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.ClassExpr;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.Visitable;

import br.ufc.mutant_creator.ParameterVisitor;
import br.ufc.mutant_creator.VisitorCBD;
import br.ufc.mutant_creator.VisitorCBI;

public class Main {
	
	private static final String CLASS_FOLDER =
	        "src/main/java/br/ufc/teste/";
	
	private static Class getClassFromFile(String fullClassName) throws Exception {
	    URLClassLoader loader = new URLClassLoader(new URL[] {
	            new URL("file://" + CLASS_FOLDER)
	    });
	    return loader.loadClass(fullClassName);
	}
	
	public static void main(String[] args) {
		VisitorClasses cm = new VisitorClasses();
		Visi v = new Visi();
		File f = new File("src/main/java/br/ufc/teste/Test.java");	
		
		CompilationUnit cu;
		try {
			
			//ParameterVisitor p = new ParameterVisitor();
			//p.setPosition(1);
			
			ClassParameter p = new ClassParameter();
			
			cu = JavaParser.parse(f);
			v.visit(cu, null);
			
			//System.out.println(p.toString());
			
			//Integer i = new VisitorCount().genericCount(cu);
			
			//Random ran = new Random();
			
			//System.out.println(i);
			//System.out.println(ran.nextInt(i-1));
			
			//Path fw = Paths.get("src/main/java/br/ufc/teste/Test.java");
			
			//Files.write(fw, cu.toString().getBytes());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public static class Visi extends ModifierVisitor<Void>{
		@Override
		public Visitable visit(CatchClause n, Void arg) {
			// TODO Auto-generated method stub
			System.out.println(n.getParameter());
			System.out.println(n.getParameter().getName());
			System.out.println(n.getParameter().getType());
			System.out.println(n.getParameter().getType().getElementType());
			System.out.println(n.getParameter().getParsed());
			
			return super.visit(n, arg);
		}
	}
}
