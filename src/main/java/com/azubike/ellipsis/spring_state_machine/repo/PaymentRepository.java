package com.azubike.ellipsis.spring_state_machine.repo;

import com.azubike.ellipsis.spring_state_machine.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
