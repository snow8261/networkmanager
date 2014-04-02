package com.nsg.config;

import org.springframework.context.annotation.Configuration;

import com.nsg.collector.service.snmp.SnmpUtil;

@Configuration
public class SnmpConfig {
//	@Bean
	public SnmpUtil getSnmpUtil(){
		//SnmpUtil snmp=new SnmpV2Util(_strAddress, _community, _retries);
		return null;
	}
}
