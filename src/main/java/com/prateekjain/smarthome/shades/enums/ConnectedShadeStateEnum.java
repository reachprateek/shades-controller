package com.prateekjain.smarthome.shades.enums;

public enum ConnectedShadeStateEnum implements IntTypesafeEnum {

	OFF(0, "OFF"), ON(1, "ON"), STOP(2, "STOP");

	public String getName() {
		return name;
	}

	public int getCode() {
		return code;
	}

	private final String name;
	private final int code;

	private ConnectedShadeStateEnum(final int code, final String name) {
		this.code = code;
		this.name = name;
	}

	@Override
	public int toInt() {
		return code;
	}
	
    @Override
    public String toString() {
        return name;
    }
	
}
