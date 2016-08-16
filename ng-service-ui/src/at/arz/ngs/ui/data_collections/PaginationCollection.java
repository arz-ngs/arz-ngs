package at.arz.ngs.ui.data_collections;


public class PaginationCollection {

	public static final String ACTIVE = "active";
	public static final String DISABLED = "disabled";

	private boolean showLeftCaret = true;
	private boolean showRightCaret = true;

	private boolean showSecondElem = true;
	private boolean showThirdElem = true;
	private boolean showFourthElem = true;
	private boolean showFifthElem = true;

	private String firstElement = "1";
	private String secondElement = "2";
	private String thirdElement = "3";
	private String fourthElement = "4";
	private String fithElement = "10";

	private String leftCaretClass = DISABLED;
	private String firstElementClass = ACTIVE;
	private String secondElementClass;
	private String thirdElementClass;
	private String fourthElementClass;
	private String fifthElementClass;
	private String rightCaretClass;

	public boolean isShowLeftCaret() {
		return showLeftCaret;
	}

	public void setShowLeftCaret(boolean showLeftCaret) {
		this.showLeftCaret = showLeftCaret;
	}

	public boolean isShowRightCaret() {
		return showRightCaret;
	}

	public void setShowRightCaret(boolean showRightCaret) {
		this.showRightCaret = showRightCaret;
	}

	public boolean isShowSecondElem() {
		return showSecondElem;
	}

	public void setShowSecondElem(boolean showSecondElem) {
		this.showSecondElem = showSecondElem;
	}

	public boolean isShowThirdElem() {
		return showThirdElem;
	}

	public void setShowThirdElem(boolean showThirdElem) {
		this.showThirdElem = showThirdElem;
	}

	public boolean isShowFourthElem() {
		return showFourthElem;
	}

	public void setShowFourthElem(boolean showFourthElem) {
		this.showFourthElem = showFourthElem;
	}

	public boolean isShowFifthElem() {
		return showFifthElem;
	}

	public void setShowFifthElem(boolean showFifthElem) {
		this.showFifthElem = showFifthElem;
	}

	public String getFirstElement() {
		return firstElement;
	}

	public void setFirstElement(String firstElement) {
		this.firstElement = firstElement;
	}

	public String getSecondElement() {
		return secondElement;
	}

	public void setSecondElement(String secondElement) {
		this.secondElement = secondElement;
	}

	public String getThirdElement() {
		return thirdElement;
	}

	public void setThirdElement(String thirdElement) {
		this.thirdElement = thirdElement;
	}

	public String getFourthElement() {
		return fourthElement;
	}

	public void setFourthElement(String fourthElement) {
		this.fourthElement = fourthElement;
	}

	public String getFithElement() {
		return fithElement;
	}

	public void setFithElement(String fithElement) {
		this.fithElement = fithElement;
	}

	public String getLeftCaretClass() {
		return leftCaretClass;
	}

	public void setLeftCaretClass(String leftCaretClass) {
		this.leftCaretClass = leftCaretClass;
	}

	public String getFirstElementClass() {
		return firstElementClass;
	}

	public void setFirstElementClass(String firstElementClass) {
		this.firstElementClass = firstElementClass;
	}

	public String getSecondElementClass() {
		return secondElementClass;
	}

	public void setSecondElementClass(String secondElementClass) {
		this.secondElementClass = secondElementClass;
	}

	public String getThirdElementClass() {
		return thirdElementClass;
	}

	public void setThirdElementClass(String thirdElementClass) {
		this.thirdElementClass = thirdElementClass;
	}

	public String getFourthElementClass() {
		return fourthElementClass;
	}

	public void setFourthElementClass(String fourthElementClass) {
		this.fourthElementClass = fourthElementClass;
	}

	public String getFifthElementClass() {
		return fifthElementClass;
	}

	public void setFifthElementClass(String fifthElementClass) {
		this.fifthElementClass = fifthElementClass;
	}

	public String getRightCaretClass() {
		return rightCaretClass;
	}

	public void setRightCaretClass(String rightCaredClass) {
		this.rightCaretClass = rightCaredClass;
	}

	public static String getActive() {
		return ACTIVE;
	}

	public static String getDisabled() {
		return DISABLED;
	}


}
