//******************************************************************
//
//  ComposerSearchPart.java
//  Copyright 2014 PSI AG. All rights reserved.
//  PSI PROPRIETARY/CONFIDENTIAL. Use is subject to license terms
//
// ******************************************************************

package pl.robakowski;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.extensions.Preference;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import pl.robakowski.repository.IRepository;
import pl.robakowski.repository.PackagistRepository;
import pl.robakowski.repository.Repository;
import pl.robakowski.repository.RepositoryContentProvider;

import com.google.common.base.Splitter;

public class ComposerSearchPart {
	private TableViewer result;
	private List<IRepository> repositories;

	@Inject
	private IEclipseContext context;

	@Inject
	private UISynchronize sync;
	private Text query;

	@PostConstruct
	public void create(Composite parent) {
		parent.setLayout(GridLayoutFactory.fillDefaults().numColumns(2)
				.create());

		new Label(parent, SWT.NONE).setText("Query:");
		query = new Text(parent, SWT.BORDER);
		query.setLayoutData(GridDataFactory.fillDefaults().grab(true, false)
				.create());
		query.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent aE) {
				setQuery(query.getText());
			}
		});

		result = new TableViewer(parent, SWT.VIRTUAL | SWT.BORDER
				| SWT.FULL_SELECTION);
		Table table = result.getTable();
		table.setHeaderVisible(true);
		table.setLayoutData(GridDataFactory.fillDefaults().grab(true, true)
				.span(2, 1).create());
		TableViewerColumn nameColumn = new TableViewerColumn(result, SWT.NONE);
		nameColumn.setLabelProvider(new ArrayLabelProvider<String>(0));
		nameColumn.getColumn().setText("Name");
		nameColumn.getColumn().setWidth(200);
		TableViewerColumn descriptionColumn = new TableViewerColumn(result,
				SWT.NONE);
		descriptionColumn.setLabelProvider(new ArrayLabelProvider<String>(1));
		descriptionColumn.getColumn().setText("Description");
		descriptionColumn.getColumn().setWidth(200);

		context.set(TableViewer.class, result);
	}

	@Inject
	public void setRepositories(
			@Preference(nodePath = "composer.search", value = "usePackagist") boolean usePackagist,
			@Preference(nodePath = "composer.search", value = "repositories") String repositoriesString) {
		repositories = new LinkedList<IRepository>();
		if (repositoriesString != null) {
			for (String repo : Splitter.on("|").omitEmptyStrings()
					.split(repositoriesString)) {
				int space = repo.indexOf(' ');
				String type = repo.substring(0, space);
				String url = repo.substring(space + 1);
				IEclipseContext child = context.createChild();
				child.set("type", type);
				child.set("url", url);
				repositories.add(ContextInjectionFactory.make(Repository.class,
						child));
			}
		}

		if (usePackagist) {
			repositories.add(new PackagistRepository());
		}

		context.set("repositories", repositories);

		if (query != null && !query.isDisposed()) {
			query.setText("");
		}
	}

	protected void setQuery(String text) {
		result.setItemCount(0);
		for (IRepository repo : repositories) {
			repo.setQuery(text);
		}
		result.setContentProvider(ContextInjectionFactory.make(
				RepositoryContentProvider.class, context));
		boolean empty = text == null || text.trim().isEmpty();
		result.setItemCount(empty ? 0 : 1);
	}
}
