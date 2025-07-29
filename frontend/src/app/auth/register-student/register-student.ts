import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../auth';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-register-student',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './register-student.html',
  styleUrls: ['./register-student.css']
})
export class RegisterStudentComponent {
  private authService = inject(AuthService);
  private router = inject(Router);
  private fb = inject(FormBuilder);

  registerForm: FormGroup = this.fb.group({
    name: ['', [Validators.required, Validators.minLength(3)]],
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(8), Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,}$/)]],
    confirmPassword: ['', [Validators.required]],
    
    fullName: ['', [Validators.required]],
    cpf: ['', [Validators.required]],
    birthDate: ['', [Validators.required]],
    phoneNumber: ['', [Validators.required]],
    degreeProgram: ['', [Validators.required]],
    currentPeriod: [1, [Validators.required, Validators.min(1)]]
  }, { validators: this.passwordMatchValidator });

  passwordMatchValidator(formGroup: FormGroup) {
    const password = formGroup.get('password')?.value;
    const confirmPassword = formGroup.get('confirmPassword')?.value;
    return password === confirmPassword ? null : { passwordMismatch: true };
  }

  onSubmit() {
    if (this.registerForm.valid) {
      console.log('botão submit clicado');

      const {
        name,
        email,
        password,
        fullName,
        cpf,
        birthDate,
        phoneNumber,
        degreeProgram,
        currentPeriod
      } = this.registerForm.value;

      const payload = {
        userDTO: {
          name,
          email,
          password
        },
        studentProfileDTO: {
          fullName,
          cpf,
          birthDate,
          phoneNumber,
          degreeProgram,
          currentPeriod
        }
      };

      this.authService.registerStudent(payload)
        .then(() => this.router.navigate(['/login']))
        .catch(err => alert('Erro ao registrar estudante: ' + err));
    }
  }
}
