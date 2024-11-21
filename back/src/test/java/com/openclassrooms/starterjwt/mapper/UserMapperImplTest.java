package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class UserMapperImplTest {

    public static final String MAIL = "jeanphilippe@renault.fr";
    public static final String LAST_NAME = "DeLaPorte";
    public static final String FIRST_NAME = "Jean-Philippe";
    public static final String PASSWORD = "pass@Word01";
    public static final LocalDateTime NOW = LocalDateTime.now();
    public static final LocalDateTime UPDATED_AT = LocalDateTime.now().plusDays(1);
    @InjectMocks
    private UserMapperImpl userMapper;

    private UserDto userDto;

    private User user;



    @BeforeEach
    void setUp() {

        userDto = new UserDto(1L, MAIL, LAST_NAME, FIRST_NAME, false, PASSWORD, NOW, UPDATED_AT);
user = new User();
user.setId(1L);
user.setEmail(MAIL);
user.setLastName(LAST_NAME);
user.setFirstName(FIRST_NAME);
user.setPassword(PASSWORD);
user.setCreatedAt(NOW);
user.setUpdatedAt(UPDATED_AT);

    }

    @Test
    void toEntity_whenUserDtoIsNull_returnNull() {
        UserDto nullUserDto = null;
        assertNull(userMapper.toEntity(nullUserDto));

    }

    @Test
    void toEntity_whenUserDtoIsNotNull_returnUserEntity() {
        user.setId(userDto.getId());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        user.setAdmin(userDto.isAdmin());
        user.setCreatedAt(userDto.getCreatedAt());
        User actualUserEntity = userMapper.toEntity(userDto);
        assertEquals(actualUserEntity, user);


    }

    @Test
    void toDto_withSessionIsNull_returnNull() {
        User nullUserDto = null;
        assertNull(userMapper.toDto(nullUserDto));
    }
    @Test
    void toDto_withUserIsNotNull_returnUserDto() {
        UserDto expectedUserDto = userDto;
        UserDto actualUserDto = userMapper.toDto(user);
        assertEquals(userDto, actualUserDto);

    }

    @Test
    void testToEntity_withUserDtoListIsNull_returnNull() {
        List<UserDto> nullUserDto = null;
        assertNull(userMapper.toEntity(nullUserDto));
    }
    @Test
    void testToEntity_withUserDtoListIsNotNull_returnUserEntityList() {
        List<UserDto> userDtoList = new ArrayList<>();
        userDtoList.add(userDto);
        List<User> expectedUserList = new ArrayList<>();
        expectedUserList.add(user);
        List<User> actualUserList = userMapper.toEntity(userDtoList);
        assertEquals(expectedUserList, actualUserList);
    }

    @Test
    void testToDto_withUserEntityListIsNull_returnNull() {
        List<User> nullUserList = null;
        assertNull(userMapper.toDto(nullUserList));

    }
    @Test
    void testToDto_withUserEntityListIsNotNull_returnEntityList() {
        List<User> userList = new ArrayList<>();
        userList.add(user);
        List<UserDto> expectedUserDtoList = new ArrayList<>();
        expectedUserDtoList.add(userDto);
        List<UserDto> actualUserDtoList = userMapper.toDto(userList);
        assertEquals(expectedUserDtoList, actualUserDtoList);

    }
}