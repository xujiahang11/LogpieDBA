package com.logpie.dba.sample;

import com.logpie.dba.annotation.AutoGenerate;
import com.logpie.dba.annotation.Column;
import com.logpie.dba.annotation.ID;
import com.logpie.dba.core.Model;


public class SampleModelWithAGIntId extends Model {

    @ID
    @AutoGenerate(strategy = AutoGenerate.AutoGenerateType.NumberAutoIncrement)
    @Column(label = "SAMPLE_MODEL_ID", type = Column.DataType.INTEGER)
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
