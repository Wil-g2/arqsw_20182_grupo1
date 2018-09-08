package exemploeclipsejdt.handlers;

import java.awt.image.RasterFormatException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.BlockComment;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ITrackedNodePosition;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jdt.internal.core.JavadocContents;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.text.edits.UndoEdit;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import org.eclipse.jface.*;
import org.eclipse.ui.handlers.HandlerUtil;

import exemploeclipsejdt.ast.CyclomaticComplexityVisitor;
import exemploeclipsejdt.ast.LCOM;
import exemploeclipsejdt.ast.MethodVisitor;
import exemploeclipsejdt.ast.NumberOfAttributes;
import exemploeclipsejdt.ast.Refactor;
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
	 * the command has been executed, so extract extract the needed information from
	 * the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {

		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);

		IWorkbenchPart workbenchPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.getActivePart();

		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		// raiz
		IWorkspaceRoot root = workspace.getRoot();
		// pega todos projetos
		IProject[] projetos = root.getProjects();		

		/*
		 * IJavaProject: É o nó do modelo Java e representa um projeto Java. Ele contém
		 * IPackageFragmentRoots como nós filhos. IPackageFragmentRoot: pode ser uma
		 * fonte ou uma pasta de classe de um projeto, um .zipou um .jararquivo.
		 * IPackageFragmentRootpode conter arquivos binários ou de origem.
		 * IPackageFragment: Um único pacote. Ele contém ICompilationUnits ou
		 * IClassFiles, dependendo IPackageFragmentRootdo tipo de fonte ou do tipo
		 * binário. Observe que IPackageFragmentnão estão organizados como pai-filho.
		 * Por exemplo, net.sf.anão é o pai de net.sf.a.b. Eles são dois filhos
		 * independentes do mesmo IPackageFragmentRoot. ICompilationUnit: um arquivo de
		 * origem Java. IImportDeclaration, IType, IField, IInitializer, IMethod: Filhos
		 * de ICompilationUnit. As informações fornecidas por esses nós também estão
		 * disponíveis na AST.
		 */
		Refactor rf;
		for (IProject projeto : projetos) { 											// percorre todos projetos do eclipse
			try {
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
							// inicia a modificação da classe
							classUnit.recordModifications();
							AST ast = classUnit.getAST();
							
							rf = new Refactor(un);
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
