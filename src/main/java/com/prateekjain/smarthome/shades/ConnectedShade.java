package com.prateekjain.smarthome.shades;

import com.amazonaws.services.iot.client.AWSIotDevice;
import com.amazonaws.services.iot.client.AWSIotDeviceProperty;
import com.prateekjain.smarthome.shades.enums.ConnectedShadeStateEnum;

public class ConnectedShade extends AWSIotDevice {
	
	private final String onCode;
	private final String offCode;
	private final String stopCode;

	public ConnectedShade(final String thingName, final String onCode, final String offCode, final String stopCode) {
		super(thingName);
		this.onCode = onCode;
		this.offCode = offCode;
		this.stopCode = stopCode;
	}
	
	@AWSIotDeviceProperty
	private ConnectedShadeStateEnum state;
	
	public ConnectedShadeStateEnum getState() {
		System.out.println(
                System.currentTimeMillis() + " >>> reported shade state: " + state);
		return state;
	}

	public void setState(ConnectedShadeStateEnum state) {
		this.state = state;
	     System.out.println(
	                System.currentTimeMillis() + " <<< setting desired shade state to " + (this.state));		
	}
}
