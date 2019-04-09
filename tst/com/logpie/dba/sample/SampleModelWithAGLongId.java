package com.logpie.dba.sample;

import com.logpie.dba.annotation.AutoGenerate;
import com.logpie.dba.annotation.Column;
import com.logpie.dba.annotation.ID;
import com.logpie.dba.core.Model;

public class SampleModelWithAGLongId extends Model {

    @ID
    @AutoGenerate(strategy = AutoGenerate.AutoGenerateType.NumberAutoIncrement)
    @Column(label = "SAMPLE_MODEL_ID", type = Column.DataType.LONG)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
