package com.logpie.dba.sample;

import com.logpie.dba.api.repository.JDBCTemplateRepository;

public class SampleRepoForAGBigIntIdModel extends JDBCTemplateRepository<SampleModelWithAGBigIntId>{

    public SampleRepoForAGBigIntIdModel() {
        super(SampleModelWithAGBigIntId.class);
    }
}
