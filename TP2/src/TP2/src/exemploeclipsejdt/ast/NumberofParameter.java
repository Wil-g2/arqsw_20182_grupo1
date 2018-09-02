package exemploeclipsejdt.ast;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.internal.core.search.matching.MethodLocator;

public class NumberofParameter extends ASTVisitor {
	private int numberOfParameters;	
	private ArrayList methodsList;
	private CompilationUnit fullClass;
	private String className;
	
	public NumberofParameter(ICompilationUnit unit) throws Exception {		
		this.numberOfParameters = 0;
		methodsList = new ArrayList<>(); 
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
	private void calculateNumberOfMethods(CompilationUnit unit){
		for (Object type :unit.types()){
			if (type instanceof TypeDeclaration){
				
				MethodDeclaration [] methods = ((TypeDeclaration) type).getMethods();
				
				for (MethodDeclaration method: methods){
					 
					if (method.parameters().size()>2){ 
						this.methodsList.add(method.getName());
					}	
				}
			}
		}
	}
	
	public List<String> LongParameterList(){		
		return methodsList;		
	}
}
