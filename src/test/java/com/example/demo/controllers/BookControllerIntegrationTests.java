package com.example.demo.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.example.demo.entities.Book;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerIntegrationTests {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper mapper;
	
	@Test
	public void getAllTest() throws Exception {
		List<Book> books = new ArrayList<>();
		
		MvcResult mvcResult = mockMvc.perform(get("/books"))
			.andExpect(status().isOk())
			.andDo(print())
			.andReturn();
		
		
		String actualResp = mvcResult.getResponse().getContentAsString();
		String expectedResult = mapper.writeValueAsString(books);
		
		assertThat(actualResp).isEqualToIgnoringWhitespace(expectedResult);
	}
}
