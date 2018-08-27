package exemploeclipsejdt.ast;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;

public class SimpleASTVisitor extends ASTVisitor {
	private List<String> dependencies;	
	private List<String> attributes;

	private CompilationUnit fullClass;
	private String className;
	
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
	
	public void gerarArq(String classe, String line, String qtdatributos, List<String>metodos, List<String>attributes, String getCountMet) {			
		//generate file DOT (graph description language)		
		
		//path of file 
		String path="C:\\temp.";
		
		//FileWriter file;
		try {			
			
			GraphViz gv = new GraphViz();
			gv.addln(gv.start_graph());		
			gv.addln("node [shape=box];");
			gv.addln("\""+this.className+"\" [style=filled color=red];");			
			gv.addln("LineofCode [label=\"Line of Code\\n"+ line +" \"] style=dotted;");
			gv.addln("QtdAttributes [label=\"Qtd. Attributes\\n "+ qtdatributos +"\"] style=dotted; ");
			gv.addln("QtdMethods [label=\"Qtd. Methods\\n"+ getCountMet +"\"] style=dotted;");
			for (String m : metodos) { 				
				gv.addln ("\""+this.className+"\" -> "+m+";"); 
			} 			
			gv.addln("Attributes [label=\"Attributes\\n ");
			for (String a : attributes) { 
				gv.add ("\n* "+a); 
			} 			
			gv.add("\" style=dotted];");
			gv.addln(" \""+this.className+"\" -> Attributes");
			gv.add(gv.end_graph());						
			//out of file .dot 
			System.out.println("========= Start Copy ========");
			System.out.println(gv.getDotSource());
			System.out.println("========= End Copy ========");
			gv.increaseDpi();
			
			//Type file
			String type = "png";
			File out = new File("/tmp/out"+gv.getImageDpi()+"."+ type);   // Linux
			//File out = new File(path+"."+ type);    // Windows
			gv.writeGraphToFile( gv.getGraph(gv.getDotSource(),type,"dot"), out );
							
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

}
