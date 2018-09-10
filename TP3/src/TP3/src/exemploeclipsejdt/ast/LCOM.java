package exemploeclipsejdt.ast;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.compiler.IScanner;
import org.eclipse.jdt.core.compiler.ITerminalSymbols;
import org.eclipse.jdt.core.compiler.InvalidInputException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

public class LCOM extends ASTVisitor {

	private Map<String, HashSet<String>> compartilhaAtributoporMetodo;
	private Map<String, HashSet<String>> naoCompartilhaAtributos;
	private Map<String, HashSet<String>> componentesConectados;
	private Double lcomValue;
	private String lcomType;
	private CompilationUnit fullClass;
	private ICompilationUnit un;
	private String className;
	private String nameMethod;

	public LCOM(ICompilationUnit unit) {
		super();
		this.className = unit.getParent().getElementName() + "."
				+ unit.getElementName().substring(0, unit.getElementName().length() - 5);

		ASTParser parser = ASTParser.newParser(AST.JLS10);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(unit);
		parser.setResolveBindings(true);

		this.fullClass = (CompilationUnit) parser.createAST(null); // parse
		this.fullClass.accept(this);
		compartilhaAtributoporMetodo = new HashMap<>();
		
		naoCompartilhaAtributos = new HashMap<>();
		componentesConectados = new HashMap<>();
		this.lcomValue = 0d;
	}

	public enum LCOMType {
		LCOM
	}

	public void getMethods(ICompilationUnit unit) {

		try {
			/*
			 * IMethod[] methods = null; IField[] fields = null; IType[] type =
			 * unit.getTypes(); for (IType iType : type) { methods = iType.getMethods();
			 * fields = iType.getFields(); }
			 * 
			 * for (IMethod me : methods) { //
			 * System.out.println(me.getNumberOfParameters()); //conta quantidade de //
			 * atributos da classe // System.out.println(me.getSource()); //pega o código
			 * fonte do método // System.out.println(me.getFlags()); //
			 * System.out.println(me.getElementName()); //pega o nome dos métodos //
			 * System.out.println("getKey"+me.getKey()); //
			 * System.out.println("getreturnType"+me.getReturnType()); //
			 * System.out.println("getParameterType"+me.getParameterTypes()); //
			 * System.out.println("getKey"+me.getRawParameterNames()); //
			 * System.out.println("getJavadocRange"+me.getJavadocRange().toString());
			 * 
			 * }
			 * 
			 * for (IField fd : fields) { // System.out.println(fd.getElementName()); //pega
			 * atributos da classe // System.out.println(fd.getDeclaringType()); //pega
			 * declaração da classe // System.out.println("getConstrant"+fd.getConstant());
			 * }
			 */
			
			un = unit;
			IMethod[] iMethods = null;
			IField[] iFields = null;
			IType[] iTypes = unit.getTypes();			
			for (IType iType : iTypes) {
				iMethods = iType.getMethods();
				iFields = iType.getFields();				
			}

			if ((iFields != null && iMethods != null) && (iFields.length > 1 && iMethods.length > 1)) {
				for (IField field : iFields) {					
					compartilhaAtributoporMetodo.put(field.getElementName(), new HashSet<String>());
					naoCompartilhaAtributos.put(field.getElementName(), new HashSet<String>());
					//atributos.add(field.getElementName());
				}
				for (IMethod method : iMethods) {
					IMember member =  method.getDeclaringType();
					System.out.println(member.getElementName()); 
					componentesConectados.put(method.getElementName(), new HashSet<String>());
				}
				checkMetedoCompartilhaAtributos(iMethods);

				if (LCOMType.LCOM.toString() == getLcomType()) {
					setLcomValue(calculateLCOMValue());
				}
			}

		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public boolean visit(FieldDeclaration node) {
		//System.out.println(node.modifiers().toString()); 
		//System.out.println(node.fragments().toString());
		//System.out.println(" fielddeclaration");		
		return true;
	}
	
	
	public boolean visit(MethodDeclaration node) {				
		Block md = node.getBody();
		//System.out.println(md.getParent().toString());
		List <Block> bloskd = md.statements();		
		SimpleName sn = node.getName();
		System.out.println("MethodDeclaration "+ sn.getIdentifier());  //indentifier do method
		//VariableDeclarationFragment vf = (VariableDeclarationFragment) md.statements();
		//System.out.println(vf.getName());  
	
		return true; 
	}
	
	
	public boolean visit(SimpleName node) {

	    IBinding binding = node.resolveBinding();	    

	    if (binding instanceof IVariableBinding) {

	        IVariableBinding variable = (IVariableBinding) binding;
	        
	        if (variable.isField()) {    	        	
	          //System.out.println("Method:"+((IVariableBinding) binding).getDeclaringMethod().getName());	
	          //System.out.println("field: " + node.toString());
	        }
	    }

	    return super.visit(node);
	}
	
	public boolean visit(VariableDeclarationStatement node) {		
		//System.out.println(node.getType());		
		//System.out.println(node.getParent());		
		return true; 
	}
	
	public boolean visit(VariableDeclarationFragment node) {
		//System.out.println("key:"+node.resolveBinding().getKey()); 
		//System.out.println("fieldacess "+node.getInitializer().FIELD_ACCESS);
		//System.out.println("GetName "+ node.getName());		
		//System.out.println("GetType "+ node.getNodeType());
		//System.out.println("GetNodeType "+ node.getName().getNodeType());
		//System.out.println("GetFlags "+ node.getName().getFlags());
		//System.out.println("GetRelsoveType"+node.resolveBinding().getType().toString());
		//System.out.println("GetFlags"+node.getFlags());
		//System.out.println("GetResolveKind"+node.resolveBinding().getKind());
		//System.out.println("GetResolveModifiers"+node.resolveBinding().getModifiers());	
		SimpleName sn = node.getName();
		System.out.println("VariableDeclarationFragnment "+sn);
		return true;
		
	}
	
	public boolean visit(TypeDeclaration node) {
	 //System.out.println(" TypeDeclaration");	
	  return true;	
	}
	
	
	public boolean visit(VariableDeclaration node) {
		//System.out.println(" variable");		
		return true;		
	}
	
	@Override
	public boolean visit(CatchClause node) { // visita todo os catchs
		// System.out.println("visitei o catch"+ node.getStartPosition() +" - ");
		AST reast = node.getAST();
		Block body;
		body = node.getBody();
		List<Statement> stm = body.statements(); // pega o corpo do catch
		if (stm.isEmpty()) { // verifica se está vazio.
			//System.out.println("catch está vázio!");
		} else {
			for (Statement st : stm) {
				//System.out.println(st.toString());
			}
		}

		return true;
	}

	public void checkMetedoCompartilhaAtributos(IMethod[] methods) {
		IScanner scanner = null;
		for (IMethod method : methods) {
			String methodName = method.getElementName();

			try {
				scanner = ToolFactory.createScanner(false, false, false, false);
				scanner.setSource(method.getSource().toCharArray());
				while (true) {
					int charactere = scanner.getNextToken();
					if (charactere == ITerminalSymbols.TokenNameEOF)
						break;
					if (charactere == ITerminalSymbols.TokenNameIdentifier) {
						addMethods(new String(scanner.getCurrentTokenSource()), methodName);
					}
				}
			} catch (JavaModelException exception1) {
				System.out.println(exception1);
			} catch (InvalidInputException exception2) {
				System.out.println(exception2);
			}

		}
	}

	private void addMethods(String field, String method) {
		Set<String> metodosCompartilhados = null;
		// if (LCOMType.LCOM.toString() == getLcomType() || LCOMType.LCOM2.toString() ==
		// getLcomType()){
		if (compartilhaAtributoporMetodo.containsKey(field)) {
			metodosCompartilhados = compartilhaAtributoporMetodo.get(field);
			this.addMethod(method, metodosCompartilhados);
		} else {
			Set<String> nonSharedMethods = naoCompartilhaAtributos.get(field);
			this.addMethod(method, nonSharedMethods);
		}
		// }else{
		// if(connectedComponents.containsKey(field)){
		// sharedMethods = connectedComponents.get(field);
		// addMethod(method, sharedMethods);
		// }
		// }
	}

	public void addMethod(String method, Set<String> methodSet) {
		if (methodSet != null)
			methodSet.add(method);
	}

	private Double calculateLCOMValue() {

		Set<String> metodosCompartilha = new HashSet<>();
		Set<String> metodosNaoCompartilha = new HashSet<>();

		for (Iterator<HashSet<String>> it = compartilhaAtributoporMetodo.values().iterator(); it.hasNext();) {
			Set<String> methods = it.next();
			metodosCompartilha.addAll(methods);
		}

		for (Iterator<HashSet<String>> it = naoCompartilhaAtributos.values().iterator(); it.hasNext();) {
			Set<String> methods = it.next();
			metodosNaoCompartilha.addAll(methods);
		}

		Double index = Double.valueOf(metodosCompartilha.size()) - Double.valueOf(metodosNaoCompartilha.size());

		if (metodosCompartilha.size() < metodosNaoCompartilha.size())
			return 0d;

		return index;
	}

	public String lcom() {
		return calculateLCOMValue().toString();
	}

	public void setLcomValue(Double lcomValue) {
		this.lcomValue = lcomValue;
	}

	public String getLcomType() {
		return lcomType;
	}
}
