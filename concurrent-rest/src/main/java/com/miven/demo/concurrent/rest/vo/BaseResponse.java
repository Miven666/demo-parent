package com.miven.demo.concurrent.rest.vo;

import java.io.Serializable;


/**
 * @author mingzhi.xie
 */

public class BaseResponse implements Serializable {

	private static final long serialVersionUID = -1079473662654884435L;

	protected String resultCode;
	protected String resultDesc;

	public String getResultCode() {
		return resultCode;
	}

	public String getResultDesc() {
		return resultDesc;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public void setResultDesc(String resultDesc) {
		this.resultDesc = resultDesc;
	}
}
