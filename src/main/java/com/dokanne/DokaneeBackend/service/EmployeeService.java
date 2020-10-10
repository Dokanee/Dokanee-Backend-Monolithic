package com.dokanne.DokaneeBackend.service;

import com.dokanne.DokaneeBackend.Util.UserUtils;
import com.dokanne.DokaneeBackend.dto.request.NewEmployeeRequest;
import com.dokanne.DokaneeBackend.jwt.model.User;
import com.dokanne.DokaneeBackend.jwt.repository.UserRepository;
import com.dokanne.DokaneeBackend.jwt.services.SignUpAndSignInService;
import com.dokanne.DokaneeBackend.model.ProfileModel;
import com.dokanne.DokaneeBackend.model.StoreIds;
import com.dokanne.DokaneeBackend.model.StoreModel;
import com.dokanne.DokaneeBackend.repository.ProfileRepository;
import com.dokanne.DokaneeBackend.repository.StoreRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.*;

@AllArgsConstructor

@Service
public class EmployeeService {

    private final ProfileRepository profileRepository;
    private final UserUtils userUtils;
    private final EmailService emailService;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final SignUpAndSignInService signUpAndSignInService;

    public ResponseEntity getEmployeeList(String storeId) {

//        List<StoreIds> storeIdList = new ArrayList<>();
//        StoreIds storeIds = new StoreIds();
//        storeIds.setStoreId(storeId);
//        storeIds.setId((long) 1);
//
//        storeIdList.add(storeIds);
//
//        Optional<List<ProfileModel>> optionalProfileModelList = profileRepository.findByStoreIds(storeIdList);
//
//        if(optionalProfileModelList.isPresent()){
//            List<ProfileModel> profileModels = optionalProfileModelList.get();
//
//            for (ProfileModel profileModel: profileModels){
//                System.out.println(profileModel.getFirstName() + " "+profileModel.getLastName());
//            }
//
//        }
//        else {
//
//        }
        return new ResponseEntity("We are working on this :D ", HttpStatus.OK);

    }

    public ResponseEntity<Boolean> getEmailAvailable(String email) {
        Optional<ProfileModel> profileModelOptional = profileRepository.findByEmail(email);

        if (profileModelOptional.isPresent()) {
            return new ResponseEntity(true, HttpStatus.OK);
        } else {
            return new ResponseEntity(false, HttpStatus.NO_CONTENT);
        }
    }

    public ResponseEntity addEmployeeViaEmail(String email, String storeId) throws IOException, MessagingException {
        Optional<ProfileModel> profileModelOptional = profileRepository.findByEmail(email);

        boolean storeAuth = userUtils.isStoreIdAuth(storeId);

        if (profileModelOptional.isPresent() && storeAuth) {
            ProfileModel profileModel = profileModelOptional.get();
            StoreModel storeModel = storeRepository.findById(storeId).get();


            List<StoreIds> storeIdsList = profileModel.getStoreIds();

            StoreIds storeIds = new StoreIds();
            storeIds.setStoreId(storeId);

            storeIdsList.add(storeIds);

            profileModel.setStoreIds(storeIdsList);

            profileRepository.save(profileModel);

            String body = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                    "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                    "<head>\n" +
                    "\n" +
                    "<meta name=\"viewport\" content=\"width=device-width\" />\n" +
                    "\n" +
                    "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n" +
                    "<title>The Dokanee Team</title>\n" +
                    "\n" +
                    "<link rel=\"stylesheet\" type=\"text/css\" href=\"stylesheets/email.css\" />\n" +
                    "\n" +
                    "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                    "<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css\">\n" +
                    "<style>\n" +
                    ".fa {\n" +
                    "  padding: 10px;\n" +
                    "  font-size: 15px;\n" +
                    "  width: 15px;\n" +
                    "  text-align: center;\n" +
                    "  text-decoration: none;\n" +
                    "  margin: 5px 2px;\n" +
                    "  border-radius: 50%;\n" +
                    "}\n" +
                    "\n" +
                    ".fa:hover {\n" +
                    "    opacity: 0.7;\n" +
                    "}\n" +
                    ".fa-link {\n" +
                    "  background: #333;\n" +
                    "  color: white;\n" +
                    "}\n" +
                    "\n" +
                    ".fa-facebook {\n" +
                    "  background: #3B5998;\n" +
                    "  color: white;\n" +
                    "}.fa-facebook {\n" +
                    "  background: #3B5998;\n" +
                    "  color: white;\n" +
                    "}\n" +
                    "\n" +
                    ".fa-twitter {\n" +
                    "  background: #55ACEE;\n" +
                    "  color: white;\n" +
                    "}\n" +
                    "\n" +
                    ".fa-linkedin {\n" +
                    "  background: #007bb5;\n" +
                    "  color: white;\n" +
                    "}\n" +
                    "\n" +
                    ".fa-youtube {\n" +
                    "  background: #bb0000;\n" +
                    "  color: white;\n" +
                    "}\n" +
                    "\n" +
                    "</style>\n" +
                    "\n" +
                    "</head>\n" +
                    "\n" +
                    "<body style=\"color:#4b4b4b;font-family: 'Helvetica', 'Arial', sans-serif;\" bgcolor=\"#efefef\">\n" +
                    "\n" +
                    "<!-- HEADER -->\n" +
                    "<table class=\"head-wrap\" bgcolor=\"#efefef\">\n" +
                    "\t<tr>\n" +
                    "\t\t<td></td>\n" +
                    "\t\t<td class=\"header container\" >\n" +
                    "\n" +
                    "\t\t\t\t<div style=\"margin-top:-20px;margin-bottom:-25px\" class=\"content\">\n" +
                    "\t\t\t\t<table bgcolor=\"#efefef\">\n" +
                    "\t\t\t\t\t<tr>\n" +
                    "\t\t\t\t\t\t<td align=\"center\"><img width=\"60%\" height=\"auto\" src=\"https://i.ibb.co/N9BjjPS/logo.png\" /></td>\n" +
                    "\t\t\t\t\t</tr>\n" +
                    "\t\t\t\t</table>\n" +
                    "\t\t\t</div>\n" +
                    "\t\t</td>\n" +
                    "\t\t<td></td>\n" +
                    "\t</tr>\n" +
                    "</table><!-- /HEADER -->\n" +
                    "\n" +
                    "<!-- BODY -->\n" +
                    "<table style=\"padding:5px\" class=\"body-wrap\">\n" +
                    "\t<tr>\n" +
                    "\t\t<td></td>\n" +
                    "\t\t<td style=\"border-top:3px solid #25AA94;\" class=\"container\" bgcolor=\"#FFFFFF\">\n" +
                    "\n" +
                    "\t\t\t<div style=\"padding:30px;\"class=\"content\">\n" +
                    "\t\t\t<table>\n" +
                    "\t\t\t\t<tr>\n" +
                    "\t\t\t\t\t<td>\n" +
                    "\t\t\t\t\t\t<h4 style=\"color:#4b4b4b\">Hi, " + profileModel.getFirstName() + " " + profileModel.getLastName() + "</h4>\n" +
                    "\t\t\t\t\t\t<p class=\"lead\">We will be very glad to inform you that Someone Select you as a employee of <b>" + storeModel.getStoreName() + "</b> on Dokanee.com.bd</p>\n" +
                    "\n" +
                    "\t\t\t\t\t\t<br/>\n" +
                    "\t\t\t\t\t\t<p align=\"center\" class=\"lead\" >Now, Setting up your profile.It takes just a couple of minutes, and you only have to do it once.</p>\n" +
                    "\n" +
                    "\t\t\t\t\t\t<table width=\"100%\">\n" +
                    "\t\t\t\t\t\t\t\t\t<td align=\"center\"><button style=\"background-color:#25AA94; padding: 18px 5px 18px 5px;width : 45%;border:1px;border-radius:5px;color:white;font-size:15px;cursor:pointer;\"><b>Go to my profile</b></button></td>\n" +
                    "\t\t\t\t\t\t\t\t\t<span class=\"clear\"></span>\n" +
                    "\t\t\t\t\t\t</table>\n" +
                    "\t\t\t\t\t</td>\n" +
                    "\t\t\t\t</tr>\n" +
                    "\t\t\t</table>\n" +
                    "\t\t\t</div>\n" +
                    "\n" +
                    "\t\t</td>\n" +
                    "\t\t<td></td>\n" +
                    "\t</tr>\n" +
                    "</table><!-- /BODY -->\n" +
                    "\n" +
                    "<!-- FOOTER -->\n" +
                    "<table class=\"footer-wrap\">\n" +
                    "\t<tr>\n" +
                    "\t\t<td></td>\n" +
                    "\t\t<td class=\"container\">\n" +
                    "\t\t\t\t<div class=\"content\">\n" +
                    "\t\t\t\t<table>\n" +
                    "\t\t\t\t<tr>\n" +
                    "\t\t\t\t\t<td align=\"center\">\n" +
                    "\t\t\t\t\t\t<p class=\"lead\">Stay safe , and stay with us</p>\n" +
                    "\t\t\t\t\t<a style=\"color:#2196F3;font-size:20px; text-decoration:solid;\" href=\"mailto:dokaneeteam@gmail.com?subject = Feedback & body = Message\">The Dokanee Team</a>\n" +
                    "\t\t\t\t\t\t<br><br><br>\n" +
                    "\t\t\t\t\t\t<p>\n" +
                    "\t\t\t\t\t\t\t<a href=\"https://dokanee.com.bd\" class=\"fa fa-link\"></a>\n" +
                    "\t\t\t\t\t\t\t<a href=\"https://facebook.com/dokanee.com.bd\" class=\"fa fa-facebook\"></a>\n" +
                    "\t\t\t\t\t\t\t<a href=\"#\" class=\"fa fa-twitter\"></a>\n" +
                    "\t\t\t\t\t\t\t<a href=\"#\" class=\"fa fa-linkedin\"></a>\n" +
                    "\t\t\t\t\t\t\t<a href=\"#\" class=\"fa fa-youtube\"></a>\n" +
                    "\t\t\t\t\t\t</p>\n" +
                    "\t\t\t\t\t\t<p>Copyright © 2020 Dokanee.com.bd , All rights reserved.</p>\n" +
                    "\t\t\t\t\t</td>\n" +
                    "\t\t\t\t</tr>\n" +
                    "\t\t\t</table>\n" +
                    "\t\t\t\t</div>\n" +
                    "\n" +
                    "\t\t</td>\n" +
                    "\t\t<td></td>\n" +
                    "\t</tr>\n" +
                    "</table><!-- /FOOTER -->\n" +
                    "\n" +
                    "</body>\n" +
                    "</html>\n";

            emailService.sendMail(profileModel.getEmail(), "Welcome to Your New Shop", body);

            return new ResponseEntity("OK", HttpStatus.OK);


        } else {
            return new ResponseEntity("Problem", HttpStatus.FORBIDDEN);
        }
    }

    public ResponseEntity addNewEmployee(NewEmployeeRequest newEmployeeRequest, String storeId) throws IOException, MessagingException {

        if (userRepository.existsByEmail(newEmployeeRequest.getEmail())) {
            //return true;
            return new ResponseEntity("Email Already Exists", HttpStatus.NO_CONTENT);
        }

        Optional<StoreModel> storeModelOptional = storeRepository.findById(storeId);
        boolean storeAuth = userUtils.isStoreIdAuth(storeId);

        if (storeModelOptional.isPresent() && storeAuth) {
            Set<String> stringSet = new HashSet<>();
            stringSet.add("EMPLOYEE");

            User user = new User();
            UUID id = UUID.randomUUID();
            String uuid = id.toString();
            user.setId(uuid);
            user.setUsername(newEmployeeRequest.getEmail() + newEmployeeRequest.getPhoneNo());
            user.setEmail(newEmployeeRequest.getEmail());
            user.setPhoneNo(newEmployeeRequest.getPhoneNo());
            user.setPassword(encoder.encode(newEmployeeRequest.getPassword()));
            user.setRoles(signUpAndSignInService.getRolesFromStringToRole(stringSet));
            userRepository.saveAndFlush(user);

            UUID newId = UUID.randomUUID();
            System.out.println("1");
            ProfileModel profileModel = new ProfileModel(newId.toString(), "", "", newEmployeeRequest.getEmail(), newEmployeeRequest.getPhoneNo(), "", newEmployeeRequest.getNid(), "", (newEmployeeRequest.getEmail() + newEmployeeRequest.getPhoneNo()));

            List<StoreIds> storeIdsList = new ArrayList<>();
            StoreIds storeIds = new StoreIds();
            storeIds.setStoreId(storeId);
            storeIdsList.add(storeIds);

            profileModel.setStoreIds(storeIdsList);
            profileRepository.save(profileModel);

            emailService.sendMail(newEmployeeRequest.getEmail(), "Welcome to your new Store", getNewEmployeeEmailBody("",
                    newEmployeeRequest.getEmail(), newEmployeeRequest.getPassword(), storeModelOptional.get().getStoreName()
            ));

            return new ResponseEntity("OK", HttpStatus.OK);
        } else {
            return new ResponseEntity("Not ok", HttpStatus.UNAUTHORIZED);
        }


    }

    private String getNewEmployeeEmailBody(String name, String email, String password, String shopName) {
        String body = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                "<head>\n" +
                "\n" +
                "<meta name=\"viewport\" content=\"width=device-width\" />\n" +
                "\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n" +
                "<title>The Dokanee Team</title>\n" +
                "\n" +
                "<link rel=\"stylesheet\" type=\"text/css\" href=\"stylesheets/email.css\" />\n" +
                "\n" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                "<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css\">\n" +
                "<style>\n" +
                ".fa {\n" +
                "  padding: 10px;\n" +
                "  font-size: 15px;\n" +
                "  width: 15px;\n" +
                "  text-align: center;\n" +
                "  text-decoration: none;\n" +
                "  margin: 5px 2px;\n" +
                "  border-radius: 50%;\n" +
                "}\n" +
                "\n" +
                ".fa:hover {\n" +
                "    opacity: 0.7;\n" +
                "}\n" +
                ".fa-link {\n" +
                "  background: #333;\n" +
                "  color: white;\n" +
                "}\n" +
                "\n" +
                ".fa-facebook {\n" +
                "  background: #3B5998;\n" +
                "  color: white;\n" +
                "}.fa-facebook {\n" +
                "  background: #3B5998;\n" +
                "  color: white;\n" +
                "}\n" +
                "\n" +
                ".fa-twitter {\n" +
                "  background: #55ACEE;\n" +
                "  color: white;\n" +
                "}\n" +
                "\n" +
                ".fa-linkedin {\n" +
                "  background: #007bb5;\n" +
                "  color: white;\n" +
                "}\n" +
                "\n" +
                ".fa-youtube {\n" +
                "  background: #bb0000;\n" +
                "  color: white;\n" +
                "}\n" +
                "\n" +
                "</style>\n" +
                "\n" +
                "</head>\n" +
                "\n" +
                "<body style=\"color:#4b4b4b;font-family: 'Helvetica', 'Arial', sans-serif;\" bgcolor=\"#efefef\">\n" +
                "\n" +
                "<!-- HEADER -->\n" +
                "<table class=\"head-wrap\" bgcolor=\"#efefef\">\n" +
                "\t<tr>\n" +
                "\t\t<td></td>\n" +
                "\t\t<td class=\"header container\" >\n" +
                "\n" +
                "\t\t\t\t<div style=\"margin-top:-20px;margin-bottom:-25px\" class=\"content\">\n" +
                "\t\t\t\t<table bgcolor=\"#efefef\">\n" +
                "\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t<td align=\"center\"><img width=\"60%\" height=\"auto\" src=\"https://i.ibb.co/N9BjjPS/logo.png\" /></td>\n" +
                "\t\t\t\t\t</tr>\n" +
                "\t\t\t\t</table>\n" +
                "\t\t\t</div>\n" +
                "\t\t</td>\n" +
                "\t\t<td></td>\n" +
                "\t</tr>\n" +
                "</table><!-- /HEADER -->\n" +
                "\n" +
                "<!-- BODY -->\n" +
                "<table style=\"padding:5px\" class=\"body-wrap\">\n" +
                "\t<tr>\n" +
                "\t\t<td></td>\n" +
                "\t\t<td style=\"border-top:3px solid #25AA94;\" class=\"container\" bgcolor=\"#FFFFFF\">\n" +
                "\n" +
                "\t\t\t<div style=\"padding:30px;\"class=\"content\">\n" +
                "\t\t\t<table>\n" +
                "\t\t\t\t<tr>\n" +
                "\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t<h4 style=\"color:#4b4b4b\">Hi, " + name + "</h4>\n" +
                "\t\t\t\t\t\t<p class=\"lead\">We will be very glad to inform you that Someone Select you as a employee of <b>" + shopName + "</b> on Dokanee.com.bd</p>\n" +
                "\t\t\t\t\t\t<p>Your login info is given below.</p>\n" +
                "\n" +
                "\t\t\t\t\t\t<p class=\"callout\">\n" +
                "\t\t\t\t\t\t\tEmail: " + email + " <br/>\n" +
                "\t\t\t\t\t\t\tPassword: " + password + "" +
                "\t\t\t\t\t\t</p>\n" +
                "\t\t\t\t\t\t<br/>\n" +
                "\t\t\t\t\t\t<p align=\"center\" class=\"lead\" >Now, Setting up your profile.It takes just a couple of minutes, and you only have to do it once.</p>\n" +
                "\n" +
                "\t\t\t\t\t\t<table width=\"100%\">\n" +
                "\t\t\t\t\t\t\t\t\t<td align=\"center\"><button style=\"background-color:#25AA94; padding: 18px 5px 18px 5px;width : 45%;border:1px;border-radius:5px;color:white;font-size:15px;cursor:pointer;\"><b>Go to my profile</b></button></td>\n" +
                "\t\t\t\t\t\t\t\t\t<span class=\"clear\"></span>\n" +
                "\t\t\t\t\t\t</table>\n" +
                "\t\t\t\t\t</td>\n" +
                "\t\t\t\t</tr>\n" +
                "\t\t\t</table>\n" +
                "\t\t\t</div>\n" +
                "\n" +
                "\t\t</td>\n" +
                "\t\t<td></td>\n" +
                "\t</tr>\n" +
                "</table><!-- /BODY -->\n" +
                "\n" +
                "<!-- FOOTER -->\n" +
                "<table class=\"footer-wrap\">\n" +
                "\t<tr>\n" +
                "\t\t<td></td>\n" +
                "\t\t<td class=\"container\">\n" +
                "\t\t\t\t<div class=\"content\">\n" +
                "\t\t\t\t<table>\n" +
                "\t\t\t\t<tr>\n" +
                "\t\t\t\t\t<td align=\"center\">\n" +
                "\t\t\t\t\t\t<p class=\"lead\">Stay safe , and stay with us</p>\n" +
                "\t\t\t\t\t<a style=\"color:#2196F3;font-size:20px; text-decoration:solid;\" href=\"mailto:dokaneeteam@gmail.com?subject = Feedback & body = Message\">The Dokanee Team</a>\n" +
                "\t\t\t\t\t\t<br><br><br>\n" +
                "\t\t\t\t\t\t<p>\n" +
                "\t\t\t\t\t\t\t<a href=\"https://dokanee.com.bd\" class=\"fa fa-link\"></a>\n" +
                "\t\t\t\t\t\t\t<a href=\"https://facebook.com/dokanee.com.bd\" class=\"fa fa-facebook\"></a>\n" +
                "\t\t\t\t\t\t\t<a href=\"#\" class=\"fa fa-twitter\"></a>\n" +
                "\t\t\t\t\t\t\t<a href=\"#\" class=\"fa fa-linkedin\"></a>\n" +
                "\t\t\t\t\t\t\t<a href=\"#\" class=\"fa fa-youtube\"></a>\n" +
                "\t\t\t\t\t\t</p>\n" +
                "\t\t\t\t\t\t<p>Copyright © 2020 Dokanee.com.bd , All rights reserved.</p>\n" +
                "\t\t\t\t\t</td>\n" +
                "\t\t\t\t</tr>\n" +
                "\t\t\t</table>\n" +
                "\t\t\t\t</div>\n" +
                "\n" +
                "\t\t</td>\n" +
                "\t\t<td></td>\n" +
                "\t</tr>\n" +
                "</table><!-- /FOOTER -->\n" +
                "\n" +
                "</body>\n" +
                "</html>\n";

        return body;
    }

}
