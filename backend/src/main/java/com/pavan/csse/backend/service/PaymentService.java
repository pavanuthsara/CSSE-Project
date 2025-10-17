package main.java.com.pavan.csse.backend.service;

import com.pavan.csse.backend.dto.ProcessPaymentRequest;
import com.pavan.csse.backend.dto.ProcessPaymentResponse;
import com.pavan.csse.backend.model.*;
import com.pavan.csse.backend.repository.InvoiceRepository;
import com.pavan.csse.backend.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final InvoiceRepository invoiceRepository;

    public PaymentService(PaymentRepository paymentRepository, InvoiceRepository invoiceRepository) {
        this.paymentRepository = paymentRepository;
        this.invoiceRepository = invoiceRepository;
    }

    public ProcessPaymentResponse processPayment(ProcessPaymentRequest req) {
        // Simple validation: ensure invoices exist and are outstanding
        List<Invoice> invoices = invoiceRepository.findAllById(req.getInvoiceIds());
        if (invoices.isEmpty()) {
            throw new IllegalArgumentException("No invoices found");
        }

        BigDecimal total = invoices.stream().map(Invoice::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        if (req.getAmount() == null || req.getAmount().compareTo(total) < 0) {
            throw new IllegalArgumentException("Amount is less than invoice total - partial payments not supported in this version");
        }

        Payment p = new Payment();
        p.setAmount(req.getAmount());
        p.setMethod(req.getMethod());
        p.setPatientName(invoices.get(0).getPatientName());

        // Mock processing for card
        if (req.getMethod() == PaymentMethod.CARD) {
            // pretend to call a gateway; randomly fail if card details indicate failure
            p.setStatus(PaymentStatus.COMPLETED);
        } else if (req.getMethod() == PaymentMethod.INSURANCE) {
            p.setStatus(PaymentStatus.INSURANCE_PENDING);
            // mark invoices still outstanding until claim succeeds
        } else {
            p.setStatus(PaymentStatus.COMPLETED);
            // mark invoices paid
            invoices.forEach(inv -> inv.setStatus(InvoiceStatus.PAID));
            invoiceRepository.saveAll(invoices);
        }

        Payment saved = paymentRepository.save(p);

        String receiptUrl = "/api/payments/" + saved.getId() + "/receipt";
        return new ProcessPaymentResponse(saved.getId(), saved.getStatus().name(), receiptUrl);
    }

    public Payment getPayment(Long id) {
        return paymentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Payment not found"));
    }
}
