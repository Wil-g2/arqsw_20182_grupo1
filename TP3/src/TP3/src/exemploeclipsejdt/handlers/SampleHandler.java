package exemploeclipsejdt.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import exemploeclipsejdt.ast.TestLCOM;

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
	public Object execute(ExecutionEvent event) throws ExecutionException {

		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);

		IWorkbenchPart workbenchPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.getActivePart();

		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		IProject[] projetos = root.getProjects();		

		for (IProject projeto : projetos) { 											// percorre todos projetos do eclipse
			try {
				System.out.flush();
				if (projeto.isAccessible() & projeto.isOpen()) { 						// verifica se o projeto está acessível e aberto
					IJavaProject jprojeto = JavaCore.create(projeto);
					IPackageFragmentRoot[] pkgs = jprojeto.getPackageFragmentRoots(); 	// arquivos do projeto
					jprojeto.getAllPackageFragmentRoots();
					IPackageFragment[] teste = jprojeto.getPackageFragments(); 			// contém as classes java

					for (IPackageFragment fg : teste) { 								// percorre os arquivos java
						ICompilationUnit[] unit = fg.getCompilationUnits(); 			// cria um arquivo para ser analisado
																						// (ICompilationUnit contém todas
																						// informações da classe)
						for (ICompilationUnit un : unit) {
							ASTParser parser = ASTParser.newParser(AST.JLS10);
							parser.setKind(ASTParser.K_COMPILATION_UNIT);
							parser.setSource(un);
							parser.setResolveBindings(true);
							CompilationUnit classUnit = (CompilationUnit) parser.createAST(null);
							
							// initiazer modofied class
							classUnit.recordModifications();
							//AST ast = classUnit.getAST();
							
							TestLCOM lcom = new TestLCOM(un);																
							
							JSONObject json = new JSONObject();
							
							Map<String, HashSet<String>> refatorClass = new HashMap<>();
							Map<String, HashSet<String>> copyRefatorClass = new HashMap<>();
							List<String> attributeExclusive = new ArrayList<>();
							
							refatorClass = lcom.getResult();
							copyRefatorClass = lcom.getResult();
							
							int count = 0;
							int i = 0;						
							
							json.put("class", lcom.getClasse());
							
							
							for (Iterator<HashSet<String>> it = refatorClass.values().iterator(); it.hasNext();i++) { 
								Set<String> methods = it.next();								
								//HashSet<String> key = (HashSet<String>)it.next();
								//Set<Entry<String, HashSet<String>>> keys = refatorClass.entrySet();
								if (methods.size() == 1){    //use in one method									
									for (String m: methods) { 
										for (Iterator<HashSet<String>> itCopy = copyRefatorClass.values().iterator(); itCopy.hasNext();) {
											Set<String> methodsCopy = itCopy.next();
											if(methodsCopy.size()>1) {  //addEndereco, addPessoa
												count=0;
												if(!methodsCopy.contains(m)) {
													count++;
												}
											}	
										}
										if (count>0) {
											if (!attributeExclusive.contains(m)){
												attributeExclusive.add(m);			
											};											
										}
									}	
									
								}
							}
							
							System.out.println("===========================================================================");
							System.out.println("Sugestão para análise de refatoração do Projeto");
							
							if (attributeExclusive.isEmpty()) {
								System.out.println("----------------------------------------------------------------");
								System.out.println("Analisando Pacote/Classe: " + lcom.getClasse());
								System.out.println("A classe está coesa.");
								System.out.println("----------------------------------------------------------------");
							}else { 
								System.out.println("----------------------------------------------------------------");
								System.out.println("Analisando Pacote/Classe: " + lcom.getClasse());
								System.out.println("Os Seguintes métodos possuem atributos exclusivos:");
								System.out.println(" --> " + attributeExclusive.toString());
								System.out.println("Avalie fazer um ExtracClasss nestes métodos!");
								System.out.println("----------------------------------------------------------------");

							}
						}
					}

				}
			} catch (Exception e) {

			}

		}

		IFile file = (IFile) workbenchPart.getSite().getPage().getActiveEditor().getEditorInput()
				.getAdapter(IFile.class);
		if (file == null) {
			MessageDialog.openInformation(window.getShell(), "ExemploEclipseJDT", "Não existem arquivos abertos");
			return null;
		}

		final ICompilationUnit unit = ((ICompilationUnit) JavaCore.create(file));
		return null;

	}

}
