package com.nsg.collector.service.snmp;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.Vector;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.Counter32;
import org.snmp4j.smi.Counter64;
import org.snmp4j.smi.Gauge32;
import org.snmp4j.smi.IpAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.Opaque;
import org.snmp4j.smi.TimeTicks;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.UnsignedInteger32;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.DefaultPDUFactory;
import org.snmp4j.util.TableEvent;
import org.snmp4j.util.TableUtils;
import org.snmp4j.util.TreeUtils;

import com.nsg.collector.service.snmp.annotation.MibObjectType;
import com.nsg.collector.service.snmp.mibobject.IfTable;
import com.nsg.collector.service.snmp.mibobject.SnmpObject;
import com.nsg.core.constant.SmiType;

public class SnmpV2Util implements SnmpUtil {
	private String community = "public";
	private String strAddress = "127.0.0.1";
	private int port = 161;
	private CommunityTarget comtarget;
	private Snmp snmp;
	private long _timeout = 35000;
	private int _retries = 3;
	private int maxRepetitions = 4;

	public SnmpV2Util(String _strAddress, String _community, int _retries)
			throws IOException {
		this.strAddress = _strAddress;
		if (_community != null && _community.trim().length() > 0) {
			this.community = _community;
		}
		if (_retries > 1) {
			this._retries = _retries;
		}
		init();
	}

	public boolean isBaseOidHasValue(String baseOidStr) {
		PDU pdu = new PDU();
		OID oid = new OID(baseOidStr);
		try {
			TreeUtils treeUtils = new TreeUtils(snmp, new DefaultPDUFactory());
			List events = treeUtils.getSubtree(comtarget, oid);
			if (!events.isEmpty() && events.size() > 1) {
				return true;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	public void init() throws IOException {
		OctetString osCommunity = new OctetString(this.community);
		String address = strAddress + "/" + port;
		Address targetaddress = new UdpAddress(address);
		TransportMapping transport = new DefaultUdpTransportMapping();
		snmp = new Snmp(transport);
		snmp.listen();
		comtarget = new CommunityTarget();
		comtarget.setCommunity(osCommunity);
		comtarget.setVersion(SnmpConstants.version2c);
		comtarget.setAddress(targetaddress);
		comtarget.setRetries(_retries);
		comtarget.setTimeout(_timeout);

	}

	public int snmpGetInt(String strOID) throws IOException {
		Object ob = snmpGet(strOID, SmiType.INTEGER);
		return (Integer) ob;
	}

	public long snmpGetLong(String strOID) throws IOException {
		Object ob = snmpGet(strOID, SmiType.INTEGER);
		return (Integer) ob;
	}

	public String snmpGetString(String strOID) throws IOException {
		Object ob = snmpGet(strOID, SmiType.DISPLAY_STRING);
		return (String) ob;
	}

	public VariableBinding snmpGet(String strOID) throws IOException {
		OID oid=new OID(strOID);
		return snmpGet(oid);
	}
	
	public VariableBinding snmpGet(OID strOID) throws IOException {
		PDU pdu = createPDU(strOID);
		ResponseEvent response = snmp.get(pdu, comtarget);
		VariableBinding result = getResult(response);
		return result;
	}

	private PDU createPDU(OID strOID) {
		PDU pdu = new PDU();
		pdu.add(new VariableBinding(strOID));
		return pdu;
	}

	
	private PDU createPDU(String strOID) {
		OID oid=new OID(strOID);
		return createPDU(oid);
	}

	public Object snmpGet(String strOID, SmiType type) throws IOException {
		PDU pdu = createPDU(strOID);
		ResponseEvent response = snmp.get(pdu, comtarget);
		return getValue(type, response);
	}

	private Object getValue(SmiType type, ResponseEvent response)
			throws IOException {
		VariableBinding result = getResult(response);
		return getValue(type, result);
	}

	private Object getValue(SmiType type, VariableBinding result)
			throws IOException {
		Variable variable = result.getVariable();
		if (variable.isException()) {
			throw new IOException("vb is null and " + result.getVariable());
		}
		if (type == SmiType.INTEGER) {
			return variable.toInt();
		} else if (type == SmiType.DISPLAY_STRING) {
			return new String(((OctetString) variable).getValue());
		} else if (type == SmiType.OID) {
			return ((OID) variable).toString();
		} else if (type == SmiType.OCTET_STRING) {
			return ((OctetString) variable).getValue();
		} else if (type == SmiType.UNSIGNED32) {
			return ((UnsignedInteger32) variable).getValue();
		} else if (type == SmiType.COUNTER32) {
			return ((Counter32) variable).getValue();
		} else if (type == SmiType.GAUGE32) {
			return ((Gauge32) variable).getValue();
		} else if (type == SmiType.TIMETICKS) {
			return ((TimeTicks) variable).getValue();
		} else if (type == SmiType.COUNTER64) {
			return ((Counter64) variable).getValue();
		} else if (type == SmiType.OPAQUE) {
			return ((Opaque) variable).toString();
		} else if (type == SmiType.IPADDRESS) {
			return ((IpAddress) variable).toString();
		} else if (type == SmiType.PHYSADDRESS) {
			return variable.toString();
		} else {
			throw new RuntimeException("Unknow smiType: " + type);
		}
	}

	@Override
	public <T> List<T> getTable(Class<T> aclass) throws IOException {
		Field[] fields = aclass.getDeclaredFields();
		List<OID> oids = new ArrayList<OID>();
		Map<String, Field> maps = new HashMap<String, Field>();
		for (Field field : fields) {
			boolean flag = field.isAnnotationPresent(MibObjectType.class);
			if (flag) {
				MibObjectType mibobjecttype = field
						.getAnnotation(MibObjectType.class);
				oids.add(new OID(mibobjecttype.oid()));
				maps.put(mibobjecttype.oid(), field);
			}
		}
		OID[] colums = (OID[]) oids.toArray(new OID[oids.size()]);
		List<TableEvent> events = snmpGetTable(colums, null, null);
		List<T> ts = new ArrayList<T>();
		for (TableEvent tableEvent : events) {
			try {
				T t = aclass.newInstance();
				VariableBinding[] vbs = tableEvent.getColumns();
				for (int i = 0; i < vbs.length; i++) {
					if (tableEvent.isError()) {
						throw new RuntimeException("table get error");
					}
					if (vbs[i] == null) {
						// throw new RuntimeException("vb is null");
						System.err.println("vb is null at" + i);
						continue;
					}
					VariableBinding vb = vbs[i];
					OID oid = vb.getOid();
					for (String keyoid : maps.keySet()) {
						if (oid.startsWith(new OID(keyoid))) {
							Field field = maps.get(keyoid);
							MibObjectType mibobjecttype = field
									.getAnnotation(MibObjectType.class);
							Object o = getValue(mibobjecttype.type(), vb);
							field.setAccessible(true);
							field.set(t, o);
						}
					}
				}
				ts.add(t);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}

		}
		return ts;
	}

	@Override
	public <T> List<T> getTable(Class<T> aclass, List<String> indexes)
			throws IOException {
		List<T> ts= new ArrayList<T>();
		for (String index: indexes) {
			T t=get(aclass,index);
			ts.add(t);
		}
		return ts;
	}

	@Override
	public  <T> List<T> getTable(Class<T> aclass, List<String> indexes,
			String[] fields)throws IOException{
		List<T> ts= new ArrayList<T>();
		for (String index: indexes) {
			T t=get(aclass,index,fields);
			ts.add(t);
		}
		return ts;
	}

	@Override
	public <T> T get(Class<T> aclass) throws IOException {
		Field[] fields = aclass.getDeclaredFields();
		List<OID> oids = new ArrayList<OID>();
		Map<OID, Field> maps = new HashMap<OID, Field>();
		for (Field field : fields) {
			boolean flag = field.isAnnotationPresent(MibObjectType.class);
			if (flag) {
				MibObjectType mibobjecttype = field
						.getAnnotation(MibObjectType.class);
				OID oid=new OID(mibobjecttype.oid());
				oids.add(oid);
				maps.put(oid, field);
			}
		}
		List<VariableBinding> vbs = snmpGet(oids);
		try {
			T t = aclass.newInstance();
			for (OID oid : maps.keySet()) {
				VariableBinding vb = getVb(vbs, oid);
				Field field = maps.get(oid);
				MibObjectType mibobjecttype = field
						.getAnnotation(MibObjectType.class);
				Object o = getValue(mibobjecttype.type(), vb);
				field.setAccessible(true);
				field.set(t, o);
			}
			return t;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return null;
	}

	public <T> T get(Class<T> aclass,String index) throws IOException {
		Field[] fields = aclass.getDeclaredFields();
		List<OID> oids = new ArrayList<OID>();
		Map<String, Field> maps = new HashMap<String, Field>();
		for (Field field : fields) {
			boolean flag = field.isAnnotationPresent(MibObjectType.class);
			if (flag) {
				MibObjectType mibobjecttype = field
						.getAnnotation(MibObjectType.class);
				String oidstr=mibobjecttype.oid()+"."+index;
				oids.add(new OID(oidstr));
				maps.put(oidstr, field);
			}
		}
		List<VariableBinding> vbs = snmpGet(oids);
		try {
			T t = aclass.newInstance();
			for (String oid : maps.keySet()) {
				VariableBinding vb = getVb(vbs, oid);
				Field field = maps.get(oid);
				MibObjectType mibobjecttype = field
						.getAnnotation(MibObjectType.class);
				Object o = getValue(mibobjecttype.type(), vb);
				field.setAccessible(true);
				field.set(t, o);
			}
			return t;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public <T> T get(Class<T> aclass,String index,String[] strfields) throws IOException{
		List<Field> fields=new ArrayList<Field>();
		for (String fieldname : strfields) {
			try {
				Field f =  aclass.getDeclaredField(fieldname);
				fields.add(f);
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
		}
		List<OID> oids = new ArrayList<OID>();
		Map<String, Field> maps = new HashMap<String, Field>();
		for (Field field : fields) {
			boolean flag = field.isAnnotationPresent(MibObjectType.class);
			if (flag) {
				MibObjectType mibobjecttype = field
						.getAnnotation(MibObjectType.class);
				String oidstr=mibobjecttype.oid()+"."+index;
				oids.add(new OID(oidstr));
				maps.put(oidstr, field);
			}
		}
		List<VariableBinding> vbs = snmpGet(oids);
		try {
			T t = aclass.newInstance();
			for (String oid : maps.keySet()) {
				VariableBinding vb = getVb(vbs, oid);
				Field field = maps.get(oid);
				MibObjectType mibobjecttype = field
						.getAnnotation(MibObjectType.class);
				Object o = getValue(mibobjecttype.type(), vb);
				field.setAccessible(true);
				field.set(t, o);
			}
			return t;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return null;
	}
	public VariableBinding snmpGetNext(String strOID) throws IOException {
		PDU pdu = createPDU(strOID);
		ResponseEvent response = snmp.getNext(pdu, comtarget);
		VariableBinding result = getResult(response);
		return result;
	}


	@Override
	public List<VariableBinding> snmpGet(List<OID> oids) throws IOException {
		PDU pdu = new PDU();
		for (OID strOID : oids) {
			pdu.add(new VariableBinding(strOID));
			pdu.setType(PDU.GET);
		}
		List<VariableBinding> result = getResults(pdu);
		return result;
	}
	@Override
	public void snmpGetValues(List<SnmpObject> snmpobs) throws IOException {
		List<OID> oids = new ArrayList<OID>();
		for (SnmpObject snmpObject : snmpobs) {
			oids.add(new OID(snmpObject.getOid()));
		}
		List<VariableBinding> vbs = snmpGet(oids);
		for (SnmpObject snmpObject : snmpobs) {
			String oid = snmpObject.getOid();
			VariableBinding vb = getVb(vbs, oid);
			if (vb != null) {
				Object object = getValue(snmpObject.getType(), vb);
				snmpObject.setValue(object);
			}
		}
	}

	private VariableBinding getVb(List<VariableBinding> vbs, String oid) {
		for (VariableBinding vb : vbs) {
			if (vb.getOid().toString().startsWith(oid)) {
				return vb;
			}
		}
		return null;
	}
	
	private VariableBinding getVb(List<VariableBinding> vbs, OID oid) {
		for (VariableBinding vb : vbs) {
			if (vb.getOid().startsWith(oid)) {
				return vb;
			}
		}
		return null;
	}

	private VariableBinding getResult(ResponseEvent response)
			throws IOException {
		List<VariableBinding> results = getResults(response);
		if (results.size() < 1) {
			throw new IOException("Snmp failed get value " + this.getAddress());
		}
		return results.get(0);

	}

	private List<VariableBinding> getResults(ResponseEvent response)
			throws IOException {
		PDU pduresponse = response.getResponse();
		if (pduresponse == null) {
			throw new IOException("Snmp failed connect to " + this.getAddress());
		}
		List<VariableBinding> result = new ArrayList<VariableBinding>();

		int errorStatus = pduresponse.getErrorStatus();
		if (errorStatus == PDU.noError) {
			result = new ArrayList<VariableBinding>(
					pduresponse.getVariableBindings());
		} else {
			System.err.println("response is " + errorStatus);
			throw new IOException("response error is " + errorStatus
					+ " address is " + strAddress);
		}

		return result;
	}

	private List<VariableBinding> getResults(PDU pdu) throws IOException {

		ResponseEvent response = snmp.get(pdu, comtarget);
		if (response == null) {
			System.err.println("Feeling like a TimeOut occured ");
			throw new IOException("time out! Snmp failed connect to "
					+ this.getAddress());
		}
		return getResults(response);
	}

	@Override
	public long snmpGetMaxValue(String strOID) throws IOException {
		OID[] oids = new OID[1];
		oids[0] = new OID(strOID);
		long result = 0;
		List tablevalue = this.snmpGetTable(oids, null, null);
		for (int i = 0; i < tablevalue.size(); i++) {
			TableEvent event = (TableEvent) tablevalue.get(i);
			long temp = event.getColumns()[0].getVariable().toInt();
			if (temp > result) {
				result = temp;
			}
		}
		return result;
	}

	@Override
	public List<TableEvent> snmpGetTable(OID[] columnOIDs, OID lowerBoundIndex,
			OID upperBoundIndex) throws IOException {
		List<TableEvent> result = new ArrayList<TableEvent>();
		TableUtils tableUtils = new TableUtils(this.snmp,
				new DefaultPDUFactory(PDU.GETBULK));
		tableUtils.setMaxNumRowsPerPDU(maxRepetitions);
		result = tableUtils.getTable(this.comtarget, columnOIDs,
				lowerBoundIndex, upperBoundIndex);
		return result;
	}

	public void close() {
		if (snmp == null)
			return;
		try {
			snmp.close();
			snmp = null;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			snmp = null;
		}
	}

	public long get_timeout() {
		return _timeout;
	}

	public void setTimeOut(long timeout) {
		this._timeout = timeout;
	}

	public int get_retries() {
		return _retries;
	}

	public void set_retries(int _retries) {
		this._retries = _retries;
	}

	public String getAddress() {
		return strAddress;
	}

	public static void main(String[] args) throws IOException {
		SnmpUtil snmpUtil = new SnmpV2Util("127.0.0.1", "public", 1);
		long numbers = snmpUtil.snmpGetLong(".1.3.6.1.2.1.1.7.0");
		System.out.println(numbers);
	}
}
