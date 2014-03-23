package com.nsg.collector.service.snmp;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.DefaultPDUFactory;
import org.snmp4j.util.TableEvent;
import org.snmp4j.util.TableUtils;
import org.snmp4j.util.TreeUtils;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class SnmpV2Util implements SnmpUtil {
    private String community = "public";
    private String strAddress = "127.0.0.1";
    private int port = 161;

    private CommunityTarget comtarget;
    private Snmp snmp;
    private long _timeout = 35000;
    private int _retries = 3;
    private int maxRepetitions = 4;

    public String getAddress() {
        return strAddress;
    }


    public SnmpV2Util(String _strAddress, String _community,int _retries) throws IOException {
        this.strAddress = _strAddress;
        if (_community != null && _community.trim().length() > 0) {
            this.community = _community;
        }
        if(_retries>1){
        	this._retries=_retries;
        }
        init();
    }
 

	/** {@inheritDoc}*/
	public boolean isBaseOidHasValue(String baseOidStr)  {
		PDU pdu = new PDU();
		OID oid = new OID(baseOidStr);
		try {
			TreeUtils treeUtils = new TreeUtils(snmp, new DefaultPDUFactory());
			List events = treeUtils.getSubtree(comtarget, oid);
			if (!events.isEmpty()&&events.size()>1) {
					return true;
				}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

    /** {@inheritDoc}*/
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

    /** {@inheritDoc}*/
    public int snmpGetInt(String strOID) throws IOException {
        VariableBinding vb = snmpGet(strOID);
        if(vb.getVariable().isException()){
        	throw new IOException("vb is null and "+vb.getVariable());
        }
        return vb.getVariable().toInt();
    }

    /** {@inheritDoc}*/
    public long snmpGetLong(String strOID) throws IOException {
        VariableBinding vb = snmpGet(strOID);
        if(vb.getVariable().isException()){// noSuchObject
        	throw new IOException("vb is null and "+vb.getVariable());
        }
        return vb.getVariable().toLong();
    }

    /** {@inheritDoc}*/
    public String snmpGetString(String strOID) throws IOException {
        VariableBinding vb = snmpGet(strOID);
        if(vb.getVariable().isException()){
        	throw new IOException("vb is null and "+vb.getVariable());
        }
        return vb.getVariable().toString();
    }


    /** {@inheritDoc}*/
    public VariableBinding snmpGet(String strOID) throws IOException {
        VariableBinding result = null;
        PDU pdu = createPDU();
        ResponseEvent response;

        pdu.add(new VariableBinding(new OID(strOID)));
        pdu.setType(PDU.GET);

        response = snmp.get(pdu, comtarget);
        if (response != null) {
            if (response.getResponse() == null) {
                throw new IOException("Snmp failed connect to "+this.getAddress());
            }
            if (response.getResponse().getErrorStatusText().
                equalsIgnoreCase("Success")) {
                PDU pduresponse = response.getResponse();
                result = (VariableBinding) pduresponse.getVariableBindings().
                         firstElement();
            }else{
            	throw new IOException("response error is "+response.getResponse().getErrorStatusText());
            }
        } else {
            System.out.println("Feeling like a TimeOut occured ");
            throw new IOException("time out! Snmp failed connect to "+this.getAddress());
        }

        return result;
    }

    public List<VariableBinding> snmpGet(List<String> oids) throws IOException{
	List<VariableBinding> result=null;
	  PDU pdu = createPDU();
	  ResponseEvent response;
	   for (String strOID : oids) {
	            pdu.add(new VariableBinding(new OID(strOID)));
	            pdu.setType(PDU.GET);
	        }
	   response = snmp.get(pdu, comtarget);
	   if (response != null) {
	            if (response.getResponse() == null) {
	                throw new IOException("Snmp failed connect to "+this.getAddress());
	            }
	            if (response.getResponse().getErrorStatusText().
	                equalsIgnoreCase("Success")) {
	                PDU pduresponse = response.getResponse();
	                result=new ArrayList<VariableBinding>(pduresponse.getVariableBindings());
	            }else{
	            	System.err.println("response is "+response.getResponse().getErrorStatusText());
	            	throw new IOException("response error is "+response.getResponse().getErrorStatusText()+" address is "+strAddress);
	            }
	        } else {
	            System.out.println("Feeling like a TimeOut occured ");
	            throw new IOException("time out! Snmp failed connect to "+this.getAddress());
	        }
	return result;
    }

    /** {@inheritDoc}*/
    public List snmpGet(Vector<String> strOIDs) throws IOException {
        Vector result = null;
        PDU pdu = createPDU();
        ResponseEvent response;

        for (String strOID : strOIDs) {
            pdu.add(new VariableBinding(new OID(strOID)));
            pdu.setType(PDU.GET);
        }
        response = snmp.get(pdu, comtarget);
        if (response != null) {
            if (response.getResponse().getErrorStatusText().equalsIgnoreCase("Success")) {
                PDU pduresponse = response.getResponse();
                result = pduresponse.getVariableBindings();
            }
        } else {
            System.out.println("Feeling like a TimeOut occured ");
        }

        return result;
    }
    
    public  long snmpGetMaxValue(String strOID) throws IOException{
   	 OID[] oids=new OID[1];
   	 oids[0]=new OID(strOID);
   	 long result = 0;
        List tablevalue = this.snmpGetTable(oids, null, null);
        for (int i = 0; i < tablevalue.size(); i++) {
            TableEvent event = (TableEvent)tablevalue.get(i);
            long temp=event.getColumns()[0].getVariable().toInt();
            if(temp>result){
            	result=temp;
            }
        }
        return result;
   }

    /** {@inheritDoc}*/
    public List snmpGetTable(OID[] columnOIDs, OID lowerBoundIndex, OID upperBoundIndex) throws IOException {
        List result = null;
        TableUtils tableUtils = new TableUtils(this.snmp, new DefaultPDUFactory(PDU.GETBULK));
        tableUtils.setMaxNumRowsPerPDU(maxRepetitions);
        result = tableUtils.getTable(this.comtarget, columnOIDs, lowerBoundIndex, upperBoundIndex);
        return result;
    }


    /** {@inheritDoc}*/
    public void close()  {
        if (snmp == null) return;
        try {
            snmp.close();
            snmp = null;
        } catch (Exception ex) {
            ex.printStackTrace();
        }finally{
            snmp = null;
        }
    }
    public static void main(String[] args) throws IOException {
	SnmpUtil snmpUtil=new SnmpV2Util("127.0.0.1","public",1);
	long numbers =snmpUtil.snmpGetLong(".1.3.6.1.2.1.1.7.0");
	System.out.println(numbers);
    }

    /** {@inheritDoc}*/
    public PDU createPDU() {
	return  new PDU();
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

}
