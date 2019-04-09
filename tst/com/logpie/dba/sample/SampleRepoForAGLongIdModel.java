package com.logpie.dba.sample;

import com.logpie.dba.core.JDBCTemplateRepository;

public class SampleRepoForAGLongIdModel extends JDBCTemplateRepository<SampleModelWithAGLongId> {

    public SampleRepoForAGLongIdModel() { super(SampleModelWithAGLongId.class); }
}
