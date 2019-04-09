package com.logpie.dba.sample;

import com.logpie.dba.annotation.AutoGenerate;
import com.logpie.dba.annotation.Column;
import com.logpie.dba.annotation.Entity;
import com.logpie.dba.annotation.ID;
import com.logpie.dba.core.Model;

import java.math.BigInteger;
import java.sql.Timestamp;

@Entity(table = "SAMPLE_TABLE")
public class SampleModelWithAGBigIntId extends Model {
    @ID
    @AutoGenerate(strategy = AutoGenerate.AutoGenerateType.NumberAutoIncrement)
    @Column(label = "SAMPLE_MODEL_ID", type = Column.DataType.BIGINT)
    private BigInteger id;

    @Column(label = "SAMPLE_MODEL_MESSAGE", type = Column.DataType.STRING)
    private String message;

    @Column(label = "SAMPLE_MODEL_IS_TRUE", type = Column.DataType.BOOLEAN)
    private Boolean isTrue;

    @Column(label = "SAMPLE_MODEL_INT_NUMBER", type = Column.DataType.INTEGER)
    private Integer intNumber;

    @Column(label = "SAMPLE_MODEL_LONG_NUMBER", type = Column.DataType.LONG)
    private Long longNumber;

    @Column(label = "SAMPLE_MODEL_FLOAT_NUMBER", type = Column.DataType.FLOAT)
    private Float floatNumber;

    @AutoGenerate(strategy = AutoGenerate.AutoGenerateType.CurrentTime)
    @Column(label = "SAMPLE_MODEL_TIMESTAMP", type = Column.DataType.TIMESTAMP)
    private Timestamp timestamp;

    private String messageWithPrivateGetterAndSetter = "private message";

    private String messageWithNoGetterAndSetter = "message with no getter and setter";

    private String illegalArgumentException = "illegal argument exception";


    public BigInteger getId() { return id; }

    public void setId(BigInteger id) { this.id = id; }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getIsTrue() {
        return isTrue;
    }

    public void setIsTrue(Boolean isTrue) {
        this.isTrue = isTrue;
    }

    public Integer getIntNumber() {
        return intNumber;
    }

    public void setIntNumber(Integer intNumber) {
        this.intNumber = intNumber;
    }

    public Long getLongNumber() {
        return longNumber;
    }

    public void setLongNumber(Long longNumber) {
        this.longNumber = longNumber;
    }

    public Float getFloatNumber() {
        return floatNumber;
    }

    public void setFloatNumber(Float floatNumber) {
        this.floatNumber = floatNumber;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    private String getMessageWithPrivateGetterAndSetter() {
        return messageWithPrivateGetterAndSetter;
    }

    private void setMessageWithPrivateGetterAndSetter(String messageWithPrivateGetterAndSetter) {
        this.messageWithPrivateGetterAndSetter = messageWithPrivateGetterAndSetter;
    }

    public String checkPrivateMessageForTest() {
        return getMessageWithPrivateGetterAndSetter();
    }

    public String checkNoSetterMessageForTest() {
        return messageWithNoGetterAndSetter;
    }

    public String getIllegalArgumentException() throws IllegalArgumentException {
        throw new IllegalArgumentException();
    }

}
