package pl.robakowski.repository;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

public interface IRepository {

	public abstract boolean hasMoreResults();

	public abstract void setQuery(String query);

	public abstract List<String[]> getNextResults(IProgressMonitor monitor);
}