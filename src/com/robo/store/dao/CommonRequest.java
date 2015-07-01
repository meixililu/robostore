package com.robo.store.dao;

public class CommonRequest {
	private String softVer;
	private String channelId;
	private String platform;
	private String systemVer;
	private String machId;
	
	public String getSoftVer() {
		return softVer;
	}
	public void setSoftVer(String softVer) {
		this.softVer = softVer;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getSystemVer() {
		return systemVer;
	}
	public void setSystemVer(String systemVer) {
		this.systemVer = systemVer;
	}
	public String getMachId() {
		return machId;
	}
	public void setMachId(String machId) {
		this.machId = machId;
	}
}
