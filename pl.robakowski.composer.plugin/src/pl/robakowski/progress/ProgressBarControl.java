//******************************************************************
//
//  ProgressBar.java
//  Copyright 2014 PSI AG. All rights reserved.
//  PSI PROPRIETARY/CONFIDENTIAL. Use is subject to license terms
//
// ******************************************************************

package pl.robakowski.progress;

import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.ProgressProvider;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Text;

public class ProgressBarControl extends NullProgressMonitor {
	@Inject
	private UISynchronize sync;

	@PostConstruct
	public void create(final Composite composite) {
		composite.setLayout(GridLayoutFactory.fillDefaults().numColumns(2)
				.equalWidth(false).create());
		final Composite parent = new Composite(composite, SWT.NONE);
		parent.setLayoutData(GridDataFactory.fillDefaults().grab(true, false)
				.create());
		parent.setLayout(GridLayoutFactory.fillDefaults().numColumns(2)
				.create());

		final Text label = new Text(parent, SWT.NONE);
		final ProgressBar bar = new ProgressBar(parent, SWT.INDETERMINATE);
		bar.setLayoutData(GridDataFactory.fillDefaults().grab(true, false)
				.create());

		final AtomicInteger count = new AtomicInteger();
		Job.getJobManager().setProgressProvider(new ProgressProvider() {
			@Override
			public IProgressMonitor createMonitor(final Job job) {
				return new NullProgressMonitor() {
					private int id;

					@Override
					public void beginTask(final String name, int totalWork) {
						sync.asyncExec(new Runnable() {
							@Override
							public void run() {
								id = count.incrementAndGet();
								label.setText(job.getName());
								bar.setToolTipText(name);
								parent.setVisible(true);
								parent.layout();
							}
						});

					}

					@Override
					public void done() {
						sync.asyncExec(new Runnable() {
							@Override
							public void run() {
								if (count.get() == id) {
									parent.setVisible(false);
								}
							}
						});

					}
				};
			}
		});

		parent.setVisible(false);

		Label placeholder = new Label(composite, SWT.NONE);
		placeholder.setText(" ");
		placeholder.setLayoutData(GridDataFactory.fillDefaults().create());
	}
}
