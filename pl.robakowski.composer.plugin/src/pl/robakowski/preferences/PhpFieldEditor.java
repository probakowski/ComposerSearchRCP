package pl.robakowski.preferences;

import java.io.IOException;

import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.swt.widgets.Composite;

public class PhpFieldEditor extends FileFieldEditor {
	public PhpFieldEditor() {
		super();
	}

	public PhpFieldEditor(String name, String labelText,
			boolean enforceAbsolute, Composite parent) {
		super(name, labelText, enforceAbsolute, parent);
	}

	public PhpFieldEditor(String name, String labelText,
			boolean enforceAbsolute, int validationStrategy, Composite parent) {
		super(name, labelText, enforceAbsolute, validationStrategy, parent);
	}

	public PhpFieldEditor(String name, String labelText, Composite parent) {
		super(name, labelText, parent);
	}

	@Override
	protected boolean checkState() {
		String path = getTextControl().getText();

		if (path == null || path.trim().isEmpty()) {
			return false;
		}

		try {
			Process process = new ProcessBuilder().command(path, "--version")
					.start();
			byte[] php = new byte[3];
			process.getInputStream().read(php);
			boolean correct = "PHP".equals(new String(php, "UTF-8"));
			process.destroy();
			return correct;
		} catch (IOException e) {
		}
		showErrorMessage("Command not correct");
		return false;
	}
}
