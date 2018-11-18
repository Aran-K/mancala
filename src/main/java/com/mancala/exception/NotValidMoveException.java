package com.mancala.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by aran on 18/07/17.
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "That's not a valid move")
public class NotValidMoveException extends Exception
{

    static final long serialVersionUID = 1L;


    public NotValidMoveException(String message)
    {
        super(message);
    }

}