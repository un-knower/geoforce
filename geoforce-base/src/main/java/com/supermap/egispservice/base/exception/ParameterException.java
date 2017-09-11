package com.supermap.egispservice.base.exception;


public class ParameterException extends Exception {

	
	public static final String NULL_NONEED_EXCEPTION = "参数[#]不允许为空"; 
	
	public static final String NOT_FOUND = "未找到[#]数据"; 

	public static final String CONVERT_EXCEPTION = "数据转换异常[#]";
	
	public static final String NO_DATA_UPDATED = "未发现可更新的数据";
	
	public static final String EXISTED_DATA = "[#]已使用";
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ParameterException() {
		// TODO Auto-generated constructor stub
	}
	
	public ParameterException(String message){
		this.messge = message;
	}
	private String messge;
	
	public ParameterException(ExceptionType type,String ... infos){
		if(ExceptionType.NULL_NO_NEED.equals(type)){
			String info = infos[0];
			this.messge = NULL_NONEED_EXCEPTION.replaceAll("#", info);
		}else if(ExceptionType.NOT_FOUND.equals(type)){
			String info = infos[0];
			this.messge = NOT_FOUND.replaceAll("#", info);
		}else if(ExceptionType.CONVERT_EXCEPTION.equals(type)){
			String info = infos[0];
			this.messge = CONVERT_EXCEPTION.replaceAll("#", info);
		}else if(ExceptionType.EXISTED_DATA.equals(type)){
			String info = infos[0];
			this.messge = EXISTED_DATA.replaceAll("#", info);
		}
	}
	
	@Override
	public String getMessage() {
		return this.messge;
	}
	
	public  enum ExceptionType{
		NULL_NO_NEED, // 不允许为空
		NOT_FOUND,
		CONVERT_EXCEPTION,
		EXISTED_DATA;
		
	}
	
	
}
