package main.java.com.pavan.csse.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProcessPaymentResponse {
    private Long paymentId;
    private String status;
    private String receiptUrl;
}
