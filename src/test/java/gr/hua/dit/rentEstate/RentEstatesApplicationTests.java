package gr.hua.dit.rentEstate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RentEstatesApplicationTests {

	@Test
	void contextLoads() {
	}
	@Test
	public void testCreateUser() throws Exception {
		// Arrange
		String userJson = "{\"username\":\"apiuser\",\"email\":\"api@hua.gr\",\"password\":\"pass123\"}";


		// Act
		ResultActions result = mockMvc.perform(post("/api/auth/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(userJson));

		// Assert
		result.andExpect(status().isOk());
	}

}
