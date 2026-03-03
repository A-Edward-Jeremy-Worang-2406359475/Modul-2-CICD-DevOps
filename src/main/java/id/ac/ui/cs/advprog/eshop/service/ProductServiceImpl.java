package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.IProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final IProductRepository productRepository;
    private final ProductIdGenerator idGenerator;

    public ProductServiceImpl(final IProductRepository productRepository,
                              final ProductIdGenerator idGenerator) {
        this.productRepository = productRepository;
        this.idGenerator = idGenerator;
    }

    @Override
    public Product create(final Product product) {
        if (product.getProductId() == null || product.getProductId().isBlank()) {
            product.setProductId(idGenerator.generate());
        }
        productRepository.save(product);
        return product;
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Product findById(final String productId) {
        return productRepository.findById(productId).orElse(null);
    }

    @Override
    public void update(final Product product) {
        productRepository.update(product);
    }

    @Override
    public boolean deleteById(final String productId) {
        return productRepository.deleteById(productId);
    }
}