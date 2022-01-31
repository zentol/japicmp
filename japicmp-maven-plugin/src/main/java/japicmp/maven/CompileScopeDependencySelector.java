/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.	See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.	You may obtain a copy of the License at
 *
 *		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package japicmp.maven;

import org.eclipse.aether.collection.DependencyCollectionContext;
import org.eclipse.aether.collection.DependencySelector;
import org.eclipse.aether.graph.Dependency;

/**
 * A {@link DependencySelector} that replicates the actual compile scope.
 * Specifically, it excludes all test dependencies and all transitive dependencies of provided/optional dependencies.
 * Note that direct provided/optional dependencies are included.
 */
class CompileScopeDependencySelector implements DependencySelector {

	@Override
	public boolean selectDependency(Dependency dependency) {
		return isNoTestDependency(dependency);
	}

	@Override
	public DependencySelector deriveChildSelector(DependencyCollectionContext context) {
		return isProvidedDependency(context.getDependency()) || context.getDependency().isOptional()
			? new CompileScopeTransitiveDependencySelector()
			: this;
	}

	private static class CompileScopeTransitiveDependencySelector implements DependencySelector {
		@Override
		public boolean selectDependency(Dependency dependency) {
			return isNoTestDependency(dependency) && !dependency.isOptional() && !isProvidedDependency(dependency);
		}

		@Override
		public DependencySelector deriveChildSelector(DependencyCollectionContext context) {
			return this;
		}
	}

	private static boolean isNoTestDependency(Dependency dependency) {
		return !"test".equalsIgnoreCase(dependency.getScope());
	}

	private static boolean isProvidedDependency(Dependency dependency) {
		return "provided".equalsIgnoreCase(dependency.getScope());
	}
}

