package br.edu.ufape.plataformaeventos.dto;

public class UserRegistrationDTO {
    
    private UserDTO userDTO;
    private StudentProfileDTO studentProfileDTO;

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public StudentProfileDTO getStudentProfileDTO() {
        return studentProfileDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public void setStudentProfileDTO(StudentProfileDTO studentProfileDTO) {
        this.studentProfileDTO = studentProfileDTO;
    }

}
