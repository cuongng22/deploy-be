package com.example.demo.service.user;

import com.example.demo.repository.IUserRepository;
import com.example.demo.request.SignUpRequest;
import com.example.demo.service.JwtService;
import com.example.demo.table.JwtResponse;
import com.example.demo.table.Role;
import com.example.demo.table.User;
import com.example.demo.table.UserPrincipal;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private IUserRepository userRepository;

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Value("${file-upload}")
    private String uploadPath;
    
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        return UserPrincipal.build(user.get());
    }

    public User register(SignUpRequest request) throws Exception {
        Optional<User> userByUsername = userRepository.findByUsername(request.getUsername());
        if (userByUsername.isPresent()) {
            throw new Exception("Tên tài khoản đã tồn tại!");
        }
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new Exception("Mật khẩu không trùng khớp!");
        }
        User createItem = new User();
        BeanUtils.copyProperties(request, createItem, "id" );
        String password = createItem.getPassword();
        String encodePassword = passwordEncoder.encode(password);//Mã hóa pass của người dùng
        createItem.setPassword(encodePassword);
        List<Role> roles = new ArrayList<>();
        roles.add(new Role(2L, "ROLE_USER"));
        createItem.setRoles(roles);
        createItem.setAvatar("avatar1-default.jpg");
        createItem.setBackground("background-default.jpg");
        userRepository.save(createItem);
        return createItem;
    }

    public JwtResponse login(User user) {
        //Kiểm tra username và pass có đúng hay không
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        //Lưu user đang đăng nhập vào trong context của security
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtService.generateTokenLogin(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<User> currentUser = userRepository.findByUsername(user.getUsername());
        return new JwtResponse(currentUser.get().getId(), jwt, userDetails.getUsername(), userDetails.getAuthorities());
    }

    public User changePassword(SignUpRequest user) throws Exception {
        User u = userRepository.findById(user.getId()).get();
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            throw new Exception("Mật khẩu mới không trùng khớp");
        }
        BeanUtils.copyProperties(user, u , "id");
        return userRepository.save(u);
    }

    public User update(SignUpRequest request) throws Exception {
        Optional<User> user = userRepository.findById(request.getId());
        if (!user.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu");
        }
        User userUpdate = user.get();
        String[] ignoredFields = {"id", "avatar", "background"};
        BeanUtils.copyProperties(request,userUpdate, ignoredFields);

        // avartar
        if (!ObjectUtils.isEmpty(request.getAvatar())) {
            MultipartFile multipartFile = request.getAvatar();
            String avatar = multipartFile.getOriginalFilename();
            try {
                FileCopyUtils.copy(multipartFile.getBytes(), new File(uploadPath + avatar));
            } catch (IOException e) {
                e.printStackTrace();
            }
            userUpdate.setAvatar(avatar);
        }

        // background

        if (!ObjectUtils.isEmpty(request.getBackground())) {
            MultipartFile multipartFile1 = request.getBackground();
            String background = multipartFile1.getOriginalFilename();
            try {
                FileCopyUtils.copy(multipartFile1.getBytes(), new File(uploadPath + background));
            } catch (IOException e) {
                e.printStackTrace();
            }
            userUpdate.setBackground(background);
        }
        return userRepository.save(userUpdate);
    }
}
