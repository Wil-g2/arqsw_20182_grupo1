package exemploeclipsejdt.handlers;

import java.util.Collection;
import java.util.LinkedList;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import exemploeclipsejdt.ast.NumberOfAttributes;
import exemploeclipsejdt.ast.NumberofLine;
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

		SimpleASTVisitor cv;
		NumberofLine nl;
		NumberOfAttributes na; 			
		try {
			cv = new SimpleASTVisitor(unit);	
			nl = new NumberofLine(unit);
			na = new NumberOfAttributes(unit);
			
			dependencies.addAll(cv.getDependencies());
			//pega o nome da classe
			MessageDialog.openInformation(window.getShell(),
					"ExemploEclipseJDT", cv.getClassName());
			
			MessageDialog.openInformation(window.getShell(),
					"Atributos ", "Attributes Class:"+na.getNumberOfAttributes());
			
			MessageDialog.openInformation(window.getShell(),
					"Atributos ", "Attributes Class:"+na.attributes());
			
			MessageDialog.openInformation(window.getShell(),"LOC", 
					"Line of Code Class:"+nl.getLOC());
			
			MessageDialog.openInformation(window.getShell(),"NOM", 
					"Number of Method Class:"+ cv.getNMC().toString());
			
			cv.gerarArq(cv.getClassName(),nl.getLOC(),na.getNumberOfAttributes().toString(),cv.getDependencies(),na.attributes());
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		MessageDialog.openInformation(window.getShell(),
				"ExemploEclipseJDT", "Dependencies of Class:"+ dependencies.toString());
		
	   
		
		return null;

	}
}
