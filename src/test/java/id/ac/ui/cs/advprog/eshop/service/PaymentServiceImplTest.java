package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.OrderStatus;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @InjectMocks
    PaymentServiceImpl paymentService;

    @Mock
    PaymentRepository paymentRepository;

    Order order;
    List<Payment> payments;

    @BeforeEach
    void setUp() {
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(2);
        products.add(product);

        order = new Order(
                "13652556-012a-4c07-b546-54eb1396d79b",
                products,
                1708560000L,
                "Safira Sudrajat"
        );

        payments = new ArrayList<>();
    }

    @Test
    void testAddPaymentVoucherCodeSuccess() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");

        doAnswer(invocation -> invocation.getArgument(0))
                .when(paymentRepository).save(any(Payment.class));

        Payment result = paymentService.addPayment(order, "Voucher Code", paymentData);

        assertNotNull(result);
        assertEquals("Voucher Code", result.getMethod());
        assertEquals(PaymentStatus.SUCCESS.getValue(), result.getStatus());
        assertEquals("ESHOP1234ABC5678", result.getPaymentData().get("voucherCode"));
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testAddPaymentVoucherCodeRejected() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "INVALID");

        doAnswer(invocation -> invocation.getArgument(0))
                .when(paymentRepository).save(any(Payment.class));

        Payment result = paymentService.addPayment(order, "Voucher Code", paymentData);

        assertNotNull(result);
        assertEquals(PaymentStatus.REJECTED.getValue(), result.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testAddPaymentCashOnDeliverySuccess() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("address", "Jalan Margonda");
        paymentData.put("deliveryFee", "10000");

        doAnswer(invocation -> invocation.getArgument(0))
                .when(paymentRepository).save(any(Payment.class));

        Payment result = paymentService.addPayment(order, "Cash on Delivery", paymentData);

        assertNotNull(result);
        assertEquals("Cash on Delivery", result.getMethod());
        assertEquals(PaymentStatus.SUCCESS.getValue(), result.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testAddPaymentCashOnDeliveryRejectedIfAddressEmpty() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("address", "");
        paymentData.put("deliveryFee", "10000");

        doAnswer(invocation -> invocation.getArgument(0))
                .when(paymentRepository).save(any(Payment.class));

        Payment result = paymentService.addPayment(order, "Cash on Delivery", paymentData);

        assertNotNull(result);
        assertEquals(PaymentStatus.REJECTED.getValue(), result.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testAddPaymentCashOnDeliveryRejectedIfDeliveryFeeNull() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("address", "Jalan Margonda");
        paymentData.put("deliveryFee", null);

        doAnswer(invocation -> invocation.getArgument(0))
                .when(paymentRepository).save(any(Payment.class));

        Payment result = paymentService.addPayment(order, "Cash on Delivery", paymentData);

        assertNotNull(result);
        assertEquals(PaymentStatus.REJECTED.getValue(), result.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testSetStatusSuccessMakesOrderSuccess() {
        Payment payment = new Payment(
                "payment-1",
                order,
                "Voucher Code",
                PaymentStatus.PENDING.getValue(),
                new HashMap<>()
        );

        doReturn(payment).when(paymentRepository).findById(payment.getId());
        doReturn(payment).when(paymentRepository).save(any(Payment.class));

        Payment result = paymentService.setStatus(payment, PaymentStatus.SUCCESS.getValue());

        assertEquals(PaymentStatus.SUCCESS.getValue(), result.getStatus());
        assertEquals(OrderStatus.SUCCESS.getValue(), order.getStatus());
        verify(paymentRepository, times(1)).save(payment);
    }

    @Test
    void testSetStatusRejectedMakesOrderFailed() {
        Payment payment = new Payment(
                "payment-1",
                order,
                "Voucher Code",
                PaymentStatus.PENDING.getValue(),
                new HashMap<>()
        );

        doReturn(payment).when(paymentRepository).findById(payment.getId());
        doReturn(payment).when(paymentRepository).save(any(Payment.class));

        Payment result = paymentService.setStatus(payment, PaymentStatus.REJECTED.getValue());

        assertEquals(PaymentStatus.REJECTED.getValue(), result.getStatus());
        assertEquals(OrderStatus.FAILED.getValue(), order.getStatus());
        verify(paymentRepository, times(1)).save(payment);
    }

    @Test
    void testGetPaymentIfIdFound() {
        Payment payment = new Payment(
                "payment-1",
                order,
                "Voucher Code",
                PaymentStatus.SUCCESS.getValue(),
                new HashMap<>()
        );

        doReturn(payment).when(paymentRepository).findById(payment.getId());

        Payment result = paymentService.getPayment(payment.getId());
        assertEquals(payment.getId(), result.getId());
    }

    @Test
    void testGetPaymentIfIdNotFound() {
        doReturn(null).when(paymentRepository).findById("not-found");

        assertNull(paymentService.getPayment("not-found"));
    }

    @Test
    void testGetAllPayments() {
        List<Payment> paymentList = new ArrayList<>();

        Payment payment1 = new Payment(
                "payment-1",
                order,
                "Voucher Code",
                PaymentStatus.SUCCESS.getValue(),
                new HashMap<>()
        );

        Payment payment2 = new Payment(
                "payment-2",
                order,
                "Cash on Delivery",
                PaymentStatus.PENDING.getValue(),
                new HashMap<>()
        );

        paymentList.add(payment1);
        paymentList.add(payment2);

        doReturn(paymentList).when(paymentRepository).findAll();

        List<Payment> result = paymentService.getAllPayments();
        assertEquals(2, result.size());
    }
}