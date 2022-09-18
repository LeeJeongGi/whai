package com.y2829.whai.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.y2829.whai.api.dto.MentorDto;
import com.y2829.whai.api.entity.Mentor;
import com.y2829.whai.api.entity.User;
import com.y2829.whai.api.service.MentorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.y2829.whai.api.dto.MentorDto.PatchMentorRequest;
import static com.y2829.whai.api.dto.MentorDto.PostMentorRequest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class MentorRestControllerTest {

    @InjectMocks
    MentorRestController mentorRestController;

    @Mock
    MentorService mentorService;

    private MockMvc mockMvc;

    private static User user;

    private static ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(mentorRestController)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .setCustomArgumentResolvers(
                        new PageableHandlerMethodArgumentResolver()
                ).build();

        user = new User();
        user.setId(1L);
        user.setName("sample");

    }

    @DisplayName("Create Mentor Controller")
    @Test
    public void mentorCreate() throws Exception {
        //given
        PostMentorRequest request = new PostMentorRequest();
        request.setUserId(user.getId());
        request.setCompany("KAKAO");
        request.setDescription("HI");

        Long result = 1L;
        String contentBody = objectMapper.writeValueAsString(request);

        doReturn(result).when(mentorService)
                .saveMentor(any(MentorDto.PostMentorRequest.class));

        //when
        ResultActions resultAction = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/mentor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contentBody)
        );

        //then
        MvcResult mvcResult = resultAction.andExpect(status().isOk()).andReturn();

    }

    @DisplayName("Update Mentor Controller")
    @Test
    public void mentorUpdate() throws Exception {
        //given
        PatchMentorRequest request = new PatchMentorRequest();
        request.setUserId(user.getId());
        request.setMentorId(1L);
        request.setCompany("LINE");
        request.setDescription("HELLO");

        String contentBody = objectMapper.writeValueAsString(request);
        Long result = 1L;
        doReturn(result).when(mentorService)
                .modifyMentor(any(PatchMentorRequest.class));

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/v1/mentor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contentBody)
        );

        //then
        MvcResult mvcResult = resultActions.andExpect(status().isOk()).andReturn();
        System.out.println("mvcResult = " + mvcResult);
    }

    @DisplayName("Mentor List Controller")
    @Test
    public void listOfMentor() throws Exception {
        //given
        List<Mentor> initMentors = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            initMentors.add(new Mentor(user, null, "3.3", "hi" + i, 3, LocalDateTime.now()));
        }

        Page<Mentor> mentors = new PageImpl<>(initMentors);
        doReturn(mentors).when(mentorService).findAll(any());

        //when
        MultiValueMap<String , String> info = new LinkedMultiValueMap<>();
        info.add("page", "5");
        info.add("size", "5");
        info.add("sort", "true");

        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/mentor/list")
                        .params(info)
        );

        //then
        MvcResult mvcResult = resultActions.andExpect(status().isOk())
                .andDo(System.out::println).andReturn();
        System.out.println("mvcResult = " + mvcResult);
//        assertThat(mvcResult.getResponse().getContentAsString())
    }

    @DisplayName("Delete Mentor Controller")
    @Test
    public void deleteMentor() throws Exception {
        //given
        Long removeMentorId = 1L;

        //when
        doReturn(removeMentorId).when(mentorService)
                .removeMentor(1L);

        ResultActions resultAction = mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/v1/mentor/1/1")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        MvcResult mvcResult = resultAction.andExpect(status().isOk()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }
}
