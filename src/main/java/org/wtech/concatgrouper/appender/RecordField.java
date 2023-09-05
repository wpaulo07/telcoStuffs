package org.wtech.concatgrouper.appender;

public class RecordField {
	
    private String value;
    private int startPosition;
    private int endPosition;

    public RecordField(String value, int startPosition, int endPosition) {
        this.value = value;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }

    // getters, setters, and other methods...

    public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getStartPosition() {
		return startPosition;
	}

	public void setStartPosition(int startPosition) {
		this.startPosition = startPosition;
	}

	public int getEndPosition() {
		return endPosition;
	}

	public void setEndPosition(int endPosition) {
		this.endPosition = endPosition;
	}

	public String getPaddedValue() {
        return String.format("%-" + (endPosition - startPosition + 1) + "s", value);
    }
	

}
