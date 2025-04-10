package com.example.userservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.userservice.client.OrderServiceClient;
import com.example.userservice.dto.UserDto;
import com.example.userservice.error.FeignErrorDecoder;
import com.example.userservice.jpa.UserEntity;
import com.example.userservice.jpa.UserRepository;
import com.example.userservice.security.EncoderConfig;
import com.example.userservice.vo.ResponseOrder;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
	 
	private final UserRepository userRepository;
	private final EncoderConfig encoderConfig;
	
	private final RestTemplate restTemplate;
	private final Environment env;
	
	private final OrderServiceClient orderServiceClient; 
	
	@Override
	public UserDto createUser(UserDto userDto) {
		userDto.setUserId(UUID.randomUUID().toString());
		
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		UserEntity userEntity = mapper.map(userDto, UserEntity.class);
		userEntity.setEncryptedPwd(encoderConfig.encoder().encode(userDto.getPwd()));
		userRepository.save(userEntity);
		
		userDto = mapper.map(userEntity, UserDto.class);
		
		return userDto;
	}

	@Override
	public UserDto getUserByUserId(String userId) {
		UserEntity userEntity = userRepository.findByUserId(userId);
		
		if(userEntity == null) throw new UsernameNotFoundException("user not found");
		
		UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);
		
		/*
		 // RestTemplate사용해서 Micro Service간 동기 통신
		List<ResponseOrder> orders = new ArrayList<>();
		String orderUrl = String.format(env.getProperty("order_service.url"), userId); 
		 ResponseEntity<List<ResponseOrder>> orderListResponse = 
				 restTemplate.exchange(orderUrl, HttpMethod.GET, null, 
						 	new ParameterizedTypeReference<List<ResponseOrder>>() {
				 });
		 List<ResponseOrder> orderList = orderListResponse.getBody();
		 */

		// FeignClient 사용해서 Micro Service간 동기 통신
//		List<ResponseOrder> orderList = null;
//		try {
//			orderList = orderServiceClient.getOrders(userId);
//		} catch (FeignException e) {
//			log.error(e.getMessage());
//		}
		
		// ErrorDecoder
		List<ResponseOrder> orderList = orderServiceClient.getOrders(userId);
		userDto.setOrders(orderList);
		
		return userDto;
	}

	@Override
	public Iterable<UserEntity> getUserByAll() { 
		return userRepository.findAll();
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity userEntity = userRepository.findByEmail(username);
		if(userEntity == null) throw new UsernameNotFoundException(username);
		return new User(userEntity.getEmail(), userEntity.getEncryptedPwd(), true, true, true, true, new ArrayList<>());
	}
	
	@Override
	public UserDto getUserDetailsByEmail(String email) {
		UserEntity userEntity = userRepository.findByEmail(email);
		
		if(userEntity == null) throw new UsernameNotFoundException(email);
		
		UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);
		return userDto;
	}

}
