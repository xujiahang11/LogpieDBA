package com.logpie.dba.sample;

import com.logpie.dba.core.JDBCTemplateRepository;

public class SampleRepoForAGIntIdModel extends JDBCTemplateRepository<SampleModelWithAGIntId>{

    public SampleRepoForAGIntIdModel() { super(SampleModelWithAGIntId.class); }
}
