package com.logpie.dba.sample;

import com.logpie.dba.api.annotation.AutoGenerate;
import com.logpie.dba.api.annotation.Column;
import com.logpie.dba.api.annotation.ID;
import com.logpie.dba.api.basic.Model;

public class SampleModelWithAGLongId extends Model {

    @ID
    @AutoGenerate(strategy = AutoGenerate.AutoGenerateType.NumberAutoIncrement)
    @Column(name = "SAMPLE_MODEL_ID", type = Column.DataType.LONG)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
