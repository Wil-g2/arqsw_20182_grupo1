package ast.handlers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import clustering.simplekmeans.Dependency;
import clustering.simplekmeans.Entry;
import clustering.simplekmeans.FileGenerate;
import clustering.simplekmeans.Similarity;

public class SampleHandler extends AbstractHandler {

	private Set<String> allClass;

	private static String classes;


	public static ArrayList<Similarity> similaritys;
	public Set<String> allDependencies = new HashSet<>();

	private Map<String, Set<String>> dependencies = new HashMap<>();

	Map<String, Set<ICompilationUnit>> informations = new HashMap<>();

	ArrayList<Entry> queueSimiliraty = new ArrayList<Entry>();

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {		
		
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		similaritys = new ArrayList<Similarity>();
		
		StringBuilder msg = new StringBuilder();
		msg.append("O arquivo 'dataset_kmeans.arff.arff' Foi gerado com sucesso.");
		
		try {
			getWorkspaceInfo();
			MessageDialog.openInformation(window.getShell(), "ExemploEclipseJDTPlugin", msg.toString());

		} catch (Exception e) {
			MessageDialog.openInformation(window.getShell(), "ExemploEclipseJDTPlugin",
					"Erro! Não foi possível executar o plug-in!");
			e.printStackTrace();
		}
		
		return null;
	}
	//Get a information of Workspace
	private void getWorkspaceInfo() throws Exception {

		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		IProject[] projects = root.getProjects();
		// Iterator about all the projects
		for (IProject project : projects) {
			if (project.isOpen() && project.isNatureEnabled("org.eclipse.jdt.core.javanature")) {
				IJavaProject javaProject = JavaCore.create(project);
				getAllDependenciesOfClass(javaProject);
				try {
					FileGenerate.getInstance().criarArquivo(allDependencies, dependencies);
				}catch(Exception e) {
					
				}
			}
		}

	}

	private void getAllDependenciesOfClass(IJavaProject javaProject) throws Exception {
		allClass = new HashSet<>();
		Set<ICompilationUnit> classesC = new HashSet<>();

		for (IPackageFragment p : javaProject.getPackageFragments()) {

			if (p.getKind() == IPackageFragmentRoot.K_SOURCE) {
				System.out.println("Pacotes: " + p.getElementName());
				String pacoteAnalisado = p.getElementName();
				ICompilationUnit[] classes = p.getCompilationUnits();
				Set<ICompilationUnit> strSet = Arrays.stream(classes).collect(Collectors.toSet());
				for (ICompilationUnit unit : p.getCompilationUnits()) {

					allClass.add(unit.getElementName());
					classesC.add(unit);

					Set<String> typeDependencies = Dependency.getInstance().getTypeDepencies(unit);
					this.allDependencies.addAll(typeDependencies);
					dependencies.put(unit.getElementName().toString(), typeDependencies);
				}
				informations.put(pacoteAnalisado, strSet);
			}

		}

	}


	public Set<String> getAllDependencies() {
		return allDependencies;
	}

	public void setAllDependencies(Set<String> allDependencies) {
		this.allDependencies = allDependencies;
	}
	
	public ArrayList<Similarity> getDados() {
		return similaritys;
	}

	public static String getClasses() {
		return classes;
	}

	public Map<String, Set<ICompilationUnit>> getInformations() {
		return informations;
	}
}
