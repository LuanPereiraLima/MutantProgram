package br.ufc.teste;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import br.ufc.mutant_creator.CreateMutants;
import br.ufc.mutant_creator.VisitorClasses;

public class ClassUtil {
	
	private static List<String> getSubTypesAux(String name, List<ClassParameter> classes) {
		System.out.println("-----------\n"+name);
		List<String> listaClasses = new ArrayList<String>();
		for(ClassParameter c : classes) {
			for(ClassOrInterfaceType ci : c.getListExtends()) {
				
				//System.out.println("Name Bafore: " + ci.getName().asString());
				String nameClass = returnNamedWithPackageIfNecessary(ci.getName().asString(), c);
				//System.out.println("Name After: " + nameClass);
				
				System.out.println("----------\n"+nameClass);
				//System.out.println("-----------\n"+name);
				if(nameClass.equals(name)){
					System.out.println("Classe: "+c.getName());
					System.out.println("Classe Comparação: "+name);
					System.out.println("Lista de Imports: "+c.getListImports());
					System.out.println("Package: "+c.getPkg());
					System.out.println("Lista de Extends: "+c.getListExtends());
					//System.out.println(c.getListImports().get(0).getName().asString());
					System.out.println("-----------");
					
					listaClasses.add(c.getPkg()+"."+c.getName());
					break;
				}
			}
		}
		return listaClasses;
	}
	
	private static String returnNamedWithPackageIfNecessary(String className, ClassParameter c){
		
		if(className.contains("."))
			return className;
			
		for(ImportDeclaration id: c.getListImports()){
			//System.out.println("Import Declaration: "+id.getName().asString());
			if(id.getName().asString().lastIndexOf(className)>-1)
				return id.getName().asString();
		}
		
		for(ImportDeclaration id: c.getListImports()){
			String nameImport = id.getName().asString();
			if(nameImport.lastIndexOf("*")>-1){
				Map map = VerificarPackage.generatePathFileList("", new File(CreateMutants.PROJECT_PATH_JAVA));
				Iterator it = map.entrySet().iterator();
				while (it.hasNext()) {
					String pack = (String)it.next();
					//System.out.println("EOQ: "+nameImport.substring(0, nameImport.length()-2));
					if(pack.equals(nameImport.substring(0, nameImport.length()-2))){
						Iterator fileNamesIt = ((FileList)
								map.get(pack)).getFilesNames().iterator();
						
						while (fileNamesIt.hasNext()) {
							String fileName = (String) fileNamesIt.next();
							if((className+".class").equals(fileName)){
								return pack+"."+className;
							}
                      		//System.out.println("--> " + fileName);
						}
					}
				}
			}
		}
		
		return c.getPkg()+"."+className;
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
