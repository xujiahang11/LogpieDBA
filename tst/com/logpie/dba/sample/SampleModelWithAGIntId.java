package com.logpie.dba.sample;

import com.logpie.dba.api.annotation.AutoGenerate;
import com.logpie.dba.api.annotation.Column;
import com.logpie.dba.api.annotation.ID;
import com.logpie.dba.api.basic.Model;


public class SampleModelWithAGIntId extends Model {

    @ID
    @AutoGenerate(strategy = AutoGenerate.AutoGenerateType.NumberAutoIncrement)
    @Column(name = "SAMPLE_MODEL_ID", type = Column.DataType.INTEGER)
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
