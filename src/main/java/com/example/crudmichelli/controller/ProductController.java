package com.example.crudmichelli.controller;

import com.example.crudmichelli.dtos.ProductRecordDto;
import com.example.crudmichelli.models.ProductModel;
import com.example.crudmichelli.repositories.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class ProductController {

    @Autowired
    ProductRepository productRepository;    //ponto de injecao

    //PARA INSERIR UM ITEM NO BANCO DE DADOS
    @PostMapping("/products")
    //     tipo do retorno              nome metodo recebe como argumento no corpo os campos do DTO
    public ResponseEntity<ProductModel> saveProduct(@RequestBody @Valid ProductRecordDto productRecordDto){
        // criacao de uma instancia do tipo Model para ser salvo na Base de Dados
        var productModel = new ProductModel();

        //realiza a conversao de DTO para Model
        BeanUtils.copyProperties(productRecordDto, productModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(productRepository.save(productModel));
    }

    //PARA LISTAR TODOS OS ITENS DO BANCO DE DADOS
    @GetMapping("/products")
    //Retorna a lista de todos os Produtos
    public ResponseEntity<List<ProductModel>> getAllProducts(){

        List<ProductModel> productList = productRepository.findAll(); //joga os dados da busca primeiramente em uma lista
        if(!productList.isEmpty()){
            for(ProductModel product : productList){
                Long id = product.getId();
                /*PARA UTILIZACAO DO .ADD montando o novo agrupamento precisou ir na classe controler ProductController e
                * extender a classe a RepresentationModel<ProductModel> para poder utilizar o metodo*/
                product.add(linkTo(methodOn(ProductController.class).getOneProduct(id)).withSelfRel());
            }
        }
        System.out.println("Lista: " + productList);
        return ResponseEntity.status(HttpStatus.OK).body(productList);

        //return ResponseEntity.status(HttpStatus.OK).body(productRepository.findAll());    //busca padrao sem a lista
    }

    //PARA LISTAR UM ITEM DO BANCO DE DADOS
    @GetMapping("/products/{id}")
    //o tipo de retorno deve ser Object
    public ResponseEntity<Object> getOneProduct(@PathVariable (value="id") Long id){
        Optional<ProductModel> newProduct = productRepository.findById(id); //deve utilizar esse tipo Optional pq pode encontrar ou nao a ocorrencia
        if(newProduct.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto nao encontrado");
        }
        //Forma de pegar a referencia agora montando a lista
        newProduct.get().add(linkTo(methodOn(ProductController.class).getAllProducts()).withRel("Products List"));
        return ResponseEntity.status(HttpStatus.OK).body(newProduct.get());

    }

    //PARA ATUALIZAR UM ITEM NO BANCO DE DADOS
    @PutMapping("/products/{id}")
    public ResponseEntity<Object> updateProduct(@PathVariable(value="id") Long id,
                                                @RequestBody @Valid ProductRecordDto productRecordDto){ //passa como parametro o id e o body

        Optional<ProductModel> auxProduct = productRepository.findById(id); //busca o produto pelo id na base de dados
        if(auxProduct.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto nao encontrado para atualizar");
        }
        var productModel = auxProduct.get();    //cria o productModel com o valor jah buscado anteriormente
        BeanUtils.copyProperties(productRecordDto, productModel);   //realiza a conversao para atualizar os campos
        return ResponseEntity.status(HttpStatus.OK).body(productRepository.save(productModel));

    }

    //PARA EXCLUIR UM ITEM NO BANCO DE DADOS
    @DeleteMapping("/products/{id}")
    public ResponseEntity<Object> deleteProdut(@PathVariable(value="id") Long id){

        Optional<ProductModel> auxProduct = productRepository.findById(id); //busca o produto pelo id na base de dados

        if(auxProduct.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto nao encontrado para excluir");
        }
        productRepository.delete(auxProduct.get()); //forma de excluir o item encontrado do repositorio
        return  ResponseEntity.status(HttpStatus.OK).body("Produto excluido com sucesso");
    }
}
