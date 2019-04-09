package com.logpie.dba.sample;

import com.logpie.dba.annotation.AutoGenerate;
import com.logpie.dba.annotation.Column;
import com.logpie.dba.annotation.ID;
import com.logpie.dba.core.Model;

public class SampleModelWithAGStringId extends Model {
    @ID
    @AutoGenerate(strategy = AutoGenerate.AutoGenerateType.NumberAutoIncrement)
    @Column(label = "SAMPLE_MODEL_ID", type = Column.DataType.STRING)
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
