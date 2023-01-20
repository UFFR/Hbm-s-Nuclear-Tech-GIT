package com.hbm.interfaces;

import com.hbm.util.I18nUtil;

public interface ILocalizable
{
	public String getUloc();
	
	public default String getLoc()
	{
		return I18nUtil.resolveKey(getUloc());
	}
}
