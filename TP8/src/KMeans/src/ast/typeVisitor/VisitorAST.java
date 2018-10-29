package ast.typeVisitor;

import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class VisitorAST extends ASTVisitor {

	private Set<MethodDeclaration> methods ;
	private Set<TypeDeclaration> declarations;
	private CompilationUnit compUnit;
	
	public VisitorAST(ICompilationUnit unit) throws JavaModelException {
		
		ASTParser parser = ASTParser.newParser(AST.JLS10);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(unit);
		compUnit = (CompilationUnit) parser.createAST(null);
		compUnit.recordModifications();
		this.declarations = new LinkedHashSet<TypeDeclaration>();		
		this.methods = new LinkedHashSet<MethodDeclaration>();
		
		
	}
	
	public boolean visit(TypeDeclaration node) {
		declarations.add(node);
		return super.visit(node);
	}
	public boolean visit(MethodDeclaration node) {
		methods.add(node);
		return super.visit(node);
	}
	public Set<TypeDeclaration> getDeclaration() {
		return declarations;
	}
	public Set<MethodDeclaration> getMethods(){
		return methods;
	}

	public CompilationUnit getCompUnit() {
		return compUnit;
	}

	
	

	
}
