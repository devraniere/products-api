package com.products.productsapi.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.hateoas.RepresentationModel;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// anotações para definir essa classe como uma tabela na base de dados. cada instância dessa classe será um novo produto na tabela graças ao método http post
@Entity
@Table(name = "TB_PRODUCTS")
public class ProductModel extends RepresentationModel<ProductModel> implements Serializable {

	private static final long SERIAL_VERSION_UID = 1L;
	
	// anotações para definir esse atributo como id do produto na base de dados
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO) 
	private UUID idProduct;
	private String name;
	private BigDecimal value;
	
	public static long getSerialVersionUid() {
		return SERIAL_VERSION_UID;
	}
	
	public UUID getIdProduct() {
		return idProduct;
	}
	
	public void setIdProduct(UUID idProduct) {
		this.idProduct = idProduct;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public BigDecimal getValue() {
		return value;
	}
	
	public void setValue(BigDecimal value) {
		this.value = value;
	}
	
}
