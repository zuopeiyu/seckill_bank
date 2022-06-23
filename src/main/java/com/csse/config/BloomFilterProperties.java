package com.csse.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "bf")
public class BloomFilterProperties {
	/**
	 * 预期插入量
	 */
	private Long expectedInsertions = 1000L;
	/**
	 * 误判率（大于0，小于1.0）
	 */
	private Double fpp = 0.001D;
 
	public Long getExpectedInsertions() {
		return expectedInsertions;
	}
 
	public void setExpectedInsertions(Long expectedInsertions) {
		this.expectedInsertions = expectedInsertions;
	}
 
	public Double getFpp() {
		return fpp;
	}
 
	public void setFpp(Double fpp) {
		this.fpp = fpp;
	}
}