package main.java.com.pavan.csse.backend.dto;

import com.pavan.csse.backend.model.PaymentMethod;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProcessPaymentRequest {
    private Long patientId;
    private List<Long> invoiceIds;
    private PaymentMethod method;
    private BigDecimal amount;
    private Object details;
}
