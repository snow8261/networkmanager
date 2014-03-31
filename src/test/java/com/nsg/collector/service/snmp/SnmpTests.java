package com.nsg.collector.service.snmp;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.util.TableEvent;

import com.nsg.collector.service.snmp.annotation.MibObjectType;
import com.nsg.collector.service.snmp.mibobject.SnmpObject;
import com.nsg.collector.service.snmp.mibobject.SystemInfo;
import com.nsg.core.constant.SmiType;

public class SnmpTests {

	@Test
	public void _1_test_snmp_local() throws Exception {
		SnmpUtil snmpUtil = new SnmpV2Util("127.0.0.1", "public", 1);
		long numbers = snmpUtil.snmpGetLong("1.3.6.1.2.1.1.7.0");
		assertEquals(76, numbers);
	}

	@Test
	public void _2_test_snmp_local() throws Exception {
		SnmpUtil snmpUtil = new SnmpV2Util("127.0.0.1", "public", 1);
		VariableBinding vb = snmpUtil.snmpGet("1.3.6.1.2.1.1.7.0");
		assertEquals(76 + "", vb.getVariable().toString());
	}

	@Test
	public void _3_test_snmp_getnext_local() throws Exception {
		SnmpUtil snmpUtil = new SnmpV2Util("127.0.0.1", "public", 1);
		VariableBinding vb = snmpUtil.snmpGetNext("1.3.6.1.2.1.1.6.0");
		assertEquals(76 + "", vb.getVariable().toString());
	}

	@Test
	public void _4_test_snmp_getlist_local() throws Exception {
		SnmpUtil snmpUtil = new SnmpV2Util("127.0.0.1", "public", 1);
		List<SnmpObject> snmpobs = createSysinfo();
		snmpUtil.snmpGetValues(snmpobs);
		assertEquals(
				"Hardware: Intel64 Family 6 Model 42 Stepping 7 AT/AT COMPATIBLE - Software: Windows Version 6.1 (Build 7601 Multiprocessor Free)",
				snmpobs.get(0).getValue());
		assertEquals("1.3.6.1.4.1.311.1.1.3.1.1", snmpobs.get(1).getValue());
	}

	@Test
	public void _5_test_systeminfo_local() throws Exception {
		SnmpUtil snmpUtil = new SnmpV2Util("127.0.0.1", "public", 1);
		SystemInfo info = snmpUtil.get(SystemInfo.class);
		System.out.println(info);
	}

	private List<SnmpObject> createSysinfo() {
		List<SnmpObject> snmpobs = new ArrayList<SnmpObject>();
		SnmpObject sysDesc = new SnmpObject("1.3.6.1.2.1.1.1.0",
				SmiType.DISPLAY_STRING, "");
		SnmpObject sysObjecid = new SnmpObject("1.3.6.1.2.1.1.2.0",
				SmiType.OID, "");
		snmpobs.add(sysDesc);
		snmpobs.add(sysObjecid);
		return snmpobs;
	}

	@Test
	public void _5_test_snmp_local() throws Exception {
		SnmpUtil snmpUtil = new SnmpV2Util("127.0.0.1", "public", 1);
		Integer vb = (Integer) snmpUtil.snmpGet("1.3.6.1.2.1.1.7.0",
				SmiType.INTEGER);
		assertEquals(76, vb.intValue());
	}

	@Test
	public void _6_test_snmp_list() throws Exception{
		SnmpUtil snmpUtil = new SnmpV2Util("127.0.0.1", "public", 1);
		OID[] oids=new OID[]{new OID("1.3.6.1.2.1.2.2.1.1"),new OID("1.3.6.1.2.1.2.2.1.2")};
		List list=snmpUtil.snmpGetTable(oids, null, null);
		for (int i = 0; i <list.size(); i++) {
	 		 TableEvent tb= (TableEvent)list.get(i);
	 		 System.out.println("index:"+tb.getIndex());
	 		 System.out.println(tb.getColumns()[0].getOid());
	 		 System.out.println(tb.getColumns()[0].getVariable().toString());
	 		 System.out.println(tb.getColumns()[1].getOid());
	 		 System.out.println(new String(((OctetString)tb.getColumns()[1].getVariable()).getValue()));
		}
	}
	
	@Test
	public void _7_test_snmp_list() throws Exception{
		SnmpUtil snmpUtil = new SnmpV2Util("127.0.0.1", "public", 1);
		
		
	}
	// @Test
	// public void _2_test_snmp_getNext_local() throws Exception {
	// SnmpUtil snmpUtil=new SnmpV2Util("127.0.0.1","public",1);
	// long numbers =snmpUtil.snmpGetLong("1.3.6.1.2.1.1.5.0");
	// assertEquals(76,numbers);
	// }
}
