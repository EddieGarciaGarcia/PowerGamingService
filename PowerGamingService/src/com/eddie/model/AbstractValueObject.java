package com.eddie.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

public abstract class AbstractValueObject implements ValueObject{
	
	public String toString() { return ToStringBuilder.reflectionToString(this) ;}
}
