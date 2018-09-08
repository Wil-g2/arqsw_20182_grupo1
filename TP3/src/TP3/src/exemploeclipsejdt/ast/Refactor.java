package exemploeclipsejdt.ast;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class Refactor extends ASTVisitor{

	private Map<String, String> methodsClass;
	private Map<String, String> analazerExtractClass;	
	private CompilationUnit fullClass;
	private String className;
	private String[][] analizer; 
	private String metedoatual;
	
	
	public Refactor(ICompilationUnit unit) {
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
	
	public boolean visit(MethodDeclaration node) {						
		SimpleName sn = node.getName();
		System.out.println("MethodDeclaration "+ sn.getIdentifier());  //identifier do method	
		//methodsClass.put(sn.getIdentifier().toString(), node.getName().toString());
		metedoatual=sn.getIdentifier();		
		//VariableDeclarationFragment vdf = (VariableDeclarationFragment) mt.getParent(); 
		//System.out.println(vdf.getName()); 
		
		return true; 
	}
			
	public boolean visit(VariableDeclarationFragment node) {						
		SimpleName sn = node.getName();		
		System.out.println("Method: "+this.metedoatual+ " Atributo:"+sn);
		//System.out.println("VariableDeclarationFragnment "+sn);		
		//attributesClass.put(sn.getIdentifier(),sn.toString());
		return true;
		
	}
}
