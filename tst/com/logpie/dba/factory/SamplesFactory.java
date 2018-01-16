package com.logpie.dba.factory;

import com.logpie.dba.api.annotation.Column;
import com.logpie.dba.api.basic.Model;
import com.logpie.dba.api.repository.JDBCTemplateRepository;
import com.logpie.dba.sample.*;

public class SamplesFactory {

    public static Model createSampleModelWithAutoGeneratedId(Column.DataType idType) {

        switch (idType) {
            case BIGINT: {
                return new SampleModelWithAGBigIntId();
            }
            case LONG: {
                return new SampleModelWithAGLongId();
            }
            case INTEGER: {
                return new SampleModelWithAGIntId();
            }
            default: {
                throw new IllegalArgumentException(
                        "ID with @AutoGenerate should use either BigInteger or Long or Integer as DataType");
            }
        }
    }

    public static Model createSampleModelWithId(Column.DataType idType, Object id) {

        switch (idType) {
            case STRING: {
                if(id instanceof String)
                    return new SampleModelWithStringId((String) id);
                throw new IllegalArgumentException("Argument 'id' is expected to be String.");
            }
            default: {
                // TODO: add more sample models
                return null;
            }
        }
    }

    public static JDBCTemplateRepository createSampleRepo(Model model) {

        if(model instanceof SampleModelWithAGBigIntId) {
            return new SampleRepoForAGBigIntIdModel();
        }else if(model instanceof SampleModelWithAGIntId) {
            return new SampleRepoForAGIntIdModel();
        }else if(model instanceof SampleModelWithAGLongId) {
            return new SampleRepoForAGLongIdModel();
        }else if(model instanceof SampleModelWithStringId) {
            return new SampleRepoForStringIdModel();
        }

        return null;
    }
}