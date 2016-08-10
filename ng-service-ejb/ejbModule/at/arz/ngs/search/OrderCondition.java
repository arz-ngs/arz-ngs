package at.arz.ngs.search;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "order-condition")
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderCondition {

	public static final String ORDERBY_SERVICE = "service";
	public static final String ORDERBY_ENVIRONMENT = "environment";
	public static final String ORDERBY_HOST = "host";
	public static final String ORDERBY_SERVICEINSTANCE = "instance";

	public static final String ASCENDING = "ascending";
	public static final String DESCENDING = "descending";

	/**
	 * Can either be service, environment, host, instance. Default is instance.
	 */
	@XmlElement
	private String orderByField = ORDERBY_SERVICEINSTANCE;

	/**
	 * Can either be ascending or descending. Default is ascending.
	 */
	@XmlElement
	private String order = ASCENDING;

	/**
	 * Sets default values.
	 */
	public OrderCondition() {
		this.orderByField = ORDERBY_SERVICEINSTANCE;
		this.order = ASCENDING;
	}

	public OrderCondition(String orderByfield, String order) {
		this.orderByField = orderByfield;
		this.order = order;
	}

	public String getOrderByField() {
		return orderByField;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public void setOrderByField(String orderByField) {
		this.orderByField = orderByField;
	}

}
