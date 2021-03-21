package org.coffeejug.cafe.payment.service;

import org.coffeejug.cafe.payment.model.CafeOrder;
import org.coffeejug.cafe.payment.model.OrderPayment;

public interface PaymentService {

    OrderPayment withdrawnMoney(CafeOrder cafeOrder);

}
