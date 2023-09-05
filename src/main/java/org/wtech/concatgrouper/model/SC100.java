package org.wtech.concatgrouper.model;

import org.wtech.concatgrouper.appender.BaseRecord;
import org.wtech.concatgrouper.appender.RecordField;

public class SC100 extends BaseRecord {
	
    public SC100(String documentType, String documentNumber, String emissionDate, String dueDate, String documentAmount, String currentBalance) {
        fields.add(new RecordField("SC100", 1, 5));
        fields.add(new RecordField(documentType, 6, 17));
        fields.add(new RecordField(documentNumber, 18, 29)); // 12 characters
        fields.add(new RecordField(emissionDate, 30, 37));   // 8 characters
        fields.add(new RecordField(dueDate, 38, 45));       // 8 characters
        fields.add(new RecordField(documentAmount, 46, 57)); // 12 characters
        fields.add(new RecordField(currentBalance, 58, 69)); // 12 characters
    }
    
    @Override
    public String toString() {
        return generateRecord();
    }
}
