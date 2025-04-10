package com.example.catalogservice.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.catalogservice.jpa.CatalogEntity;
import com.example.catalogservice.service.CatalogService;
import com.example.catalogservice.vo.ResponseCatalog; 

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/catalog-service")
@RequiredArgsConstructor
public class CatalogController {
	
	private final Environment env;
	private final CatalogService catalogService;
	
	@GetMapping("/health_check")
	public String status() {
		return String.format("It' s Working in Catalog Service on Port %s", env.getProperty("local.server.port"));
	}
	
	@GetMapping("/catalogs")
	public ResponseEntity<List<?>> getCatalogs() {
		Iterable<CatalogEntity> catalogList = catalogService.getAllCatalogs();
		List<ResponseCatalog> result = new ArrayList<>();
		
		catalogList.forEach(v -> {
			result.add(new ModelMapper().map(v, ResponseCatalog.class));
		});
		
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}

}
