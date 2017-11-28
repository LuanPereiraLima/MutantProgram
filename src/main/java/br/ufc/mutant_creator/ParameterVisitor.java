package br.ufc.mutant_creator;

import br.ufc.teste.ClassParameter;

public class ParameterVisitor {
	private ClassParameter classParameter;
	private Integer beginLine;
	private Integer beginLineModifier;
	private Integer endLine;
	private Integer endLineModifier;
	private Integer position;
	private String before;
	private String after;
	private boolean needModification = true;
	
	public Integer getBeginLine() {
		return beginLine;
	}
	public void setBeginLine(Integer beginLine) {
		this.beginLine = beginLine;
	}
	public Integer getBeginLineModifier() {
		return beginLineModifier;
	}
	public void setBeginLineModifier(Integer beginLineModifier) {
		this.beginLineModifier = beginLineModifier;
	}
	public Integer getEndLine() {
		return endLine;
	}
	public void setEndLine(Integer endLine) {
		this.endLine = endLine;
	}
	public Integer getEndLineModifier() {
		return endLineModifier;
	}
	public void setEndLineModifier(Integer endLineModifier) {
		this.endLineModifier = endLineModifier;
	}
	public Integer getPosition() {
		return position;
	}
	public void setPosition(Integer position) {
		this.position = position;
	}
	public String getBefore() {
		return before;
	}
	public void setBefore(String before) {
		this.before = before;
	}
	public String getAfter() {
		return after;
	}
	public void setAfter(String after) {
		this.after = after;
	}
	
	public ClassParameter getClassParameter() {
		return classParameter;
	}
	
	public void setClassParameter(ClassParameter classParameter) {
		this.classParameter = classParameter;
	}
	
	public boolean isNeedModification() {
		return needModification;
	}
	
	public void setNeedModification(boolean needModification) {
		this.needModification = needModification;
	}
	
	@Override
	public String toString() {
		return  "\nbeginLine=" + beginLine + 
				", \n\nendLine=" + endLine + 
				//", \n\nbeginLineModifier=" + beginLineModifier + 
				//", \n\nendLineModifier=" + endLineModifier + 
				", \n\nposition=" + position + 
				", \n\nCodeBefore=\n" + before + 
				", \n\nCodeAfter=\n" + after + 
				", \n\nNeed modification=\n" + needModification + 
				"\n";
	}
}
