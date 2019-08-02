package kr.co.crim.oss.rimdrive.common.service;

public class ReturnVO {
	String result = "";
	String resultCode = "";
	String message = "";

	public void setReturnInfo(String result,String message){		
	    	this.result = result;		
		this.message = message;
	}	
	
	public void setReturnInfo(String result, String resultCode,String message){
		this.result = result;
		this.resultCode = resultCode;
		this.message = message;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	

}
