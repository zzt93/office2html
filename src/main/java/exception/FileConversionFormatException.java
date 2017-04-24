package exception;

/**
 * 所有自定义异常的超类,xy是xiao yun的的首字母
 */
public class FileConversionFormatException extends RuntimeException {

	private static final long serialVersionUID = -2097465532154739597L;

	/**
	 *
	 * @param message 错误信息
     */
	public FileConversionFormatException(String message){
		super(message);
	}

	/**
	 *
	 * @param message 错误信息
	 * @param e 原始异常
     */
	public FileConversionFormatException(String message, Exception e){
		super(message,e);
	}

}
