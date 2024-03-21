package ru.yandex.practicum.filmorate.exceptions;

public class NotFoundFilmorateExceptions extends RuntimeException {
    private String parameter;

    public NotFoundFilmorateExceptions(String parameter) {
        this.parameter = parameter;
    }

    public String getParameter() {
        return parameter;
    }
}
