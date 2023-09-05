package org.wtech.concatgrouper.appender;

public class LineWriterController {

    private StringBuilder stringBuilder;

    public LineWriterController() {
        this.stringBuilder = new StringBuilder();
    }

    public void writeLine(String line) {
        stringBuilder.append(line).append(System.lineSeparator());
    }

    public String getContent() {
        return stringBuilder.toString();
    }

    // You might also add a method to reset the StringBuilder if necessary.
    public void reset() {
        stringBuilder.setLength(0);
    }
}
