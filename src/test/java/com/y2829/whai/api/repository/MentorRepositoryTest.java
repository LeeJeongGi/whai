package com.y2829.whai.api.repository;

import com.y2829.whai.api.dto.MentorDto.PatchMentorRequest;
import com.y2829.whai.api.entity.Mentor;
import com.y2829.whai.api.entity.User;
import com.y2829.whai.oauth.entity.RoleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MentorRepositoryTest {

    @Autowired
    MentorRepository mentorRepository;

    @Autowired
    UserRepository userRepository;

    static private User user;

    @BeforeEach
    void init() {
        user = new User();
        user.setName("sample");
        user.setEmail("test@tet.com");
        user.setCompany("KAKAO");
        user.setRoleType(RoleType.USER);

        userRepository.save(user);
    }

    @DisplayName("Create Mentor")
    @Test
    public void createMentor() {
        //given
        Mentor initMentor = new Mentor(user, null, "3.3", "hi", 3, LocalDateTime.now());
        Mentor saveMentor = mentorRepository.save(initMentor);

        //when
        Optional<Mentor> findReview = mentorRepository.findById(saveMentor.getId());

        //then
        assertThat(saveMentor.getId()).isEqualTo(findReview.get().getId());
    }

    @DisplayName("Update Mentor")
    @Test
    public void updateMentor() {
        //given
        Mentor initMentor = new Mentor(user, null, "3.3", "hi", 3, LocalDateTime.now());
        Mentor saveMentor = mentorRepository.save(initMentor);

        PatchMentorRequest request = new PatchMentorRequest();
        request.setMentorId(saveMentor.getId());
        request.setCompany("Naver");
        request.setDescription("hello");

        saveMentor.update(request);

        //when
        Mentor updateMentor = mentorRepository.save(saveMentor);

        //then
        assertThat(saveMentor.getId()).isEqualTo(updateMentor.getId());
        assertThat(updateMentor.getCompany()).isEqualTo("Naver");
        assertThat(updateMentor.getDescription()).isEqualTo("hello");
    }

    @DisplayName("listOfMentor")
    @Test
    public void listOfMenters() {
        //given
        saveMentors();

        //when
        List<Mentor> mentors = mentorRepository.findAll();

        //then
        assertThat(mentors.size()).isEqualTo(5);
    }

    @DisplayName("deleteMentor")
    @Test
    public void deleteMentor() {
        //given
        Mentor initMentor = new Mentor(user, null, "3.3", "hi", 3, LocalDateTime.now());
        Mentor saveMentor = mentorRepository.save(initMentor);

        //when
        mentorRepository.delete(saveMentor);
        Optional<Mentor> result = mentorRepository.findById(saveMentor.getId());

        //then
        assertThat(result).isEmpty();
    }

    private void saveMentors() {
        for (int i = 0; i < 5; i++) {
            Mentor temp = new Mentor(user, null, "3.3", "hi" + i, 3, LocalDateTime.now());
            mentorRepository.save(temp);
        }
    }
}
