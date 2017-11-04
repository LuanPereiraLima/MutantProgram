package br.ufc.mutant_creator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.FileUtils;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;

import br.ufc.teste.ClassParameter;
import br.ufc.teste.ClassUtil;

public class CreateMutants {
	
	public static String USER_REFERENCE_TO_PROJECT = "/home/luan/mutacoes/";
	public static String PROJECT_NAME = "forMutant";
	public static String PROJECT_PATH = USER_REFERENCE_TO_PROJECT+PROJECT_NAME;
	public static String PROJECT_PATH_JAVA = PROJECT_PATH+"/src/main/java";
	
	public static Integer changeNumber = 1;
	private static float qtdMutant = 0;
	private static float qtdMutantDead = 0;

    public static void main(String[] args) {
   //   	VisitorCBD cbd = new VisitorCBD();
    //	listAndModifierFilesJava(cbd);
    	//VisitorFBD fbd = new VisitorFBD();
  //  	listAndModifierFilesJava(fbd);
    	
    	System.out.println(ClassUtil.getSubTypes("Assert", PROJECT_PATH_JAVA));
    }

    public static void listAndModifierFilesJava(MyModifierVisitor cm) {
    	
    	resetResults();
    	
    	File source = new File(PROJECT_PATH_JAVA);
    	System.out.println("Gerando mutantes nas classes...");
    	String [] tiposDeArquivo = new String[] { "java" };
    
    	for(File f : FileUtils.listFiles(source, tiposDeArquivo, true)) {
    		
    		System.out.println(f.getName());
    		
		 	CompilationUnit cu;
			try {
				cu = JavaParser.parse(f);
				
		        Random ram = new Random();
		        
		        cm.resetPosition();
		        int qtd = cm.countTimes(cu);
		        
		        System.out.println("QUANTIDADDE: "+qtd);
		        		
		        if(qtd == 0) {
		        	System.out.println("Jump, no ocurrences");
		        	continue;
		        }
		        
		        int position = 1;
		        if(qtd > 1)
		        	position = ram.nextInt(qtd-1)+1;
		        
		        ParameterVisitor parameterVisitor = new ParameterVisitor();
		        
		        parameterVisitor.setPosition(position);
		        
		        cm.visit(cu, parameterVisitor);
		        
		    	String copyProjectPath = USER_REFERENCE_TO_PROJECT+cm.pathIdentification()+"/"+PROJECT_NAME+"-"+(CreateMutants.changeNumber++);
		        		
	        	Util.createACopyMutantTest(copyProjectPath);
	        	qtdMutant++;
	        	
	        	String novoCaminho = f.toString().
	        	replace(PROJECT_NAME, cm.pathIdentification()+"/"+PROJECT_NAME+"-"+(changeNumber-1));
	        	
	        	System.out.println("Caminho antigo: "+f.toString());
	        	System.out.println("Novo caminho: "+novoCaminho);
	        	
		        Path fw = Paths.get(novoCaminho);
		        
		        Files.write(fw, cu.toString().getBytes());
		    	int result = TesteInvoker.testInvoker(fw, copyProjectPath);
		    	
		    	createResult(fw, result, parameterVisitor, cm.pathIdentification());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    	finalResult(cm);
    }
    
    //PRINTANDO O RESULTADO NO ARQUIVO
    public static void finalResult(MyModifierVisitor modifierVisitor){
    	System.out.println("----------------");
    	float resultFrac = qtdMutantDead/qtdMutant;
    	System.out.println("Quantidade de Mutants: "+qtdMutant);
    	System.out.println("Quantidade de Mutants Mortos: "+qtdMutantDead);
    	System.out.println("Quantidade de Mutants Vivos: "+(qtdMutant-qtdMutantDead));
    	System.out.println("Fração de Mutants / Mutants Mortos: "+ resultFrac);
    	System.out.println("----------------");
    	
    	String pathResultFile = USER_REFERENCE_TO_PROJECT+modifierVisitor.pathIdentification()+"/"+"resultAll.txt";
    	
    	Path fw = Paths.get(pathResultFile);
        
    	String escritaArquivo = "Mutação realizada: "+modifierVisitor;
    	escritaArquivo+= "\n Quantidade de Mutantes: "+qtdMutant;
    	escritaArquivo+= "\n Quantidade de Mutants Mortos: "+qtdMutantDead;
    	escritaArquivo+= "\n Quantidade de Mutants Vivos: "+(qtdMutant-qtdMutantDead);
    	escritaArquivo+= "\n Fração de Mutants Mortos / Mutants: "+ resultFrac;
    	
        try { 
			Files.write(fw, escritaArquivo.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
  //PRINTANDO O RESULTADO INDIVIDUAL PARA CADA MUTANTE ARQUIVO
    public static void createResult(Path pathFile, int result, ParameterVisitor parameter, String namePath) {
    	String pathResultFile = USER_REFERENCE_TO_PROJECT+namePath+"/"+PROJECT_NAME+"-"+(changeNumber-1)+"/result.txt";
    	
    	Path fw = Paths.get(pathResultFile);
        
    	String writeFile = "mutantDead=false,";
    	if(result!=0) {
    		writeFile = "mutantDead=true,"; 
     		qtdMutantDead++;
    	}
    	
    	writeFile+="\n\npathFile="+pathFile+",";
    	
    	writeFile+="\n"+parameter;
    	
        try {
			Files.write(fw, writeFile.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    private static void resetResults() {
    	changeNumber = 1;
    	qtdMutant = 0;
    	qtdMutantDead = 0;
    }
}