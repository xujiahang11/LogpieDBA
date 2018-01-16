package com.logpie.dba.sample;

import com.logpie.dba.api.repository.JDBCTemplateRepository;

public class SampleRepoForAGLongIdModel extends JDBCTemplateRepository<SampleModelWithAGLongId> {

    public SampleRepoForAGLongIdModel() { super(SampleModelWithAGLongId.class); }
}
