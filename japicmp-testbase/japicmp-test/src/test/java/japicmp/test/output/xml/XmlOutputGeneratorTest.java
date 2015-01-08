package japicmp.test.output.xml;

import static japicmp.test.util.Helper.getArchive;
import japicmp.cmp.JarArchiveComparator;
import japicmp.cmp.JarArchiveComparatorOptions;
import japicmp.cmp.jsf.JsfArchiveComparator;
import japicmp.config.Options;
import japicmp.model.JApiClass;
import japicmp.model.jsf.JsfComponent;
import japicmp.output.xml.XmlOutputGenerator;

import java.io.File;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.base.Optional;

public class XmlOutputGeneratorTest {
    private static List<JApiClass> jApiClasses;
	private static List<JsfComponent> jsfComponents;

	@BeforeClass
    public static void beforeClass() {
        JarArchiveComparator jarArchiveComparator = new JarArchiveComparator(new JarArchiveComparatorOptions());
		File oldArchive = getArchive("japicmp-test-v1.jar");
		File newArchive = getArchive("japicmp-test-v2.jar");
		jApiClasses = jarArchiveComparator.compare(oldArchive, newArchive);
		JsfArchiveComparator jsfArchiveComparator = new JsfArchiveComparator();
		jsfComponents = jsfArchiveComparator.compare(oldArchive, newArchive, jApiClasses);
	}

	@Test
	public void testHtmlOutput() {
		XmlOutputGenerator generator = new XmlOutputGenerator();
		Options options = new Options();
		options.setXmlOutputFile(Optional.of("target/diff.xml"));
		options.setHtmlOutputFile(Optional.of("target/diff.html"));
		generator.generate("/old/Path", "/new/Path", jApiClasses, options, jsfComponents);
	}
}
