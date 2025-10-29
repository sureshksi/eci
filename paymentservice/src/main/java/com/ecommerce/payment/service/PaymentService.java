package com.ecommerce.payment.service;

import java.util.List;

import com.ecommerce.payment.entity.Payment;
import com.ecommerce.payment.exception.PaymentException;

public interface PaymentService {
	
	public Payment createPayment(Payment payment) throws PaymentException;
	public Payment updatePayment(Payment payment) throws PaymentException;
	public void deletePayment(Integer paymentId) throws PaymentException;
	public List<Payment> getAllPayments();
	public Payment getPaymentById(Integer paymentId);
	public void updatePaymentStatus(Integer paymentId, String paymentStatus) throws PaymentException;
	public Payment getPaymentStausByTrackId(String trackId);

}
