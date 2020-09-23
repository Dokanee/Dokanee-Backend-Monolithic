//package com.dokanne.DokaneeBackend.Util;
//
//import com.dokanne.DokaneeBackend.jwt.dto.response.OwnerProfileResponse;
//import com.dokanne.DokaneeBackend.jwt.model.Role;
//import com.dokanne.DokaneeBackend.model.OwnerProfile;
//import com.dokanne.DokaneeBackend.repository.OwnerProfileRepository;
//import lombok.AllArgsConstructor;
//import lombok.NoArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.HashSet;
//import java.util.Set;
//
//@AllArgsConstructor
//
//public class UserUtils{
//
//    private final OwnerProfileRepository opRepo;
//    public OwnerProfileResponse getAuthUserInfo() {
//
//        Object authUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        OwnerProfileResponse response;
//
//        if (authUser instanceof UserDetails) {
//
//            String username = ((UserDetails) authUser).getUsername();
//            OwnerProfile data = opRepo.findByUserName(username);
//
//            response = new OwnerProfileResponse("OK",data.getOwnerId(),data.getFirstName(),data.getLastName(),data.getEmail(),data.getPhone(),data.getAddress(),data.getStoreIds());
//
//            return response;
//
//        } else if (authUser instanceof UserDetails == false) {
//            response = new OwnerProfileResponse();
//            response.setMassage("Bearer Token Problem");
//            return response;
//
//        } else {
//            String username = authUser.toString();
//
//            System.out.println(username);
//        }
//        response = new OwnerProfileResponse();
//        response.setMassage("Not Found");
//        return response;
//
//    }
//
//    private Set<String> getRolesToString(Set<Role> roles2) {
//        Set<String> roles = new HashSet<>();
//        for (Role role : roles2) {
//
//            roles.add(role.getName().toString());
//        }
//        return roles;
//    }
//
//
//
//}
