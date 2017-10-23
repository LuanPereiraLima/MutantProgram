package br.ufc.mutant_creator;

public class ParameterVisitor {
	private Integer beginLine;
	private Integer beginLineModifier;
	private Integer endLine;
	private Integer endLineModifier;
	private Integer position;
	private String before;
	private String after;
	
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
	@Override
	public String toString() {
		return  "  \nbeginLine=" + beginLine + 
				", \n\nendLine=" + endLine + 
				//", \n\nbeginLineModifier=" + beginLineModifier + 
				//", \n\nendLineModifier=" + endLineModifier + 
				", \n\nposition=" + position + 
				", \n\nCode before=\n" + before + 
				", \n\nCode after=\n" + after + 
				"\n";
	}
}
