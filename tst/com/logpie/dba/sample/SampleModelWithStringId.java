package com.logpie.dba.sample;

import com.logpie.dba.annotation.Column;
import com.logpie.dba.annotation.ID;
import com.logpie.dba.core.Model;

public class SampleModelWithStringId extends Model {
    @ID
    @Column(label = "SAMPLE_MODEL_ID", type = Column.DataType.STRING)
    private String id;

    public SampleModelWithStringId() {
        this.id = "";
    }

    public SampleModelWithStringId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
