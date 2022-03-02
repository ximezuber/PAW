package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.model.Doctor;
import ar.edu.itba.paw.model.Image;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    void createProfileImage(MultipartFile file, Doctor doctor);

    long updateProfileImage(MultipartFile file, Doctor doctor);

    void createProfileImage(byte[] image, Doctor doctor);

    void updateProfileImage(byte[] image, Doctor doctor);

    void deleteProfileImage(String license);

    Image getProfileImage(String license);


}
