package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.model.Doctor;
import ar.edu.itba.paw.model.Image;

import java.util.Optional;

public interface ImageService {

    Image createProfileImage(byte[] image, Doctor doctor);

    void deleteProfileImage(Image image);

    Optional<Image> getProfileImage(int id);

    Optional<Image> getImageByLicense(String license);


}
