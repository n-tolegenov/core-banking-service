package kz.almaty.finance.corebankingservice.service;

import kz.almaty.finance.corebankingservice.model.dto.User;
import kz.almaty.finance.corebankingservice.model.entity.UserEntity;
import kz.almaty.finance.corebankingservice.model.mapper.UserMapper;
import kz.almaty.finance.corebankingservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private UserMapper userMapper = new UserMapper();
    private final UserRepository userRepository;

    public User readUser(String identificatoin){
        UserEntity userEntity = userRepository.findByIdentificationNumber(identificatoin).get();
        return userMapper.convertToDto(userEntity);
    }

    /*
        Pageable - это интерфейс в Spring Framework,
        который используется для определения параметров пагинации
        при выполнении запросов к базе данных или другим источникам данных.
        Он позволяет задавать параметры, такие как номер страницы,
        размер страницы и сортировку результатов.
     */
    public List<User> readUsers(Pageable pageable){
        return userMapper.convertToDtoList(userRepository.findAll(pageable).getContent());
    }
}
