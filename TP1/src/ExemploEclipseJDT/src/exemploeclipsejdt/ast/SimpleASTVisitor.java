package exemploeclipsejdt.ast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Comment;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.search.TypeDeclarationMatch;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class SimpleASTVisitor extends ASTVisitor {
	private List<String> dependencies;	
	private List<String> attributes;

	private CompilationUnit fullClass;
	private String className;
	private Double numberOfLinesOfCode;
	
	public SimpleASTVisitor(ICompilationUnit unit) throws Exception {
		this.dependencies = new ArrayList<>();		
		this.attributes = new ArrayList<>();

		this.className = unit.getParent().getElementName() + "."
				+ unit.getElementName().substring(0, unit.getElementName().length() - 5);
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(unit);
		parser.setResolveBindings(true);
						
		this.fullClass = (CompilationUnit) parser.createAST(null); // parse
		this.fullClass.accept(this);		
		
		this.numberOfLinesOfCode = (double) unit.toString().split("\n").length;
		
		for (Object type : unit.getTypes()){			
			if (type instanceof TypeDeclaration) {
				FieldDeclaration [] attributes = ((TypeDeclaration) type).getFields();
				for (FieldDeclaration attribute: attributes){
					List<FieldDeclaration> fragments = attribute.fragments();
					Object obj = fragments.get(0);
					if (obj instanceof VariableDeclarationFragment){
						String str = ((VariableDeclarationFragment) obj).getName().toString();
						this.attributes.add(str);
					}
				}
			}
			
		}
				
		
	}

	public final List<String> getDependencies() {
		return this.dependencies;
	}

	public final String getClassName() {
		return this.className;
	}
	
	public final String getAtributos() {
		return this.attributes.toString();		
	}
	
	//retorna quantide de linhas de uma clase
	public final String getLOC(){
		return this.numberOfLinesOfCode.toString();
		
	}
	
	public final Integer getNMC() {
		return this.dependencies.size();
		
	}
	
	@Override
	public boolean visit(MethodDeclaration node) {
		if (!node.isConstructor()){
			dependencies.add(node.getName().toString());
		}
		return true;
	}
	
	public void gerarArq() {			
		//Gerar arquivo DOT (graph description language)
		String path="C:\\Users\\Willian\\Desktop\\Arquivo.txt";
		String textoQueSeraEscrito = "graph graphname {";
		FileWriter arquivo;
		try {
			arquivo = new FileWriter(new File(path));
			arquivo.write(textoQueSeraEscrito);
			arquivo.write(" \" " +this.className+" \" " +" [shape=box]; }");
			
			System.out.println();
			arquivo.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

}
