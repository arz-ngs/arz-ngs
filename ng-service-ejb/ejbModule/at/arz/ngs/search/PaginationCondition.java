package at.arz.ngs.search;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PaginationCondition {

	public static final int SHOW_ALL = Integer.MAX_VALUE;

	/**
	 * The size of the list/page which should be returned. Range from 1 to Integer.MAX_VALUE
	 */
	private int elementsPerPage;

	/**
	 * The number of the current page starting at number 1! Range from 1 to Integer.MAX_VALUE
	 */
	private int currentPage;

	/**
	 * Sets default values.
	 */
	public PaginationCondition() {
		this.elementsPerPage = 50;
		this.currentPage = 1;
	}

	public PaginationCondition(int elementsPerPage, int currentPage) {
		this.elementsPerPage = elementsPerPage;
		this.currentPage = currentPage;
	}

	public int getElementsPerPage() {
		return elementsPerPage;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setElementsPerPage(int elementsPerPage) {
		this.elementsPerPage = elementsPerPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

}
