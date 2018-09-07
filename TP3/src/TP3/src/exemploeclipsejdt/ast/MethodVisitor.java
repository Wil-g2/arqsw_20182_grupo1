package exemploeclipsejdt.ast;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class MethodVisitor extends ASTVisitor {

	List<TypeDeclaration> methods = new ArrayList<>();
	
	
	public List<TypeDeclaration> getMethods() {
		return methods;
	}

	public boolean visit(TypeDeclaration node) {
		methods.add(node);
		return super.visit(node);
		
	}
	
	
}
