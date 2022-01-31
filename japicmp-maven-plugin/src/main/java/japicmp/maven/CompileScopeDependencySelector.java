package japicmp.maven;

import org.apache.maven.plugin.logging.Log;
import org.eclipse.aether.collection.DependencyCollectionContext;
import org.eclipse.aether.collection.DependencySelector;
import org.eclipse.aether.graph.Dependency;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * A {@link DependencySelector} that excludes test dependencies, as well as all transitive provided/optional
 * dependencies. Direct provided/optional dependencies are included.
 */
class CompileScopeDependencySelector implements DependencySelector {

	private final Log log;
	private final boolean isTopLevel;

	CompileScopeDependencySelector(Log log) {
		this(true, log);
	}

	private CompileScopeDependencySelector(boolean isTopLevel, Log log) {
		this.isTopLevel = isTopLevel;
		this.log = log;
	}

	@Override
	public boolean selectDependency(Dependency dependency) {
		boolean include = isNoTestDependency(dependency);
		logSelection(dependency, include, isTopLevel ? 0 : 1, log);
		return include;
	}

	@Override
	public DependencySelector deriveChildSelector(DependencyCollectionContext context) {
		logSelection(context.getDependency(), true, 0, log);
		return isTopLevel
				? new CompileScopeDependencySelector(false, log)
				: new CompileScopeTransitiveDependencySelector(2, log);
	}

	private static class CompileScopeTransitiveDependencySelector implements DependencySelector {

		private final Log log;
		private final int level;

		private CompileScopeTransitiveDependencySelector(int level, Log log) {
			this.level = level;
			this.log = log;
		}

		@Override
		public boolean selectDependency(Dependency dependency) {
			boolean include = isNoTestDependency(dependency) && isNoProvidedDependency(dependency) && !dependency.isOptional();
			logSelection(dependency, include, level, log);
			return include;
		}

		@Override
		public DependencySelector deriveChildSelector(DependencyCollectionContext context) {
			return new CompileScopeTransitiveDependencySelector(level + 1, log);
		}
	}

	private static void logSelection(Dependency dependency, boolean sign, int level, Log log) {
		final String indentation = IntStream.range(0, level).mapToObj(ignored -> "\t").collect(Collectors.joining());
		log.debug((sign ? "+" : "-") + " " + indentation + dependency);
	}

	private static boolean isNoTestDependency(Dependency dependency) {
		return !"test".equalsIgnoreCase(dependency.getScope());
	}

	private static boolean isNoProvidedDependency(Dependency dependency) {
		return !"provided".equalsIgnoreCase(dependency.getScope());
	}
}
