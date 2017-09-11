package com.supermap.egispservice.area.exceptions;


public class AreaException extends Exception {

public static final String NULL_ALLOW_EXCEPTION = "参数[#]不允许为空"; 
	
	public static final String NOT_FOUND = "未找到[#]数据"; 

	public static final String CONVERT_EXCEPTION = "数据转换异常[#]";
	
	public static final String NO_DATA_UPDATED = "未发现可更新的数据";
	
	public static final String EXISTED_DATA = "[#]已使用";
	
	public static final String INNER_EXCEPTION = "服务内部异常[#]";
	
	private String message;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AreaException(){
		
	}
	public AreaException(String info){
		this.message = info;
	}
	
	public AreaException(ExceptionType type,String... infos){
		String info = infos[0]==null?"":infos[0];
		if(ExceptionType.NULL_NO_ALLOW.equals(type)){
			this.message = NULL_ALLOW_EXCEPTION.replaceAll("#", info);
		}else if(ExceptionType.NOT_FOUND.equals(type)){
			this.message = NOT_FOUND.replaceAll("#", info);
		}else if(ExceptionType.CONVERT_EXCEPTION.equals(type)){
			this.message = CONVERT_EXCEPTION.replaceAll("#", info);
		}else if(ExceptionType.EXISTED_DATA.equals(type)){
			this.message = EXISTED_DATA.replaceAll("#", info);
		}else if(ExceptionType.INNER_EXCEPTION.equals(type)){
			this.message = INNER_EXCEPTION.replaceAll("#", info);
		}else{
			this.message = info;
		}
	}
	
	public  enum ExceptionType{
		NULL_NO_ALLOW, // 不允许为空
		NOT_FOUND,
		CONVERT_EXCEPTION,
		EXISTED_DATA,
		INNER_EXCEPTION,
		OTHER;
		
	}

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
	
}
