package in.newdevpoint.bootcamp;

import in.newdevpoint.bootcamp.dto.UserReq;
import in.newdevpoint.bootcamp.entity.UserEntity;
import in.newdevpoint.bootcamp.mapper.UserMapper;
import in.newdevpoint.bootcamp.repository.UserRepository;
import in.newdevpoint.bootcamp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private Environment environment;

    @Mock
    private MongoTemplate template;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private UserEntity userEntity;
    private UserReq userReq;

    @BeforeEach
    void setUp() {
        userEntity = new UserEntity("testUser", "test@example.com", "123456");
        userEntity.setId("1");


        userReq = new UserReq();
        userReq.setId("1");
        userReq.setUsername("testUser");
        userReq.setEmail("test@example.com");
    }

    @Test
    void searchUser_returnsPageOfUsers() {
        List<UserEntity> userEntities = new ArrayList<>();
        userEntities.add(userEntity);

        List<UserReq> userReqs = new ArrayList<>();
        userReqs.add(userReq);

        Page<UserEntity> page = new PageImpl<>(userEntities);
        when(template.count(any(), eq(UserEntity.class))).thenReturn((long) userEntities.size());
        when(template.find(any(), eq(UserEntity.class))).thenReturn(userEntities);
        when(userMapper.mapToResponseEntityList(any())).thenReturn(userReqs);

        Page<UserReq> result = userService.searchUser(10, 0, "ASC", null, "id");

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("testUser", result.getContent().get(0).getUsername());
    }

    @Test
    void getUsers_returnsAllUsers() {
        List<UserEntity> userEntities = new ArrayList<>();
        userEntities.add(userEntity);
        when(userRepository.findAll()).thenReturn(userEntities);

        List<UserEntity> result = userService.getUsers();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("testUser", result.get(0).getUsername());
    }

    @Test
    void updateUser_updatesExistingUser() {

        UserEntity existingUser = new UserEntity("oldUser", "test@example.com", "123456");
        existingUser.setId("1");
        existingUser.setUsername("oldUser");

        UserEntity updatedUser = new UserEntity("newUser", "test@example.com", "123456");
        updatedUser.setUsername("newUser");

        when(userRepository.findById("1")).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(UserEntity.class))).thenReturn(existingUser);

        UserEntity result = userService.updateUser("1", updatedUser);

        assertNotNull(result);
                UsernameNotFoundException.class, () -> userService.updateUser("1", new UserEntity("testUser", "test@example.com", "123456")));
    }

    @Test
    void deleteUser_deletesUserById() {
        String userId = "1";
        userService.deleteUser(userId);
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void getUser_returnsUserByUsername() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(userEntity));

        Optional<UserEntity> result = userService.getUser("testUser");

        assertTrue(result.isPresent());
        assertEquals("testUser", result.get().getUsername());
    }

    @Test
    void getUserById_returnsUserById() {
        when(userRepository.findById("1")).thenReturn(Optional.of(userEntity));

        Optional<UserEntity> result = userService.getUserById("1");

        assertTrue(result.isPresent());
        assertEquals("testUser", result.get().getUsername());
    }

    @Test
    void loadUserByUsername_returnsUserByUsername() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(userEntity));

        UserEntity result = userService.loadUserByUsername("testUser");

        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
    }

    @Test
    void loadUserByUsername_throwsExceptionWhenUserNotFound() {
        when(userRepository.findByUsername("nonExistentUser")).thenReturn(Optional.empty());

        assertThrows(
                UsernameNotFoundException.class,
                () -> userService.loadUserByUsername("nonExistentUser"));
    }

    @Test
    void findById_returnsUserById() {
        when(userRepository.findById("1")).thenReturn(Optional.of(userEntity));

        UserEntity result = userService.findById("1");

        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
    }

    @Test
    void findById_throwsExceptionWhenUserNotFoundById() {
        when(userRepository.findById("nonExistentId")).thenReturn(Optional.empty());

        assertThrows(
                UsernameNotFoundException.class, () -> userService.findById("nonExistentId"));
    }

    @Test
    void uploadUserProfile_updatesUserProfile() {
        MultipartFile file =
                new MockMultipartFile("file", "test.jpg", "image/jpeg", "test image content".getBytes());
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(userEntity));
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        when(environment.getProperty("image.upload.dir")).thenReturn("/tmp");

        UserEntity result = userService.uploadUserProfile(file, "testUser");

        assertNotNull(result);
    }

    @Test
    void uploadUserProfile_returnsNullWhenUserNotFound() {
        MultipartFile file =
                new MockMultipartFile("file", "test.jpg", "image/jpeg", "test image content".getBytes());
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.empty());

        UserEntity result = userService.uploadUserProfile(file, "testUser");

        assertNull(result);
    }
}
