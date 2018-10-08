package exemploeclipsejdt.ast;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class VisitorUtil extends ASTVisitor{
	
	ArrayList<TypeDeclaration> listTypes = new ArrayList<TypeDeclaration>();
	ArrayList<MethodDeclaration> methods = new ArrayList<MethodDeclaration>();
	
	@Override
	public boolean visit(TypeDeclaration node) {
		listTypes.add(node);
		return super.visit(node);
	}

	public ArrayList<TypeDeclaration> getListType() {
		return listTypes;
	}
		

	@Override
	public boolean visit(MethodDeclaration node) {
		methods.add(node);
		return super.visit(node);
	}

	public ArrayList<MethodDeclaration> getMethods() {
		return methods;
	}
	

}
