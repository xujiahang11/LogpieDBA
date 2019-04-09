package com.logpie.dba.sample;

import com.logpie.dba.core.JDBCTemplateRepository;

public class SampleRepoForStringIdModel extends JDBCTemplateRepository<SampleModelWithStringId> {

    public SampleRepoForStringIdModel() { super(SampleModelWithStringId.class); }
}
