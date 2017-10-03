package br.ufc.mutant_creator;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class Util {
	
    //MÉTODO UTILIZADO PARA CRIAR A CÓPIA DO PROJETO NA QUAL SERÁ O MUTANTE
    public static void createACopyMutantTest(){
    	File source = new File(MethodChanger.PROJECT_PATH);
    	
    	MethodChanger.COPY_PROJECT_PATH = MethodChanger.PROJECT_PATH+"-"+(MethodChanger.changeNumber++);
    	
    	File dest = new File(MethodChanger.COPY_PROJECT_PATH);
    	try {
    		System.out.println("Gerando copia do arquivo...");
    	    FileUtils.copyDirectory(source, dest);
    	} catch (IOException e) {
    	    e.printStackTrace();
    	}
    }
}
