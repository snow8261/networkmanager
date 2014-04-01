package com.nsg.collector.service.snmp.mibobject;

import com.nsg.collector.service.snmp.annotation.MibObjectType;
import com.nsg.core.constant.SmiType;

public class IfTable {
	@MibObjectType(oid = "1.3.6.1.2.1.2.2.1.1", type = SmiType.INTEGER) 
	private int ifIndex;
 
	@MibObjectType(oid = "1.3.6.1.2.1.2.2.1.2", type=SmiType.DISPLAY_STRING) 
	private String ifDescr;

	@MibObjectType(oid = "1.3.6.1.2.1.2.2.1.3", type=SmiType.INTEGER) 
	private int ifType;

	@MibObjectType(oid = "1.3.6.1.2.1.2.2.1.5", type=SmiType.GAUGE32) 
	private long ifSpeed;

	@MibObjectType(oid = "1.3.6.1.2.1.2.2.1.6", type=SmiType.PHYSADDRESS ) 
	private String ifPhysAddress;

	@MibObjectType(oid = "1.3.6.1.2.1.2.2.1.7", type=SmiType.INTEGER) 
	private int ifAdminStatus;

	@MibObjectType(oid = "1.3.6.1.2.1.2.2.1.8", type=SmiType.INTEGER) 
	private int ifOperStatus;

	@MibObjectType(oid = "1.3.6.1.2.1.2.2.1.9", type=SmiType.TIMETICKS) 
	private long ifLastChange;

	@MibObjectType(oid = "1.3.6.1.2.1.2.2.1.10", type=SmiType.COUNTER32) 
	private long ifInOctets;

	@MibObjectType(oid = "1.3.6.1.2.1.2.2.1.16", type=SmiType.COUNTER32) 
	private long ifOutOctets;

	public int getIfIndex() {
		return ifIndex;
	}

	public void setIfIndex(int ifIndex) {
		this.ifIndex = ifIndex;
	}

	public String getIfDescr() {
		return ifDescr;
	}

	public void setIfDescr(String ifDescr) {
		this.ifDescr = ifDescr;
	}

	public int getIfType() {
		return ifType;
	}

	public void setIfType(int ifType) {
		this.ifType = ifType;
	}

	public long getIfSpeed() {
		return ifSpeed;
	}

	public void setIfSpeed(long ifSpeed) {
		this.ifSpeed = ifSpeed;
	}

	public String getIfPhysAddress() {
		return ifPhysAddress;
	}

	public void setIfPhysAddress(String ifPhysAddress) {
		this.ifPhysAddress = ifPhysAddress;
	}

	public int getIfAdminStatus() {
		return ifAdminStatus;
	}

	public void setIfAdminStatus(int ifAdminStatus) {
		this.ifAdminStatus = ifAdminStatus;
	}

	public int getIfOperStatus() {
		return ifOperStatus;
	}

	public void setIfOperStatus(int ifOperStatus) {
		this.ifOperStatus = ifOperStatus;
	}

	public long getIfLastChange() {
		return ifLastChange;
	}

	public void setIfLastChange(long ifLastChange) {
		this.ifLastChange = ifLastChange;
	}

	public long getIfInOctets() {
		return ifInOctets;
	}

	public void setIfInOctets(long ifInOctets) {
		this.ifInOctets = ifInOctets;
	}

	public long getIfOutOctets() {
		return ifOutOctets;
	}

	public void setIfOutOctets(long ifOutOctets) {
		this.ifOutOctets = ifOutOctets;
	}

	@Override
	public String toString() {
		return "IfTable [ifIndex=" + ifIndex + ", ifDescr=" + ifDescr
				+ ", ifType=" + ifType + ", ifSpeed=" + ifSpeed
				+ ", ifPhysAddress=" + ifPhysAddress
				+ ", ifAdminStatus=" + ifAdminStatus + ", ifOperStatus="
				+ ifOperStatus + ", ifLastChange=" + ifLastChange
				+ ", ifInOctets=" + ifInOctets + ", ifOutOctets=" + ifOutOctets
				+ "]";
	}
	
	
}
