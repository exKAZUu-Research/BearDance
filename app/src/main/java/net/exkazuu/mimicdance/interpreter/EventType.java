package net.exkazuu.mimicdance.interpreter;

public enum EventType {
    White(IconType.White.code),
    Yellow(IconType.Yellow.code),
    Gmail(IconType.Gmail.code),
    Facebook(IconType.Facebook.code),
    Twitter(IconType.Twitter.code),
    Calendar(IconType.Calendar.code),;
    public final String text;

    EventType(String text) {
        this.text = text;
    }
}
