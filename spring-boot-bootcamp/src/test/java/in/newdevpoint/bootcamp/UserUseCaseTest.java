package in.newdevpoint.bootcamp;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import in.newdevpoint.bootcamp.dto.UserReq;
import in.newdevpoint.bootcamp.entity.UserEntity;
import in.newdevpoint.bootcamp.exceptions.UserNotFoundException;
import in.newdevpoint.bootcamp.mapper.UserMapper;
import in.newdevpoint.bootcamp.service.UserService;
import in.newdevpoint.bootcamp.usecase.UserUseCase;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
public class UserUseCaseTest {

  @Mock private UserService userService;

  @Mock private UserMapper userMapper;

  @InjectMocks private UserUseCase userUseCase;

  private UserEntity userEntity;
  private UserReq userReq;

  @BeforeEach
  void setUp() {
    userEntity = new UserEntity();
    userEntity.setId("1");
    userEntity.setUsername("testUser");
    userEntity.setEmail("test@example.com");

    userReq = new UserReq();
    userReq.setId("1");
    userReq.setUsername("testUser");
    userReq.setEmail("test@example.com");
  }

  @Test
  void getUsers_returnsListOfUserReq() {
    List<UserEntity> userEntities = new ArrayList<>();
    userEntities.add(userEntity);
    when(userService.getUsers()).thenReturn(userEntities);
    when(userMapper.mapToResponseEntityList(userEntities)).thenReturn(List.of(userReq));

    List<UserReq> result = userUseCase.getUsers();

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("testUser", result.get(0).getUsername());
  }

  @Test
  void searchUser_returnsPageOfUserReq() {
    Page<UserReq> mockPage = new PageImpl<>(List.of(userReq));
    when(userService.searchUser(10, 0, "ASC", null, "id")).thenReturn(mockPage);

    Page<UserReq> result = userUseCase.searchUser(10, 0, "ASC", null, "id");

    assertNotNull(result);
    assertEquals(1, result.getContent().size());
    assertEquals("testUser", result.getContent().get(0).getUsername());
  }

  @Test
  void updateUser_returnsUpdatedUserReq() {
    when(userMapper.mapFromReqToUserEntity(userReq)).thenReturn(userEntity);
    when(userService.updateUser("1", userEntity)).thenReturn(userEntity);
    when(userMapper.mapToResponseEntity(userEntity)).thenReturn(userReq);

    UserReq result = userUseCase.updateUser(userReq);

    assertNotNull(result);
    assertEquals("testUser", result.getUsername());
  }

  @Test
  void deleteUser_deletesUser() {
    String userId = "1";
    userUseCase.deleteUser(userId);
    verify(userService, times(1)).deleteUser(userId);
  }

  @Test
  void getUser_returnsUserReq() {
    when(userService.getUser("testUser")).thenReturn(Optional.of(userEntity));
    when(userMapper.mapToResponseEntity(userEntity)).thenReturn(userReq);

    UserReq result = userUseCase.getUser("testUser");

    assertNotNull(result);
    assertEquals("testUser", result.getUsername());
  }

  @Test
  void getUser_throwsExceptionWhenUserNotFound() {
    when(userService.getUser("nonExistentUser")).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> userUseCase.getUser("nonExistentUser"));
  }

  @Test
  void getUserById_returnsUserReq() {
    when(userService.getUserById("1")).thenReturn(Optional.of(userEntity));
    when(userMapper.mapToResponseEntity(userEntity)).thenReturn(userReq);

    UserReq result = userUseCase.getUserById("1");

    assertNotNull(result);
    assertEquals("testUser", result.getUsername());
  }

  @Test
  void getUserById_throwsExceptionWhenUserNotFound() {
    when(userService.getUserById("nonExistentId")).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> userUseCase.getUserById("nonExistentId"));
  }

  @Test
  void uploadUserProfile_returnsUpdatedUserReq() {
    MultipartFile file =
        new MockMultipartFile("file", "test.jpg", "image/jpeg", "test image content".getBytes());
    when(userService.uploadUserProfile(file, "testUser")).thenReturn(userEntity);
    when(userMapper.mapToResponseEntity(userEntity)).thenReturn(userReq);

    UserReq result = userUseCase.uploadUserProfile(file, "testUser");

    assertNotNull(result);
    assertEquals("testUser", result.getUsername());
  }
}
