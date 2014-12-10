package japicmp.cmp.jsf;

import japicmp.model.JApiAnnotation;
import japicmp.model.JApiAnnotationElement;
import japicmp.model.JApiAnnotationElementValue;
import japicmp.model.JApiChangeStatus;
import japicmp.model.JApiClass;
import japicmp.model.jsf.JsfComponent;
import japicmp.model.jsf.JsfComponentType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Optional;

public class JsfArchiveComparator {
	public static final String FACES_COMPONENT_FQN = "javax.faces.component.FacesComponent";

	public List<JsfComponent> compare(File oldArchive, File newArchive, List<JApiClass> jApiClasses) {
		List<JsfComponent> jsfComponents = new ArrayList<>();
		for(JApiClass jApiClass : jApiClasses) {
			List<JApiAnnotation> annotations = jApiClass.getAnnotations();
			for(JApiAnnotation jApiAnnotation : annotations) {
				if(FACES_COMPONENT_FQN.equals(jApiAnnotation.getFullyQualifiedName())) {
					JApiChangeStatus changeStatusJsfComponent = JApiChangeStatus.UNCHANGED;
					if(jApiClass.getChangeStatus() == JApiChangeStatus.NEW) {
						changeStatusJsfComponent = JApiChangeStatus.NEW;
					} else if(jApiClass.getChangeStatus() == JApiChangeStatus.REMOVED) {
						changeStatusJsfComponent = JApiChangeStatus.REMOVED;
					}
					List<JApiAnnotationElement> elements = jApiAnnotation.getElements();
					for(JApiAnnotationElement jApiAnnotationElement : elements) {
						if("value".equals(jApiAnnotationElement.getName())) {
							JApiChangeStatus changeStatus = jApiAnnotationElement.getChangeStatus();
							switch(changeStatus) {
							case NEW:
								List<JApiAnnotationElementValue> newElementValues = jApiAnnotationElement.getNewElementValues();
								if(newElementValues.size() != 1) {
									continue;
								}
								String newValue = newElementValues.get(0).getValueString();
								JsfComponentType jsfComponentType = new JsfComponentType(JApiChangeStatus.NEW, Optional.<String>absent(), Optional.of(newValue));
								JsfComponent jsfComponent = new JsfComponent(changeStatusJsfComponent, jApiClass, jsfComponentType);
								jsfComponents.add(jsfComponent);
								break;
							case REMOVED:
								List<JApiAnnotationElementValue> oldElementValues = jApiAnnotationElement.getOldElementValues();
								if(oldElementValues.size() != 1) {
									continue;
								}
								String oldValue = oldElementValues.get(0).getValueString();
								jsfComponentType = new JsfComponentType(JApiChangeStatus.REMOVED, Optional.of(oldValue), Optional.<String>absent());
								jsfComponent = new JsfComponent(changeStatusJsfComponent, jApiClass, jsfComponentType);
								jsfComponents.add(jsfComponent);
								break;
							case MODIFIED:
								newElementValues = jApiAnnotationElement.getNewElementValues();
								if(newElementValues.size() != 1) {
									continue;
								}
								newValue = newElementValues.get(0).getValueString();
								oldElementValues = jApiAnnotationElement.getOldElementValues();
								if(oldElementValues.size() != 1) {
									continue;
								}
								oldValue = oldElementValues.get(0).getValueString();
								jsfComponentType = new JsfComponentType(JApiChangeStatus.MODIFIED, Optional.of(oldValue), Optional.of(newValue));
								jsfComponent = new JsfComponent(changeStatusJsfComponent, jApiClass, jsfComponentType);
								jsfComponents.add(jsfComponent);
								break;
							case UNCHANGED:
								newElementValues = jApiAnnotationElement.getNewElementValues();
								if(newElementValues.size() != 1) {
									continue;
								}
								newValue = newElementValues.get(0).getValueString();
								jsfComponentType = new JsfComponentType(JApiChangeStatus.UNCHANGED, Optional.of(newValue), Optional.of(newValue));
								jsfComponent = new JsfComponent(changeStatusJsfComponent, jApiClass, jsfComponentType);
								jsfComponents.add(jsfComponent);
								break;
							default:
								throw new IllegalStateException("Unsupported changeStatus: " + changeStatus);
							}
						}
					}
				}
			}
		}
		return jsfComponents;
	}
}
