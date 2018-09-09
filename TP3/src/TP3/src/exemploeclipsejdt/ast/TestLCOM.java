package exemploeclipsejdt.ast;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.compiler.IScanner;
import org.eclipse.jdt.core.compiler.ITerminalSymbols;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;

public class TestLCOM extends ASTVisitor {
	private Map<String, HashSet<String>> sharedAttributesPerMethods;
	private Map<String, HashSet<String>> nonSharedAttributesPerMethods;
	private Map<String, HashSet<String>> connectedComponents;
	private Double lcomValue;
	private Double lcom2Value;
	private Double lcom4Value;
	private String lcomType;
	private CompilationUnit fullClass;
	private ICompilationUnit un;
	private String className;

	public TestLCOM(ICompilationUnit unit) {
		super();
		this.className = unit.getParent().getElementName() + "."
				+ unit.getElementName().substring(0, unit.getElementName().length() - 5);

		ASTParser parser = ASTParser.newParser(AST.JLS10);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(unit);
		parser.setResolveBindings(true);

		this.fullClass = (CompilationUnit) parser.createAST(null); // parse
		this.fullClass.accept(this);
		sharedAttributesPerMethods = new HashMap<>();
		nonSharedAttributesPerMethods = new HashMap<>();
		connectedComponents = new HashMap<>();

		this.lcomValue = 0d;
		this.lcom2Value = 0d;
		this.lcom4Value = 0d;

		this.lcomValue = 0d;
		this.setLcomType("LCOM");
		calculateValue(unit);
	}
	
	public enum LCOMType {
		LCOM,
		LCOM2,
		LCOM4;
	}

	public void calculateValue(ICompilationUnit unit) {

		IMethod[] iMethods = null;
		IField[] iFields = null;
		try {
			IType[] iTypes = unit.getTypes();

			for (IType iType : iTypes) {
				iMethods = iType.getMethods();
				iFields = iType.getFields();
			}

			if ((iFields != null && iMethods != null) && (iFields.length > 1 && iMethods.length > 1)) {
				for (IField field : iFields) {
					sharedAttributesPerMethods.put(field.getElementName(), new HashSet<String>());
					nonSharedAttributesPerMethods.put(field.getElementName(), new HashSet<String>());
				}
				for (IMethod method : iMethods) {
					connectedComponents.put(method.getElementName(), new HashSet<String>());
				}
				checkMethodsWithSharedAttributes(iMethods);

				if (LCOMType.LCOM.toString() == getLcomType()) {
					setLcomValue(calculateLCOMValue());
				} else if (LCOMType.LCOM2.toString() == getLcomType()) {
					setLcom2Value(calculateLCOM2Value());
				} else {
					setLcom4Value(calculateLCOM4Value());
				}
			}
		} catch (Exception exception) {

		}
	}

	private void addMethods(String field, String method) {
		Set<String> sharedMethods = null;
		if (LCOMType.LCOM.toString() == getLcomType() || LCOMType.LCOM2.toString() == getLcomType()) {
			if (sharedAttributesPerMethods.containsKey(field)) {
				sharedMethods = sharedAttributesPerMethods.get(field);
				addMethod(method, sharedMethods);
			} else {
				Set<String> nonSharedMethods = nonSharedAttributesPerMethods.get(field);
				addMethod(method, nonSharedMethods);
			}
		} else {
			if (connectedComponents.containsKey(field)) {
				sharedMethods = connectedComponents.get(field);
				addMethod(method, sharedMethods);
			}
		}

	}

	public void addMethod(String method, Set<String> methodSet) {
		if (methodSet != null)
			methodSet.add(method);
	}

	private void checkMethodsWithSharedAttributes(IMethod[] methods) {

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
			} catch (Exception exception1) {
				System.out.println(exception1.getMessage());
			}
		}

	}

	private Double calculateLCOMValue() {

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

		if (allSharedMethods.size() < allNonSharedMethods.size())
			return 0d;

		return index;
	}

	private Double calculateLCOM2Value() {

		Set<String> allSharedMethods = new HashSet<>();

		for (Iterator<HashSet<String>> it = sharedAttributesPerMethods.values().iterator(); it.hasNext();) {
			Set<String> methods = it.next();
			allSharedMethods.addAll(methods);
		}

		Double index = Double.valueOf(allSharedMethods.size()) / 2;
		Double entrySize = Double.valueOf(sharedAttributesPerMethods.keySet().size()) * allSharedMethods.size();

		if (entrySize < 0)
			return 0d;
		Double result = (1 - index / entrySize);
		if (result.isInfinite() || result.isNaN())
			return 0d;

		return result;
	}

	private Double calculateLCOM4Value() {
		return new BigDecimal(sharedAttributesPerMethods.size(), new MathContext(2, RoundingMode.UP)).doubleValue();
	}

	public  Map<String, HashSet<String>> getResult(){
		return sharedAttributesPerMethods;
	}
	
	public void cleanMapsAndVariables() {
		sharedAttributesPerMethods.clear();
		nonSharedAttributesPerMethods.clear();
		connectedComponents.clear();

		this.lcomValue = 0d;
		this.lcom2Value = 0d;
		this.lcom4Value = 0d;
	}

	public String getLcomType() {
		return lcomType;
	}

	public void setLcomType(String lcomType) {
		this.lcomType = lcomType;
	}

	public void setLcomValue(Double lcomValue) {
		this.lcomValue = lcomValue;
	}

	public void setLcom2Value(Double lcom2Value) {
		this.lcom2Value = lcom2Value;
	}

	public void setLcom4Value(Double lcom4Value) {
		this.lcom4Value = lcom4Value;
	}

	public Double getLcomValue() {
		return lcomValue;
	}

	public Double getLcom2Value() {
		return lcom2Value;
	}

	public Double getLcom4Value() {
		return lcom4Value;
	}
}
