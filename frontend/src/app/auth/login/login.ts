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

  public errorMessage: string | null = null;

  onSubmit() {
    if (this.loginForm.valid) {
      this.errorMessage = null;
      console.log('botão submit clicado');
      const { email, password } = this.loginForm.value;

      this.authService.login(email, password)
        .then(sucess => {
          if (sucess) {
            this.router.navigate(['/home']);
         } else {
           this.errorMessage = 'Tentativa de login inválida. Verifique suas credenciais.';
          }
        })
        .catch(() => {
          this.errorMessage = 'Ocorreu um erro ao tentar fazer login. Por favor, tente novamente.';
      });
    }
  }
}