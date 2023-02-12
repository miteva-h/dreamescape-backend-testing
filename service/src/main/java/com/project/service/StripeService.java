package com.project.service;

import com.project.domain.dto.ChargeRequest;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;

public interface StripeService {

    Charge createCharge(ChargeRequest chargeRequest) throws StripeException;
}
