package br.ufc.teste;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;

import br.ufc.mutant_creator.ParameterVisitor;
import br.ufc.mutant_creator.VisitorCBD;
import br.ufc.mutant_creator.VisitorCount;

public class Main {
	public static void main(String[] args) {
		TestVisitorCatch cm = new TestVisitorCatch();
		VisitorCBD visitorCBD = new VisitorCBD();
		
		File f = new File("src/main/java/br/ufc/teste/Test.java");
		
		CompilationUnit cu;
		try {
			
			ParameterVisitor p = new ParameterVisitor();
			p.setPosition(2);
			
			cu = JavaParser.parse(f);
			visitorCBD.visit(cu, p);
			
			System.out.println(p.toString());
			
			//Integer i = new VisitorCount().genericCount(cu);
			
			//Random ran = new Random();
			
			//System.out.println(i);
			//System.out.println(ran.nextInt(i-1));
			
			Path fw = Paths.get("src/main/java/br/ufc/teste/Test.java");
			
			Files.write(fw, cu.toString().getBytes());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        
	}
}
