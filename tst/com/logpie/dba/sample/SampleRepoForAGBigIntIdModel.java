package com.logpie.dba.sample;

import com.logpie.dba.core.JDBCTemplateRepository;

public class SampleRepoForAGBigIntIdModel extends JDBCTemplateRepository<SampleModelWithAGBigIntId>{

    public SampleRepoForAGBigIntIdModel() {
        super(SampleModelWithAGBigIntId.class);
    }
}
