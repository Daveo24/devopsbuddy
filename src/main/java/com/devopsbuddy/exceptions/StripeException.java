package com.devopsbuddy.exceptions;

import com.stripe.exception.ApiException;

public class StripeException extends RuntimeException {

    public StripeException(Throwable e) {
        super(e);
    }
}
