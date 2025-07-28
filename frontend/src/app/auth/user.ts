export interface User {
    id: number;
    email: string;
    name: string;
    password: string;
    role: string;
}

export interface UserDTO {
  name: string;
  email: string;
  password: string;
}

export interface StudentProfileDTO {
  fullName: string;
  cpf: string;
  birthDate: string; 
  phoneNumber: string;
  degreeProgram: string; 
  currentPeriod: number;
}

export interface StudentRegisterRequest {
  userDTO: UserDTO;
  studentProfileDTO: StudentProfileDTO;
}
