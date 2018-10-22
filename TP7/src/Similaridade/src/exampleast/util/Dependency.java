package exampleast.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.NameQualifiedType;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.QualifiedType;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

public class Dependency {

	private static Dependency dependency;
	protected Dependency () {}
	public static Dependency getInstance() {
		if(dependency == null) {
			dependency = new Dependency();
		}
		return dependency;
	}

	public Set<String> getTypeDepencies(ICompilationUnit unit) throws JavaModelException {
		Set<String> typesDepencies = new HashSet<>();
		Type superClass;

		VisitorAST visitor = new VisitorAST(unit);
		visitor.getCompUnit().accept(visitor);
		
		Set<MethodDeclaration> methods = visitor.getMethods();
		
		for (MethodDeclaration m : methods) {
			typesDepencies.addAll(getTypeDependencies(m.getBody()));
			typesDepencies.addAll(getTypeDepencies(m.parameters()));
		}
		
		Set<TypeDeclaration> typeDeclarations = visitor.getDeclaration();
//		ArrayList<TypeDeclaration> typeDeclarations = declarations;

		for (TypeDeclaration t : typeDeclarations) {
			superClass = t.getSuperclassType();
			if (!(superClass == null)) {
				typesDepencies.add(superClass.toString() + "+");
				System.out.println("SuperClass:    " + superClass + "  of " + unit.getElementName());
			}

			for (FieldDeclaration f : t.getFields()) {
				addDependencieSet(f.getType(), typesDepencies);
			}
		}
		return typesDepencies;
	}

	// pega as dependências de um determinado bloco
	public Set<String> getTypeDependenciesBodyStatement(Statement statement) {
		if (statement instanceof Block) {
			return getTypeDependencies((Block) statement);
		} // adiciona as dependências de uma declaração de variável
		if (statement instanceof VariableDeclarationStatement) {
			Set<String> typesDependencies = new HashSet<>();
			VariableDeclarationStatement variableDeclaration = ((VariableDeclarationStatement) statement);
			addDependencieSet(variableDeclaration.getType(), typesDependencies);
			return typesDependencies;
		}
		return new HashSet<>();
	}

	public Set<String> getTypeDependencies(Block block) {
		Set<String> typesDepencies = new HashSet<>();
		for (Statement statement : (List<Statement>) block.statements()) {
			if (statement instanceof Block) {
				typesDepencies.addAll(getTypeDependencies(block));
			} else if (statement instanceof DoStatement) { // adiciona as dependências de um bloco Do WHILE
				typesDepencies.addAll(getTypeDependenciesBodyStatement(((DoStatement) statement).getBody()));
			} else if (statement instanceof WhileStatement) {
				typesDepencies.addAll(getTypeDependenciesBodyStatement(
						((WhileStatement) statement).getBody())); 
				// adiciona dependências de um  bloco WHILE
			} else if (statement instanceof EnhancedForStatement) {
				typesDepencies.addAll(getTypeDependenciesBodyStatement((
						(EnhancedForStatement) statement).getBody()));
				// adiciona as dependências de um  bloco FOR EACH
			} else if (statement instanceof ForStatement) {
				typesDepencies.addAll(getTypeDependenciesBodyStatement(((ForStatement) statement).getBody()));
				// adiciona e um bloco FOR
			} else if (statement instanceof IfStatement) {
				IfStatement ifStatement = (IfStatement) statement; // adiciona as dependências de um bloco IF
				typesDepencies.addAll(getTypeDependenciesBodyStatement(ifStatement.getThenStatement()));
				Statement elseStatement = ifStatement.getElseStatement();
				if (elseStatement != null) { // adiciona as dependências de um bloco ELSE
					typesDepencies.addAll(getTypeDependenciesBodyStatement(elseStatement));
				}
			} else if (statement instanceof SwitchStatement) { // adiciona as dependências de um bloco SWITCH
				for (Statement st : (List<Statement>) (((SwitchStatement) statement).statements())) {
					typesDepencies.addAll(getTypeDependenciesBodyStatement(st));
				}
			} else if (statement instanceof VariableDeclarationStatement) {
				addDependencieSet(((VariableDeclarationStatement) statement).getType(), typesDepencies);
			}

		}
		return typesDepencies;
	}

	// adiciona as dependências em um conjunto para ser comparadas
	public void addDependencieSet(Type type, Set<String> dependencies) {
		if (type instanceof ParameterizedType) {
			ParameterizedType paramType = (ParameterizedType) type;
			for (Type t : (List<Type>) paramType.typeArguments()) {
				addDependencieSet(t, dependencies);
			}
			addNameOfTypes(paramType.getType(), dependencies);
		}
		addNameOfTypes(type, dependencies);
	}

	public void addNameOfTypes(Type type, Set<String> types) {

		if (type instanceof SimpleType) {
			types.add(((SimpleType) type).getName().getFullyQualifiedName());
		} else if (type instanceof QualifiedType) {
			types.add(((QualifiedType) type).getName().getFullyQualifiedName());
		} else if (type instanceof NameQualifiedType) {
			types.add(((NameQualifiedType) type).getName().getFullyQualifiedName());
		}
	}

	// Retorna um tipo de dependência a ser comparada
	public Set<String> getTypeDepencies(List<SingleVariableDeclaration> parameters) {
		Set<String> typesDepencies = new HashSet<>();
		for (SingleVariableDeclaration param : parameters) {
			addDependencieSet(param.getType(), typesDepencies);
		}
		return typesDepencies;
	}
	public void verifyDependenciesOfSuperClass(String class1, String class2, Map<String, Set<String>> dependencias) throws Exception {

		boolean aSuperClassOfb = false;
		boolean bSuperClassOfa = false;
		String array1[] = new String[2];
		String array2[] = new String[2];
		array1 = class1.split("[.]");
		array2 = class2.split("[.]");

		String newClass1 = array1[0];
		String newClass2 = array2[0];
		
		for (String s : dependencias.get(class1)) {
			if ((s.contains(newClass2)) && (s.contains("+"))) {
				bSuperClassOfa = true;
			}
		}
		for (String s2 : dependencias.get(class2)) {
			if ((s2.contains(newClass1)) && (s2.contains("+"))) {
				aSuperClassOfb = true;
			}
		}

		if (aSuperClassOfb && bSuperClassOfa) {
			throw new Exception("Erro na dependências das classes: " + class1 + "e " + class2);
		} else {
			if (aSuperClassOfb) {
				// System.out.println(newClass1 + "super OOOOOOOOF " + newClass2);
				Set<String> aux = dependencias.get(class1);
				Set<String> aux2 = dependencias.get(class2);
				aux2.addAll(aux);
				dependencias.put(class2, aux2);
			}
			if (bSuperClassOfa) {
				// System.out.println(class2 + "super OOOOOOF " + newClass1);
				Set<String> aux = dependencias.get(class2);
				Set<String> aux2 = dependencias.get(class1);
				aux2.addAll(aux);
				dependencias.put(class1, aux2);
				for (String s : dependencias.get(class1)) {
					// System.out.print(s + " ");
				}
			}
		}
	}

}
