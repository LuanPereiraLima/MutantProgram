package br.ufc.teste;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import br.ufc.mutant_creator.MyModifierVisitor;
import br.ufc.mutant_creator.VisitorClasses;

public class ClassUtil {
	
	private static List<String> getSubTypesAux(String name, List<ClassParameter> classes) {
		List<String> listaClasses = new ArrayList<String>();
		for(ClassParameter c : classes) {
			for(ClassOrInterfaceType ci : c.getListExtends()) {
				if(ci.getName().asString().equals(name)){
					System.out.println("Classe: "+c.getName());
					System.out.println("Classe Comparação: "+name);
					System.out.println("Lista de Imports: "+c.getListImports());
					System.out.println("Package: "+c.getPkg());
					System.out.println("Lista de Extends: "+c.getListExtends());
					System.out.println("-----------");
					
					
					
					listaClasses.add(c.getName());
					break;
				}
			}
		}
		return listaClasses;
	}
	
	   
    public static List<String> getSubTypes(String className,String path_projeto) {
    	String [] tiposDeArquivo = new String[] { "java" };
    	File source = new File(path_projeto);
    	List<ClassParameter> cl = new ArrayList<ClassParameter>();
    	CompilationUnit cu;
    	for(File f : FileUtils.listFiles(source, tiposDeArquivo, true)) {
    		try {
    			ClassParameter c = new ClassParameter();
				cu = JavaParser.parse(f);
				VisitorClasses vc = new VisitorClasses();
				cu.accept(vc, c);
				cl.add(c);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
    	}
    	
    	return ClassUtil.getSubTypesAux(className, cl);
    }
}
