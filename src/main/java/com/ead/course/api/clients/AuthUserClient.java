package com.ead.course.api.clients;

import com.ead.course.api.dtos.request.CourseUserRequest;
import com.ead.course.api.dtos.response.ResponsePageDTO;
import com.ead.course.api.dtos.response.UserDTO;
import com.ead.course.domain.models.Course;
import com.ead.course.domain.services.UtilsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Log4j2
@Component
@RequiredArgsConstructor
public class AuthUserClient {

    private final RestTemplate restTemplate;
    private final UtilsService utilsService;
    @Value("${ead.api.url.authuser}")
    String REQUEST_URL_AUTHUSER;

    public Page<UserDTO> getAllUsersByCourse(UUID courseId, Pageable pageable) {
        List<UserDTO> userDTOS = null;
        ResponseEntity<ResponsePageDTO<UserDTO>> result = null;

        String url = REQUEST_URL_AUTHUSER + utilsService.createUrlGetAllUsersByCourse(courseId, pageable);

        log.debug("Request URL: {} ", url);
        log.info("Request URL: {} ", url);

        try {
            ParameterizedTypeReference<ResponsePageDTO<UserDTO>> responseType =
                    new ParameterizedTypeReference<ResponsePageDTO<UserDTO>>() {
                    };

            result = restTemplate.exchange(url, HttpMethod.GET, null, responseType);

            userDTOS = result.getBody().getContent();

            log.debug("Response Number of Elements: {} ", userDTOS.size());
        } catch (HttpStatusCodeException e) {
            log.error("Error request / users {} ", e);
        }
        log.info("Ending request /users courseId {} ", courseId);
        return result.getBody();

    }

    public UserDTO getOneUserById(UUID userId) {
        String url = REQUEST_URL_AUTHUSER + "/api/users/" + userId;
        return restTemplate.exchange(url, HttpMethod.GET, null, UserDTO.class).getBody();
    }

    public void postSubscriptionUserInCourse(Course course, UUID userId) {
        String ulr = REQUEST_URL_AUTHUSER + "/api/users/" + userId + "/courses/subscription";
        var courseUserRequest = new CourseUserRequest();
        courseUserRequest = courseUserRequest.toBuilder()
                .courseId(course.getCourseId())
                .userId(userId)
                .build();

        restTemplate.postForObject(ulr, courseUserRequest, String.class);
    }
}
