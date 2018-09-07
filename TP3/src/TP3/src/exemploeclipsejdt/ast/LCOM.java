package exemploeclipsejdt.ast;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.plaf.synth.SynthSeparatorUI;
import javax.swing.plaf.synth.SynthSplitPaneUI;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
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

public class LCOM extends ASTVisitor{

	
	private Map<String, HashSet<String>> sharedAttributesPerMethods;
	private Map<String, HashSet<String>> nonSharedAttributesPerMethods;
	private Map<String, HashSet<String>> connectedComponents;
	private Double lcomValue;
	private String lcomType;
	
	public LCOM(ICompilationUnit unit) {
		super();
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(unit);
		parser.setResolveBindings(true);
		
		sharedAttributesPerMethods = new HashMap<>();
		nonSharedAttributesPerMethods = new HashMap<>();
		connectedComponents = new HashMap<>();		
		this.lcomValue = 0d;
	}
	
	public enum LCOMType {
		LCOM
	}
	
	public void getMethods(ICompilationUnit unit) {
		
		try {
			/*IMethod[] methods = null;
			IField[] fields = null; 
			IType [] type = unit.getTypes();
			for (IType iType : type){
				methods = iType.getMethods();
				fields = iType.getFields();
			}
			
			for (IMethod me: methods) {
				//System.out.println(me.getNumberOfParameters()); //conta quantidade de atributos da classe
				//System.out.println(me.getSource());   //peda o código fonte do método
				//System.out.println(me.getFlags());
				System.out.println(me.getElementName()); //pega o nome dos métodos
				System.out.println("getKey"+me.getKey());
				System.out.println("getreturnType"+me.getReturnType());
				System.out.println("getParameterType"+me.getParameterTypes());
				System.out.println("getKey"+me.getRawParameterNames());				
				
			}
			
			for (IField fd: fields) {
				//System.out.println(fd.getElementName());   //pega atributos da classe 
				//System.out.println(fd.getDeclaringType()); //pega declaração da classe				
			}*/
			IMethod[] iMethods = null;
			IField[] iFields = null;
			IType[] iTypes = unit.getTypes();
			
			for (IType iType : iTypes){
				iMethods = iType.getMethods();
				iFields = iType.getFields();
			}
			
			if ((iFields != null && iMethods != null) && (iFields.length > 1 && iMethods.length > 1)) {
				for (IField field: iFields){
					sharedAttributesPerMethods.put(field.getElementName(), new HashSet<String>());
					nonSharedAttributesPerMethods.put(field.getElementName(), new HashSet<String>());
				}
				for (IMethod method: iMethods){
					connectedComponents.put(method.getElementName(), new HashSet<String>());
				}
				checkMethodsWithSharedAttributes(iMethods);
				
				if (LCOMType.LCOM.toString() == getLcomType()){
					setLcomValue(calculateLCOMValue());
				}
			}
			
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
					
	}
	

	public void checkMethodsWithSharedAttributes(IMethod[] methods) {
		IScanner scanner = null;
		for (IMethod method : methods) {
			String methodName = method.getElementName();
			
			try {
				scanner = ToolFactory.createScanner(false, false, false, false);
				scanner.setSource(method.getSource().toCharArray());
				while(true){
					int charactere = scanner.getNextToken();
					if (charactere == ITerminalSymbols.TokenNameEOF) break;
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
		Set<String> sharedMethods = null;
		//if (LCOMType.LCOM.toString() == getLcomType() || LCOMType.LCOM2.toString() == getLcomType()){
			if(sharedAttributesPerMethods.containsKey(field)){
				sharedMethods = sharedAttributesPerMethods.get(field);
				addMethod(method, sharedMethods);
			}else{
				Set<String> nonSharedMethods = nonSharedAttributesPerMethods.get(field);
				addMethod(method, nonSharedMethods);
			}
		//}else{
		//	if(connectedComponents.containsKey(field)){
		//		sharedMethods = connectedComponents.get(field);
        //		addMethod(method, sharedMethods);
		//	}
		//}		
	}
	

	public void addMethod(String method, Set<String> methodSet) {
		if (methodSet != null)
			methodSet.add(method);
	}
	
	
private Double calculateLCOMValue(){
		
		Set<String> allSharedMethods = new HashSet<>();
		Set<String> allNonSharedMethods = new HashSet<>();
		
		for (Iterator<HashSet<String>> it = sharedAttributesPerMethods.values().iterator(); it.hasNext();) {
			Set<String> methods = it.next();
			allSharedMethods.addAll(methods);
		}
		
		for (Iterator<HashSet<String>> it = nonSharedAttributesPerMethods.values().iterator(); it.hasNext();) {
			Set<String> methods = it.next();
			allNonSharedMethods.addAll(methods);
		}
		
		Double index = Double.valueOf(allSharedMethods.size()) - Double.valueOf(allNonSharedMethods.size());
		
		if (allSharedMethods.size() < allNonSharedMethods.size()) return 0d;
		
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

