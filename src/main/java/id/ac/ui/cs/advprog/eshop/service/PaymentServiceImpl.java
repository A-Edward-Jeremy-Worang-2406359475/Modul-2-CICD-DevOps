package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Order;
import enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    private static final String VOUCHER_CODE_METHOD = "Voucher Code";
    private static final String CASH_ON_DELIVERY_METHOD = "Cash on Delivery";

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public Payment addPayment(Order order, String method, Map<String, String> paymentData) {
        String status = determinePaymentStatus(method, paymentData);

        Payment payment = new Payment(
                UUID.randomUUID().toString(),
                order,
                method,
                status,
                paymentData
        );

        return paymentRepository.save(payment);
    }

    @Override
    public Payment setStatus(Payment payment, String status) {
        payment.setStatus(status);

        if (PaymentStatus.SUCCESS.getValue().equals(status)) {
            payment.getOrder().setStatus(OrderStatus.SUCCESS.getValue());
        } else if (PaymentStatus.REJECTED.getValue().equals(status)) {
            payment.getOrder().setStatus(OrderStatus.FAILED.getValue());
        }

        return paymentRepository.save(payment);
    }

    @Override
    public Payment getPayment(String paymentId) {
        return paymentRepository.findById(paymentId);
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    private String determinePaymentStatus(String method, Map<String, String> paymentData) {
        if (VOUCHER_CODE_METHOD.equals(method)) {
            return isValidVoucherCode(paymentData.get("voucherCode"))
                    ? PaymentStatus.SUCCESS.getValue()
                    : PaymentStatus.REJECTED.getValue();
        }

        if (CASH_ON_DELIVERY_METHOD.equals(method)) {
            return isValidCashOnDelivery(paymentData)
                    ? PaymentStatus.SUCCESS.getValue()
                    : PaymentStatus.REJECTED.getValue();
        }

        throw new IllegalArgumentException();
    }

    private boolean isValidVoucherCode(String voucherCode) {
        if (voucherCode == null || voucherCode.length() != 16 || !voucherCode.startsWith("ESHOP")) {
            return false;
        }

        int digitCount = 0;
        for (char c : voucherCode.toCharArray()) {
            if (Character.isDigit(c)) {
                digitCount++;
            }
        }

        return digitCount == 8;
    }

    private boolean isValidCashOnDelivery(Map<String, String> paymentData) {
        return !isNullOrEmpty(paymentData.get("address"))
                && !isNullOrEmpty(paymentData.get("deliveryFee"));
    }

    private boolean isNullOrEmpty(String value) {
        return value == null || value.isEmpty();
    }
}