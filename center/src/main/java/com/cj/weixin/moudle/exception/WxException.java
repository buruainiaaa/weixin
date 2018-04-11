package com.cj.weixin.moudle.exception;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 微信通用的Exception
 * 
 * 继承自RuntimeException, 从由Spring管理事务的函数中抛出时会触发事务回滚.
 * 
 * @author cody
 *
 */

public class WxException extends RuntimeException {

	private static final long serialVersionUID = -4192005000264304517L;

	private static final String SLF_Prefix = "======::";

	private int wfdErrorCode;
	private String wfdErrorMsg;

	public int getWfdErrorCode() {
		return wfdErrorCode;
	}

	public String getWfdErrorMsg() {
		return wfdErrorMsg;
	}

	public WxException() {
		super();
	}

	public WxException(int wfdErrorCode, String wfdErrorMsg) {
		super(wfdErrorMsg);
		this.wfdErrorCode = wfdErrorCode;
		this.wfdErrorMsg = wfdErrorMsg;
	}

	public WxException(String msg) {
		super(SLF_Prefix + msg);
		this.wfdErrorCode = -1;
		this.wfdErrorMsg = msg;
	}

	public WxException(Throwable e) {
		super(e.getMessage(), e);
	}

	public WxException(String msg, Throwable e) {
		super(msg, e);
	}

	@Override
	public String getMessage() {
		return String.format("%s:['%s':'%s']", this.wfdErrorCode, wfdErrorMsg, super.getMessage());
	}

	/**
	 * 根据错误代码，获取异常对象
	 * 
	 * @param errorCode
	 *            参见 errordefine.properties
	 * @return 异常类实例
	 */
	public static WxException getException(int errorCode) {
		if (WxExceptionCfg.define.containsKey(errorCode)) {
			return new WxException(errorCode, WxExceptionCfg.define.get(errorCode));
		}
		return new WxException(String.valueOf(errorCode));
	}

	public static WxException getException(Throwable error) {
		if (error instanceof WxException) {
			return (WxException) error;
		}
		return new WxException(-1, error.getMessage());
	}
	
	public static String getStacktrace(Throwable error) {
		StringWriter sw = null;
        PrintWriter pw = null;
        try {
            sw = new StringWriter();
            pw =  new PrintWriter(sw);
            //将出错的栈信息输出到printWriter中
            error.printStackTrace(pw);
            pw.flush();
            sw.flush();
        } finally {
            if (sw != null) {
                try {
                    sw.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (pw != null) {
                pw.close();
            }
        }
        return sw.toString();
	}
}
