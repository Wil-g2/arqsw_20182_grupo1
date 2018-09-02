package exemploeclipsejdt.ast;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;

public class NumberOfAttributes extends ASTVisitor{
	private Double numberOfAttributes;	
	private List<String> listAttributes;
	private CompilationUnit fullClass;
	private String className;
	
	public NumberOfAttributes(ICompilationUnit unit) throws Exception {
		this.listAttributes = new ArrayList<>();
		this.numberOfAttributes = 0.0;
		this.className = unit.getParent().getElementName() + "."
				+ unit.getElementName().substring(0, unit.getElementName().length() - 5);
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(unit);
		parser.setResolveBindings(true);

		this.fullClass = (CompilationUnit) parser.createAST(null); // parse
		this.fullClass.accept(this);

	}
	
	/**
	 * @see ASTVisitor#visit(FieldDeclaration)
	 */
	@Override
	public boolean visit(FieldDeclaration variable) {
		listAttributes.add(variable.toString()); //get all attributes of class  
		numberOfAttributes++;                    //count attributes of class
		return false;
	}

	public Double getNumberOfAttributes(){
		return new BigDecimal(numberOfAttributes, new MathContext(2, RoundingMode.UP)).doubleValue();
	}
	
	//return list of attributes 
	public List<String> attributes() {
		return listAttributes;
	}
	
	public void cleanVariable(){
		this.numberOfAttributes = 0d;
	}
}
