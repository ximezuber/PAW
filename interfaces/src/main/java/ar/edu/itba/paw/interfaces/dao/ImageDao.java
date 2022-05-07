package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.model.Doctor;
import ar.edu.itba.paw.model.Image;

import java.util.Optional;

public interface ImageDao {

    Image createProfileImage(byte[] image, Doctor doctor);

    void deleteProfileImage(Image profileImage);

    Optional<Image> getProfileImageByLicense(String license);

    Optional<Image> getProfileImageById(int id);
}
