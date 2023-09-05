package org.wtech.concatgrouper.model;

import org.wtech.concatgrouper.appender.BaseRecord;
import org.wtech.concatgrouper.appender.RecordField;

public class SC150 extends BaseRecord {

    public SC150(String totalDocumentAmount, String totalCurrentBalance) {
        fields.add(new RecordField("SC150", 1, 5));
        fields.add(new RecordField(totalDocumentAmount, 6, 17));  // 12 characters
        fields.add(new RecordField(totalCurrentBalance, 18, 29)); // 12 characters
    }
    
    @Override
    public String toString() {

		return generateRecord();
    }
}
