import { Injectable } from '@angular/core';
import { StudentRegisterRequest, User, UserDTO } from './user';
import { jwtDecode} from 'jwt-decode';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/auth';

  async login(email: string, password: string): Promise<boolean> {
    console.log('Tentando fazer login');
    try {
      const response = await fetch(this.apiUrl + '/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, password }),
      });

      if (!response.ok) {
        throw new Error('Login falhou');
      }

      const data = await response.json();
      localStorage.setItem('token', data.token);
      return true;
    } catch (error) {
      console.error('Erro ao fazer login:', error);
      return false;
    }
  }

  logout(): void {
    localStorage.removeItem('token');
  }

  async register(newUser: UserDTO): Promise<User | null> {
    console.log('Tentando fazer register');
    try {
      const response = await fetch(this.apiUrl + '/register/organizer', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(newUser)
      });
      return await response.json();
    } catch (error) {
      console.error('Error registering user:', error);
      return null;
    }
  }

  async registerStudent(request: StudentRegisterRequest): Promise<any> {
  try {
    const response = await fetch(this.apiUrl + '/register/student', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(request)
    });

    if (!response.ok) {
      throw new Error('Erro ao registrar estudante');
    }

    return await response.json();
  } catch (error) {
    console.error('Erro no registro do estudante:', error);
    return null;
  }
}

  isAuthenticated(): boolean {
    return !!this.getToken();
  }

  hasRole(requiredRole: string): boolean {
    const token = this.getToken();
    if (!token) {
      return false;
    }
    try {
      const decodedToken: any = jwtDecode(token);
      console.log('Decoded token:', decodedToken.role);
      return decodedToken.role === requiredRole;
    } catch (error) {
      console.error('Token error:', error);
      return false;
    }
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }
}