package com.ecommerce.payment.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.payment.entity.Payment;
import com.ecommerce.payment.exception.PaymentException;
import com.ecommerce.payment.repository.PaymentRepository;
import com.ecommerce.payment.service.PaymentService;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

	@Autowired
	PaymentRepository paymentRepository;

	@Transactional
	@Override
	public Payment createPayment(Payment payment) throws PaymentException {
		try {			
			paymentRepository.save(payment);
			log.info("Payment success");
			return payment;
		}catch(Exception e) {
			throw new PaymentException("Payment failed");
		}
	}

	@Transactional
	@Override
	public Payment updatePayment(Payment payment) throws PaymentException {
		try {
			paymentRepository.save(payment);
			log.info("Payment update success");
			return payment;
		} catch (Exception e) {
			throw new PaymentException("Payment update failed");
		}
	}

	@Transactional
	@Override
	public void deletePayment(Integer paymentId) throws PaymentException {
		try {
			paymentRepository.deleteById(paymentId);
			log.info("Payment record deletion success");
		} catch (Exception e) {
			throw new PaymentException("Payment record delete failed");
		}
	}

	@Override
	public List<Payment> getAllPayments() {
		return paymentRepository.findAll();
	}

	@Override
	public Payment getPaymentById(Integer paymentId) {
	Optional<Payment> paymentOpt = paymentRepository.findById(paymentId);
	if(paymentOpt.isEmpty())
		return null;
	else
		return paymentOpt.get();
	}

	@Transactional
	@Override
	public void updatePaymentStatus(Integer paymentId, String paymentStatus) throws PaymentException {
		try {
			paymentRepository.updateStatus(paymentStatus, paymentId);
			log.info("Payment status update success");
		} catch (Exception e) {
			throw new PaymentException("Payment status update failed");
		}
	}
	
	@Override
    public Payment getPaymentStausByTrackId(String trackId) {
		log.info("TrakingId::"+trackId);
        return paymentRepository.getTrackStatus(trackId);
    }
	
}
