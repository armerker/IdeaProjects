package edu.phystech.hw2.contact;

record Contact(String username, String email) implements Comparable<Contact> {
    public static final String UNKNOWN_EMAIL = "unknown@gmail.com";

    public Contact {
        if (username == null || username.trim().isEmpty()) {
            throw new InvalidContactFieldException("username");
        }
        if (email != null) {
            if (email.trim().isEmpty() || !email.matches(".*@gmail\\.com$")) {
                throw new InvalidContactFieldException("email");
            }
        }
    }

    public Contact(String username) {
        this(username, UNKNOWN_EMAIL);
    }

    @Override
    public int compareTo(Contact other) {
        return Integer.compare(this.username.length(), other.username.length());
    }
}

class InvalidContactFieldException extends RuntimeException {
    private final String fieldName;

    public InvalidContactFieldException(String fieldName) {
        super("Invalid " + fieldName);
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
