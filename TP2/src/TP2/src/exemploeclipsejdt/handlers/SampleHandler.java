package exemploeclipsejdt.handlers;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
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
			
			/*MessageDialog.openInformation(window.getShell(), "teste",
					unit.getElementName().);*/
			MessageDialog.openInformation(window.getShell(), "teste",
					unit.getSource());
			
			if (unit.getSource().contains("new File")) {
				MessageDialog.openInformation(window.getShell(),null,"Classe faz acesso ao disco!");				
			}
								
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	   
		return null;

	}
}
