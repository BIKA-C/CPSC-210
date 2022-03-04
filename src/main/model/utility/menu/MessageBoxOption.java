package model.utility.menu;

// represents the message box style
// it can has the OK button only
// or the yes/no/cancel buttons
public enum MessageBoxOption {
    YES_NO_CANCEL(24),
    OK(9);

    private int length;

    private MessageBoxOption(int val) {
        this.length = val;
    }

    public int getLength() {
        return length;
    }
}
