package ar.edu.itba.paw.webapp.dto;

public class ImageDto {
    private byte[] image;

    public static ImageDto fromImage(byte[] img) {
        ImageDto imageDto = new ImageDto();
        imageDto.image = img;
        return imageDto;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
