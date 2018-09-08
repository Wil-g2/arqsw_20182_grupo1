package exemploeclipsejdt.ast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.internal.localstore.Bucket.Visitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.VariableDeclaration;

public class Teste extends ASTVisitor{
	
	private Map<String, String> methodsClass;
	private Map<String, String> analazerExtractClass;	
	private CompilationUnit fullClass;
	private String className;
	private String[][] analizer; 
	public List<String> teste;
	public Teste(ICompilationUnit unit) {
		super();
		this.className = unit.getParent().getElementName() + "."
				+ unit.getElementName().substring(0, unit.getElementName().length() - 5);

		ASTParser parser = ASTParser.newParser(AST.JLS10);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(unit);
		parser.setResolveBindings(true);
		this.fullClass = (CompilationUnit) parser.createAST(null); // parse
		this.fullClass.accept(this);

		methodsClass = new HashMap<>();
		analazerExtractClass = new HashMap<>();
	}
	
	
	public boolean visit(VariableDeclaration node) {
		teste.add(node.getName().toString());		
		return true;
	}


	public List<String> getTeste() {
		return teste;
	}
}
