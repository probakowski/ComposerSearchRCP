package pl.robakowski.progress;

import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Text;

public class ProgressMonitor extends NullProgressMonitor {
	private final Composite parent;
	private final Job job;
	private final Text label;
	private final ProgressBar bar;
	private final AtomicInteger count;
	private final UISynchronize sync;
	private int id;

	public ProgressMonitor(Composite parent, Job job, Text label,
			ProgressBar bar, AtomicInteger count, UISynchronize sync) {
		this.parent = parent;
		this.job = job;
		this.label = label;
		this.bar = bar;
		this.count = count;
		this.sync = sync;

	}

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
}