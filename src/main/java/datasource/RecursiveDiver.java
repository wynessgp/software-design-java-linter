package datasource;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.io.File;

public class RecursiveDiver implements StandardInput {
	private Iterator<String> iter;

	public RecursiveDiver(File path) {
		iter = findClassFiles(path).iterator();
	}

	private Set<String> findClassFiles(File path) {
		Set<String> result = new HashSet<String>();
		if (path.isDirectory()) {
			if (path.listFiles() == null) {
				return result;
			}
			for (File f : path.listFiles()) {
				result.addAll(findClassFiles(f));
			}
		} else if (path.getName().endsWith(".class")) {
			String currentPath = path.getPath()
					.replace("/", ".")
					.replace("\\", ".")
					.replace(".class", "");
			result.add(currentPath);
		}
		return result;
	}

	@Override
	public String nextLine() {
		return null;
	}

	@Override
	public boolean hasNext() {
		return iter.hasNext();
	}

	@Override
	public int nextInt() {
		return -1;
	}

	@Override
	public String next() {
		return hasNext() ? iter.next() : null;
	}

}