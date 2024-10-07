package com.yashcode.EcommerceBackend.Repository;

import com.yashcode.EcommerceBackend.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment,Long> {
    Payment findUserByEmail(String userEmail);
}
