package in.newdevpoint.bootcamp.repository;

import in.newdevpoint.bootcamp.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Product entity. Provides methods for database operations on products.
 */
@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
  /**
   * Checks if a product with the given name exists.
   *
   * @param name The product name to check
   * @return true if a product with the given name exists, false otherwise
   */
  boolean existsByName(String name);
}
