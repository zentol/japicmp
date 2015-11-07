package japicmp.test;

import japicmp.cmp.JarArchiveComparator;
import japicmp.cmp.JarArchiveComparatorOptions;
import japicmp.cmp.Statistics;
import japicmp.model.AccessModifier;
import japicmp.model.JApiClass;
import org.junit.Test;

import java.text.NumberFormat;
import java.util.List;

import static japicmp.test.util.Helper.getArchive;

public class PerformanceTest {

	@Test
	public void testPerformanceTest() {
		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
		options.setAccessModifier(AccessModifier.PRIVATE);
		JarArchiveComparator jarArchiveComparator = new JarArchiveComparator(options);
		List<JApiClass> jApiClasses = jarArchiveComparator.compare(getArchive("japicmp-test-v1.jar"), getArchive("japicmp-test-v2.jar"));
		Statistics statistics = jarArchiveComparator.getStatistics();
		double timeForOneClass = (double) statistics.getComputationTimeInMillis() / (double) jApiClasses.size();
		System.out.println("Comparison took " + statistics.getComputationTimeInMillis() + " ms, i.e. it took " + NumberFormat.getInstance().format(timeForOneClass) + " ms for one class.");
		// 2015-11-07: Comparison took 314 ms, i.e. it took 1,847 ms for one class.
		// 2015-11-07: Comparison took 312 ms, i.e. it took 1,835 ms for one class.
		// 2015-11-07: Comparison took 272 ms, i.e. it took 1,6 ms for one class.
		// 2015-11-07: Comparison took 277 ms, i.e. it took 1,629 ms for one class.
	}

	@Test
	public void testPerformanceGuava() {
		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
		options.setAccessModifier(AccessModifier.PRIVATE);
		JarArchiveComparator jarArchiveComparator = new JarArchiveComparator(options);
		List<JApiClass> jApiClasses = jarArchiveComparator.compare(getArchive("guava-17.0.jar"), getArchive("guava-18.0.jar"));
		Statistics statistics = jarArchiveComparator.getStatistics();
		double timeForOneClass = (double) statistics.getComputationTimeInMillis() / (double) jApiClasses.size();
		System.out.println("Comparison took " + statistics.getComputationTimeInMillis() + " ms, i.e. it took " + NumberFormat.getInstance().format(timeForOneClass) + " ms for one class.");
		// 2015-11-07: Comparison took 314 ms, i.e. it took 1,847 ms for one class.
		// 2015-11-07: Comparison took 312 ms, i.e. it took 1,835 ms for one class.
		// 2015-11-07: Comparison took 272 ms, i.e. it took 1,6 ms for one class.
		// 2015-11-07: Comparison took 277 ms, i.e. it took 1,629 ms for one class.
	}
}
