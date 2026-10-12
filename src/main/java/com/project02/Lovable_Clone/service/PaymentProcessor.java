package com.project02.Lovable_Clone.service;

import com.project02.Lovable_Clone.dto.subscription.CheckoutRequest;
import com.project02.Lovable_Clone.dto.subscription.CheckoutResponse;
import com.project02.Lovable_Clone.dto.subscription.PortalResponse;
import com.stripe.model.StripeObject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;


public interface PaymentProcessor {

    CheckoutResponse createCheckoutSessionUrl(CheckoutRequest request);

    PortalResponse openCustomerPortal();

    void handleWebhookEvent(String type, StripeObject stripeObject, Map<String, String> metadata);
}
