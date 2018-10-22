package exampleast.handlers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
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
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import exampleast.util.SimilarityData;
import exampleast.util.Dependency;
import exampleast.util.MoveClass;
import exampleast.util.VisitorAST;
import exampleast.util.Utils;
import exampleast.util.View;

public class SampleHandler extends AbstractHandler {
	/**
	 * The constructor.
	 */
	public SampleHandler() {
	}
	
	
	public Map<String, Set<ICompilationUnit>> getInformations() {
		return informations;
	}
	int ignore = 0;
	StringBuilder message = new StringBuilder();
	private String pack1 = JOptionPane.showInputDialog("Entre com pacote 1");
	private String pack2 = JOptionPane.showInputDialog("Entre com pacote 2");
	float weight = 0;
	Map<String, String> matrizInfo = new HashMap<>();
	ArrayList<String> classes = new ArrayList<>();
	Map<IPackageFragment, Set<ICompilationUnit>> estrutura = new HashMap<>();

	public static ArrayList<SimilarityData> dados;

	private Map<String, Set<String>> dependencies = new HashMap<>();

	Map<String, Set<ICompilationUnit>> informations = new HashMap<>();

	StringBuilder msg = new StringBuilder();

	private Set<String> getSetPackage() {
		return new HashSet<>(Arrays.asList(pack1, pack2));
	}

	public String getModel() {
		return pack1;
	}

	public String getViewPackage() {
		return pack2;
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		dados = new ArrayList<SimilarityData>();
		try {
			//View.getInstance().openView(countMSG);
			//hideView();
			if (ignore==0) {
				getWorkspaceInfo();
			}
			//openView();

		} catch (Exception e) {
			MessageDialog.openInformation(window.getShell(), "ExemploEclipseJDTPlugin",
					"Erro! Não foi possível executar o plug-in!");
			e.printStackTrace();
		}
		//MessageDialog.openInformation(window.getShell(), "ExemploEclipseJDTPlugin", msg.toString());

		MessageDialog.openInformation(window.getShell(), "ExemploEclipseJDTPlugin", message.toString());
		//Utils.getInstance().messagens(estrutura);
		ignore += 1; 
		return null;
	}

	// get workspace information 
	private void getWorkspaceInfo() throws Exception {

		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		IProject[] projects = root.getProjects();
		// iterate about all projects 
		for (IProject project : projects) {
			if (project.isOpen() && project.isNatureEnabled("org.eclipse.jdt.core.javanature")) {
				IJavaProject javaProject = JavaCore.create(project);
				getAllDependenciesOfClass(javaProject);
			}
		}

	}

	private void getAllDependenciesOfClass(IJavaProject javaProject) throws Exception {
		Set<String> analysisPackages = getSetPackage();
		Set<String> allClass = new HashSet<>();
		Set<ICompilationUnit> classesC = new HashSet<>();

		for (IPackageFragment p : javaProject.getPackageFragments()) {

			if (analysisPackages.contains(p.getElementName())) {
				String pacoteAnalisado = p.getElementName();
				ICompilationUnit[] classes = p.getCompilationUnits();
				Set<ICompilationUnit> strSet = Arrays.stream(classes).collect(Collectors.toSet());
				for (ICompilationUnit unit : p.getCompilationUnits()) {

					allClass.add(unit.getElementName());
					classesC.add(unit);
					
					Set<String> typeDependencies = Dependency.getInstance().getTypeDepencies(unit);
					dependencies.put(unit.getElementName().toString(), typeDependencies);

				}
				informations.put(pacoteAnalisado, strSet);
				estrutura.put(p, classesC);
			}

		}
		verifySimilarityBetweenPackages();

	}

	public void verifySimilarityBetweenPackages() throws Exception {
		SimilarityData ds = new SimilarityData();
		String values = "";		
		float totalSimilaridade = 0;
		float media = 0;
		int cont = 0;
		for (String p1 : informations.keySet()) {
			for (ICompilationUnit c1 : informations.get(p1)) {
				for (String p2 : informations.keySet()) {
					for (ICompilationUnit c2 : informations.get(p2)) {
						String c11 = c1.getElementName();
						String c22 = c2.getElementName();						
						if (!c11.equals(c22)) {							
							if (p1.equals(p2)) { // similarity of class same pack
								message.append("-------- Comparação classe "+ c11 + " com a classe   " + c22 +" ----- \n");
								System.out.println("Classe:  " + c11 + " com a classe   " + c22);															
								Dependency.getInstance().verifyDependenciesOfSuperClass(c11, c22, dependencies);
								//verifyDependenciesOfSuperClass(c11, c22);
								float anterior = verifySimilarity(c11, c22);
								totalSimilaridade += verifySimilarity(c11, c22);
								System.out.println("Anterior:  " + anterior);
								if (totalSimilaridade> 0.5) {
									message.append("Classe: " + c22+ " pode ser agrupada no mesmo pacote da classe:"+ c11 +" Similaridade de:"+ totalSimilaridade +"\n");
								}else {
									message.append("Classe: " + c22+ " Similaridade de:"+ totalSimilaridade +"\n");
								}
								System.out.println("Valor similaridade: " + totalSimilaridade);
								cont++;
							}

							else {

								System.out.println("Classe:  " + c11 + " com a classe   " + c22);
								message.append("-------- Comparação classe "+ c11 + " com a classe   " + c22 +" ----- \n");
								//message.append("Classe:  " + c11 + " com a classe   " + c22 +" \n");
								Dependency.getInstance().verifyDependenciesOfSuperClass(c11, c22, dependencies);
								float anterior = verifySimilarity(c11, c22);
								totalSimilaridade += verifySimilarity(c11, c22);
								System.out.println("Anterior:  " + anterior);							
								if (totalSimilaridade> 0.5) {
																	
									message.append("Classe: " + c22+ " pode ser agrupada no mesmo pacote da classe:"+ c11 +" Similaridade de:"+ totalSimilaridade +"\n");
								}else {
									message.append("Classe: " + c22+ " Similaridade de:"+ totalSimilaridade +"\n");
								}
								System.out.println("Valor similaridade: " + totalSimilaridade);
								cont++;
							}
							//message.append("-----------------------------------------------\n");
							media = totalSimilaridade / cont;
							System.out.println("Média   " + media);
							String t = Float.toString(media);
							// matrizInfo.put(c1, new HashSet<>(values));
							// values.add(t);
							String cp = c11 + " " + p2;
							matrizInfo.put(cp, t);
							ds = new SimilarityData(p2, c11, t);

						}
					}
					dados.add(ds);
					media = 0;
					cont = 0;
					totalSimilaridade = 0;
				}

			}
		}
		//Utils.getInstance().imprime2(matrizInfo);
		// imprime(informations);

		System.out.println();
		infoModel();
		System.out.println();
		infoView();
		System.out.println();
		//Utils.getInstance().imprimeArray(dados);
		//Utils.getInstance().imprime3(dependencies);

	}

	public Map<String, String> infoModel() {

		Map<String, String> pModel = new HashMap<>();
		for (String s : matrizInfo.keySet()) {
			String array1[] = new String[2];
			array1 = s.split(" ");
			String name = array1[0];
			String pacote1 = array1[1];
			String value = matrizInfo.get(s);
			if (pacote1.equals(pack1)) {
				pModel.put(name, value);

			}
		}
		//Utils.getInstance().imprime2(pModel);
		return pModel;
	}

	public Map<String, String> infoController() {

		Map<String, String> pController = new HashMap<>();
		for (String s : matrizInfo.keySet()) {
			String array1[] = new String[2];
			array1 = s.split(" ");
			String name = array1[0];
			String pacote1 = array1[1];
			String value = matrizInfo.get(s);	
		}
		//Utils.getInstance().imprime2(pController);
		return pController;
	}

	public Map<String, String> infoView() {
		Map<String, String> pView = new HashMap<>();
		for (String s : matrizInfo.keySet()) {

			String array1[] = new String[2];
			array1 = s.split(" ");
			String name = array1[0];

			String pacote1 = array1[1];
			String value = matrizInfo.get(s);
			if (pacote1.equals(pack2)) {
				pView.put(name, value);
			}
		}
		//Utils.getInstance().imprime2(pView);
		return pView;
	}


	private float verifySimilarity(String c1, String c2) {
		Set<String> deps1 = new HashSet<>();
		Set<String> deps2 = new HashSet<>();
		float a = 0;
		float similarity = 0;

		float b = 0;
		float c = 0;

		deps1.addAll(dependencies.get(c1));
		deps2.addAll(dependencies.get(c2));

		int t1 = deps1.size();
		int t2 = deps2.size();
		for (String s1 : deps1) {
			for (String s2 : deps2) {
				if (s1.equals(s2)) {
					a += 1;
				}
				String array1[] = new String[2];
				array1 = c1.split("[.]");						
				String array2[] = new String[2];
				array2 = c2.split("[.]");

				String newClass1 = array1[0];
				String newClass2 = array2[0];
				
			

				if (deps1.contains(newClass2) || deps2.contains(newClass1)) {
					weight = (float) 1.2;
				}
			}
		}

		b = (float) t1 - a;
		c = (float) t2 - a;
		if (!(weight == 0.0)) {
			similarity = (a / (b + c + a)) * weight;
		} else {
			similarity = (a / (b + c + a));
		}

		weight = 0;
		return similarity;

	}

	private void addTypeDeclarationName(TypeDeclaration typeDeclaration, Set<String> typeDeclarations) {
		typeDeclarations.add(typeDeclaration.getName().getFullyQualifiedName());
		for (TypeDeclaration t : typeDeclaration.getTypes()) {
			addTypeDeclarationName(t, typeDeclarations);
		}
	}

	// statement return  of a variable 
	private Set<String> getTypeDeclarations(IPackageFragment packageFragment) throws JavaModelException {
		Set<String> typeDeclarations = new HashSet<>();
		if (packageFragment.getKind() == IPackageFragmentRoot.K_SOURCE) {
			for (ICompilationUnit compilationUnit : packageFragment.getCompilationUnits()) {

				VisitorAST visitor = new VisitorAST(compilationUnit);
				Set<TypeDeclaration> declarations = visitor.getDeclaration();
				for (TypeDeclaration typeDeclaration : declarations) {
					addTypeDeclarationName(typeDeclaration, typeDeclarations);
				}
			}
		}
		return typeDeclarations;
	}

	/*int countMSG = 0;
	public void openView() {
		try {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
					.showView("Similaridade.exampleast.util.Similaridades");
			if (countMSG==0) {
				msg.append("As similaridades das classes em relacao aos pacotes do projeto sao apresentadas na tabela abaixo");
				countMSG +=1;
			}
				
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}
	public void hideView() {
		IWorkbenchPage wp = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IViewPart myView = wp.findView("Similaridade.exampleast.util.Similaridades");
		wp.hideView(myView);
	}
	public ArrayList<SimilarityData> getDados() {
		return dados;
	}*/

	public Map<String, String> getMatrizInfo() {

		return matrizInfo;
	}

}
