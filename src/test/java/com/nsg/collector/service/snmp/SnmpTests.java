package com.nsg.collector.service.snmp;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.util.TableEvent;

import com.nsg.collector.service.snmp.mibobject.IfTable;
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

	// @Test
	// public void _6_test_snmp_list() throws Exception{
	// SnmpUtil snmpUtil = new SnmpV2Util("127.0.0.1", "public", 1);
	// OID[] oids=new OID[]{new OID("1.3.6.1.2.1.2.2.1.1"),new
	// OID("1.3.6.1.2.1.2.2.1.2")};
	// List list=snmpUtil.snmpGetTable(oids, null, null);
	// for (int i = 0; i <list.size(); i++) {
	// TableEvent tb= (TableEvent)list.get(i);
	// System.out.println("index:"+tb.getIndex());
	// System.out.println(tb.getColumns()[0].getOid());
	// System.out.println(tb.getColumns()[0].getVariable().toString());
	// System.out.println(tb.getColumns()[1].getOid());
	// System.out.println(new
	// String(((OctetString)tb.getColumns()[1].getVariable()).getValue()));
	// }
	// }

	// @Test
	public void _8_test_snmp_list() throws Exception {
		SnmpUtil snmpUtil = new SnmpV2Util("127.0.0.1", "public", 1);
		List<OID> oidlist = new ArrayList<OID>();
		oidlist.add(new OID("1.3.6.1.2.1.2.2.1.1"));
		oidlist.add(new OID("1.3.6.1.2.1.2.2.1.2"));
		oidlist.add(new OID("1.3.6.1.2.1.2.2.9.2"));
		oidlist.add(new OID("1.3.6.1.2.1.31.1.1.1.15"));

		OID[] oids = (OID[]) oidlist.toArray(new OID[oidlist.size()]);
		List list = snmpUtil.snmpGetTable(oids, null, null);
		for (int i = 0; i < list.size(); i++) {
			TableEvent tb = (TableEvent) list.get(i);
			System.out.println(tb.isError());
			System.out.println("length:" + tb.getColumns().length);
			System.out.println("index:" + tb.getIndex());
			System.out.println(tb.getColumns()[2]);
			System.out.println(tb.getColumns()[0].isException());
			System.out.println(tb.getColumns()[0]);
			System.out.println(tb.getColumns()[1]);
			System.out.println(tb.getColumns()[3]);
			// System.out.println(new
			// String(((OctetString)tb.getColumns()[1].getVariable()).getValue()));
			// System.out.println(tb.getColumns()[2].isException());
			// System.out.println(tb.getColumns()[2].getOid());
			// System.out.println(tb.getColumns()[2].getVariable());
		}
	}

	@Test
	public void _7_test_snmp_list() throws Exception {
		SnmpUtil snmpUtil = new SnmpV2Util("127.0.0.1", "public", 1);
		List<IfTable> iftables = snmpUtil.getTable(IfTable.class);
		for (IfTable ifTable : iftables) {
			System.out.println(ifTable);
		}

	}

	@Test
	public void _9_test_snmp_getTablewithindex() throws Exception {
		SnmpUtil snmpUtil = new SnmpV2Util("127.0.0.1", "public", 1);
		List<String> indexes=new ArrayList<String>();
		indexes.add("50");
		indexes.add("32");
		List<IfTable> iftables = snmpUtil.getTable(IfTable.class,indexes);
		for (IfTable ifTable : iftables) {
			System.out.println(ifTable);
		}
	}
	
	@Test
	public void _10_test_snmp_getTablewithindex_custom_field() throws Exception {
		SnmpUtil snmpUtil = new SnmpV2Util("127.0.0.1", "public", 1);
		List<String> indexes=new ArrayList<String>();
		indexes.add("50");
		indexes.add("32");
		String[] fields=new String[]{"ifInOctets","ifOutOctets"};
		List<IfTable> iftables = snmpUtil.getTable(IfTable.class,indexes,fields);
		for (IfTable ifTable : iftables) {
			System.out.println(ifTable);
		}
	}
	

}
