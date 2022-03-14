package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.model.Doctor;
import ar.edu.itba.paw.model.Image;

public interface ImageDao {

    void createProfileImage(byte[] image, Doctor doctor);

    void deleteProfileImage(Image profileImage);

    Image getProfileImage(String doctor);


}
