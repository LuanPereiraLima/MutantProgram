package br.ufc.mutant_creator;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.InvocationResult;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;

public class TesteInvoker {
	
	public static List<String> GOALS_PROJECT = Arrays.asList( "test" );
	private static String HOME_MAVEN =  "/usr";
	
	//MÉTODO UTILIZADO PARA REALIZAR O TESTE NO MUTANTE CRIADO
    public static int testInvoker(Path path, String copyProjectPath) {
    	try {
			Invoker invoker = new DefaultInvoker();
			invoker.setMavenHome( new File( HOME_MAVEN ) );
			
			InvocationRequest request = new DefaultInvocationRequest();
			request.setPomFile( new File( copyProjectPath ) );
			request.setGoals( GOALS_PROJECT );
			
			InvocationResult result = invoker.execute( request );
			
			return result.getExitCode();
			
		} catch (MavenInvocationException e) {
			e.printStackTrace();
			return 0;
		}
    }

}
