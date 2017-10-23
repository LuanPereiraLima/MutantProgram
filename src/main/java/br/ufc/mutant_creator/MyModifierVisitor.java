package br.ufc.mutant_creator;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.ModifierVisitor;

public abstract class MyModifierVisitor extends ModifierVisitor<ParameterVisitor>{
	protected Integer actualPosition = 0;
	public abstract int countTimes(CompilationUnit cu);
	public abstract String pathIdentification();
	public void resetPosition() {
		this.actualPosition=0;
	}
}
