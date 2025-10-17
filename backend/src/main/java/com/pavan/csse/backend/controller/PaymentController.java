package main.java.com.pavan.csse.backend.controller;

import com.pavan.csse.backend.dto.ProcessPaymentRequest;
import com.pavan.csse.backend.dto.ProcessPaymentResponse;
import com.pavan.csse.backend.model.Payment;
import com.pavan.csse.backend.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/process")
    public ResponseEntity<ProcessPaymentResponse> processPayment(@RequestBody ProcessPaymentRequest req) {
        ProcessPaymentResponse resp = paymentService.processPayment(req);
        return ResponseEntity.status(201).body(resp);
    }

    @GetMapping("/{id}/receipt")
    public ResponseEntity<Payment> getReceipt(@PathVariable Long id) {
        Payment p = paymentService.getPayment(id);
        return ResponseEntity.ok(p);
    }
}
