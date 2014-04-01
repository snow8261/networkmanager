/**
 *
 */

package com.nsg.collector.service.snmp;

import java.io.IOException;
import java.util.List;

import org.snmp4j.PDU;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.util.TableEvent;

import com.nsg.collector.service.snmp.mibobject.IfTable;
import com.nsg.collector.service.snmp.mibobject.SnmpObject;
import com.nsg.collector.service.snmp.mibobject.SystemInfo;
import com.nsg.core.constant.SmiType;

/**
 * author litongjie
 */
public interface SnmpUtil {

	public boolean isBaseOidHasValue(String baseOidStr);

	public void init() throws IOException;

	public int snmpGetInt(String strOID) throws IOException;

	public long snmpGetLong(String strOID) throws IOException;

	public String snmpGetString(String strOID) throws IOException;

	public VariableBinding snmpGet(String strOID) throws IOException;

	public VariableBinding snmpGetNext(String string) throws IOException;

	public long snmpGetMaxValue(String strOID) throws IOException;

	public List<VariableBinding> snmpGet(List<OID> oids) throws IOException;

	public void snmpGetValues(List<SnmpObject> snmpobs) throws IOException;

	public void close();
	
	public String getAddress();
	
	public void setTimeOut(long timeout);
	
	public void set_retries(int _retries);
	
	public Object snmpGet(String string, SmiType type) throws IOException;
	
	public List<TableEvent> snmpGetTable(OID[] columnOIDs, OID lowerBoundIndex,
			OID upperBoundIndex) throws IOException;


	
	public <T> T get(Class<T> aclass) throws IOException;
	
	public <T> T get(Class<T> aclass,String index) throws IOException;
	
	public <T> T get(Class<T> aclass,String index,String[] fields) throws IOException;
	
	public <T> List<T> getTable(Class<T> class1, List<String> indexes)
			throws IOException;

	public <T> List<T> getTable(Class<T> class1, List<String> indexes,
			String[] fields)throws IOException;

	public <T> List<T> getTable(Class<T> aClass) throws IOException; 
}
