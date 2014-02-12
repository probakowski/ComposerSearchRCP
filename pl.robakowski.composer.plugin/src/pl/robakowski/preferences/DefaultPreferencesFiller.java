package pl.robakowski.preferences;

import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.e4.core.di.annotations.Execute;

public class DefaultPreferencesFiller {

	@Execute
	public void fillDefaults() {
		IEclipsePreferences prefs = DefaultScope.INSTANCE
				.getNode("composer.search");

		prefs.put("composerPath", "composer.phar");
		prefs.put("phpPath", "php");
		prefs.putBoolean("usePackagist", true);
	}
}
