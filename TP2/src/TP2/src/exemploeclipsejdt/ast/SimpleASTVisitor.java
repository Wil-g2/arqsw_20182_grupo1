package exemploeclipsejdt.ast;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;

public class SimpleASTVisitor extends ASTVisitor {
	private List<String> dependencies;	
	private List<String> attributes;
	private List<String> parameters; 
	private CompilationUnit fullClass;
	private String className;
	
	public SimpleASTVisitor(ICompilationUnit unit) throws Exception {
		this.dependencies = new ArrayList<>();		
		this.attributes = new ArrayList<>();
		this.parameters = new ArrayList<>();
								
		this.className = unit.getParent().getElementName() + "."
				+ unit.getElementName().substring(0, unit.getElementName().length() - 5);
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(unit);
		parser.setResolveBindings(true);
						
		this.fullClass = (CompilationUnit) parser.createAST(null); // parse
		this.fullClass.accept(this);															
		
	}

	public final List<String> getDependencies() {
		return this.dependencies;
	}

	public final String getClassName() {
		return this.className;
	}
	
	public final String getAtributos() {
		return this.attributes.toString();		
	}
	
	public final Integer getNMC() {
		return this.dependencies.size();
		
	}
	
	@Override
	public boolean visit(MethodDeclaration node) {
		if (!node.isConstructor()){
			dependencies.add(node.getName().toString());
			parameters.addAll(node.parameters());
		}
		return true;
	}		
	
	public List<String> getParameters(){
		return parameters;
	}
		

}
