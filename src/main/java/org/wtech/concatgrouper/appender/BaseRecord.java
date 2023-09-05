package org.wtech.concatgrouper.appender;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecord {
    protected List<RecordField> fields = new ArrayList<>();

    public String generateRecord() {
        StringBuilder record = new StringBuilder();
        for (RecordField field : fields) {
            record.append(field.getPaddedValue());
        }
        return record.toString();
    }
}
