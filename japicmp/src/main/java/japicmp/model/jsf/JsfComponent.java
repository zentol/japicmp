package japicmp.model.jsf;

import japicmp.model.JApiChangeStatus;
import japicmp.model.JApiClass;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class JsfComponent {
    private final JApiClass jApiClass;
    private final JsfComponentType jsfComponentType;
    private final JApiChangeStatus changeStatus;

    public JsfComponent(JApiChangeStatus changeStatus, JApiClass jApiClass, JsfComponentType jsfComponentType) {
        this.jApiClass = jApiClass;
        this.jsfComponentType = jsfComponentType;
        this.changeStatus = computeChangeStatus(changeStatus);
    }

    private JApiChangeStatus computeChangeStatus(JApiChangeStatus changeStatus) {
        if (changeStatus == JApiChangeStatus.UNCHANGED) {
            if(jsfComponentType.getChangeStatus() != JApiChangeStatus.UNCHANGED) {
                changeStatus = JApiChangeStatus.MODIFIED;
            }
        }
        return changeStatus;
    }

    @XmlAttribute(name = "changeStatus")
    public JApiChangeStatus getChangeStatus() {
        return changeStatus;
    }

    @XmlElement(name = "jsf-component-type")
    public JsfComponentType getJsfComponentType() {
        return jsfComponentType;
    }

    @XmlAttribute(name = "fullyQualifiedName")
    public String getFullyQualifiedName() {
        return jApiClass.getFullyQualifiedName();
    }
}
