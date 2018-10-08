package exemploeclipsejdt.handlers;

import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.text.TableView;
import javax.xml.ws.handler.MessageContext;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.ISourceReference;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.NameQualifiedType;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.QualifiedType;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import exemploeclipsejdt.ast.VerifySingleton;
import exemploeclipsejdt.ast.VisitorUtil;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class SampleHandler extends AbstractHandler {
	/**
	 * The constructor.
	 */
	public SampleHandler() {
	}

	/**
	 * the command has been executed, so extract extract the needed information from
	 * the application context.
	 */	
	private String pkgModel = JOptionPane.showInputDialog("Entre com pacote Model ?");
	private String PkgView = JOptionPane.showInputDialog("Entre com pacote View ?");;
	private String pkgPresenter = JOptionPane.showInputDialog("Entre com pacote Presenter ?");	

	Map<String, IPackageFragment> pkgInformation = new HashMap<>();

	StringBuilder log = new StringBuilder();

	/*
	 * @Override public Object execute(ExecutionEvent event) throws
	 * ExecutionException {
	 * 
	 * IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
	 * IWorkbenchPart workbenchPart =
	 * PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
	 * .getActivePart(); IFile file = null; try { file = (IFile)
	 * workbenchPart.getSite().getPage().getActiveEditor().getEditorInput().
	 * getAdapter(IFile.class); } catch (NullPointerException e) {
	 * MessageDialog.openInformation(window.getShell(), "Padrão arquitetural",
	 * "Nenhum arquivo aberto."); return null; }
	 * 
	 * IWorkspace workspace = ResourcesPlugin.getWorkspace(); // raiz IWorkspaceRoot
	 * root = workspace.getRoot(); // pega todos projetos IProject[] projetos =
	 * root.getProjects(); for (IProject projeto : projetos) { // percorre todos
	 * projetos do eclipse try { if (projeto.isAccessible() & projeto.isOpen()) { //
	 * verifica se o projeto está acessível e aberto IJavaProject jprojeto =
	 * JavaCore.create(projeto); IPackageFragmentRoot[] pkgs =
	 * jprojeto.getPackageFragmentRoots(); // arquivos do projeto
	 * jprojeto.getAllPackageFragmentRoots(); IPackageFragment[] teste =
	 * jprojeto.getPackageFragments(); // contém as classes java
	 * 
	 * for (IPackageFragment fg : teste) { // percorre os arquivos java
	 * ICompilationUnit[] unit = fg.getCompilationUnits(); // cria um arquivo para
	 * ser analisado // (ICompilationUnit contém todas // informações da classe) for
	 * (ICompilationUnit un : unit) {
	 * 
	 * } } } } catch (Exception e) {
	 * System.out.println("Erro ao tentar ler o projeto."); } } return null; }
	 */
	
	private Set<String> getPacksMVP() {
		return new HashSet<>(Arrays.asList(pkgModel, PkgView, pkgPresenter));
		
	}
	
	/**
	 * configuração das retrições do padrão MVP
	 * 
	 */
	private Map<String, Set<String>> getConstraints() {
		Map<String, Set<String>> constraints = new HashMap<>();
		constraints.put(PkgView, new HashSet<>(Arrays.asList(pkgModel)));
		//constraints.put(pkgPresenter, new HashMap<>(Arrays.asList()));
		constraints.put(pkgModel, new HashSet<>(Arrays.asList(PkgView, pkgPresenter)));
		return constraints;
	}
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		try {
			getWorkspaceInfo();
		} catch (Exception e) {
			MessageDialog.openInformation(window.getShell(), "ExemploEclipseJDTPlugin",
					"Problema na execução!");
			e.printStackTrace();
		}
		MessageDialog.openInformation(window.getShell(), "ExemploEclipseJDTPlugin", log.toString());
		log = new StringBuilder();
		return null;
	}

	/**
	 * Método para pegar informações do Workspace
	 * 
	 */
	private void getWorkspaceInfo() throws CoreException {

		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		IProject[] projects = root.getProjects();
		for (IProject project : projects) {
			if (project.isOpen() && project.isAccessible()) {
				IJavaProject javaProject = JavaCore.create(project);
				accessVerify(javaProject);
			}
		}

	}

	/**
	 * Método para validar dependências de acesso
	 * 
	 */
	private void accessVerify(IJavaProject javaProject) throws JavaModelException {
		Map<String, IPackageFragment> projectInfo = getInfoProject(javaProject);
		Set<String> pakgsAnalysis = getPacksMVP();
		Map<String, Set<String>> constraints = getConstraints();
		for (IPackageFragment p : javaProject.getPackageFragments()) {
			if (pakgsAnalysis.contains(p.getElementName())) {
				String analyzed = p.getElementName();
				log.append(String.format("Análise do pacote '%s'\n", analyzed));
				Set<String> currentRestriction = constraints.get(analyzed);
				boolean violation = false;
				for (ICompilationUnit unit : p.getCompilationUnits()) {
					Set<String> typeDependencies = getTypeDepencies(unit);
					for (String dependency : typeDependencies) {
						if (!projectInfo.containsKey(dependency)) {
							continue;
						}
						String currentPack = projectInfo.get(dependency).getElementName();
						if (currentRestriction.contains(currentPack)) {
							violation = true;
							log.append(String.format("pacote:'%s' - Classe '%s' acessa a seguinte classe '%s'.!\n",
									currentPack, unit.getElementName(), dependency));
						}
					}

				}
				if (!violation) {
					log.append("Sem violação \n ");
				}
				
			}
		}

	}

	/**
	 * Mapeamento de informações do projeto
	 * 
	 */
	private Map<String, IPackageFragment> getInfoProject(IJavaProject javaProject) throws JavaModelException {

		IPackageFragment[] packages = javaProject.getPackageFragments();

		for (IPackageFragment p : packages) {
			if (p.getKind() == IPackageFragmentRoot.K_SOURCE) {

				Set<String> typeDeclaration = getTypeDeclarations(p);
				for (String c : typeDeclaration) {
					pkgInformation.put(c, p);
				}

			}
		}
		return pkgInformation;

	}

	/**
	 * Retorna um compilation unit
	 * 
	 */
	private CompilationUnit getComplilationUnit(ICompilationUnit source) throws JavaModelException {
		Document document = new Document(source.getSource());
		ASTParser parser = ASTParser.newParser(AST.JLS9);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(source);
		CompilationUnit compUnit = (CompilationUnit) parser.createAST(null);
		return compUnit;
	}

	/**
	 * Analisa os tipo de declação da classe
	 * 
	 */
	private ArrayList<TypeDeclaration> typeDeclarationParser(ICompilationUnit unit) throws JavaModelException {
		CompilationUnit compUnit = getComplilationUnit(unit);
		compUnit.recordModifications(); // modificação do registro
		AST ast = compUnit.getAST();
		VisitorUtil visitor = new VisitorUtil();
		compUnit.accept(visitor);
		ArrayList<TypeDeclaration> declarations = visitor.getListType();
		return declarations;

	}

	private void addTypeDeclarationName(TypeDeclaration typeDeclaration, Set<String> typeDeclarations) {
		typeDeclarations.add(typeDeclaration.getName().getFullyQualifiedName());
		for (TypeDeclaration t : typeDeclaration.getTypes()) {
			addTypeDeclarationName(t, typeDeclarations);
		}
	}

	/**
	 * declaração de variável
	 * 
	 */
	private Set<String> getTypeDeclarations(IPackageFragment packageFragment) throws JavaModelException {
		Set<String> typeDeclarations = new HashSet<>();
		if (packageFragment.getKind() == IPackageFragmentRoot.K_SOURCE) {
			for (ICompilationUnit compilationUnit : packageFragment.getCompilationUnits()) {
				for (TypeDeclaration typeDeclaration : typeDeclarationParser(compilationUnit)) {
					addTypeDeclarationName(typeDeclaration, typeDeclarations);
				}
			}
		}
		return typeDeclarations;
	}

	
	/**
	 *tipos de depenência de uma classe 
	 * 
	 */
	private Set<String> getTypeDepencies(ICompilationUnit unit) throws JavaModelException {
		Set<String> typesDepencies = new HashSet<>();
		ArrayList<MethodDeclaration> methods = methodsParser(unit);
		for (MethodDeclaration m : methods) {			
			typesDepencies.addAll(getTypeDependencies(m.getBody()));
			typesDepencies.addAll(getTypeDepencies(m.parameters()));
		}
		ArrayList<TypeDeclaration> typeDeclarations = typeDeclarationParser(unit);
		for (TypeDeclaration t : typeDeclarations) {
			for (FieldDeclaration f : t.getFields()) {
				addDependencieSet(f.getType(), typesDepencies);
			}
		}
		return typesDepencies;
	}

	void addNameOfTypes(Type type, Set<String> types) {
		if (type instanceof SimpleType) {
			types.add(((SimpleType) type).getName().getFullyQualifiedName());
		} else if (type instanceof QualifiedType) {
			types.add(((QualifiedType) type).getName().getFullyQualifiedName());
		} else if (type instanceof NameQualifiedType) {
			types.add(((NameQualifiedType) type).getName().getFullyQualifiedName());
		}
	}

	/**
	 * comparação do tipo de dependência
	 * 
	 */
	Set<String> getTypeDepencies(List<SingleVariableDeclaration> parameters) {
		Set<String> typesDepencies = new HashSet<>();
		for (SingleVariableDeclaration param : parameters) {
			addDependencieSet(param.getType(), typesDepencies);
		}
		return typesDepencies;
	}

	/**
	 *conjunto de dependências a serem comparados  
	 * 
	 */
	void addDependencieSet(Type type, Set<String> dependencies) {
		if (type instanceof ParameterizedType) {
			ParameterizedType paramType = (ParameterizedType) type;
			for (Type t : (List<Type>) paramType.typeArguments()) {
				addDependencieSet(t, dependencies);
			}
			addNameOfTypes(paramType.getType(), dependencies);
		}
		addNameOfTypes(type, dependencies);
	}

	/**
	 * obtem as dependências de um bloco
	 * 
	 */
	Set<String> getTypeDependenciesBodyStatement(Statement statement) {
		if (statement instanceof Block) {
			return getTypeDependencies((Block) statement);
		} 
		if (statement instanceof VariableDeclarationStatement) {
			Set<String> typesDependencies = new HashSet<>();
			VariableDeclarationStatement variableDeclaration = ((VariableDeclarationStatement) statement);
			addDependencieSet(variableDeclaration.getType(), typesDependencies);
			return typesDependencies;
		}
		return new HashSet<>();
	}

	Set<String> getTypeDependencies(Block block) {
		Set<String> typesDepencies = new HashSet<>();
		for (Statement statement : (List<Statement>) block.statements()) {
			if (statement instanceof Block) {
				typesDepencies.addAll(getTypeDependencies(block));
			} else if (statement instanceof DoStatement) { 
				typesDepencies.addAll(getTypeDependenciesBodyStatement(((DoStatement) statement).getBody()));
			} else if (statement instanceof WhileStatement) {
				typesDepencies.addAll(getTypeDependenciesBodyStatement(((WhileStatement) statement).getBody())); 
			} else if (statement instanceof EnhancedForStatement) {
				typesDepencies.addAll(getTypeDependenciesBodyStatement(((EnhancedForStatement) statement).getBody()));
			} else if (statement instanceof ForStatement) {
				typesDepencies.addAll(getTypeDependenciesBodyStatement(((ForStatement) statement).getBody())); 
			} else if (statement instanceof IfStatement) {
				IfStatement ifStatement = (IfStatement) statement; 
				typesDepencies.addAll(getTypeDependenciesBodyStatement(ifStatement.getThenStatement()));
				Statement elseStatement = ifStatement.getElseStatement();
				if (elseStatement != null) { 
					typesDepencies.addAll(getTypeDependenciesBodyStatement(elseStatement));
				}
			} else if (statement instanceof SwitchStatement) { 
				for (Statement st : (List<Statement>) (((SwitchStatement) statement).statements())) {
					typesDepencies.addAll(getTypeDependenciesBodyStatement(st));
				}
			} else if (statement instanceof VariableDeclarationStatement) {
				addDependencieSet(((VariableDeclarationStatement) statement).getType(), typesDepencies);
			}

		}
		return typesDepencies;
	}

	private ArrayList<MethodDeclaration> methodsParser(ICompilationUnit unit) throws JavaModelException {		
		CompilationUnit compUnit = getComplilationUnit(unit);
		compUnit.recordModifications();// modificação do registro
		AST ast = compUnit.getAST();
		VisitorUtil visitor = new VisitorUtil();
		compUnit.accept(visitor);
		ArrayList<MethodDeclaration> methods = visitor.getMethods();
		return methods;

	}

}
