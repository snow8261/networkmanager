package com.nsg.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nsg.collector.service.snmp.SnmpUtil;
import com.nsg.collector.service.snmp.SnmpV2Util;

@Configuration
public class SnmpConfig {
	@Bean
	public SnmpUtil getSnmpUtil(){
		//SnmpUtil snmp=new SnmpV2Util(_strAddress, _community, _retries);
		return null;
	}
}
