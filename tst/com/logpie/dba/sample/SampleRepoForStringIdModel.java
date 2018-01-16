package com.logpie.dba.sample;

import com.logpie.dba.api.repository.JDBCTemplateRepository;

public class SampleRepoForStringIdModel extends JDBCTemplateRepository<SampleModelWithStringId> {

    public SampleRepoForStringIdModel() { super(SampleModelWithStringId.class); }
}
