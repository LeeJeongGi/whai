package com.y2829.whai.api.repository;

import com.y2829.whai.api.dto.ReviewDto;
import com.y2829.whai.api.entity.Mentor;
import com.y2829.whai.api.entity.Review;
import com.y2829.whai.api.entity.User;
import com.y2829.whai.oauth.entity.RoleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ReviewRepositoryTest {

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MentorRepository mentorRepository;

    static private User user;
    static private Mentor mentor;

    @BeforeEach
    void init() {
        user = new User();
        user.setName("sample");
        user.setEmail("test@tet.com");
        user.setCompany("KAKAO");
        user.setRoleType(RoleType.USER);
        userRepository.save(user);

        mentor = new Mentor();
        mentor.setUser(user);
        mentor.setCompany("KAKAO");
        mentorRepository.save(mentor);
    }

    @DisplayName("CreateReview")
    @Test
    public void createReview() {
        //given
        Review initReview = new Review(user, mentor, 5, "good", LocalDateTime.now());
        Review saveReview = reviewRepository.save(initReview);

        //when
        Optional<Review> findReview = reviewRepository.findById(saveReview.getId());

        //then
        assertThat(saveReview.getId()).isEqualTo(findReview.get().getId());
    }

    @DisplayName("UpdateReview")
    @Test
    public void updateReview() {
        //given
        Review initReview = new Review(user, mentor, 5, "good", LocalDateTime.now());
        Review saveReview = reviewRepository.save(initReview);

        ReviewDto.PatchReviewRequest request = new ReviewDto.PatchReviewRequest();
        request.setReviewId(saveReview.getId());
        request.setGrade(4);
        request.setContent("Not Good");

        saveReview.update(request);

        //when
        Review updateReview = reviewRepository.save(saveReview);

        //then
        assertThat(saveReview.getId()).isEqualTo(updateReview.getId());
        assertThat(updateReview.getGrade()).isEqualTo(4);
        assertThat(updateReview.getContent()).isEqualTo("Not Good");
    }

    @DisplayName("List Of Review")
    @Test
    public void listOfReviews() {
        //given
        saveReviews();

        //when
        List<Review> findReviews = reviewRepository.findAll();

        //then
        assertThat(findReviews.size()).isEqualTo(5);
    }

    @DisplayName("Delete Review")
    @Test
    public void deleteReview() {
        //given
        Review initReview = new Review(user, mentor, 5, "good", LocalDateTime.now());
        Review saveReview = reviewRepository.save(initReview);

        //when
        reviewRepository.delete(saveReview);
        Optional<Review> result = reviewRepository.findById(saveReview.getId());

        assertThat(result).isEmpty();

    }

    private void saveReviews() {
        List<Review> reviews = new ArrayList<>(5);
        for (int i = 0; i < 5; i++) {
            Review temp = new Review(user, mentor, 5, "good" + i, LocalDateTime.now());
            reviewRepository.save(temp);
        }
    }
}