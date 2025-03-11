package com.ecommerce.sb_ecomProject.exception;

public class APIException extends RuntimeException
{
    private static final long serialVersionUID=1L;  //requires to avoid class version mismatch

    public APIException()
    {
    }

    public APIException(String message)
    {
        super(message);
    }
}
