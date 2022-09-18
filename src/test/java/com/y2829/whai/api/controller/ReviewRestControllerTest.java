package com.y2829.whai.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.y2829.whai.api.dto.ReviewDto.PatchReviewRequest;
import com.y2829.whai.api.entity.Mentor;
import com.y2829.whai.api.entity.Review;
import com.y2829.whai.api.entity.User;
import com.y2829.whai.api.service.ReviewService;
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

import static com.y2829.whai.api.dto.ReviewDto.PostReviewRequest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ReviewRestControllerTest {

    @InjectMocks
    ReviewRestController reviewRestController;

    @Mock
    ReviewService reviewService;

    private MockMvc mockMvc;
    private static User user;
    private static Mentor mentor;
    private static ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(reviewRestController)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .setCustomArgumentResolvers(
                        new PageableHandlerMethodArgumentResolver()
                ).build();

        user = new User();
        user.setId(1L);
        user.setName("sample");

        mentor = new Mentor();
        mentor.setId(1L);
        mentor.setUser(user);
    }

    @DisplayName("Create Review Controller")
    @Test
    public void createReview() throws Exception {
        //given
        PostReviewRequest request = new PostReviewRequest();
        request.setUserId(1L);
        request.setMentorId(1L);
        request.setGrade(5);
        request.setContent("Good");

        Long result = 1L;
        String contentBody = objectMapper.writeValueAsString(request);

        doReturn(result).when(reviewService)
                .saveReview(any(PostReviewRequest.class));

        //when
        ResultActions resultAction = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/review")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contentBody)
        );

        //then
        MvcResult mvcResult = resultAction.andExpect(status().isOk()).andReturn();
    }

    @DisplayName("Review list Controller")
    @Test
    public void listOfReview() throws Exception {
        //given
        List<Review> initReviews = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            initReviews.add(new Review(user, mentor, 5, "good" + i, LocalDateTime.now()));
        }

        Page<Review> reviews = new PageImpl<>(initReviews);
        doReturn(reviews).when(reviewService).findAll(any());

        //when
        MultiValueMap<String , String> info = new LinkedMultiValueMap<>();
        info.add("page", "5");
        info.add("size", "5");
        info.add("sort", "true");

        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/review/list")
                        .params(info)
        );

        //then
        MvcResult mvcResult = resultActions.andExpect(status().isOk())
                .andDo(System.out::println).andReturn();
        System.out.println("mvcResult = " + mvcResult);
//        assertThat(mvcResult.getResponse().getContentAsString())
    }

    @DisplayName("Update Review Controller")
    @Test
    public void updateReview() throws Exception {
        //given
        PatchReviewRequest request = new PatchReviewRequest();
        request.setUserId(user.getId());
        request.setMentorId(mentor.getId());
        request.setReviewId(1L);
        request.setGrade(3);
        request.setContent("리뷰 수정~");

        String contentBody = objectMapper.writeValueAsString(request);
        Long result = 1L;
        doReturn(result).when(reviewService)
                .modifyReview(any(PatchReviewRequest.class));

        //when
        ResultActions resultAction = mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/v1/review")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contentBody)
        );

        //then
        MvcResult mvcResult = resultAction.andExpect(status().isOk()).andReturn();
        System.out.println("mvcResult = " + mvcResult);
    }

    @DisplayName("Delete Review Controller")
    @Test
    public void deleteReview() throws Exception {
        //given
        Long removeReviewId = 1L;

        //when
        doReturn(removeReviewId).when(reviewService)
                .removeReview(1L, 1L);

        ResultActions resultAction = mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/v1/review/1/1")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        MvcResult mvcResult = resultAction.andExpect(status().isOk()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }
}