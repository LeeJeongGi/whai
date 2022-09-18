package com.y2829.whai.api.service;

import com.y2829.whai.api.entity.Mentor;
import com.y2829.whai.api.entity.Review;
import com.y2829.whai.api.entity.User;
import com.y2829.whai.api.repository.MentorRepository;
import com.y2829.whai.api.repository.ReviewRepository;
import com.y2829.whai.api.repository.UserRepository;
import com.y2829.whai.api.service.impl.ReviewServiceImpl;
import com.y2829.whai.api.util.CommonUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.y2829.whai.api.dto.ReviewDto.PatchReviewRequest;
import static com.y2829.whai.api.dto.ReviewDto.PostReviewRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    @InjectMocks
    @Spy
    ReviewServiceImpl reviewService;

    @Mock
    ReviewRepository reviewRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    MentorRepository mentorRepository;

    static private MockMvc mockMvc;
    static private User user;
    static private Mentor mentor;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(reviewService)
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

    @DisplayName("Create Review Service")
    @Test
    public void createReview() {
        //given
        Long reviewId = 1L;
        PostReviewRequest request = new PostReviewRequest();
        request.setUserId(1L);
        request.setMentorId(1L);
        request.setGrade(5);
        request.setContent("Good Review");

        doReturn(reviewId).when(reviewService).saveReview(any(PostReviewRequest.class));

        //when
        Long result = reviewService.saveReview(request);

        //then
        assertThat(result).isEqualTo(1L);
        verify(reviewService, times(1)).saveReview(request);
    }

    @DisplayName("Update Review Service")
    @Test
    public void updateReview() {
        //given
        PatchReviewRequest request = new PatchReviewRequest();
        request.setUserId(1L);
        request.setMentorId(1L);
        request.setGrade(3);
        request.setContent("Not Good");
        request.setReviewId(1L);

        Long resultReviewId = 1L;
        doReturn(resultReviewId).when(reviewService).modifyReview(any(PatchReviewRequest.class));

        //when
        Long result = reviewService.modifyReview(request);

        //then
        assertThat(result).isEqualTo(1L);
    }

    @DisplayName("Review List Service")
    @Test
    public void listOfReviews() {
        //given
        List<Review> reviews = getReviews();
        Page<Review> pages = new PageImpl<>(reviews);

        doReturn(pages).when(reviewService).findAll(any(Pageable.class));
        Pageable pageable = CommonUtil.getPageable();

        //when
        Page<Review> results = reviewService.findAll(pageable);

        //then
        assertThat(results.getContent().size()).isEqualTo(5);
    }

    @DisplayName("Delete Review Service")
    @Test
    public void deleteReview() {
        //given
        Long resultId = 1L;

        doReturn(resultId).when(reviewService).removeReview(1L, 1L);

        //when
        Long result = reviewService.removeReview(1L, 1L);

        //then
        assertThat(result).isEqualTo(1);
    }

    private List<Review> getReviews() {

        List<Review> reviews = new ArrayList<>(5);
        for (int i = 0; i < 5; i++) {
            reviews.add(new Review(user, mentor, 5, "good" + i, LocalDateTime.now()));
        }

        return reviews;
    }
}
