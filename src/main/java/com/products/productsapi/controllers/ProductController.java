package com.products.productsapi.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.products.productsapi.dtos.ProductRecordDto;
import com.products.productsapi.models.ProductModel;
import com.products.productsapi.repositories.ProductRepository;

import jakarta.validation.Valid;

@RestController
public class ProductController {
	
	// injeção de dependência para essa classe acessar a base de dados pelo repository
	@Autowired
	private ProductRepository productRepository;
	
	/* método http post para salvar um novo produto na base de dados, uma instância de ProductModel é criada e 
	 * os dados do DTO são transferidos para essa instância
	*/
	@PostMapping("/products")
	public ResponseEntity<ProductModel> saveProduct(@RequestBody @Valid ProductRecordDto productRecordDto) {
		ProductModel productModel = new ProductModel();
		BeanUtils.copyProperties(productRecordDto, productModel);
		return ResponseEntity.status(HttpStatus.CREATED).body(productRepository.save(productModel));
	}
	
	
	/* método http get que retorna toda a lista de produtos na base de dados, com o uso de hateoas para 
	 * adicionar um link para o método getOneProduct para cada produto da lista 
	*/
	@GetMapping("/products")
	public ResponseEntity<List<ProductModel>> getAllProducts() {
		List<ProductModel> productsList = productRepository.findAll();
		if(!productsList.isEmpty()) {
			for(ProductModel product : productsList) {
				UUID id = product.getIdProduct();
				product.add(linkTo(methodOn(ProductController.class).getOneProduct(id)).withSelfRel());
			}
		}
		return ResponseEntity.status(HttpStatus.OK).body(productsList);
	}
	
	
	/* método http get que retorna um produto específico com base no seu id inserido na url. retorna um tipo 
	 * object pois se o id inserido na url for inválido, ele não retornará um produto. utiliza hateoas para gerar
	 * um link para o método getAllProducts 
	*/
	@GetMapping("/products/{id}")
	public ResponseEntity<Object> getOneProduct(@PathVariable(value="id") UUID id) {
		Optional<ProductModel> productO = productRepository.findById(id);
		if(productO.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
		}
		productO.get().add(linkTo(methodOn(ProductController.class).getAllProducts()).withSelfRel());
		return ResponseEntity.status(HttpStatus.OK).body(productO.get());
	}
	
	/* método http put que atualiza um produto já existente na base de dados com base no seu id. 
	 * o método recebe também o parâmetro do tipo ProductRecordDto para atualizar as colunas do produto na tabela
	*/
	@PutMapping("/products/{id}")
	public ResponseEntity<Object> updateProduct(@PathVariable(value="id") UUID id, 
			@RequestBody @Valid ProductRecordDto productRecordDto) {
		Optional<ProductModel> productO = productRepository.findById(id);
		if(productO.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
		}
		ProductModel productModel = productO.get();
		BeanUtils.copyProperties(productRecordDto, productModel);
		return ResponseEntity.status(HttpStatus.OK).body(productRepository.save(productModel));	
	}
	
	// método http delete que deleta um produto já existente na base de dados com base no seu id
	@DeleteMapping("/products/{id}")
	public ResponseEntity<Object> deleteProduct(@PathVariable(value="id") UUID id) {
		Optional<ProductModel> productO = productRepository.findById(id);
		if(productO.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
		}
		productRepository.delete(productO.get());
		return ResponseEntity.status(HttpStatus.OK).body("Product deleted succesfully");
	}
 
}