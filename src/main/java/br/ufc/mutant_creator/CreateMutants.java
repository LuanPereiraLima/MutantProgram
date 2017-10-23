package br.ufc.mutant_creator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

import org.apache.commons.io.FileUtils;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;

public class CreateMutants {
	
	public static String USER_REFERENCE_TO_PROJECT = "/home/luan/mutacoes/";
	public static String HOME_MAVEN =  "/usr";
	public static String PROJECT_NAME = "forMutant";
	public static String PROJECT_PATH = USER_REFERENCE_TO_PROJECT+PROJECT_NAME;
	
	public static Integer changeNumber = 1;
	
	private static float qtdMutant = 0;
	private static float qtdMutantDead = 0;
	
	public static String COPY_PROJECT_PATH;
	public static String PROJECT_PATH_JAVA = PROJECT_PATH+"/src/main/java";
	
	public static boolean changeClass = false;

    public static void main(String[] args) {
    	VisitorFBD cm = new VisitorFBD();
    	listAndModifierFilesJava(cm);
    }
    
    public static void listAndModifierFilesJava(MyModifierVisitor cm) {
    	
    	File source = new File(PROJECT_PATH_JAVA);
    	System.out.println("Gerando mutantes nas classes...");
    	String [] tiposDeArquivo = new String[] { "java" };
    
    	for(File f : FileUtils.listFiles(source, tiposDeArquivo, true)) {
    		
    		System.out.println(f.getName());
    		
		 	CompilationUnit cu;
			try {
				cu = JavaParser.parse(f);
				
		        Random ram = new Random();
		        
		        int qtd = cm.countTimes(cu);
		        		
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
		        		
	        	Util.createACopyMutantTest();
	        	qtdMutant++;
	        	
	        	String novoCaminho = f.toString().replace(PROJECT_NAME, PROJECT_NAME+"-"+(changeNumber-1));
	        	
	        	System.out.println("Caminho antigo: "+f.toString());
	        	System.out.println("Novo caminho: "+novoCaminho);
	        	
		        Path fw = Paths.get(novoCaminho);
		        
		        Files.write(fw, cu.toString().getBytes());
		    	int result = TesteInvoker.testInvoker(fw);
		    	
		    	createResult(fw, result, parameterVisitor);
		        
			    //break;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    	finalResult();
    }
    
    //PRINTANDO O RESULTADO NO ARQUIVO
    public static void finalResult(){
    	System.out.println("----------------");
    	float resultFrac = qtdMutantDead/qtdMutant;
    	System.out.println("Quantidade de Mutants: "+qtdMutant);
    	System.out.println("Quantidade de Mutants Mortos: "+qtdMutantDead);
    	System.out.println("Quantidade de Mutants Vivos: "+(qtdMutant-qtdMutantDead));
    	System.out.println("Fração de Mutants / Mutants Mortos: "+ resultFrac);
    	System.out.println("----------------");
    	
    	String pathResultFile = USER_REFERENCE_TO_PROJECT+"resultAll.txt";
    	
    	Path fw = Paths.get(pathResultFile);
        
    	String escritaArquivo = "Mutações realizadas: Deixando todos os blocos catch vazios, para cada classe.";
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
    
    public static void createResult(Path path, int result, ParameterVisitor parameter) {
    	String pathResultFile = PROJECT_PATH+"-"+(changeNumber-1)+"/result.txt";
    	
    	Path fw = Paths.get(pathResultFile);
        
    	String writeFile = "true";
    	if(result!=0) {
    		writeFile = "false"; 
     		qtdMutantDead++;
    	}
    	
    	writeFile+="\n"+path;
    	
    	writeFile+="\n\n"+parameter;
    	
        try {
			Files.write(fw, writeFile.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}