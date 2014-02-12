package pl.robakowski.preferences;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferenceNode;
import org.eclipse.jface.window.IShellProvider;

public class PreferenceHandler {

	@Execute
	public void execute(IShellProvider shellProvider) {
		PreferenceManager manager = new PreferenceManager();
		manager.addToRoot(new PreferenceNode("composer",
				new ComposerSearchPreferencePage()));
		PreferenceDialog dialog = new PreferenceDialog(
				shellProvider.getShell(), manager);
		dialog.setBlockOnOpen(true);
		dialog.open();
	}
}
