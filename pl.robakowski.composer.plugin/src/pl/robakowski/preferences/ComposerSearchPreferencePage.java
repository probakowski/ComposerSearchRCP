//******************************************************************
//
//  ComposerSearchPreferencePage.java
//  Copyright 2014 PSI AG. All rights reserved.
//  PSI PROPRIETARY/CONFIDENTIAL. Use is subject to license terms
//
// ******************************************************************

package pl.robakowski.preferences;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;

public class ComposerSearchPreferencePage extends FieldEditorPreferencePage {

	private static final File CURRENT_DIR = new File(".");
	private final ScopedPreferenceStore store;

	public ComposerSearchPreferencePage() {
		super("Composer search", FieldEditorPreferencePage.GRID);
		store = new ScopedPreferenceStore(InstanceScope.INSTANCE,
				"composer.search");
		setPreferenceStore(store);
		setDescription("Composer search preferences");
	}

	@Override
	protected void createFieldEditors() {
		createPhpField();
		createComposerPharField();
		createRepositoriesField();
		createPackagistField();
	}

	private void createRepositoriesField() {
		addField(new RepositoriesFieldEditor("repositories", "Repositories:",
				getFieldEditorParent()));
	}

	private void createPackagistField() {
		addField(new BooleanFieldEditor("usePackagist", "Use Packagist",
				getFieldEditorParent()));
	}

	private void createComposerPharField() {
		FileFieldEditor composerPath = new FileFieldEditor("composerPath",
				"Composer.phar path:", getFieldEditorParent());
		String[] ext = new String[] { "*.phar" };
		composerPath.setFileExtensions(ext);
		composerPath.setEmptyStringAllowed(false);
		composerPath.setFilterPath(CURRENT_DIR);

		addField(composerPath);
	}

	private void createPhpField() {
		FileFieldEditor phpPath = new PhpFieldEditor("phpPath", "PHP path:",
				getFieldEditorParent());
		String[] ext = new String[] { "*.exe" };
		phpPath.setFileExtensions(ext);
		phpPath.setEmptyStringAllowed(false);
		phpPath.setFilterPath(CURRENT_DIR);
		addField(phpPath);
	}

	@Override
	public boolean performOk() {
		if (!super.performOk()) {
			return false;
		}
		try {
			store.save();
		} catch (IOException e) {
		}
		return true;
	}
}
