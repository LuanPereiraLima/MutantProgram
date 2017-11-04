package br.ufc.teste;

import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

public class ClassParameter {
	private String name;
	private String pkg;
	private List<ClassOrInterfaceType> listExtends;	
	private List<ImportDeclaration> listImports;	
	
	
	public ClassParameter() {
		listExtends = new ArrayList<ClassOrInterfaceType>();
		listImports = new ArrayList<ImportDeclaration>();
	}

	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getPkg() {
		return pkg;
	}


	public void setPkg(String pkg) {
		this.pkg = pkg;
	}


	public List<ClassOrInterfaceType> getListExtends() {
		return listExtends;
	}


	public void setListExtends(List<ClassOrInterfaceType> listExtends) {
		this.listExtends = listExtends;
	}
	
	public List<ImportDeclaration> getListImports() {
		return listImports;
	}
	
	public void setListImports(List<ImportDeclaration> listImports) {
		this.listImports = listImports;
	}

	@Override
	public String toString() {
		return "ClassParameter [name=" + name + ", pkg=" + pkg + ", listExtends=" + listExtends + ", listImports="
				+ listImports + "]";
	}

	
}
