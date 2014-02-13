package pl.robakowski.repository;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.jface.viewers.ILazyContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.json.JSONException;
import org.json.JSONObject;

public class RepositoryContentProvider implements ILazyContentProvider {

	private final UISynchronize sync;
	private final LookBehindIterator<IRepository> it;
	private final TableViewer result;
	private final AtomicInteger filled = new AtomicInteger();
	private final AtomicBoolean running = new AtomicBoolean();
	private Job job;

	@Inject
	public RepositoryContentProvider(UISynchronize sync,
			@Named("repositories") List<IRepository> repositories,
			TableViewer result) {
		this.sync = sync;
		this.result = result;
		it = new LookBehindIterator<IRepository>(repositories.iterator());
	}

	@PreDestroy
	@Override
	public void dispose() {
		if (job != null) {
			job.cancel();
		}
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	@Override
	public void updateElement(final int index) {
		if (running.get() || filled.get() != index) {
			return;
		}
		setRunning(true);
		JSONObject obj = new JSONObject();
		try {
			obj.put("name", "Loading more results...");
		} catch (JSONException e) {
		}
		result.replace(obj, index);
		job = new Job("Loading results") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				loadObjects(index, monitor);
				setRunning(false);
				return Status.OK_STATUS;
			}
		};
		job.schedule();
	}

	public void setRunning(boolean run) {
		running.set(run);
	}

	public void loadObjects(int index, final IProgressMonitor monitor) {
		while (filled.get() <= index) {
			while ((it.last() == null || !it.last().hasMoreResults())
					&& it.hasNext()) {
				it.next();
			}

			if (monitor.isCanceled()) {
				return;
			}

			if (it.last() == null || !it.last().hasMoreResults()) {
				sync.asyncExec(new Runnable() {
					@Override
					public void run() {
						result.setItemCount(filled.get());
					}
				});
				return;
			}

			if (monitor.isCanceled()) {
				return;
			}

			monitor.beginTask(it.last().toString(), IProgressMonitor.UNKNOWN);

			final List<JSONObject> nextResults = it.last().getNextResults(
					monitor);

			monitor.done();

			final int in = filled.getAndAdd(nextResults.size());

			if (monitor.isCanceled()) {
				return;
			}
			sync.asyncExec(new Runnable() {
				@Override
				public void run() {
					int i = in;
					result.setItemCount(filled.get() + 1);
					for (JSONObject next : nextResults) {
						result.replace(next, i++);
					}
				}
			});
		}
	}
}
