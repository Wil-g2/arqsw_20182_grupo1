package exemploeclipsejdt.ast;

import java.util.HashMap;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import exemploeclipsejdt.ast.LCOM.LCOMType;
import org.json.*;
public class Refactor extends ASTVisitor{

	private Map<String, String> methodsClass;
	private Map<String, HashSet<String>> analazerExtractClass;	
	private CompilationUnit fullClass;
	private String className;
	private String metedoatual;
	JSONObject json = new JSONObject();
	JSONArray jsonArrayPerAttributes,jsonArrayAttributes, jsonArrayMethods;
	String lastMethod=null;
	 	
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
		//analazerExtractClass = new HashMap<>();		
		json.put("class",this.className);
		json.put("methods", jsonArrayMethods); 
	}

	
	public boolean visit(MethodDeclaration node) {	
		json.put("attributes", jsonArrayAttributes); 
		SimpleName sn = node.getName();
		System.out.println("MethodDeclaration "+ sn.getIdentifier());  //identifier do method	
		//methodsClass.put(sn.getIdentifier().toString(), node.getName().toString());
		metedoatual=sn.getIdentifier();		
		jsonArrayMethods.put(sn.getIdentifier().toString());
		//VariableDeclarationFragment vdf = (VariableDeclarationFragment) mt.getParent(); 
		//System.out.println(vdf.getName()); 
		
		return true; 
	}
			
	public boolean visit(VariableDeclarationFragment node) {						
		SimpleName sn = node.getName();
		String attribute;
		attribute = sn.getIdentifier();
		//HashSet<String> attributes = new HashSet<>();
 		if (metedoatual != null) { 		 			 
 			if ((lastMethod.equals(null)) ||(lastMethod.equals(metedoatual))) {
 				jsonArrayPerAttributes.put(attribute);
 			}else {
 				jsonArrayPerAttributes = new JSONArray();
 				jsonArrayPerAttributes.put(attribute);
 			}
 			lastMethod = metedoatual; 	//		
 		}else { 		
 			
 			jsonArrayAttributes.put(attribute); 			
 		}
		return true;
		
	}
	
	public void endVisit(MethodDeclaration node) {		
		SimpleName sn = node.getName();		
		json.put(metedoatual, jsonArrayPerAttributes);
		//jsonArray = new JSONArray();
	}
	
	public boolean visit(FieldAccess node) {
		//System.out.println("fieldAccess:"+node.getName().getIdentifier());
		return true; 
	}
	
	public String getResult() {
		return json.toString();
	}
}
