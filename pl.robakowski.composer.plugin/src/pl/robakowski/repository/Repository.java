//******************************************************************
//
//  Repository.java
//  Copyright 2014 PSI AG. All rights reserved.
//  PSI PROPRIETARY/CONFIDENTIAL. Use is subject to license terms
//
// ******************************************************************

package pl.robakowski.repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.di.extensions.Preference;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.json.JSONObject;

public class Repository implements IRepository {
	private static final String JSON = "{\"repositories\":[{\"packagist\":false}, {\"type\":\"%type\", \"url\":\"%url\"}]}";
	private boolean moreResults;
	private String query;
	private final String json;
	private final String type;
	private final String url;

	@Inject
	@Preference(nodePath = "composer.search", value = "phpPath")
	private String phpPath;

	@Inject
	@Preference(nodePath = "composer.search", value = "composerPath")
	private String composerPath;

	@Inject
	private Shell shell;

	@Inject
	private UISynchronize sync;

	@Inject
	private ECommandService commandService;

	@Inject
	private EHandlerService handlerService;

	@Inject
	public Repository(@Named("type") String type, @Named("url") String url) {
		this.type = type;
		this.url = url;
		json = JSON.replace("%type", type).replace("%url", url);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pl.robakowski.repository.IRepository#hasMoreResults()
	 */
	@Override
	public boolean hasMoreResults() {
		return moreResults;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pl.robakowski.repository.IRepository#setQuery(java.lang.String)
	 */
	@Override
	public void setQuery(String query) {
		moreResults = true;
		this.query = query;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pl.robakowski.repository.IRepository#getNextResults()
	 */
	@Override
	public List<JSONObject> getNextResults(final IProgressMonitor monitor) {
		moreResults = false;
		List<JSONObject> list = new ArrayList<JSONObject>(30);
		if (!checkRuntime()) {
			sync.asyncExec(new Runnable() {
				@Override
				public void run() {
					MessageDialog dialog = new MessageDialog(shell,
							"Wrong paths", null,
							"Invalid path to PHP or composer.phar",
							MessageDialog.ERROR, new String[] { "OK" }, 0);
					dialog.setBlockOnOpen(true);
					dialog.open();
					Map<String, String> params = new HashMap<String, String>();
					params.put("preferencePageId",
							"pl.robakowski.composer.plugin.page1");
					ParameterizedCommand command = commandService
							.createCommand("org.eclipse.ui.window.preferences",
									params);
					handlerService.executeHandler(command);
				}
			});
			return list;
		}

		writeJson(json);

		try {
			final Process exec = new ProcessBuilder().command(phpPath,
					composerPath, "search", query).start();
			Thread killer = new Thread() {

				private boolean terminated(Process exec) {
					try {
						exec.exitValue();
						return true;
					} catch (IllegalThreadStateException e) {
						return false;
					}
				}

				@Override
				public void run() {
					while (!terminated(exec)) {
						if (monitor.isCanceled()) {
							exec.destroy();
						}
						try {
							Thread.sleep(50);
						} catch (InterruptedException e) {
						}
					}
				};
			};
			killer.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					exec.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				int space = line.indexOf(' ');
				String name = line.substring(0, space);
				String repository = line.substring(space + 1);
				JSONObject obj = new JSONObject();
				obj.put("name", name);
				obj.put("description", repository);
				list.add(obj);
			}
			exec.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	protected static void writeJson(String json) {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter("composer.json"));
			writer.write(json);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
				}
			}
		}
	}

	@Override
	public String toString() {
		return url + "(type: " + type + ")";
	}

	public boolean checkRuntime() {
		try {
			Process exec = new ProcessBuilder().command(phpPath, composerPath)
					.start();
			byte[] head = new byte[9];
			InputStream inputStream = exec.getInputStream();
			inputStream.read(head);
			inputStream.close();
			boolean check = !new String(head, "UTF-8").startsWith("Could not");
			exec.waitFor();
			return check;
		} catch (Exception e1) {
			return false;
		}
	}
}
