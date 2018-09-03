package exemploeclipsejdt.ast;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

public class CyclomaticComplexityVisitor extends ASTVisitor{

	private CompilationUnit fullClass;
	private String className;
	private Double cyclomaticComplexityIndex;
	private Double sumCyclomaticComplexity;	
	
	public CyclomaticComplexityVisitor(ICompilationUnit unit) throws Exception {		
		super();
		cyclomaticComplexityIndex = 0d;
		sumCyclomaticComplexity = 0d;
		
		this.className = unit.getParent().getElementName() + "."
				+ unit.getElementName().substring(0, unit.getElementName().length() - 5);
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(unit);
		parser.setResolveBindings(true);

		this.fullClass = (CompilationUnit) parser.createAST(null); // parse
		this.fullClass.accept(this);

	}

	@Override
	public boolean visit(CatchClause node) {
		cyclomaticComplexityIndex++;
		sumCyclomaticComplexity++;
		return true;
	}

	@Override
	public boolean visit(ForStatement node) {
		cyclomaticComplexityIndex++;
		sumCyclomaticComplexity++;
		inspectExpression(node.getExpression());
		return true;
	}

	@Override
	public boolean visit(IfStatement node) {
		cyclomaticComplexityIndex++;
		sumCyclomaticComplexity++;
		inspectExpression(node.getExpression());
		return true;
	}

	@Override
	public boolean visit(WhileStatement node) {
		cyclomaticComplexityIndex++;
		sumCyclomaticComplexity++;
		inspectExpression(node.getExpression());
		return true;
	}
	
	@Override
	public boolean visit(TryStatement node) {
		cyclomaticComplexityIndex++;
		sumCyclomaticComplexity++;
		return true;
	}

	@Override
	public boolean visit(ConditionalExpression node) {
		cyclomaticComplexityIndex++;
		sumCyclomaticComplexity++;
		inspectExpression(node.getExpression());
		return true;
	}

	@Override
	public boolean visit(SwitchCase node) {
		if (!node.isDefault()){
			cyclomaticComplexityIndex++;
			sumCyclomaticComplexity++;
		}
		return true;
	}

	@Override
	public boolean visit(DoStatement node) {
		cyclomaticComplexityIndex++;
		sumCyclomaticComplexity++;
		inspectExpression(node.getExpression());
		return true;
	}

	@Override
	public boolean visit(ExpressionStatement node) {
		cyclomaticComplexityIndex++;
		sumCyclomaticComplexity++;
		inspectExpression(node.getExpression());
		return false;
	}
	
	private void inspectExpression(Expression exprs) {
		if ((exprs != null)) {
			String expression = exprs.toString();
			char[] chars = expression.toCharArray();
			for (int i = 0; i < chars.length-1; i++) {
				char next = chars[i];
				if ((next == '&' || next == '|')&&(next == chars[i+1])) {
					cyclomaticComplexityIndex++;
					sumCyclomaticComplexity++;
				}
			}
		}
	}

	public Double getCyclomaticComplexityIndex(){
		return new BigDecimal(cyclomaticComplexityIndex, new MathContext(2, RoundingMode.UP)).doubleValue();
	}
	

	public Double getAllCyclomaticComplexity() {
		return sumCyclomaticComplexity;
	}
}
