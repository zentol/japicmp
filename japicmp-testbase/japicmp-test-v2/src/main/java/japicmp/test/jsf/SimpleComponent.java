package japicmp.test.jsf;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIOutput;

/**
 * See http://docs.oracle.com/javaee/6/tutorial/doc/bnavu.html
 */
@FacesComponent("SimpleComponent")
public class SimpleComponent extends UIOutput {

	enum PropertyKeys {
		alt
	}

	public String getAlt() {
		return (String) getStateHelper().eval(PropertyKeys.alt, null);
	}

	public void setAlt(String alt) {
		getStateHelper().put(PropertyKeys.alt, alt);
	}
}
