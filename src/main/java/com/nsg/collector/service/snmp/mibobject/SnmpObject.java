package com.nsg.collector.service.snmp.mibobject;

import com.nsg.core.constant.SmiType;

public class SnmpObject {
	private String oid;
	private Object value; 
	private SmiType type;
	private String name;
	public SnmpObject(String string, SmiType smitype,String name) {
		this.oid=string;
		this.type=smitype;
		this.name=name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}

	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public SmiType getType() {
		return type;
	}
	public void setType(SmiType type) {
		this.type = type;
	}
	
}
