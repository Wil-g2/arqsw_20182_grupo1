﻿package exemploeclipsejdt.ast;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

public class VerifySingleton extends ASTVisitor {
	List<String> msg = new ArrayList<String>();
	public VerifySingleton(ICompilationUnit unit) throws JavaModelException, MalformedTreeException, BadLocationException {
		Document document = new Document(unit.getSource());
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(unit);
		CompilationUnit compUnit = (CompilationUnit) parser.createAST(null);
		
		compUnit.recordModifications();//inicia registro de	modificações
		AST ast = compUnit.getAST();

		Visitor visitor = new Visitor();
		compUnit.accept(visitor);

		int sincronized = Modifier.ModifierKeyword.SYNCHRONIZED_KEYWORD.toFlagValue();
		int privado = Modifier.ModifierKeyword.PRIVATE_KEYWORD.toFlagValue();
				
				boolean instancia = false;
				boolean construtor = false;
				boolean atributo = false;
				for (TypeDeclaration type : visitor.getList()) {
					for (MethodDeclaration m1 : type.getMethods()) {
						List<Modifier> mod = m1.modifiers();
						int modificador1 = m1.getModifiers();
						if (m1.isConstructor()) {							
							if (!((modificador1 & privado) == privado)) {
								setPrivateMethod(m1);
								construtor = true;
								msg.add("Alteração do cosntrutor para private. ");
							}
						}
						if ((m1.getName().getIdentifier().startsWith("getInstanc"))) {

							if (!((modificador1 & sincronized) == sincronized)) {
								mod.add(2, ast.newModifier(Modifier.ModifierKeyword.SYNCHRONIZED_KEYWORD));
								instancia = true;
								msg.add("'synchronized' foi adicionado! ");

							}

						}

					}

					// Percorre atributos do projeto
					for (FieldDeclaration f : type.getFields()) {
						int publico = Modifier.ModifierKeyword.PUBLIC_KEYWORD.toFlagValue();
						int tipo = f.getModifiers();
						// verifica atributo
						if ((tipo & publico) == publico) {
							atributo = true;
							setPrivateField(f);
							msg.add("O atributo da classe foi modificado para 'private'! ");
						}

					}
				}

				if (instancia || construtor || atributo) {
					TextEdit edits = compUnit.rewrite(document, unit.getJavaProject().getOptions(true));
					edits.apply(document);
					String newSource = document.get();
					unit.getBuffer().setContents(newSource);
				} else
					msg.add("Padrão Singleton correto.");
				

			}
	
	private void setPrivateField(FieldDeclaration field) {

		List<Modifier> mod = field.modifiers();
		ModifierKeyword m = Modifier.ModifierKeyword.PRIVATE_KEYWORD;

		for (Modifier m1 : mod) {
			if (m1.getKeyword().equals(m.PUBLIC_KEYWORD)) {
				m1.setKeyword(Modifier.ModifierKeyword.PRIVATE_KEYWORD);

			}
		}
	}

	private void setPrivateMethod(MethodDeclaration method) {

		List<Modifier> mod = method.modifiers();
		ModifierKeyword m = Modifier.ModifierKeyword.PRIVATE_KEYWORD;

		for (Modifier m1 : mod) {
			if (m1.getKeyword().equals(m.PUBLIC_KEYWORD)) {
				m1.setKeyword(Modifier.ModifierKeyword.PRIVATE_KEYWORD);

			}
		}
	}
		
	public String getMsg() {
		return msg.toStrig();
	}
}