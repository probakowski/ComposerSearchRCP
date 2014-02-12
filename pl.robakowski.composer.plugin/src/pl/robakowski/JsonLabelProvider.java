package pl.robakowski;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.json.JSONObject;

public class JsonLabelProvider extends ColumnLabelProvider {
	private final String property;

	public JsonLabelProvider(String property) {
		this.property = property;
	}

	@Override
	public String getText(Object element) {
		return ((JSONObject) element).optString(property, "");
	}
}
