package in.newdevpoint.bootcamp.service;

import in.newdevpoint.bootcamp.entity.Product;
import in.newdevpoint.bootcamp.exceptions.ValidationException;
import in.newdevpoint.bootcamp.repository.ProductRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Service class for handling product-related operations.
 * Includes comprehensive validation before product creation.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final Validator validator;

    /**
     * Creates a new product after validating all input fields.
     * Performs both annotation-based validation and custom business rule validation.
     *
     * @param product The product to be created
     * @return The created product
     * @throws ValidationException if validation fails
     */
    public Product createProduct(Product product) {
        log.debug("Validating product before creation: {}", product.getName());
        
        // Perform annotation-based validation
        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        if (!violations.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder("Validation failed: ");
            for (ConstraintViolation<Product> violation : violations) {
                errorMessage.append(violation.getMessage()).append("; ");
            }
            throw new ValidationException(errorMessage.toString());
        }

        // Perform custom business rule validation
        validateBusinessRules(product);

        // If all validations pass, save the product
        log.info("Product validation successful, creating product: {}", product.getName());
        return productRepository.save(product);
    }

    /**
     * Performs custom business rule validation on the product.
     * This method can be extended with additional business rules as needed.
     *
     * @param product The product to validate
     * @throws ValidationException if any business rule is violated
     */
    private void validateBusinessRules(Product product) {
        // Example business rules:
        
        // 1. Check if product name is unique
        if (productRepository.existsByName(product.getName())) {
            throw new ValidationException("Product name must be unique");
        }

        // 2. Validate price format (e.g., no more than 2 decimal places)
        if (product.getPrice() != null) {
            String priceStr = product.getPrice().toString();
            if (priceStr.contains(".") && priceStr.split("\\.")[1].length() > 2) {
                throw new ValidationException("Price cannot have more than 2 decimal places");
            }
        }

        // 3. Validate category against allowed categories
        if (!isValidCategory(product.getCategory())) {
            throw new ValidationException("Invalid product category");
        }

        // 4. Validate stock quantity is reasonable
        if (product.getStockQuantity() != null && product.getStockQuantity() > 10000) {
            throw new ValidationException("Stock quantity cannot exceed 10000");
        }
    }

    /**
     * Validates if the given category is allowed.
     * This is an example method that can be expanded based on your needs.
     *
     * @param category The category to validate
     * @return true if the category is valid, false otherwise
     */
    private boolean isValidCategory(String category) {
        // Example categories - replace with your actual categories
        return category != null && (
            category.equals("ELECTRONICS") ||
            category.equals("CLOTHING") ||
            category.equals("BOOKS") ||
            category.equals("FOOD") ||
            category.equals("OTHER")
        );
    }
} 