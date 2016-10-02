package com.register.example.uploadFiles;

import com.register.example.IntegrationTestBase;
import com.register.example.entity.CurrentUser;
import com.register.example.exceptions.StorageException;
import com.register.example.service.FileSystemStorageService;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Ignore
public class FileUploadTest extends IntegrationTestBase {

    @Autowired
    protected WebApplicationContext wac;

    private MockMvc mvc;

    @Autowired
    private UserDetailsService userDetailsService;

    @MockBean
    private FileSystemStorageService storageService;

    @Before
    public void setUp() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void shouldListAllFiles() throws Exception {
        CurrentUser currentUser = (CurrentUser) userDetailsService.loadUserByUsername("admin");
        TestingAuthenticationToken testingAuthenticationToken = new TestingAuthenticationToken(currentUser, null);
        SecurityContextHolder.getContext().setAuthentication(testingAuthenticationToken);

        given(this.storageService.loadAll())
                .willReturn(Stream.of(Paths.get("first.txt"), Paths.get("second.txt")));

        this.mvc.perform(get("/upload"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("files",
                        Matchers.contains("http://localhost/upload/files/first.txt", "http://localhost/upload/files/second.txt")));
    }

    @Test
    public void shouldSaveUploadedFile() throws Exception {
        MockMultipartFile multipartFile =
                new MockMultipartFile("file", "test.txt", "text/plain", "Spring Framework".getBytes());
        this.mvc.perform(fileUpload("/upload").file(multipartFile))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", "/upload"));

        then(this.storageService).should().store(multipartFile);
    }

    @Test
    public void should404WhenMissingFile() throws Exception {
        given(this.storageService.loadAsResource("test.txt"))
                .willThrow(StorageException.class);

        this.mvc.perform(get("/files/test.txt"))
                .andExpect(status().isNotFound());
    }

}