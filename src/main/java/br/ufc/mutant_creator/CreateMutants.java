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

	public static Integer changeNumber = 1;
	private static float qtdMutant = 0;
	private static float qtdMutantDead = 0;

	public static void main(String[] args) {
		// VisitorCBD cbd = new VisitorCBD();
		// listAndModifierFilesJava(cbd);
		// VisitorFBD fbd = new VisitorFBD();
		// listAndModifierFilesJava(fbd);
		VisitorCRE vdbi = new VisitorCRE();
		listAndModifierFilesJava(vdbi);
		 //System.out.println(ClassUtil.getSubTypes("Throwable",
		 //ConstantsPath.PROJECT_PATH_JAVA));
	}

	public static void listAndModifierFilesJava(MyModifierVisitor cm) {

		resetResults();

		File source = new File(ConstantsPath.PROJECT_PATH_JAVA);
		System.out.println("Gerando mutantes nas classes...");
		String[] tiposDeArquivo = new String[] { "java" };

		for (File f : FileUtils.listFiles(source, tiposDeArquivo, true)) {

			System.out.println(f.getName());

			CompilationUnit cu;
			CompilationUnit cuAux;
			try {
				cu = JavaParser.parse(f);
				cuAux = JavaParser.parse(f);

				Random ram = new Random();

				cm.resetPosition();
				int qtd = cm.countTimes(cu);

				System.out.println("QTD: " + qtd);

				if (qtd == 0) {
					System.out.println("Jump, no ocurrences");
					continue;
				}

				int position = 1;
				if (qtd > 1)
					position = ram.nextInt(qtd - 1) + 1;

				ClassParameter classp = new ClassParameter();

				VisitorClasses vc = new VisitorClasses();
				vc.visit(cuAux, classp);

				ParameterVisitor parameterVisitor = new ParameterVisitor();

				parameterVisitor.setClassParameter(classp);
				parameterVisitor.setPosition(position);

				cm.visit(cu, parameterVisitor);
				
				System.out.println("OUT: \n"+ parameterVisitor);

				if (parameterVisitor.isNeedModification()) {
					System.out.println("Need Modification");
					String copyProjectPath = ConstantsPath.USER_REFERENCE_TO_PROJECT + cm.pathIdentification() + "/"
							+ ConstantsPath.PROJECT_NAME + "-" + (CreateMutants.changeNumber++);

					Util.createACopyMutantTest(copyProjectPath);
					qtdMutant++;

					String novoCaminho = f.toString().replace(ConstantsPath.PROJECT_NAME,
							cm.pathIdentification() + "/" + ConstantsPath.PROJECT_NAME + "-" + (changeNumber - 1));

					System.out.println("Caminho antigo: " + f.toString());
					System.out.println("Novo caminho: " + novoCaminho);

					Path fw = Paths.get(novoCaminho);

					Files.write(fw, cu.toString().getBytes());
					int result = TesteInvoker.testInvoker(fw, copyProjectPath);

					createResult(fw, result, parameterVisitor, cm.pathIdentification());
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		finalResult(cm);
	}

	// PRINTANDO O RESULTADO NO ARQUIVO
	public static void finalResult(MyModifierVisitor modifierVisitor) {
		System.out.println("----------------");
		float resultFrac = qtdMutantDead / qtdMutant;
		System.out.println("Quantidade de Mutants: " + qtdMutant);
		System.out.println("Quantidade de Mutants Mortos: " + qtdMutantDead);
		System.out.println("Quantidade de Mutants Vivos: " + (qtdMutant - qtdMutantDead));
		System.out.println("Fração de Mutants / Mutants Mortos: " + resultFrac);
		System.out.println("----------------");

		String pathResultFile = ConstantsPath.USER_REFERENCE_TO_PROJECT + modifierVisitor.pathIdentification() + "/"
				+ "resultAll.txt";

		Path fw = Paths.get(pathResultFile);

		String escritaArquivo = "Mutação realizada: " + modifierVisitor;
		escritaArquivo += "\n Quantidade de Mutantes: " + qtdMutant;
		escritaArquivo += "\n Quantidade de Mutants Mortos: " + qtdMutantDead;
		escritaArquivo += "\n Quantidade de Mutants Vivos: " + (qtdMutant - qtdMutantDead);
		escritaArquivo += "\n Fração de Mutants Mortos / Mutants: " + resultFrac;

		try {
			Files.write(fw, escritaArquivo.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// PRINTANDO O RESULTADO INDIVIDUAL PARA CADA MUTANTE ARQUIVO
	public static void createResult(Path pathFile, int result, ParameterVisitor parameter, String namePath) {
		String pathResultFile = ConstantsPath.USER_REFERENCE_TO_PROJECT + namePath + "/" + ConstantsPath.PROJECT_NAME
				+ "-" + (changeNumber - 1) + "/result.txt";

		Path fw = Paths.get(pathResultFile);

		String writeFile = "mutantDead=false,";
		if (result != 0) {
			writeFile = "mutantDead=true,";
			qtdMutantDead++;
		}

		writeFile += "\n\npathFile=" + pathFile + ",";

		writeFile += "\n" + parameter;

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