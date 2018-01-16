package com.logpie.dba.sample;

import com.logpie.dba.api.repository.JDBCTemplateRepository;

public class SampleRepoForAGIntIdModel extends JDBCTemplateRepository<SampleModelWithAGIntId>{

    public SampleRepoForAGIntIdModel() { super(SampleModelWithAGIntId.class); }
}
