package br.ufc.mutant_creator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.InvocationResult;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class MethodChanger {
	
	private static String USER_REFERENCE_TO_PROJECT = "/home/luan/mutacoes/";
	private static String HOME_MAVEN =  "/usr";
	private static String PROJECT_NAME = "forMutant";
	private static String PROJECT_PATH = USER_REFERENCE_TO_PROJECT+PROJECT_NAME;
	
	private static Integer changeNumber = 1;
	
	private static Integer qtdMutant = 0;
	private static Integer qtdMutantDead = 0;
	
	private static String COPY_PROJECT_PATH;
	private static String PROJECT_PATH_JAVA = PROJECT_PATH+"/src/main/java";
	
	private static List<String> GOALS_PROJECT = Arrays.asList( "install" );
	
	private static boolean changeClass = false;

    public static void main(String[] args) throws Exception {
    	listFilesJava();
    }
    
    public static void listFilesJava() {
    	
    	File source = new File(PROJECT_PATH_JAVA);
    	System.out.println("Gerando mutantes nas classes...");
    	String [] tiposDeArquivo = new String[] { "java" };
    	for(File f : FileUtils.listFiles(source, tiposDeArquivo, true)) {
    		
    		System.out.println(f.getName());
    		
    		//System.out.println(f.toString());
		 	CompilationUnit cu;
			try {
				cu = JavaParser.parse(f);
	
		        MethodChangerVisitor cm = new MethodChangerVisitor();
		        cm.visit(cu, null);
	
		        if(changeClass) {
		        		
		        	createACopyMutantTest();
		        	qtdMutant++;
		        	
		        	String novoCaminho = f.toString().replace(PROJECT_NAME, PROJECT_NAME+"-"+(changeNumber-1));
		        	
		        	System.out.println("Caminho antigo: "+f.toString());
		        	System.out.println("Novo caminho: "+novoCaminho);
		        	
		        	//System.out.println(cu);
			       
			        Path fw = Paths.get(novoCaminho);
			        
			        Files.write(fw, cu.toString().getBytes());
			    	testInvoker(fw);	
			    	
			        changeClass = false;
		        }
		        //break;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    //MÉTODO UTILIZADO PARA CRIAR A CÓPIA DO PROJETO NA QUAL SERÁ O MUTANTE
    public static void createACopyMutantTest(){
    	File source = new File(PROJECT_PATH);
    	
    	COPY_PROJECT_PATH = PROJECT_PATH+"-"+(changeNumber++);
    	
    	File dest = new File(COPY_PROJECT_PATH);
    	try {
    		System.out.println("Gerando copia do arquivo...");
    	    FileUtils.copyDirectory(source, dest);
    	} catch (IOException e) {
    	    e.printStackTrace();
    	}
    }
    
    //MÉTODO UTILIZADO PARA REALIZAR O TESTE NO MUTANTE CRAIDO
    public static void testInvoker(Path path) {
    	try {
			Invoker invoker = new DefaultInvoker();
			invoker.setMavenHome( new File( HOME_MAVEN ) );
			
			InvocationRequest request = new DefaultInvocationRequest();
			request.setPomFile( new File( COPY_PROJECT_PATH ) );
			request.setGoals( GOALS_PROJECT );
			
			InvocationResult resultado = invoker.execute( request );
			
			System.out.println("Resultado: "+resultado.getExitCode());
			
			createResult(path, resultado.getExitCode());
			
		} catch (MavenInvocationException e) {
			e.printStackTrace();
		}
    }
    
    public static void createResult(Path path, int result) {
    	String pathResultFile = PROJECT_PATH+"-"+(changeNumber-1)+"/result.txt";
    	
    	Path fw = Paths.get(pathResultFile);
        
    	String escritaArquivo = "true";
    	if(result!=0) {
     		escritaArquivo = "false"; 
     		qtdMutantDead++;
    	}
    	
    	escritaArquivo+="\n"+path.getFileName();
    	
        try {
			Files.write(fw, escritaArquivo.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    //CLASSE USADA PARA NAVEGAR PELOS CATCH'S' DA CLASSE VISITADA.
    private static class MethodChangerVisitor extends VoidVisitorAdapter<Void> {
        @Override
        public void visit(CatchClause n, Void arg) {
        	System.out.println("---------CATCH ENCONTRADO: ");
        	System.out.println(n.getBody());
        	System.out.println("---------");
        	n.setBody(new BlockStmt());
        	changeClass = true;
        	super.visit(n, arg);
        }
    }
}