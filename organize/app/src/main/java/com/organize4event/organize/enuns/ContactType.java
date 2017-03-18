package com.organize4event.organize.enuns;

public enum ContactType {

    EMAIL(1),
    PHONE(2),
    CELLPHONE(3),
    WHATSAPP(4);

    private int value;

    ContactType(int Value) {
        this.value = Value;
    }

    public int getValue() {
        return value;
    }
}
