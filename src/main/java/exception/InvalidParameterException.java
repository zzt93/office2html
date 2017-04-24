package exception;

/**
 * 当参数无效时抛出该异常
 */
public class InvalidParameterException extends FileConversionFormatException {
    private static final long serialVersionUID = -2097465532154739597L;

    public InvalidParameterException(String message) {
        super(message);
    }

}
