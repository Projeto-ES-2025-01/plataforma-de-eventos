import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, AbstractControl } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../auth';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-register-student',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
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
    password: ['', [Validators.required, Validators.minLength(8), Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{8,}$/)]],
    confirmPassword: ['', [Validators.required]],
    
    fullName: ['', [Validators.required]],
    cpf: ['', [Validators.required, Validators.minLength(11), Validators.maxLength(11), Validators.pattern(/^\d{11}$/)]],
    birthDate: ['', [Validators.required, this.datePastValidator]],
    phoneNumber: ['', [Validators.required, Validators.minLength(11), Validators.maxLength(11), Validators.pattern(/^\d{11}$/)]],
    degreeProgram: ['', [Validators.required]],
    currentPeriod: [1, [Validators.required, Validators.min(1), Validators.max(9)]]
  }, { validators: this.passwordMatchValidator });

  passwordMatchValidator(formGroup: FormGroup) {
    const password = formGroup.get('password')?.value;
    const confirmPassword = formGroup.get('confirmPassword')?.value;
    return password === confirmPassword ? null : { passwordMismatch: true };
  }

  datePastValidator(control: AbstractControl) {
    const hoje = new Date();
    const valor = new Date(control.value);
    return valor > hoje ? { datafutura: true } : null;
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
