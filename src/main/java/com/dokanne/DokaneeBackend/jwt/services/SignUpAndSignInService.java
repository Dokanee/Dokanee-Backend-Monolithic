package com.dokanne.DokaneeBackend.jwt.services;

import com.dokanne.DokaneeBackend.dto.response.MassageResponse;
import com.dokanne.DokaneeBackend.jwt.dto.request.LoginForm;
import com.dokanne.DokaneeBackend.jwt.dto.request.PassChangeRequest;
import com.dokanne.DokaneeBackend.jwt.dto.request.SignUpForm;
import com.dokanne.DokaneeBackend.jwt.dto.response.JwtResponse;
import com.dokanne.DokaneeBackend.jwt.dto.response.UserResponse;
import com.dokanne.DokaneeBackend.jwt.model.Role;
import com.dokanne.DokaneeBackend.jwt.model.RoleName;
import com.dokanne.DokaneeBackend.jwt.model.User;
import com.dokanne.DokaneeBackend.jwt.repository.RoleRepository;
import com.dokanne.DokaneeBackend.jwt.repository.UserRepository;
import com.dokanne.DokaneeBackend.jwt.security.jwt.JwtProvider;
import com.dokanne.DokaneeBackend.model.product.v1.ProfileModel;
import com.dokanne.DokaneeBackend.repository.ProfileRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@Service
public class SignUpAndSignInService {

    @Autowired
    PasswordEncoder encoder;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    private final ProfileRepository profileRepository;

//    private final AreaNameRepository areaNameRepository;

    public Object signUp(SignUpForm signUpRequest) {


        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            //return true;
            return new JwtResponse("Email Already Exists");
        }


        User user = new User();
        UUID id = UUID.randomUUID();
        String uuid = id.toString();
        user.setId(uuid);
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        user.setUsername(signUpRequest.getEmail() + signUpRequest.getPhoneNo());
        user.setEmail(signUpRequest.getEmail());
        user.setPhoneNo(signUpRequest.getPhoneNo());
        user.setPassword(encoder.encode(signUpRequest.getPassword()));
        user.setRoles(getRolesFromStringToRole(signUpRequest.getRole()));
        userRepository.saveAndFlush(user);

        UUID newId = UUID.randomUUID();
        System.out.println("1");
        ProfileModel profileModel = new ProfileModel(newId.toString(), signUpRequest.getFirstName(), signUpRequest.getLastName(), signUpRequest.getEmail(), signUpRequest.getPhoneNo(), signUpRequest.getDob(), signUpRequest.getNid(), signUpRequest.getAddress(), (signUpRequest.getEmail() + signUpRequest.getPhoneNo()));
        profileRepository.save(profileModel);


        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        signUpRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateJwtToken(authentication);

        return new JwtResponse("OK", jwt, signUpRequest.getRole());
    }


    public JwtResponse signIn(LoginForm loginRequest) {
        Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());

        String userName;
        if (userOptional.isPresent()) {
            userName = userOptional.get().getUsername();
        } else {
            userName = "";
            //throw new ResponseStatusException(HttpStatus.valueOf(410),"User Not Exists");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userName,
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateJwtToken(authentication);

        return new JwtResponse("OK", jwt, getRolesStringFromRole(userOptional.get().getRoles()));
    }

    public ResponseEntity<UserResponse> getLoggedAuthUser() {

        Object authUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (authUser instanceof UserDetails) {
            String username = ((UserDetails) authUser).getUsername();

            Optional<User> userOptional = userRepository.findByUsername(username);

            if (userOptional.isPresent()) {
                User user = userOptional.get();

                UserResponse userResponse = new UserResponse(user.getUsername(), user.getEmail(), user.getFirstName(),
                        user.getLastName(), user.getPhoneNo(), getRolesStringFromRole(user.getRoles()));

                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.add("massage", "OK");
                return new ResponseEntity(userResponse, httpHeaders, HttpStatus.OK);


            } else {
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.add("massage", "No User Found");
                return new ResponseEntity(new UserResponse(), httpHeaders, HttpStatus.NO_CONTENT);
            }

        } else {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("massage", "Unauthenticated");
            return new ResponseEntity(new UserResponse(), httpHeaders, HttpStatus.UNAUTHORIZED);
        }

    }


    public Set<Role> getRolesFromStringToRole(Set<String> roles2) {
        Set<Role> roles = new HashSet<>();
        for (String role : roles2) {
            Optional<Role> roleOptional = roleRepository.findByName(RoleName.valueOf(role));
            System.out.println(roleOptional.get());
            if (!roleOptional.isPresent()) {
                throw new ValidationException("Role '" + role + "' does not exist.");
            }
            roles.add(roleOptional.get());
        }
        return roles;
    }

    private Set<String> getRolesStringFromRole(Set<Role> roles2) {
        Set<String> roles = new HashSet<>();
        for (Role role : roles2) {

            roles.add(role.getName().toString());
        }
        return roles;
    }

    public ResponseEntity changePass(PassChangeRequest passChangeRequest) {

        Optional<User> userOptional = userRepository.findByUsername(getLoggedAuthUser().getBody().getUsername());

        if(userOptional.isPresent()){
            User user = userOptional.get();
            if(encoder.matches(passChangeRequest.getOldPass(), user.getPassword())){
                user.setPassword(encoder.encode(passChangeRequest.getNewPass()));

                userRepository.save(user);

                MassageResponse massageResponse = new MassageResponse("Pass Changed Successful",200);
                return new ResponseEntity(massageResponse,HttpStatus.OK);
            }
            else {
                MassageResponse massageResponse = new MassageResponse("Old Pass Not Matched",400);
                return new ResponseEntity(massageResponse,HttpStatus.BAD_REQUEST);
            }
        }
        else {
            MassageResponse massageResponse = new MassageResponse("No User Found",204);
            return new ResponseEntity(massageResponse,HttpStatus.NO_CONTENT);
        }
    }

//    public String deleteUser(String email) {
//
//        if (userRepository.findByEmail(email).isPresent()) {
//
//            userRepository.deleteById(userRepository.findByEmail(email).get().getId());
//            return "Deleted";
//        } else {
//            return "Not Found";
//        }
//
//    }
//
//    public String editProfile(EditProfile editProfile) {
//        String username = getLoggedAuthUserName();
//
//        if (!username.isEmpty()) {
//            //System.out.println(username);
//            Optional<User> userOptional = userRepository.findByUsername(username);
//
//            if (userOptional.isPresent()) {
//                User user = userOptional.get();
//                if (!editProfile.getName().isEmpty()) {
//                    user.setName(editProfile.getName());
//                }
//                if (!editProfile.getPhoneNo().isEmpty()) {
//                    user.setPhoneNo(editProfile.getPhoneNo());
//                }
//                if (!editProfile.getNewPassword().isEmpty() && !editProfile.getCurrentPassword().isEmpty()) {
//                    if (encoder.matches(editProfile.getCurrentPassword(), userOptional.get().getPassword())) {
//
//                        user.setPassword(encoder.encode(editProfile.getNewPassword()));
//                    } else {
//                        return "Wrong Current Password";
//                    }
//                }
//
//                userRepository.save(user);
//                return "Saved Successfully";
//            } else {
//                return "User Not Found";
//            }
//
//        } else {
//            return "Unsuccessful";
//        }
//
//
//    }
//
//    public String addAreaList(AreaNameRequestsResponse areaNameRequestsResponse) {
//        for (String names : areaNameRequestsResponse.getAreaNames()) {
//            AreaNames areaNames = new AreaNames(names);
//            areaNameRepository.save(areaNames);
//        }
//        return "Saved";
//    }
//
//    public AreaNameRequestsResponse getAreaList() {
//        List<AreaNames> areaNamesOptional = areaNameRepository.findAll();
//
//        AreaNameRequestsResponse areaNameRequestsResponse = new AreaNameRequestsResponse();
//        List<String> areaNamesList = new ArrayList<>();
//        for (AreaNames areaNames : areaNamesOptional) {
//            areaNamesList.add(areaNames.getAreaName());
//        }
//        areaNameRequestsResponse.setAreaNames(areaNamesList);
//        return areaNameRequestsResponse;
//    }

//    public LoggedUserDetailsResponse getLoggedUserDetails(Authentication authentication) {
//
//        System.out.println(authentication.toString());
//        Collection<? extends GrantedAuthority> grantedAuthorities = authentication.getAuthorities();
//        List<String> userRoleList = new ArrayList<>();
//        for (GrantedAuthority grantedAuthority : grantedAuthorities) {
//            userRoleList.add(grantedAuthority.getAuthority());
//        }
//        LoggedUserDetailsResponse loggedUserDetailsResponse = new LoggedUserDetailsResponse();
//        loggedUserDetailsResponse.setUserName(authentication.getName());
//        loggedUserDetailsResponse.setUserRole(userRoleList);
//        loggedUserDetailsResponse.setIsAuthenticated(authentication.isAuthenticated());
//        return loggedUserDetailsResponse;
//    }


}
