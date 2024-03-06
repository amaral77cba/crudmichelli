package com.example.crudmichelli.models;

import jakarta.persistence.*;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;

@Entity
@Table(name = "tb_products")
//public class ProductModel{ //utilizacao sem o Hateoas
public class ProductModel extends RepresentationModel<ProductModel> {   //extendendo do RepresentationModel para poder utilizar o .add no Hateoas
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private BigDecimal valor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

}
