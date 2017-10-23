package br.ufc.mutant_creator;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class Util {
	
    //MÉTODO UTILIZADO PARA CRIAR A CÓPIA DO PROJETO NA QUAL SERÁ O MUTANTE
    public static void createACopyMutantTest(){
    	File source = new File(CreateMutants.PROJECT_PATH);
    	
    	CreateMutants.COPY_PROJECT_PATH = CreateMutants.PROJECT_PATH+"-"+(CreateMutants.changeNumber++);
    	
    	File dest = new File(CreateMutants.COPY_PROJECT_PATH);
    	try {
    		System.out.println("Gerando copia do arquivo...");
    	    FileUtils.copyDirectory(source, dest);
    	} catch (IOException e) {
    	    e.printStackTrace();
    	}
    }
}
