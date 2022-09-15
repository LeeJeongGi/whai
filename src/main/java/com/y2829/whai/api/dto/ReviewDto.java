package com.y2829.whai.api.dto;

import com.y2829.whai.api.entity.Review;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ReviewDto {

    @Setter
    @Getter
    @NoArgsConstructor
    public static class PostReviewRequest {
        @NotNull
        private Long userId;
        @NotNull
        private Long mentorId;
        @NotNull
        private Integer grade;
        @NotNull
        private String content;

        public Review toEntity() {
            return Review.builder()
                    .grade(grade)
                    .content(content)
                    .createAt(LocalDateTime.now())
                    .build();
        }
    }

    @Getter
    @Setter
    public static class PatchReviewRequest {
        @NotNull
        private Long reviewId;
        @NotNull
        private Long userId;
        @NotNull
        private Long mentorId;
        @NotNull
        private Integer grade;
        @NotNull
        private String content;
    }

    @Getter
    @Setter
    public static class SimpleReview {
        private Long userId;
        private Long mentorId;
        private Integer grade;
        private String content;

        public SimpleReview(Review review) {
            this.userId = review.getUser().getId();
            this.mentorId = review.getMentor().getId();
            this.grade = review.getGrade();
            this.content = review.getContent();

        }
    }

    @Getter
    @Setter
    public static class ReviewResponse {
        private Page<SimpleReview> reviews;
        public ReviewResponse(Page<Review> reviews) {

            List<SimpleReview> allReview = reviews.stream()
                    .map(SimpleReview::new)
                    .collect(Collectors.toList());

            this.reviews = new PageImpl<>(allReview);
        }
    }

    @Getter
    @Setter
    public static class PageReviewResponse {
        private final Page<SimpleReview> reviews;

        public PageReviewResponse(Page<Review> reviews) {
            List<SimpleReview> list = reviews.stream()
                    .map(SimpleReview::new)
                    .collect(Collectors.toList());
            this.reviews = new PageImpl<>(list);
        }
    }

}
