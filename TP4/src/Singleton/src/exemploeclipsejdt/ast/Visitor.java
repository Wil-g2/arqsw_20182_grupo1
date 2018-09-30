package exemploeclipsejdt.ast;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class Visitor extends ASTVisitor{
	
	List<TypeDeclaration> listTypes = new ArrayList<TypeDeclaration>();
	
	public boolean visit(TypeDeclaration node){
		listTypes.add(node);
		return super.visit(node);		
	}
	
	public List<TypeDeclaration> getList(){
		return listTypes;
	}
}
