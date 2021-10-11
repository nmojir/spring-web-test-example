package com.example.demo.controllers;


import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.example.demo.entities.Book;
import com.example.demo.repositories.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

//@WebMvcTest means that only provided class should be available in context
//All dependencies must be mocked
@WebMvcTest(BookController.class)
public class BookControllerTests {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper mapper;
	
	@MockBean
	private BookRepository bookRepo;
	
	@Test
	public void createTest() throws Exception {
		Book newBook = new Book();
		newBook.setTitle("test title");
		
		Book savedBook = new Book();
		savedBook.setTitle("test title");
		savedBook.setId(1);
		
		Mockito.when(bookRepo.save(Mockito.any(Book.class))).thenReturn(savedBook);
		
		MvcResult mvcResult = mockMvc.perform(
			post("/books")
				.content(mapper.writeValueAsString(newBook))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(print())
			.andReturn();
		
		String actualResp = mvcResult.getResponse().getContentAsString();
		String expectedResult = mapper.writeValueAsString(savedBook);
		
		assertThat(actualResp).isEqualToIgnoringWhitespace(expectedResult);
		
	}
	
	@Test
	public void getAllTest() throws Exception {
		List<Book> books = new ArrayList<>();
		books.add(new Book(1, "first book"));
		books.add(new Book(2, "second book"));
		
		Mockito.when(bookRepo.findAll()).thenReturn(books);
		
		MvcResult mvcResult = mockMvc.perform(get("/books"))
			.andExpect(status().isOk())
			.andDo(print())
			.andReturn();
		
		//bookRepo.findAll method should be executed only one time in this test
		Mockito.verify(bookRepo, Mockito.times(1)).findAll();
		
		String actualResp = mvcResult.getResponse().getContentAsString();
		String expectedResult = mapper.writeValueAsString(books);
		
		assertThat(actualResp).isEqualToIgnoringWhitespace(expectedResult);
	}
}
