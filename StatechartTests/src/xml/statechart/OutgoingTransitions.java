package xml.statechart;

import javax.xml.bind.annotation.XmlAttribute;  
import javax.xml.bind.annotation.XmlElement;  
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace = "xml.statechart.Statechart")
public class OutgoingTransitions {

	private String id;
	
	private String specification;
	
	private String target;

	@XmlAttribute
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@XmlAttribute
	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	@XmlAttribute
	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}
	
	public void printOutgoingTransitions() {
		System.out.println("OutgoingTransition "+specification+":");
		System.out.println("Id: "+id);
		System.out.println("Target: "+target);
	}
}
