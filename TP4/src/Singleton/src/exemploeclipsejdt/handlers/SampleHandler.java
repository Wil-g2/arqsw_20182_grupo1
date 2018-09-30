package exemploeclipsejdt.handlers;

import java.util.ArrayList;
import java.util.List;

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
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.TypeDeclaration;
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
		IFile file = null;
		try {
			file = (IFile) workbenchPart.getSite().getPage().getActiveEditor().getEditorInput().getAdapter(IFile.class);
		} catch (NullPointerException e) {
			MessageDialog.openInformation(window.getShell(), "Padrão Singleton", "Nenhum arquivo aberto.");
			return null;
		}

		/*if (JOptionPane.showConfirmDialog(null, "Verificar todo Projeto ?", "Validar", 0) == JOptionPane.YES_OPTION) {
			IWorkspace workspace = ResourcesPlugin.getWorkspace();
			// raiz
			IWorkspaceRoot root = workspace.getRoot();
			// pega todos projetos
			IProject[] projetos = root.getProjects();
			for (IProject projeto : projetos) { // percorre todos projetos do eclipse
				try {
					if (projeto.isAccessible() & projeto.isOpen()) { // verifica se o projeto está acessível e aberto
						IJavaProject jprojeto = JavaCore.create(projeto);
						IPackageFragmentRoot[] pkgs = jprojeto.getPackageFragmentRoots(); // arquivos do projeto
						jprojeto.getAllPackageFragmentRoots();
						IPackageFragment[] teste = jprojeto.getPackageFragments(); // contém as classes java

						for (IPackageFragment fg : teste) { // percorre os arquivos java
							ICompilationUnit[] unit = fg.getCompilationUnits(); // cria um arquivo para ser analisado
																				// (ICompilationUnit contém todas
																				// informações da classe)
							for (ICompilationUnit un : unit) {
								try {
									boolean edit = (JOptionPane.showConfirmDialog(null, "Permitir refatoração ?", "Alerta",
											0) == JOptionPane.YES_OPTION);
									VerifySingleton vs = new VerifySingleton(un, edit);
									MessageDialog.openInformation(window.getShell(), "Padrão Singleton", vs.getMsg());
								} catch (JavaModelException | MalformedTreeException | BadLocationException e) {
									MessageDialog.openInformation(window.getShell(), "Padrão Singleton", e.getMessage());
									e.printStackTrace();
								}
							}
						}
					}
				} catch (Exception e) {
					System.out.println("Erro ao tentar ler o projeto.");
				}
			}
		}*/

		ICompilationUnit unit = JavaCore.createCompilationUnitFrom(file);
		try {
			boolean edit = (JOptionPane.showConfirmDialog(null, "Deseja refatorar a classe?", "Validar",
					0) == JOptionPane.YES_OPTION);
			// if (JOptionPane.showConfirmDialog(null, "Deseja alterar a classe?","Validar",
			// 0)==JOptionPane.YES_OPTION) {
			VerifySingleton vs = new VerifySingleton(unit, edit);
			MessageDialog.openInformation(window.getShell(), "Padrão Singleton", vs.getMsg());

		} catch (JavaModelException | MalformedTreeException | BadLocationException e) {
			MessageDialog.openInformation(window.getShell(), "Padrão Singleton", e.getMessage());
			e.printStackTrace();
		}
		return null;

	}
}
