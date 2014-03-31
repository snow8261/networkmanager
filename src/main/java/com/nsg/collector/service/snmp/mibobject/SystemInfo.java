package com.nsg.collector.service.snmp.mibobject;

import com.nsg.collector.service.snmp.annotation.MibObjectType;
import com.nsg.core.constant.SmiType;

public class SystemInfo {

	@MibObjectType(oid="1.3.6.1.2.1.1.1.0",type=SmiType.DISPLAY_STRING)
	private String sysDesc;
	
	@MibObjectType(oid="1.3.6.1.2.1.1.2.0",type=SmiType.OID)
	private String sysObjectId;
	
	@MibObjectType(oid = "1.3.6.1.2.1.1.3.0", type = SmiType.TIMETICKS)
	private long sysUpTime;

	@MibObjectType(oid = "1.3.6.1.2.1.1.4.0", type = SmiType.DISPLAY_STRING)
	private String sysContact;

	@MibObjectType(oid = "1.3.6.1.2.1.1.5.0", type = SmiType.DISPLAY_STRING)
	private String sysName;

	@MibObjectType(oid = "1.3.6.1.2.1.1.6.0", type = SmiType.DISPLAY_STRING)
	private String sysLocation;
	
	@MibObjectType(oid = "1.3.6.1.2.1.1.7.0", type = SmiType.INTEGER)
	private long sysService;

	@Override
	public String toString() {
		return "SystemInfo [sysDesc=" + sysDesc + ", sysObjectId="
				+ sysObjectId + ", sysUpTime=" + sysUpTime + ", sysContact="
				+ sysContact + ", sysName=" + sysName + ", sysLocation="
				+ sysLocation + ", sysService=" + sysService + "]";
	}

	public String getSysDesc() {
		return sysDesc;
	}

	public void setSysDesc(String sysDesc) {
		this.sysDesc = sysDesc;
	}

	public String getSysObjectId() {
		return sysObjectId;
	}

	public void setSysObjectId(String sysObjectId) {
		this.sysObjectId = sysObjectId;
	}

	public long getSysUpTime() {
		return sysUpTime;
	}

	public void setSysUpTime(long sysUpTime) {
		this.sysUpTime = sysUpTime;
	}

	public String getSysContact() {
		return sysContact;
	}

	public void setSysContact(String sysContact) {
		this.sysContact = sysContact;
	}

	public String getSysName() {
		return sysName;
	}

	public void setSysName(String sysName) {
		this.sysName = sysName;
	}

	public String getSysLocation() {
		return sysLocation;
	}

	public void setSysLocation(String sysLocation) {
		this.sysLocation = sysLocation;
	}

	public long getSysService() {
		return sysService;
	}

	public void setSysService(long sysService) {
		this.sysService = sysService;
	}




}
