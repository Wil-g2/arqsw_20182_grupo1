package exemploeclipsejdt.handlers;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.xml.ws.handler.MessageContext;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.ICompilationUnit;
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
		ICompilationUnit unit = JavaCore.createCompilationUnitFrom(file);
		try {
			if (JOptionPane.showConfirmDialog(null, "Deseja alterar a classe?","Validar", 0)==JOptionPane.YES_OPTION) {						
				VerifySingleton vs = new VerifySingleton(unit);			
				MessageDialog.openInformation(window.getShell(), "Padrão Singleton", vs.getMsg());
			}
		} catch (JavaModelException | MalformedTreeException | BadLocationException e) {
			MessageDialog.openInformation(window.getShell(), "Padrão Singleton", e.getMessage());
			e.printStackTrace();
		}
		return null;

	}	
}
