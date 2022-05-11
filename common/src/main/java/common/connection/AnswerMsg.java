package common.connection;


import common.data.HumanBeing;

import java.util.Collection;

/**
 * Сообщение с сервера отправляется клиенту.
 */

public class AnswerMsg implements Response {
    private static final long serialVersionUID = 666;
    private String msg;
    private Status status;
    private Collection<HumanBeing> collection;


    public AnswerMsg() {
        msg = "";
        status = Status.FINE;
    }

    /**
     * Пустой ввод.
     */

    public AnswerMsg clear() {
        msg = "";
        return this;
    }

    /**
     * Ответ для пользователя.
     */

    public AnswerMsg info(Object str) {
        msg = str.toString();// + "\n";
        return this;
    }

    /**
     * Сообщение об ошибке.
     */

    public AnswerMsg error(Object str) {
        msg = /*"Error: " + */str.toString();// + "\n";
        setStatus(Status.ERROR);
        return this;
    }

    /**
     * Установка статуса сообщения.
     */

    public AnswerMsg setStatus(Status st) {
        status = st;
        return this;
    }

    /**
     * Получить сообщение.
     */

    public String getMessage() {
        return msg;
    }

    /**
     * Получить статус сообщения.
     */

    public Status getStatus() {
        return status;
    }

    public Collection<HumanBeing> getCollection() {
        return collection;
    }

    @Override
    public String toString() {
        if (getStatus() == Status.ERROR) {
            return "Error: " + getMessage();
        }
        return getMessage();
    }
}