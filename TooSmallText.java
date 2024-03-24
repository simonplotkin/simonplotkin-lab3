public class TooSmallText extends Exception {
    
    public TooSmallText(int exceptionArg) {
        super("Only found " + exceptionArg + " words.");
    }
}
