package com.y2829.whai.api.service;

import com.y2829.whai.api.dto.MentorDto;
import com.y2829.whai.api.entity.Mentor;
import com.y2829.whai.api.entity.User;
import com.y2829.whai.api.service.impl.MentorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.y2829.whai.api.dto.MentorDto.PatchMentorRequest;
import static com.y2829.whai.api.dto.MentorDto.PostMentorRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MentorServiceTest {

    @InjectMocks
    @Spy
    MentorServiceImpl mentorService;

    static private MockMvc mockMvc;

    static private User user;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(mentorService)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .setCustomArgumentResolvers(
                        new PageableHandlerMethodArgumentResolver()
                ).build();

        user = new User();
        user.setId(1L);
        user.setName("sample");
    }

    @DisplayName("Mentor create")
    @Test
    public void mentorCreateTest() {
        //given
        Long mentorId = 1L;
        PostMentorRequest request = new PostMentorRequest();
        request.setDescription("Hello, Nice to meet you");
        request.setUserId(user.getId());
        request.setCompany("KAKAO");

        doReturn(mentorId).when(mentorService).saveMentor(any(MentorDto.PostMentorRequest.class));

        //when
        Long result = mentorService.saveMentor(request);

        //then
        assertThat(result).isEqualTo(1L);
        verify(mentorService, times(1)).saveMentor(request);
    }

    @DisplayName("update Mentor")
    @Test
    public void updateMentor() {
        //given
        PatchMentorRequest request = new PatchMentorRequest();
        request.setMentorId(1L);
        request.setCompany("NAVER");
        request.setUserId(user.getId());
        request.setDescription("HELLO");

        Long resultMentorId = 1L;
        doReturn(resultMentorId).when(mentorService).modifyMentor(any(PatchMentorRequest.class));

        //when
        Long result = mentorService.modifyMentor(request);

        //then
        assertThat(result).isEqualTo(1L);
    }

    @DisplayName("MentorList")
    @Test
    public void listOfMentor() {
        //given
        List<Mentor> mentors = getMentors();
        Page<Mentor> pages = new PageImpl<>(mentors);

        doReturn(pages).when(mentorService).findAll(any(Pageable.class));
        Pageable pageable = getPageable();

        //when
        Page<Mentor> result = mentorService.findAll(pageable);

        //then
        assertThat(result.getContent().size()).isEqualTo(5);
    }

    @DisplayName("delete Mentor")
    @Test
    public void deleteMentor() {
        //given
        Long mentorId = 1L;

        doReturn(mentorId).when(mentorService).removeMentor(1L);

        //when
        Long result = mentorService.removeMentor(mentorId);

        //then
        assertThat(result).isEqualTo(1);
    }

    private List<Mentor> getMentors() {

        List<Mentor> mentors = new ArrayList<>(5);
        for (int i = 0; i < 5; i++) {
            mentors.add(new Mentor(user, null, "3.3", "hi" + i, 3, LocalDateTime.now()));
        }

        return mentors;
    }

    private Pageable getPageable() {

        Pageable pageable = new Pageable() {
            @Override
            public int getPageNumber() {
                return 0;
            }

            @Override
            public int getPageSize() {
                return 0;
            }

            @Override
            public long getOffset() {
                return 0;
            }

            @Override
            public Sort getSort() {
                return null;
            }

            @Override
            public Pageable next() {
                return null;
            }

            @Override
            public Pageable previousOrFirst() {
                return null;
            }

            @Override
            public Pageable first() {
                return null;
            }

            @Override
            public Pageable withPage(int pageNumber) {
                return null;
            }

            @Override
            public boolean hasPrevious() {
                return false;
            }
        };

        return pageable;
    }
}
