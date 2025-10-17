package main.java.com.pavan.csse.backend.repository;

import com.pavan.csse.backend.model.Invoice;
import com.pavan.csse.backend.model.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findByStatus(InvoiceStatus status);
}
