package com.ecommerce.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ecommerce.payment.entity.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    @Modifying
    @Query("UPDATE Payment p SET p.status = :status WHERE p.paymentId= :paymentId")
    void updateStatus(@Param("status") String status, @Param("paymentId") Integer paymentId);

    @Query("SELECT p FROM Payment p WHERE p.reference= :trackingId")
    Payment getTrackStatus(@Param("trackingId") String trackingId);
}
