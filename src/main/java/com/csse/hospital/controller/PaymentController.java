// File: PaymentController.java
package com.csse.hospital.controller;

// Import the required classes
import com.csse.hospital.exception.PaymentNotFoundException;
import com.csse.hospital.model.Payment;
import com.csse.hospital.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Import the util classes
import java.util.List;
import java.util.Map;
import java.util.Optional;

// Import the logger
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 * The PaymentController class
 * URL: /api/payments
 * Description: This class represents the REST API endpoints for the Payment entity.
 * It handles all the CRUD operations for the Payment entity.
 * The class is annotated with @RestController to enable REST endpoints.
 * The class is annotated with @RequestMapping to define the base URL for the REST endpoints.
 * The class is annotated with @Autowired to inject the PaymentService.
 * The class defines the following REST endpoints:
 * 1. GET /api/payments
 * 2. GET /api/payments/{id}
 * 3. POST /api/payments
 * 4. PUT /api/payments/{id}
 * 5. PUT /api/payments/bulk
 * 6. DELETE /api/payments/{id}
 * 7. DELETE /api/payments/bulk
 * 8. GET /api/payments/search
 * 9. GET /api/payments/count
 */

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    // Create a logger instance
    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    // Inject the PaymentService
    @Autowired
    private PaymentService paymentService;

    // Define the REST endpoints
    // GET /api/payments
    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments(@RequestParam int page, @RequestParam int size, @RequestParam(required = false) String key, @RequestParam(required = false) String value) {
        logger.info("Fetching all payments with page: {}, size: {}, key: {}, value: {}", page, size, key, value); // Log the request parameters

        if (key != null && value != null && !value.isEmpty()) { // Check if search parameters are provided
            List<Payment> payments = paymentService.searchPayments(key, value, page, size, null, null);
            return new ResponseEntity<>(payments, HttpStatus.OK);
        }

        List<Payment> payments = paymentService.getAllPayments(page, size); // Fetch all payments

        return new ResponseEntity<>(payments, HttpStatus.OK); // Return the response
    }

    // GET /api/payments/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Long id) {

        logger.info("Fetching payment with id: {}", id); // Log the request parameter

        Optional<Payment> payment = paymentService.getPaymentById(id); // Fetch the payment by id

        if (payment.isPresent()) { // Check if the payment exists
            return new ResponseEntity<>(payment.get(), HttpStatus.OK); // Return the response
        } else {
            throw new PaymentNotFoundException("Payment not found with id: " + id); // Throw an exception if the payment is not found
        }
    }

    // POST /api/payments
    @PostMapping
    public ResponseEntity<Payment> createPayment(@RequestBody Payment payment) {
        logger.info("Creating new payment: {}", payment); // Log the request body

        Payment createdPayment = paymentService.createPayment(payment); // Create the payment

        return new ResponseEntity<>(createdPayment, HttpStatus.CREATED); // Return the response
    }

    // PUT /api/payments/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Payment> updatePayment(@PathVariable Long id, @RequestBody Payment payment) {
        logger.info("Updating payment with id: {}", id); // Log the request parameter

        Payment updatedPayment = paymentService.updatePayment(id, payment); // Update the payment

        if (updatedPayment != null) { // Check if the payment is updated
            return new ResponseEntity<>(updatedPayment, HttpStatus.OK); // Return the response
        } else {
            throw new PaymentNotFoundException("Payment not found with id: " + id); // Throw an exception if the payment is not found
        }
    }

    // PUT /api/payments/bulk
    @PutMapping("/bulk")
    public ResponseEntity<List<Payment>> updatePaymentsBulk(@RequestBody Map<Long, Payment> paymentsToUpdate) {
        logger.info("Updating payments in bulk: {}", paymentsToUpdate.keySet()); // Log the request body

        List<Payment> updatedPayments = paymentService.updatePaymentsBulk(paymentsToUpdate); // Update the payments

        return new ResponseEntity<>(updatedPayments, HttpStatus.OK); // Return the response
    }

    // DELETE /api/payments/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deletePayment(@PathVariable Long id) {
        logger.info("Deleting payment with id: {}", id); // Log the request parameter
        try {
            paymentService.deletePayment(id); // Delete the payment
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Return the response
        } catch (Exception e) { // Catch any exceptions
            logger.error("Error deleting payment with id: {}", id, e); // Log the error
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // Return the response
        }
    }

    // DELETE /api/payments/bulk
    @DeleteMapping("/bulk")
    public ResponseEntity<HttpStatus> deletePaymentsBulk(@RequestBody List<Long> ids) {
        logger.info("Deleting payments in bulk: {}", ids); // Log the request body
        try { // Try to delete the payments
            paymentService.deletePaymentsBulk(ids); // Delete the payments
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Return the response
        } catch (Exception e) { // Catch any exceptions
            logger.error("Error deleting payments in bulk: {}", ids, e); // Log the error
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // Return the response
        }
    }

    // GET /api/payments/search
    @GetMapping("/search")
    public ResponseEntity<List<Payment>> searchPayments(@RequestParam(required = false) String key, @RequestParam(required = false) String value, @RequestParam int page, @RequestParam int size, @RequestParam(required = false) String start, @RequestParam(required = false) String end) {
        logger.info("Searching payments with key: {}, value: {}, page: {}, size: {}, start: {}, end: {}", key, value, page, size, start, end); // Log the request parameters
        List<Payment> payments = paymentService.searchPayments(key, value, page, size, start, end); // Search the payments
        return new ResponseEntity<>(payments, HttpStatus.OK); // Return the response
    }

    // GET /api/payments/count
    @GetMapping("/count")
    public ResponseEntity<Long> getPaymentsCount(@RequestParam(required = false) String key,
                                                 @RequestParam(required = false) String value,
                                                 @RequestParam(required = false) String start,
                                                 @RequestParam(required = false) String end) {
        logger.info("Getting payments count with key: {}, value: {}, start: {}, end: {}", key, value, start, end); // Log the request parameters
        long count = paymentService.getPaymentsCount(key, value, start, end); // Get the payments count
        return new ResponseEntity<>(count, HttpStatus.OK); // Return the response
    }
}