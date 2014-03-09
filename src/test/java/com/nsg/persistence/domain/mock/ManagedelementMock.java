package com.nsg.persistence.domain.mock;

import java.util.Date;

import com.nsg.persistence.domain.Managedelement;

public class ManagedelementMock {
	public static Managedelement getTestManagedelement(){
		Managedelement me=new Managedelement();
		me.setId(1000l);
		me.setCreateTime(new Date());
		return me;
	}
}
