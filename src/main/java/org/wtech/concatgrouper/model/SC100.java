package org.wtech.concatgrouper.model;

import org.wtech.concatgrouper.appender.BaseRecord;
import org.wtech.concatgrouper.appender.RecordField;

public class SC100 extends BaseRecord {
	
    public SC100(String documentType, String documentNumber, String emissionDate, String dueDate, String documentAmount, String currentBalance) {
        fields.add(new RecordField("SC100", 1, 5));
        fields.add(new RecordField(documentType, 6, 15));
        fields.add(new RecordField(documentNumber, 16, 27)); // 12 characters
        fields.add(new RecordField(emissionDate, 28, 35));   // 8 characters
        fields.add(new RecordField(dueDate, 36, 43));       // 8 characters
        fields.add(new RecordField(documentAmount, 44, 55)); // 12 characters
        fields.add(new RecordField(currentBalance, 56, 67)); // 12 characters
    }
    
    @Override
    public String toString() {
        return generateRecord();
    }
}
