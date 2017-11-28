package br.ufc.teste;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import br.ufc.mutant_creator.CreateMutants;

public class VerificarPackage {
    private static Map files = null;

    public static void main(String[] args) {
    	VerificarPackage lp = new VerificarPackage();
        lp.printPathFileList("/home/loopback/junit4/src/main/java");
    }

    public void printPathFileList(String initialPath) {
            File file = new File(initialPath);
            generatePathFileList("", file);
            if (files != null) {
                    Iterator it = files.keySet().iterator();
                    while (it.hasNext()) {
	                    String folderName = (String) it.next();
	                    System.out.println("Listing directory [" + folderName + "]");
	                    System.out.println("======================================");
	                    Iterator fileNamesIt = ((FileList)
	                    files.get(folderName)).getFilesNames().iterator();
	                    while (fileNamesIt.hasNext()) {
	                            String fileName = (String) fileNamesIt.next();
	                            System.out.println("--> " + fileName);
	                    }
	                    System.out.println("");
                    }
            }
    }

    public static Map generatePathFileList(String currentFolder, File file) {
    		
    		//if(currentFolder==null) currentFolder = CreateMutants.PROJECT_PATH_JAVA;
    	
    		if(files!=null)
    			return files;
    		
            String[] f = file.list();
            for (int x = 0; x < f.length; x++) {
                    File vfile = new File(file.getAbsolutePath() + "/" + f[x]);
                    if (vfile.isDirectory()) {
                            generatePathFileList((currentFolder.equals("") ? "" :
currentFolder.concat(".")) + f[x], vfile);
                    } else {
                            // is file
                            if (files == null) {
                                    files = new TreeMap();
                            }
                            if (files.get(currentFolder) != null) {
                                    ((FileList) files.get(currentFolder)).addFileName(f[x]);
                            } else {
                                    files.put(currentFolder, new FileList(f[x]));
                            }
                    }
            }
            return files;
    }
}
class FileList {
    private Set filesNames = new HashSet();

    public FileList(String fileName) {
            filesNames.add(fileName);
    }

    public void addFileName(String fileName) {
            filesNames.add(fileName);
    }

    public Set getFilesNames() {
            return filesNames;
    }
}
