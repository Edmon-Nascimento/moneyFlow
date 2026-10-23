package com.moneyflow.backend.category;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CategoryControllerIntegrationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void createsReadsUpdatesAndDeletesCategory() throws Exception {
		MvcResult createResult = mockMvc.perform(post("/api/categories")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{"name":"  Alimentação  ","type":"EXPENSE","targetPercentage":25.00}
						"""))
			.andExpect(status().isCreated())
			.andExpect(header().exists("Location"))
			.andExpect(jsonPath("$.name").value("Alimentação"))
			.andExpect(jsonPath("$.type").value("EXPENSE"))
			.andExpect(jsonPath("$.targetPercentage").value(25.00))
			.andReturn();

		String location = createResult.getResponse().getHeader("Location");

		mockMvc.perform(get("/api/categories"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].name").value("Alimentação"));

		mockMvc.perform(get(location))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name").value("Alimentação"));

		mockMvc.perform(put(location)
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{"name":"Mercado","type":"EXPENSE","targetPercentage":30.00}
						"""))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name").value("Mercado"))
			.andExpect(jsonPath("$.targetPercentage").value(30.00));

		mockMvc.perform(delete(location)).andExpect(status().isNoContent());
		mockMvc.perform(get(location))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.title").value("Category not found"));
		mockMvc.perform(get("/api/categories/not-a-number"))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.title").value("Invalid path parameter"));
	}

	@Test
	void rejectsInvalidCategoryAndReturnsFieldErrors() throws Exception {
		mockMvc.perform(post("/api/categories")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{"name":"   ","type":"EXPENSE","targetPercentage":125.00}
						"""))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.title").value("Validation failed"))
			.andExpect(jsonPath("$.errors.name").exists())
			.andExpect(jsonPath("$.errors.targetPercentage").exists());
	}
}
