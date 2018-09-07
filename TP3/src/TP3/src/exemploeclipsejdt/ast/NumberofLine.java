
package exemploeclipsejdt.ast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Comment;
import org.eclipse.jdt.core.dom.CompilationUnit;

public class NumberofLine extends ASTVisitor {

	private CompilationUnit fullClass;
	private String className;
	private Double numberOfLinesOfCode;

	public NumberofLine(ICompilationUnit unit) throws Exception {

		this.className = unit.getParent().getElementName() + "."
				+ unit.getElementName().substring(0, unit.getElementName().length() - 5);
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(unit);
		parser.setResolveBindings(true);

		this.fullClass = (CompilationUnit) parser.createAST(null); // parse
		this.fullClass.accept(this);

	}

	// return the lines of class
	public final String getLOC() {
		return this.numberOfLinesOfCode.toString();
	}
	

	/**
	 * @see ASTVisitor#visit(CompilationUnit)
	 */
	@Override
	public boolean visit(CompilationUnit cu) {
		List<Comment> commentList = cu.getCommentList();
		for (Comment comment : commentList) {
			comment.delete();
		}
		//count the line of code of class 
		this.numberOfLinesOfCode = (double) cu.toString().split("\n").length;
		return false;
	}

}
