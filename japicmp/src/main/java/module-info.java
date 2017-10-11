module japicmp {
	exports japicmp.cmp;
	exports japicmp.compat;
	exports japicmp.exception;
	exports japicmp.model;
	exports japicmp.output;
	requires java.xml.bind;
	requires java.logging;
	requires guava;
	requires javassist;
	requires airline;
	requires javax.inject;
}
