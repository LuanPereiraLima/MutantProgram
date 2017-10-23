package br.ufc.mutant_creator;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.ModifierVisitor;

public abstract class MyModifierVisitor extends ModifierVisitor<ParameterVisitor>{
	public abstract int countTimes(CompilationUnit cu);
}
