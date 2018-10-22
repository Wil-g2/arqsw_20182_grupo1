package exampleast.util;

import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

public class View {

	public static View view;
	
	protected View() {}
	public static View getInstance() {
		if(view == null) {
			view = new View();
		}
		return view;
	}
	
	public StringBuilder openView(int countMSG) {
		StringBuilder msg = null;
		/*try {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
					.showView("Similaridade.exampleast.util.Similaridades");
			if (countMSG==0) {
				msg.append("As similaridades das classes em relacao aos pacotes do projeto sao apresentadas na tabela abaixo");
				countMSG +=1;
			}
				
		} catch (PartInitException e) {
			e.printStackTrace();
		}*/
		return msg;
	}
	public void hideView() {
		IWorkbenchPage wp = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IViewPart myView = wp.findView("Similaridade.exampleast.util.Similaridades");
		wp.hideView(myView);
	}
}
