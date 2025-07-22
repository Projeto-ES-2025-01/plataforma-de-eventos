import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../auth';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './login.html',
  styleUrls: ['./login.css']
})
export class LoginComponent {
  private authService = inject(AuthService);
  private router = inject(Router);
  private fb = inject(FormBuilder);

  loginForm: FormGroup = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', Validators.required]
  });

  loginInvalido: boolean = false;
  async onSubmit(): Promise<void> {
  if (this.loginForm.valid) {
    const { email, password } = this.loginForm.value;
    try {
      const sucesso = await this.authService.login(email, password);
      if (sucesso) {
        this.loginInvalido = false;
        this.router.navigate(['/home']);
      } else {
        this.loginInvalido = true;
      }
    } catch (error) {
      console.error('Erro inesperado:', error);
      this.loginInvalido = true;
    }
  }
}
}