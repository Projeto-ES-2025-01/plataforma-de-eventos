import { Routes } from '@angular/router';
import { LoginComponent } from "./auth/login/login";
import { RegisterComponent } from "./auth/register/register";
import { authGuard } from './auth/auth-guard';
import { Unauthorized } from "./auth/unauthorized/unauthorized";
import { HomeComponent } from "./home/home";
import { EventoComponent } from '../evento/evento';
import { RegisterStudentComponent } from './auth/register-student/register-student';
import { CriarEventoComponent } from '../evento/criar-evento/criar-evento';

export const routes: Routes = [
    { 
        path: '', redirectTo: 'login', pathMatch: 'full'
    },
    { 
        path: 'login', component: LoginComponent, title: 'Login'
    },
    { 
        path: 'register/organizer', component: RegisterComponent, title: 'Cadastro de organizador' 
    },
    { 
        path: 'register/student', component: RegisterStudentComponent, title: 'Cadastro de estudante' 
    },
    {
        path: 'unauthorized', component: Unauthorized, title: 'Unauthorized'
    },
    { 
        path: 'home', component: HomeComponent, canActivate: [authGuard], title: 'Home Page' 
    },
    { 
        path: 'criar-evento', component: CriarEventoComponent, canActivate: [authGuard], title: 'Details Page',
        data: { role: 'ORGANIZER' }
    },
    { 
        path: '**', redirectTo: 'login' 
    }
];
