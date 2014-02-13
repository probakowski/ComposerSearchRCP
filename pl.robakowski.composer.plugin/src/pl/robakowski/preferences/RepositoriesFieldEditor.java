package pl.robakowski.preferences;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.preference.ListEditor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;

public class RepositoriesFieldEditor extends ListEditor {

	public static final String LINE_SEPARATOR = "|";
	public static final String FIELD_SEPARATOR = " ";

	class NewInputDialog extends Dialog {

		private Text textField;
		private Combo comboField;
		private String text;

		protected NewInputDialog(Shell parentShell) {
			super(parentShell);
		}

		@Override
		protected void configureShell(Shell newShell) {
			super.configureShell(newShell);
			newShell.setText("New repository");
			newShell.setMinimumSize(200, 10);
		}

		@Override
		protected Control createDialogArea(Composite parent) {
			Composite composite = (Composite) super.createDialogArea(parent);
			((GridLayout) composite.getLayout()).numColumns = 2;
			createTypeCombo(composite);
			createUrlField(composite);
			return composite;
		}

		private void createUrlField(Composite composite) {
			textField = new Text(composite, SWT.BORDER);
			textField.setLayoutData(GridDataFactory.fillDefaults()
					.grab(true, false).hint(200, SWT.DEFAULT).create());
			textField.setFocus();
		}

		private void createTypeCombo(Composite composite) {
			comboField = new Combo(composite, SWT.READ_ONLY);
			comboField.add("composer");
			comboField.add("vcs");
			comboField.add("pear");
			comboField.select(0);
		}

		public String getText() {
			return text;
		}

		@Override
		protected void okPressed() {
			text = comboField.getText() + FIELD_SEPARATOR + textField.getText();
			super.okPressed();
		}
	}

	public RepositoriesFieldEditor() {
		super();
	}

	public RepositoriesFieldEditor(String name, String labelText,
			Composite parent) {
		super(name, labelText, parent);
	}

	@Override
	protected String createList(String[] items) {
		return Joiner.on(LINE_SEPARATOR).join(items);
	}

	@Override
	protected String getNewInputObject() {
		NewInputDialog dialog = new NewInputDialog(getShell());
		dialog.setBlockOnOpen(true);

		if (dialog.open() == Window.OK) {
			return dialog.getText();
		}

		return null;
	}

	@Override
	protected String[] parseString(String stringList) {
		return Iterables.toArray(Splitter.on(LINE_SEPARATOR).split(stringList),
				String.class);
	}

}
