package com.prateekjain.smarthome.shades;

import com.amazonaws.services.iot.client.AWSIotDevice;
import com.amazonaws.services.iot.client.AWSIotDeviceProperty;

public class ConnectedShade extends AWSIotDevice {

	public ConnectedShade(String thingName) {
		super(thingName);
	}
	
	@AWSIotDeviceProperty
	private boolean shadeOpen;

	public boolean isShadeOpen() {
		  System.out.println(
	                System.currentTimeMillis() + " >>> reported shade state: " + (shadeOpen ? "open" : "closed"));
		return shadeOpen;
	}

	public void setShadeOpen(boolean shadeOpen) {
		this.shadeOpen = shadeOpen;
	     System.out.println(
	                System.currentTimeMillis() + " <<< desired shade state to " + (shadeOpen ? "open" : "closed"));
	}
	

}
