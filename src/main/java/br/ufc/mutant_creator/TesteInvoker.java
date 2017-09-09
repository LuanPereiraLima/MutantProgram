package br.ufc.mutant_creator;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;

public class TesteInvoker {
	
	private static String HOME_MAVEN =  "C:/apache-maven";
	private static String EXECUTABLE_MAVEN =  "C:/apache-maven";
	private static String PATH_PROJECT = "C:/Users/LuanLima/Desktop/junit4-master";
	private static List<String> GOALS_PROJECT = Arrays.asList( "install" );
	
	
	public static void main(String[] args) {
		try {
			Invoker invoker = new DefaultInvoker();
			invoker.setMavenExecutable( new File( EXECUTABLE_MAVEN ) );
			invoker.setMavenHome( new File( HOME_MAVEN ) );
			
			InvocationRequest request = new DefaultInvocationRequest();
			request.setPomFile( new File( PATH_PROJECT ) );
			request.setGoals( GOALS_PROJECT );
			 
			invoker.execute( request );
		} catch (MavenInvocationException e) {
			e.printStackTrace();
		}
	}

}
