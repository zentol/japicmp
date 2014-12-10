package japicmp.model.jsf;

import com.google.common.base.Optional;

import japicmp.model.JApiChangeStatus;
import japicmp.util.OptionalHelper;

import javax.xml.bind.annotation.XmlAttribute;

public class JsfComponentType {
	private final JApiChangeStatus changeStatus;
	private final Optional<String> oldType;
	private final Optional<String> newType;

	public JsfComponentType(JApiChangeStatus changeStatus, Optional<String> oldType, Optional<String> newType) {
		this.changeStatus = changeStatus;
		this.oldType = oldType;
		this.newType = newType;
	}

	@XmlAttribute(name = "changeStatus")
	public JApiChangeStatus getChangeStatus() {
		return changeStatus;
	}

	@XmlAttribute(name = "oldValue")
	public String getOldValue() {
		return OptionalHelper.optionalToString(oldType);
	}

	@XmlAttribute(name = "newValue")
	public String getNewValue() {
		return OptionalHelper.optionalToString(newType);
	}
}
