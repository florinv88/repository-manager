package com.vinsguru.students;

import com.vinsguru.students.dto.StudentRequest;
import com.vinsguru.students.dto.StudentResponse;
import com.vinsguru.students.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
class StudentApiIntegrationTest {

    private static final String URL = "/api/students";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StudentRepository repository;

    // The server handles requests on its own thread, so test-managed @Transactional rollback cannot apply; clean up explicitly.
    @BeforeEach
    void cleanDatabase() {
        repository.deleteAll();
    }

    private StudentResponse createStudent(String name, int age, String className) {
        return restTemplate.postForEntity(URL, new StudentRequest(name, age, className), StudentResponse.class).getBody();
    }

    @Test
    @DisplayName("Should create a student and return 201")
    void shouldCreateStudent() {
        ResponseEntity<StudentResponse> response =
                restTemplate.postForEntity(URL, new StudentRequest("Alice", 15, "10A"), StudentResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getLocation()).isNotNull();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().id()).isNotNull();
        assertThat(response.getBody().name()).isEqualTo("Alice");
        assertThat(response.getBody().age()).isEqualTo(15);
        assertThat(response.getBody().className()).isEqualTo("10A");
    }

    @Test
    @DisplayName("Should return all students")
    void shouldReturnAllStudents() {
        createStudent("Alice", 15, "10A");
        createStudent("Bob", 16, "11B");

        ResponseEntity<StudentResponse[]> response = restTemplate.getForEntity(URL, StudentResponse[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).extracting(StudentResponse::name).containsExactlyInAnyOrder("Alice", "Bob");
    }

    @Test
    @DisplayName("Should return a student by id")
    void shouldReturnStudentById() {
        StudentResponse created = createStudent("Alice", 15, "10A");

        ResponseEntity<StudentResponse> response =
                restTemplate.getForEntity(URL + "/" + created.id(), StudentResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(created);
    }

    @Test
    @DisplayName("Should update a student")
    void shouldUpdateStudent() {
        StudentResponse created = createStudent("Alice", 15, "10A");

        ResponseEntity<StudentResponse> response = restTemplate.exchange(URL + "/" + created.id(), HttpMethod.PUT,
                new HttpEntity<>(new StudentRequest("Alice Smith", 16, "11A")), StudentResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(new StudentResponse(created.id(), "Alice Smith", 16, "11A"));
    }

    @Test
    @DisplayName("Should delete a student and return 204")
    void shouldDeleteStudent() {
        StudentResponse created = createStudent("Alice", 15, "10A");

        ResponseEntity<Void> response =
                restTemplate.exchange(URL + "/" + created.id(), HttpMethod.DELETE, null, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(repository.count()).isZero();
    }

    @Test
    @DisplayName("Should return 404 ProblemDetail when student does not exist")
    void shouldReturn404WhenNotFound() {
        ResponseEntity<ProblemDetail> response = restTemplate.getForEntity(URL + "/999", ProblemDetail.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(404);
        assertThat(response.getBody().getDetail()).contains("999");
    }

    @Test
    @DisplayName("Should return 400 ProblemDetail when request is invalid")
    void shouldReturn400WhenInvalid() {
        ResponseEntity<ProblemDetail> response =
                restTemplate.postForEntity(URL, new StudentRequest(" ", 0, ""), ProblemDetail.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(400);
    }
}
