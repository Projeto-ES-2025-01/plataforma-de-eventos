import { Routes } from '@angular/router';
import { LoginComponent } from "./auth/login/login";
import { RegisterComponent } from "./auth/register/register";
import { authGuard } from './auth/auth-guard';
import { Unauthorized } from "./auth/unauthorized/unauthorized";
import { Home } from "./home/home";

export const routes: Routes = [
    { 
        path: '', redirectTo: 'login', pathMatch: 'full'
    },
    { 
        path: 'login', component: LoginComponent, title: 'Login'
    },
    { 
        path: 'register', component: RegisterComponent, title: 'Cadastro' 
    },
    {
        path: 'unauthorized', component: Unauthorized, title: 'Unauthorized'
    },
    { 
        path: 'home', component: Home, canActivate: [authGuard], title: 'Home Page' 
    },
    { 
        path: '**', redirectTo: 'login' 
    }
];
