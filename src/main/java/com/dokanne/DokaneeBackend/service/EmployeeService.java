package com.dokanne.DokaneeBackend.service;

import com.dokanne.DokaneeBackend.model.ProfileModel;
import com.dokanne.DokaneeBackend.model.StoreIds;
import com.dokanne.DokaneeBackend.repository.ProductRepository;
import com.dokanne.DokaneeBackend.repository.ProfileRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor

@Service
public class EmployeeService {

    private final ProfileRepository profileRepository;

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
}
