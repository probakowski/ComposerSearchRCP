package pl.robakowski.preferences;

import org.eclipse.jface.preference.IPreferencePageContainer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;


public class DelegatingWorkbenchPage implements IWorkbenchPreferencePage {
	private final PreferencePage preferencePage;

	public DelegatingWorkbenchPage() {
		preferencePage = new ComposerSearchPreferencePage();
	}

	@Override
	public int hashCode() {
		return preferencePage.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return preferencePage.equals(obj);
	}

	@Override
	public Point computeSize() {
		return preferencePage.computeSize();
	}

	@Override
	public void dispose() {
		preferencePage.dispose();
	}

	@Override
	public Control getControl() {
		return preferencePage.getControl();
	}

	@Override
	public void createControl(Composite parent) {
		preferencePage.createControl(parent);
	}

	@Override
	public String getDescription() {
		return preferencePage.getDescription();
	}

	@Override
	public String getErrorMessage() {
		return preferencePage.getErrorMessage();
	}

	@Override
	public Image getImage() {
		return preferencePage.getImage();
	}

	@Override
	public String getMessage() {
		return preferencePage.getMessage();
	}

	public int getMessageType() {
		return preferencePage.getMessageType();
	}

	public Shell getShell() {
		return preferencePage.getShell();
	}

	@Override
	public String getTitle() {
		return preferencePage.getTitle();
	}

	@Override
	public void setDescription(String description) {
		preferencePage.setDescription(description);
	}

	@Override
	public void setImageDescriptor(ImageDescriptor desc) {
		preferencePage.setImageDescriptor(desc);
	}

	public void setMessage(String newMessage) {
		preferencePage.setMessage(newMessage);
	}

	public IPreferencePageContainer getContainer() {
		return preferencePage.getContainer();
	}

	public IPreferenceStore getPreferenceStore() {
		return preferencePage.getPreferenceStore();
	}

	@Override
	public boolean isValid() {
		return preferencePage.isValid();
	}

	@Override
	public void setVisible(boolean visible) {
		preferencePage.setVisible(visible);
	}

	@Override
	public boolean okToLeave() {
		return preferencePage.okToLeave();
	}

	@Override
	public boolean performCancel() {
		return preferencePage.performCancel();
	}

	@Override
	public boolean performOk() {
		return preferencePage.performOk();
	}

	@Override
	public void setContainer(IPreferencePageContainer container) {
		preferencePage.setContainer(container);
	}

	public void setPreferenceStore(IPreferenceStore store) {
		preferencePage.setPreferenceStore(store);
	}

	@Override
	public void setSize(Point uiSize) {
		preferencePage.setSize(uiSize);
	}

	@Override
	public void setTitle(String title) {
		preferencePage.setTitle(title);
	}

	public void setValid(boolean b) {
		preferencePage.setValid(b);
	}

	@Override
	public String toString() {
		return preferencePage.toString();
	}

	@Override
	public void performHelp() {
		preferencePage.performHelp();
	}

	public void applyData(Object data) {
		preferencePage.applyData(data);
	}

	public void setErrorMessage(String newMessage) {
		preferencePage.setErrorMessage(newMessage);
	}

	public void setMessage(String newMessage, int newType) {
		preferencePage.setMessage(newMessage, newType);
	}

	@Override
	public void init(IWorkbench workbench) {
	}

}
