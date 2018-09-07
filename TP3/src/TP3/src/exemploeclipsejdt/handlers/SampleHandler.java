package exemploeclipsejdt.handlers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.plaf.synth.SynthScrollBarUI;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import exemploeclipsejdt.ast.CyclomaticComplexityVisitor;
import exemploeclipsejdt.ast.LCOM;
import exemploeclipsejdt.ast.NumberOfAttributes;
import exemploeclipsejdt.ast.SimpleASTVisitor;

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
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {		

		IWorkbenchWindow window = HandlerUtil
				.getActiveWorkbenchWindowChecked(event);

		IWorkbenchPart workbenchPart = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getActivePart();

		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		//raiz
		IWorkspaceRoot root = workspace.getRoot();
		//pega todos projetos
		IProject[] projetos = root.getProjects();
		Map<String, Double> peso = new HashMap<String, Double>();

		peso.put("read",0.25);
		peso.put("write",0.50);
		peso.put("rename",0.10);
		peso.put("delete",0.10);
		peso.put("append",0.50);
		
		Map<String, String> busca = new HashMap<String, String>();
		busca.put("read","\\.read(.*)\\(");
		busca.put("delete", "\\.delete(.*)\\(");
		busca.put("write", "\\.write(.*)\\(");
		busca.put("append", "\\.append(.*)\\(");
		busca.put("rename", "\\.renameTo(.*)\\(");
		SimpleASTVisitor sv; 
		CyclomaticComplexityVisitor ccv;
		NumberOfAttributes noa;
		LCOM lcom;
		
		/*	IJavaProject: É o nó do modelo Java e representa um projeto Java. Ele contém IPackageFragmentRoots como nós filhos.
			IPackageFragmentRoot: pode ser uma fonte ou uma pasta de classe de um projeto, um .zipou um .jararquivo. IPackageFragmentRootpode conter arquivos binários ou de origem.
			IPackageFragment: Um único pacote. Ele contém ICompilationUnits ou IClassFiles, dependendo IPackageFragmentRootdo tipo de fonte ou do tipo binário. Observe que IPackageFragmentnão estão organizados como pai-filho. Por exemplo, net.sf.anão é o pai de net.sf.a.b. Eles são dois filhos independentes do mesmo IPackageFragmentRoot.
			ICompilationUnit: um arquivo de origem Java.
			IImportDeclaration, IType, IField, IInitializer, IMethod: Filhos de ICompilationUnit. As informações fornecidas por esses nós também estão disponíveis na AST.*/
		
		for (IProject projeto :projetos) {
			try {
				if (projeto.isAccessible() & projeto.isOpen()) {
					IJavaProject jprojeto = JavaCore.create(projeto);										
					IPackageFragmentRoot [] pkgs = jprojeto.getPackageFragmentRoots();
					//IPackageFragment pf =  
					//System.out.println( pkgs.length);
					jprojeto.getAllPackageFragmentRoots();
					IPackageFragment[] teste = jprojeto.getPackageFragments();
					
					for (IPackageFragment fg : teste) {
						ICompilationUnit [] unit = fg.getCompilationUnits();
						for (ICompilationUnit un : unit) {
							sv = new SimpleASTVisitor(un);
							System.out.println("-------------------- Metrics of Software ("+sv.getClassName()+")--------------------");							
							//System.out.println(sv.getClassName()); 
							ccv = new CyclomaticComplexityVisitor(un);							
							//System.out.println("Cyclomatic Complexity:"+ ccv.getAllCyclomaticComplexity());
							//System.out.println("-------------------------------------------------------------");
							noa = new NumberOfAttributes(un);
							//System.out.println("Number of Attributes:"+ noa.getNumberOfAttributes());
							//System.out.println("-------------------------------------------------------------");
										
							Double total = 0.0; 			
							String codigo = un.getSource(); 
							int count = 0; 
							BigDecimal valorF = null;
								Pattern pattern; 
								Matcher  matcher;
								
								for (String valor: busca.keySet()) {
									pattern = Pattern.compile(busca.get(valor));
									matcher = pattern.matcher(codigo);
									count=0;					
									//total=0.0;
									while (matcher.find()) {
										count++;   
									}									
									total+=peso.get(valor);	
									total=total*count;	
									valorF = BigDecimal.valueOf(total).setScale(2, RoundingMode.DOWN);
									//System.out.println(valor+":"+valorF);												
								}
								//System.out.println("AFS:"+valorF);
								//System.out.println("-------------------------------------------------------------");
								//lcom = new LCOM(un); 
																	
						}
					}
				    
				}
			}catch (Exception e) {
			
			}

		}

		IFile file = (IFile) workbenchPart.getSite().getPage()
				.getActiveEditor().getEditorInput().getAdapter(IFile.class);
		if (file == null) {
			MessageDialog.openInformation(window.getShell(),
					"ExemploEclipseJDT", "Não existem arquivos abertos");
			return null;
		}

		final ICompilationUnit unit = ((ICompilationUnit) JavaCore.create(file));

		try {	
			
			/*CyclomaticComplexityVisitor ccv = new CyclomaticComplexityVisitor(unit);
			System.out.println("-------------------- Metrics of Software --------------------");
			System.out.println("Cyclomatic Complexity:"+ ccv.getAllCyclomaticComplexity());
			System.out.println("-------------------------------------------------------------");
			NumberOfAttributes noa = new NumberOfAttributes(unit);
			System.out.println("Number of Attributes:"+ noa.getNumberOfAttributes());
			System.out.println("-------------------------------------------------------------");
						
			Double total = 0.0; 			
			String codigo = unit.getSource(); 
			int count = 0; 
			BigDecimal valorF = null;
				Pattern pattern; 
				Matcher  matcher;
				
				for (String valor: busca.keySet()) {
					pattern = Pattern.compile(busca.get(valor));
					matcher = pattern.matcher(codigo);
					count=0;					
					//total=0.0;
					while (matcher.find()) {
						count++;   
					}									
					total+=peso.get(valor);	
					total=total*count;	
					valorF = BigDecimal.valueOf(total).setScale(2, RoundingMode.DOWN);
					//System.out.println(valor+":"+valorF);												
				}
				System.out.println("AFS:"+valorF);
				System.out.println("-------------------------------------------------------------");*/
		}catch(Exception e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return null;

	}}
