package exemploeclipsejdt.handlers;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.*;

import javax.swing.filechooser.FileSystemView;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import exemploeclipsejdt.ast.NumberOfAttributes;
import exemploeclipsejdt.ast.NumberofLine;
import exemploeclipsejdt.ast.NumberofParameter;
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
					
		List<IJavaProject> projectList = new LinkedList<IJavaProject>();
		for (IProject projeto :projetos) {
			try {
				if (projeto.isAccessible() & projeto.isOpen()) {
					IJavaProject jprojeto = JavaCore.create(projeto);					
					IPackageFragment [] pkgs = jprojeto.getPackageFragments();
				    projectList.add(JavaCore.create(projeto));
				    IResource [] members = projeto.members();				    
				    for (IResource member : members) {
				    	 if (member instanceof IFile){
				        }
				    }
				    //System.out.println(projectList.toString());				    
					//IJavaElement [] je = jprojeto.				    				

				}
			}catch (Exception e) {
				// TODO: handle exception
			}

		}

		//List<IFile> files = (IFile) workbenchPart.getSite().getPage()
		IFile file = (IFile) workbenchPart.getSite().getPage()
				.getActiveEditor().getEditorInput().getAdapter(IFile.class);
		if (file == null) {
			MessageDialog.openInformation(window.getShell(),
					"ExemploEclipseJDT", "Não existem arquivos abertos");
			return null;
		}

		final ICompilationUnit unit = ((ICompilationUnit) JavaCore.create(file));

		final Collection<String> dependencies = new LinkedList<String>();


		try {	
			File test= new File("teste"); 				
			Double total = 0.0; 
			/*MessageDialog.openInformation(window.getShell(), "teste",
					unit.getElementName().);*/
			/*MessageDialog.openInformation(window.getShell(), "teste",
					unit.getSource());*/
			String codigo = unit.getSource(); 
			int count = 0; 

				Pattern pattern; 
				Matcher  matcher;
				for (String valor: busca.keySet()) {
					pattern = Pattern.compile(busca.get(valor));
					matcher = pattern.matcher(codigo);
					count=0;
					total=0.0;
					while (matcher.find()) {
						count++;   
					}									
					total+=peso.get(valor);	
					total=total*count;			
					System.out.println(valor+":"+total);													
				}
						
			//MessageDialog.openInformation(window.getShell(),null,total.toString());
		}catch(Exception e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return null;

	}}
